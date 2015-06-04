/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */
package org.komodo.importer.vdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;
import org.junit.Ignore;
import org.junit.Test;
import org.komodo.importer.AbstractImporterTest;
import org.komodo.importer.ImportMessages;
import org.komodo.importer.ImportOptions;
import org.komodo.importer.ImportOptions.OptionKeys;
import org.komodo.importer.ImportType;
import org.komodo.modeshape.teiid.cnd.TeiidSqlLexicon;
import org.komodo.modeshape.teiid.cnd.TeiidSqlLexicon.Symbol;
import org.komodo.relational.model.Model;
import org.komodo.relational.model.Model.Type;
import org.komodo.relational.vdb.DataRole;
import org.komodo.relational.vdb.ModelSource;
import org.komodo.relational.vdb.Translator;
import org.komodo.relational.vdb.Vdb;
import org.komodo.relational.workspace.WorkspaceManager;
import org.komodo.repository.SynchronousCallback;
import org.komodo.spi.repository.KomodoObject;
import org.komodo.spi.repository.Repository;
import org.komodo.spi.repository.Repository.UnitOfWork;
import org.komodo.test.utils.TestUtilities;
import org.komodo.utils.KLog;
import org.modeshape.jcr.api.JcrConstants;
import org.modeshape.sequencer.ddl.dialect.teiid.TeiidDdlLexicon;
import org.modeshape.sequencer.teiid.lexicon.CoreLexicon;
import org.modeshape.sequencer.teiid.lexicon.VdbLexicon;

/**
 * Test Class to test Teiid VDB import
 *
 */
@SuppressWarnings({"nls", "javadoc"})
public class TestTeiidVdbImporter extends AbstractImporterTest {

    private static final String TWEET_EXAMPLE_REIMPORT = "tweet-example-vdb-reimport.xml";

    private static final String DYNAMIC_CUSTOMER_VDB = "dynamic-customer-vdb.xml";
    private static final String DYNAMIC_CUSTOMER_VDB_NAME = "DynamicCustomer";

    private static final String PARTS_DYNAMIC_VDB = "parts_dynamic-vdb.xml";
    private static final String PARTS_DYNAMIC_VDB_NAME = "MyPartsVDB_Dynamic";
    private static final String PARTS_DYNAMIC_PARTSVIEW_DDL = EMPTY_STRING +
                              "CREATE VIEW PartsSummary ( " +
                              "PART_ID string(50) NOT NULL OPTIONS (SEARCHABLE 'Searchable'), " +
                              "PART_NAME string(255) OPTIONS (SEARCHABLE 'Searchable'), " +
                              "PART_COLOR string(30) OPTIONS (SEARCHABLE 'Searchable'), " +
                              "PART_WEIGHT string(255) OPTIONS (SEARCHABLE 'Searchable'), " +
                              "SUPPLIER_ID string(10) NOT NULL OPTIONS (SEARCHABLE 'Searchable'), " +
                              "QUANTITY bigdecimal(3) OPTIONS (FIXED_LENGTH TRUE, SEARCHABLE 'All_Except_Like'), " +
                              "SHIPPER_ID bigdecimal(2) OPTIONS (FIXED_LENGTH TRUE, SEARCHABLE 'All_Except_Like')" + NEW_LINE +
                              ") OPTIONS (UPDATABLE TRUE)" + NEW_LINE +
                              "AS" + NEW_LINE +
                              "SELECT PartsSS.PARTS.PART_ID, PartsSS.PARTS.PART_NAME, PartsSS.PARTS.PART_COLOR, PartsSS.PARTS.PART_WEIGHT, PartsSS.SUPPLIER_PARTS.SUPPLIER_ID, PartsSS.SUPPLIER_PARTS.QUANTITY, PartsSS.SUPPLIER_PARTS.SHIPPER_ID FROM PartsSS.PARTS, PartsSS.SUPPLIER_PARTS WHERE PartsSS.PARTS.PART_ID = PartsSS.SUPPLIER_PARTS.PART_ID;";

    private static final String PARTS_DYNAMIC_PARTSS_DDL = EMPTY_STRING +
                            "CREATE FOREIGN TABLE PARTS ( " +
                            "PART_ID string(50) NOT NULL OPTIONS (NAMEINSOURCE '\"PART_ID\"', SEARCHABLE 'Searchable', NATIVE_TYPE 'varchar'), " +
                            "PART_NAME string(255) OPTIONS (NAMEINSOURCE '\"PART_NAME\"', SEARCHABLE 'Searchable', NATIVE_TYPE 'varchar'), " +
                            "PART_COLOR string(30) OPTIONS (NAMEINSOURCE '\"PART_COLOR\"', SEARCHABLE 'Searchable', NATIVE_TYPE 'varchar'), " +
                            "PART_WEIGHT string(255) OPTIONS (NAMEINSOURCE '\"PART_WEIGHT\"', SEARCHABLE 'Searchable', NATIVE_TYPE 'varchar')" + NEW_LINE +
                            ") OPTIONS (NAMEINSOURCE '\"partssupplier\".\"dbo\".\"PARTS\"', UPDATABLE TRUE); " +
                            "CREATE FOREIGN TABLE SHIP_VIA ( " +
                                "SHIPPER_ID bigdecimal(2) NOT NULL OPTIONS (NAMEINSOURCE '\"SHIPPER_ID\"', FIXED_LENGTH TRUE, SEARCHABLE 'All_Except_Like', NATIVE_TYPE 'numeric'), " +
                                "SHIPPER_NAME string(30) OPTIONS (NAMEINSOURCE '\"SHIPPER_NAME\"', SEARCHABLE 'Searchable', NATIVE_TYPE 'varchar')" + NEW_LINE +
                            ") OPTIONS (NAMEINSOURCE '\"partssupplier\".\"dbo\".\"SHIP_VIA\"', UPDATABLE TRUE); " +
                            "CREATE FOREIGN TABLE STATUS ( " +
                                "STATUS_ID bigdecimal(2) NOT NULL OPTIONS (NAMEINSOURCE '\"STATUS_ID\"', FIXED_LENGTH TRUE, SEARCHABLE 'All_Except_Like', NATIVE_TYPE 'numeric'), " +
                                "STATUS_NAME string(30) OPTIONS (NAMEINSOURCE '\"STATUS_NAME\"', SEARCHABLE 'Searchable', NATIVE_TYPE 'varchar')" + NEW_LINE +
                            ") OPTIONS (NAMEINSOURCE '\"partssupplier\".\"dbo\".\"STATUS\"', UPDATABLE TRUE); " +
                            "CREATE FOREIGN TABLE SUPPLIER ( " +
                                "SUPPLIER_ID string(10) NOT NULL OPTIONS (NAMEINSOURCE '\"SUPPLIER_ID\"', SEARCHABLE 'Searchable', NATIVE_TYPE 'varchar'), " +
                                "SUPPLIER_NAME string(30) OPTIONS (NAMEINSOURCE '\"SUPPLIER_NAME\"', SEARCHABLE 'Searchable', NATIVE_TYPE 'varchar'), " +
                                "SUPPLIER_STATUS bigdecimal(2) OPTIONS (NAMEINSOURCE '\"SUPPLIER_STATUS\"', FIXED_LENGTH TRUE, SEARCHABLE 'All_Except_Like', NATIVE_TYPE 'numeric'), " +
                                "SUPPLIER_CITY string(30) OPTIONS (NAMEINSOURCE '\"SUPPLIER_CITY\"', SEARCHABLE 'Searchable', NATIVE_TYPE 'varchar'), " +
                                "SUPPLIER_STATE string(2) OPTIONS (NAMEINSOURCE '\"SUPPLIER_STATE\"', SEARCHABLE 'Searchable', NATIVE_TYPE 'varchar')" + NEW_LINE +
                            ") OPTIONS (NAMEINSOURCE '\"partssupplier\".\"dbo\".\"SUPPLIER\"', UPDATABLE TRUE); " +
                            "CREATE FOREIGN TABLE SUPPLIER_PARTS ( " +
                                "SUPPLIER_ID string(10) NOT NULL OPTIONS (NAMEINSOURCE '\"SUPPLIER_ID\"', SEARCHABLE 'Searchable', NATIVE_TYPE 'varchar'), " +
                                "PART_ID string(50) NOT NULL OPTIONS (NAMEINSOURCE '\"PART_ID\"', SEARCHABLE 'Searchable', NATIVE_TYPE 'varchar'), " +
                                "QUANTITY bigdecimal(3) OPTIONS ( NAMEINSOURCE '\"QUANTITY\"', FIXED_LENGTH TRUE, SEARCHABLE 'All_Except_Like', NATIVE_TYPE 'numeric'), " +
                                "SHIPPER_ID bigdecimal(2) OPTIONS (NAMEINSOURCE '\"SHIPPER_ID\"', FIXED_LENGTH TRUE, SEARCHABLE 'All_Except_Like', NATIVE_TYPE 'numeric')" + NEW_LINE +
                            ") OPTIONS (NAMEINSOURCE '\"partssupplier\".\"dbo\".\"SUPPLIER_PARTS\"', UPDATABLE TRUE);";

    private static final String PORTFOLIO_VDB = "portfolio-vdb.xml";
    private static final String PORTFOLIO_VDB_NAME = "Portfolio";

    private static final String TPCH_VDB = "tpch-vdb.xml";
    private static final String TPCH_VDB_NAME = "tpch";

    private static final String BOOKS_EXAMPLE_FULL = "books.xml";
    private static final String BOOKS_EXAMPLE_PROPS_ONLY = "books_props_only.xml";
    private static final String BOOKS_EXAMPLE_SOURCE_MODEL_ONLY = "books_source_model_only.xml";
    private static final String BOOKS_EXAMPLE_SOURCE_WITH_ROLES = "books_source_model_with_roles.xml";
    private static final String BOOKS_EXAMPLE_VIRTUAL_MODEL_ONLY = "books_virtual_model_only.xml";
    private static final String BOOKS_EXAMPLE_TRANSLATORS_ONLY = "books_translators_only.xml";

    private static final String TWEET_EXAMPLE_DDL = EMPTY_STRING +
                                                        "CREATE VIRTUAL PROCEDURE getTweets(IN query varchar) " +
                                                        "RETURNS TABLE (created_on varchar(25), from_user varchar(25), " +
                                                        "to_user varchar(25), profile_image_url varchar(25), source " +
                                                        "varchar(25), text varchar(140)) AS select tweet.* from " +
                                                        "(EXEC twitter.invokeHTTP(" +
                                                        "action => 'GET', endpoint => querystring(\'', query as q))) AS w, " +
                                                        "XMLTABLE('results' passing JSONTOXML('myxml', w.result) columns " +
                                                        "created_on string PATH 'created_at', from_user string PATH 'from_user', " +
                                                        "to_user string PATH 'to_user', profile_image_url string PATH 'profile_image_url', " +
                                                        "source string PATH 'source', text string PATH 'text') AS tweet; " +
                                                        "CREATE VIEW Tweet AS select * FROM twitterview.getTweets;";

    private static final String TWEET_EXAMPLE_REIMPORT_DDL = EMPTY_STRING +
                                                        "CREATE VIEW Tweet AS select * FROM twitterview.getTweets;";

    private static final String TWEET_QUERY_1 = EMPTY_STRING +
                                                        "select title FROM twitterview.getTweets;";

    private static final String TWITTER_MODEL = "twitter";

    private static final String TWITTER_VIEW_MODEL = "twitterview";

    private static final String WARBLE_MODEL = "warble";

    private static final String WARBLE_VIEW_MODEL = "warbleview";

    @Override
    protected KomodoObject runImporter(Repository repository,
                                                                 File file, ImportType importType, ImportOptions importOptions,
                                                                 ImportMessages importMessages) {
        VdbImporter importer = new VdbImporter(_repo);
        return importer.importVdb(file, importOptions, importMessages);
    }

    @Override
    protected KomodoObject runImporter(Repository repository,
                                                                 InputStream inputStream, ImportType importType,
                                                                 ImportOptions importOptions,
                                                                 ImportMessages importMessages) {
        VdbImporter importer = new VdbImporter(_repo);
        return importer.importVdb(inputStream, importOptions, importMessages);
    }

    /**
     * Test Error condition - bad VDB file name supplied
     * Expected Outcome - Error message saying that the supplied file is not found
     */
    @Test
    public void testBadVdbFile() throws Exception {
        ImportOptions importOptions = new ImportOptions();

        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(new File("unknown.xml"), ImportType.VDB, importOptions, importMessages);

        // No model created
        assertNull("Failed - expected null model ", vdbNode);

        // Should have 1 error message
        assertEquals(1, importMessages.getErrorMessages().size());

        String msg = importMessages.getErrorMessages().get(0);
        assertEquals("The specified File \"unknown.xml\" was not found",msg);
    }

    /**
     * Test Error condition - unreadable VDB file supplied.
     * Expected Outcome - Error Message saying that the supplied file is not readable
     */
    @Test
    public void testUnreadableVdbFile() throws Exception {
        InputStream vdbStream = TestUtilities.tweetExample();
        File tmpFile = File.createTempFile("unreadableFile", ".xml");
        Files.copy(vdbStream, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        tmpFile.deleteOnExit();

        assertTrue(tmpFile.exists());
        assertTrue(tmpFile.length() > 0);

        // Make file unreadable
        tmpFile.setReadable(false);

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(tmpFile, ImportType.VDB, importOptions, importMessages);

        // Set back to readable
        tmpFile.setReadable(true);

        // No model created
        assertNull("Failed - expected null model ", vdbNode);

        // Should have 1 error message
        assertEquals(1, importMessages.getErrorMessages().size());

        String msg = importMessages.getErrorMessages().get(0);
        assertEquals("The specified File \"" + tmpFile.getName() + "\" is not readable", msg);
    }

    /**
     * Test Error condition - empty VDB string supplied
     * Expected Outcome - Error Message saying that the supplied VDB string is empty
     */
    @Test
    public void testEmptyVdbString() throws Exception {
        File tmpFile = File.createTempFile("emptyFile", ".xml");
        tmpFile.deleteOnExit();

        assertTrue(tmpFile.exists());
        assertEquals(0, tmpFile.length());

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(tmpFile, ImportType.VDB, importOptions, importMessages);

        // No model created
        assertNull("Failed - expected null model ", vdbNode);

        // Should have 1 error message
        assertEquals(1, importMessages.getErrorMessages().size());

        String msg = importMessages.getErrorMessages().get(0);
        assertEquals("The supplied content string is empty", msg);
    }

    private void verifyTweetExampleNode(KomodoObject tweetNode,
                                                                   String model1Name,
                                                                   String model2Name,
                                                                   String modelDefinition) throws Exception {
        String REST_TRANSLATOR = "rest";

        assertNotNull(tweetNode);

        /*
         * tweet-example-vdb.xml
         *      @jcr:primaryType=vdb:virtualDatabase
         *      @jcr:mixinTypes=[mix:referenceable]
         *      @jcr:uuid={uuid-to-be-created}
         *      @mode:sha1={sha1-to-be-created}
         *      @vdb:preview=false
         *      @vdb:version=1
         *      @vdb:originalFile=tweet-example-vdb.xml
         *      @vdb:name=twitter
         *      @vdb:description=Shows how to call Web Services
         *      @UseConnectorMetadata=cached
         */
        verify(tweetNode.getParent(this.uow), TestUtilities.TWEET_EXAMPLE_NAME, VdbLexicon.Vdb.VIRTUAL_DATABASE);
        verifyMixinType(tweetNode, "mix:referenceable");
        verifyProperty(tweetNode, VdbLexicon.Vdb.NAME, "twitter");
        verifyProperty(tweetNode, VdbLexicon.Vdb.DESCRIPTION, "Shows how to call Web Services");

        // Miscellaneous property
        verifyProperty(tweetNode, "UseConnectorMetadata", "cached");
        verifyProperty(tweetNode, VdbLexicon.Vdb.PREVIEW, Boolean.FALSE.toString());
        verifyProperty(tweetNode, VdbLexicon.Vdb.VERSION, Integer.toString(1));

        /*
         *      vdb:translators
         *          @jcr:primaryType=vdb:translators
         */
        KomodoObject translators = verify(tweetNode, VdbLexicon.Vdb.TRANSLATORS, VdbLexicon.Vdb.TRANSLATORS);

        /*
         *          rest
         *              @jcr:primaryType=vdb:translator
         *              @DefaultServiceMode=MESSAGE
         *              @DefaultBinding=HTTP
         *              @vdb:type=ws
         *              @vdb:description=Rest Web Service translator
         */
        KomodoObject rest = verify(translators, REST_TRANSLATOR, VdbLexicon.Translator.TRANSLATOR);
        verifyProperty(rest, VdbLexicon.Translator.DESCRIPTION, "Rest Web Service translator");
        verifyProperty(rest, "DefaultServiceMode", "MESSAGE");
        verifyProperty(rest, "DefaultBinding", "HTTP");
        verifyProperty(rest, VdbLexicon.Translator.TYPE, "ws");

        /*
         *      twitter
         *          @jcr:primaryType=vdb:declarativeModel
         *          @jcr:uuid={uuid-to-be-created}
         *          @mmcore:modelType=PHYSICAL
         *          @vdb:sourceTranslator=rest
         *          @vdb:sourceName=twitter
         *          @vdb:metadataType=DDL
         *          @vdb:visible=true
         *          @vdb:sourceJndiName=java:/twitterDS
         */
        KomodoObject twitter = verify(tweetNode, model1Name, VdbLexicon.Vdb.DECLARATIVE_MODEL);
        verifyProperty(twitter, CoreLexicon.JcrId.MODEL_TYPE, CoreLexicon.ModelType.PHYSICAL);
        verifyProperty(twitter, VdbLexicon.Model.VISIBLE, Boolean.TRUE.toString());
        verifyProperty(twitter, VdbLexicon.Model.METADATA_TYPE, "DDL");

        /*
         *          vdb:sources
         *              @jcr:primaryType=vdb:sources
         */
        KomodoObject twitterSources = verify(twitter, VdbLexicon.Vdb.SOURCES, VdbLexicon.Vdb.SOURCES);

        /*
         *              twitter
         *                  @jcr:primaryType=vdb:source
         *                  @vdb:sourceTranslator=rest
         *                  @vdb:sourceJndiName=java:/twitterDS
         */
        KomodoObject twitterSource = verify(twitterSources, model1Name, VdbLexicon.Source.SOURCE);
        verifyProperty(twitterSource, VdbLexicon.Source.TRANSLATOR, REST_TRANSLATOR);
        verifyProperty(twitterSource, VdbLexicon.Source.JNDI_NAME, "java:/twitterDS");

        /*
         *      twitterview
         *          @jcr:primaryType=vdb:declarativeModel
         *          @jcr:uuid={uuid-to-be-created}
         *          @mmcore:modelType=VIRTUAL
         *          @vdb:visible=true
         *          @vdb:metadataType=DDL
         *          @vdb:modelDefinition=CREATE VIRTUAL PROCEDURE getTweets(query varchar) RETURNS (created_on varchar(25), from_user varchar(25), to_user varchar(25), profile_image_url varchar(25), source varchar(25), text varchar(140)) AS select tweet.* from (call twitter.invokeHTTP(action => 'GET', endpoint =>querystring('',query as "q"))) w, XMLTABLE('results' passing JSONTOXML('myxml', w.result) columns created_on string PATH 'created_at', from_user string PATH 'from_user', to_user string PATH 'to_user', profile_image_url string PATH 'profile_image_url', source string PATH 'source', text string PATH 'text') tweet; CREATE VIEW Tweet AS select * FROM twitterview.getTweets;
         */
        KomodoObject twitterView = verify(tweetNode, model2Name, VdbLexicon.Vdb.DECLARATIVE_MODEL);
        verifyProperty(twitterView, CoreLexicon.JcrId.MODEL_TYPE, CoreLexicon.ModelType.VIRTUAL);
        verifyProperty(twitterView, VdbLexicon.Model.METADATA_TYPE, "DDL");
        verifyProperty(twitterView, VdbLexicon.Model.VISIBLE, Boolean.TRUE.toString());
        verifyProperty(twitterView, VdbLexicon.Model.MODEL_DEFINITION, modelDefinition);

        if (TWITTER_VIEW_MODEL.equals(model2Name)) {
            // Only the twitterview version of the import data has the VIRTUAL PROCEDURE
            // which creates the getTweets node
            KomodoObject getTweets = verify(twitterView, "getTweets");
            KomodoObject getTweetsQuery = verify(getTweets, TeiidSqlLexicon.Query.ID);
            verify(getTweetsQuery, TeiidSqlLexicon.From.ID, JcrConstants.NT_UNSTRUCTURED, TeiidSqlLexicon.From.ID);
        }

        KomodoObject tweet = verify(twitterView, "Tweet");
        KomodoObject tweetQuery = verify(tweet, TeiidSqlLexicon.Query.ID);
        verify(tweetQuery, TeiidSqlLexicon.From.ID, JcrConstants.NT_UNSTRUCTURED, TeiidSqlLexicon.From.ID);
    }

    @Test
    public void testBasicVdbImport() throws Exception {
        InputStream vdbStream = TestUtilities.tweetExample();

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();
        importOptions.setOption(OptionKeys.NAME, TestUtilities.TWEET_EXAMPLE_NAME);

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(vdbStream, ImportType.VDB, importOptions,
                                                                           importMessages);

        // Test that a vdb was created
        assertNotNull("Failed - No Vdb Created ", vdbNode);

        // Test vdb name
        String vdbName = vdbNode.getName(this.uow);
        assertEquals(importOptions.getOption(OptionKeys.NAME), vdbName);

        verifyTweetExampleNode(vdbNode, TWITTER_MODEL, TWITTER_VIEW_MODEL, TWEET_EXAMPLE_DDL);
    }

    private void setModelDefinitionAwaitSequencing(Model model, String defn) throws Exception {
        SynchronousCallback callback = new SynchronousCallback();
        UnitOfWork transaction = _repo.createTransaction("vdbtests-setmodeldefn-value", false, callback);
        assertNotNull(transaction);

        model.setModelDefinition(transaction, defn);

        //
        // Commit the transaction and await the response of the callback
        //
        transaction.commit();

        // Wait for the sequencing of the repository or timeout of 3 minutes
        assertTrue(callback.await(TIME_TO_WAIT, TimeUnit.MINUTES));
        assertFalse(callback.hasError());

        traverse(_repo.createTransaction("traverse-modeldef-node", true, null), model.getAbsolutePath());
    }

    @Test
    public void testBasicVdbImportThenChangeModelDefnProperty()  throws Exception {
        // Import the original vdb import first
        testBasicVdbImport();
        commit();

        KLog.getLogger().debug("\n\n=== Editing tweet example ===");

        KomodoObject vdbNode = _repo.getFromWorkspace(uow, TestUtilities.TWEET_EXAMPLE_NAME);
        assertNotNull(vdbNode);
        WorkspaceManager wkspManager = WorkspaceManager.getInstance(_repo);

        KomodoObject twitterView = vdbNode.getChild(uow, TWITTER_VIEW_MODEL);
        Model model = wkspManager.resolve(uow, twitterView, Model.class);
        commit();

        //
        // Set the model defintion of tweetview to alternative
        //
        setModelDefinitionAwaitSequencing(model, TWEET_EXAMPLE_REIMPORT_DDL);

        KomodoObject[] tweets = twitterView.getChildren(uow, "Tweet");
        assertEquals(1, tweets.length);

        /*
         *      twitterview
         *          @jcr:primaryType=vdb:declarativeModel
         *          @jcr:uuid={uuid-to-be-created}
         *          @mmcore:modelType=VIRTUAL
         *          @vdb:visible=true
         *          @vdb:metadataType=DDL
         *          @vdb:modelDefinition=CREATE VIRTUAL PROCEDURE getTweets(query varchar) RETURNS (created_on varchar(25), from_user varchar(25), to_user varchar(25), profile_image_url varchar(25), source varchar(25), text varchar(140)) AS select tweet.* from (call twitter.invokeHTTP(action => 'GET', endpoint =>querystring('',query as "q"))) w, XMLTABLE('results' passing JSONTOXML('myxml', w.result) columns created_on string PATH 'created_at', from_user string PATH 'from_user', to_user string PATH 'to_user', profile_image_url string PATH 'profile_image_url', source string PATH 'source', text string PATH 'text') tweet; CREATE VIEW Tweet AS select * FROM twitterview.getTweets;
         */
        verifyPrimaryType(twitterView, VdbLexicon.Vdb.DECLARATIVE_MODEL);
        verifyProperty(twitterView, CoreLexicon.JcrId.MODEL_TYPE, CoreLexicon.ModelType.VIRTUAL);
        verifyProperty(twitterView, VdbLexicon.Model.METADATA_TYPE, "DDL");
        verifyProperty(twitterView, VdbLexicon.Model.VISIBLE, Boolean.TRUE.toString());
        verifyProperty(twitterView, VdbLexicon.Model.MODEL_DEFINITION, TWEET_EXAMPLE_REIMPORT_DDL);

        KomodoObject tweet = verify(twitterView, "Tweet");
        KomodoObject tweetQuery = verify(tweet, TeiidSqlLexicon.Query.ID);
        verify(tweetQuery, TeiidSqlLexicon.From.ID, JcrConstants.NT_UNSTRUCTURED, TeiidSqlLexicon.From.ID);
    }

    @Test
    public void testBasicVdbImportThenChangeQueryExpressionProperty()  throws Exception {
        // Import the original vdb import first
        testBasicVdbImport();
        commit();

        KLog.getLogger().debug("\n\n=== Editing tweet example ===");

        KomodoObject vdbNode = _repo.getFromWorkspace(uow, TestUtilities.TWEET_EXAMPLE_NAME);
        assertNotNull(vdbNode);

        KomodoObject twitterView = vdbNode.getChild(uow, TWITTER_VIEW_MODEL);
        KomodoObject[] tweets = twitterView.getChildren(uow, "Tweet");
        assertEquals(1, tweets.length);

        KomodoObject tweet = verify(twitterView, "Tweet");
        commit();

        SynchronousCallback callback = new SynchronousCallback();
        UnitOfWork transaction = _repo.createTransaction("vdbtests-setqueryexp-value", false, callback);

        //
        // Change the value of the query expression for the tweet node
        //
        tweet.setProperty(transaction, TeiidDdlLexicon.CreateTable.QUERY_EXPRESSION, TWEET_QUERY_1);

        //
        // Commit the transaction and await the response of the callback
        //
        transaction.commit();

        // Wait for the sequencing of the repository or timeout of 3 minutes
        assertTrue(callback.await(TIME_TO_WAIT, TimeUnit.MINUTES));
        assertFalse(callback.hasError());

        traverse(_repo.createTransaction("traverse-queryexp-node", true, null), tweet.getAbsolutePath());

        KomodoObject tweetQuery = verify(tweet, TeiidSqlLexicon.Query.ID);
        verify(tweetQuery, TeiidSqlLexicon.From.ID, JcrConstants.NT_UNSTRUCTURED, TeiidSqlLexicon.From.ID);
        KomodoObject selectStmt = verify(tweetQuery, TeiidSqlLexicon.Select.ID, JcrConstants.NT_UNSTRUCTURED, TeiidSqlLexicon.Select.ID);
        KomodoObject symbolsStmt = verify(selectStmt, TeiidSqlLexicon.Select.SYMBOLS_REF_NAME, JcrConstants.NT_UNSTRUCTURED, TeiidSqlLexicon.ElementSymbol.ID);
        verifyProperty(symbolsStmt, Symbol.NAME_PROP_NAME, "title");
    }

    @Test
    public void testBasicVdbReImport()  throws Exception {
        // Import the original vdb import first
        testBasicVdbImport();
        commit();

        KLog.getLogger().debug("\n\n=== Reimporting edited tweet example ===");

        // Set up the vdb reimport stream
        InputStream vdbStream = TestUtilities.getResourceAsStream(getClass(),
                                                                                                       VDB_DIRECTORY,
                                                                                                       TWEET_EXAMPLE_REIMPORT); 

        // Options for the import (default)
        //
        // Handling existing node set to OVERWRITE by default
        //
        ImportOptions importOptions = new ImportOptions();
        importOptions.setOption(OptionKeys.NAME, TestUtilities.TWEET_EXAMPLE_NAME);

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(vdbStream, ImportType.VDB, importOptions,
                                                                           importMessages);

        // Test that a vdb was created
        assertNotNull("Failed - No Vdb Created ", vdbNode);

        // Test vdb name
        String vdbName = vdbNode.getName(uow);
        assertEquals(importOptions.getOption(OptionKeys.NAME), vdbName);

        verifyTweetExampleNode(vdbNode, WARBLE_MODEL, WARBLE_VIEW_MODEL, TWEET_EXAMPLE_REIMPORT_DDL);
    }

    private void verifyAllElementsExampleNode(KomodoObject allElementsNode) throws Exception {
        assertNotNull(allElementsNode);

        /*
         * teiid-vdb-all-elements.xml
         *      @jcr:primaryType=vdb:virtualDatabase
         *      @jcr:mixinTypes=[mode:derived,mix:referenceable]
         *      @jcr:uuid={uuid-to-be-created}
         *      @mode:sha1={sha1-to-be-created}
         *      @vdb:preview=false
         *      @vdb:version=1
         *      @vdb:originalFile=/vdbs/teiid-vdb-all-elements.xml
         *      @vdb:name=myVDB
         *      @vdb:description=vdb description
         *      @vdb:connectionType=BY_VERSION
         *      @vdb-property2=vdb-value2
         *      @vdb-property=vdb-value
         */
        KomodoObject myVdbExample = verify(allElementsNode.getParent(this.uow),
                                                                    TestUtilities.ALL_ELEMENTS_EXAMPLE_NAME + TestUtilities.ALL_ELEMENTS_EXAMPLE_SUFFIX,
                                                                    VdbLexicon.Vdb.VIRTUAL_DATABASE,
                                                                    "mix:referenceable");
        assertEquals(allElementsNode, myVdbExample);
        verifyProperty(myVdbExample, VdbLexicon.Vdb.NAME, "myVDB");
        verifyProperty(myVdbExample, VdbLexicon.Vdb.DESCRIPTION, "vdb description");
        verifyProperty(myVdbExample, VdbLexicon.Vdb.CONNECTION_TYPE, "BY_VERSION");
        verifyProperty(myVdbExample, VdbLexicon.Vdb.PREVIEW, Boolean.FALSE.toString());
        verifyProperty(myVdbExample, VdbLexicon.Vdb.VERSION, Integer.toString(1));
        verifyProperty(myVdbExample, "vdb-property2", "vdb-value2");
        verifyProperty(myVdbExample, "vdb-property", "vdb-value");

        /*
         *      vdb:importVdbs
         *          @jcr:primaryType=vdb:importVdb
         */
        KomodoObject importVdbs = verify(myVdbExample, VdbLexicon.Vdb.IMPORT_VDBS, VdbLexicon.Vdb.IMPORT_VDBS);

        /*
         *          x
         *              @jcr:primaryType=vdb:importVdb
         *              @vdb:version=2
         *              @vdb:import-data-policies=false
         */
        KomodoObject importVdb = verify(importVdbs, "x", VdbLexicon.ImportVdb.IMPORT_VDB);
        verifyProperty(importVdb, VdbLexicon.ImportVdb.VERSION, Integer.toString(2));
        verifyProperty(importVdb, VdbLexicon.ImportVdb.IMPORT_DATA_POLICIES, Boolean.FALSE.toString());

        /*
         *      model-one
         *          @jcr:primaryType=vdb:declarativeModel
         *          @jcr:uuid={uuid-to-be-created}
         *          @mmcore:modelType=PHYSICAL
         *          @description=model description
         *          @vdb:visible=false
         */
        KomodoObject modelOne = verify(myVdbExample, "model-one", VdbLexicon.Vdb.DECLARATIVE_MODEL);
        verifyProperty(modelOne, CoreLexicon.JcrId.MODEL_TYPE, CoreLexicon.ModelType.PHYSICAL);
        verifyProperty(modelOne, VdbLexicon.Vdb.DESCRIPTION, "model description");
        verifyProperty(modelOne, VdbLexicon.Model.VISIBLE, Boolean.FALSE.toString());

        /*
         *          vdb:sources
         *              @jcr:primaryType=vdb:sources
         */
        KomodoObject model1Sources = verify(modelOne, VdbLexicon.Vdb.SOURCES, VdbLexicon.Vdb.SOURCES);

        /*
         *              s1
         *                  @jcr:primaryType=vdb:source
         *                  @vdb:sourceTranslator=translator
         *                  @vdb:sourceJndiName=java:mybinding
         */
        KomodoObject model1Src1 = verify(model1Sources, "s1", VdbLexicon.Source.SOURCE);
        verifyProperty(model1Src1, VdbLexicon.Source.TRANSLATOR, "translator");
        verifyProperty(model1Src1, VdbLexicon.Source.JNDI_NAME, "java:mybinding");

        /*
         *      model-two
         *          @jcr:primaryType=vdb:declarativeModel
         *          @jcr:uuid={uuid-to-be-created}
         *          @mmcore:modelType=VIRTUAL
         *          @vdb:visible=true
         */
        KomodoObject modelTwo = verify(myVdbExample, "model-two", VdbLexicon.Vdb.DECLARATIVE_MODEL);
        verifyProperty(modelTwo, CoreLexicon.JcrId.MODEL_TYPE, CoreLexicon.ModelType.VIRTUAL);
        verifyProperty(modelTwo, VdbLexicon.Model.VISIBLE, Boolean.TRUE.toString());
        verifyProperty(modelTwo, VdbLexicon.Model.METADATA_TYPE, "DDL");

        String modelDefinition = "CREATE VIEW Test AS SELECT * FROM Test.getTest;";
        verifyProperty(modelTwo, VdbLexicon.Model.MODEL_DEFINITION, modelDefinition);

        /*
         *          vdb:sources
         *              @jcr:primaryType=vdb:sources
         */
        KomodoObject model2Sources = verify(modelTwo, VdbLexicon.Vdb.SOURCES, VdbLexicon.Vdb.SOURCES);

        /*
         *              s1
         *                  @jcr:primaryType=vdb:source
         *                  @vdb:sourceTranslator=translator
         *                  @vdb:sourceJndiName=java:binding-one
         */
        KomodoObject model2Src1 = verify(model2Sources, "s1", VdbLexicon.Source.SOURCE);
        verifyProperty(model2Src1, VdbLexicon.Source.TRANSLATOR, "translator");
        verifyProperty(model2Src1, VdbLexicon.Source.JNDI_NAME, "java:binding-one");

        /*
         *              s2
         *                  @jcr:primaryType=vdb:source
         *                  @vdb:sourceTranslator=translator
         *                  @vdb:sourceJndiName=java:binding-two
         */
        KomodoObject model2Src2 = verify(model2Sources, "s2", VdbLexicon.Source.SOURCE);
        verifyProperty(model2Src2, VdbLexicon.Source.TRANSLATOR, "translator");
        verifyProperty(model2Src2, VdbLexicon.Source.JNDI_NAME, "java:binding-two");

        /*
         *      vdb:translators
         *          @jcr:primaryType=vdb:translators
         */
        KomodoObject translators = verify(myVdbExample, VdbLexicon.Vdb.TRANSLATORS, VdbLexicon.Vdb.TRANSLATORS);

        /*
         *          oracleOverride
         *              @jcr:primaryType=vdb:translator
         *              @vdb:description=hello world
         *              @vdb:type=oracle
         *              my-property=my-value
         */
        KomodoObject oraTranslator = verify(translators, "oracleOverride", VdbLexicon.Translator.TRANSLATOR);
        verifyProperty(oraTranslator, VdbLexicon.Translator.DESCRIPTION, "hello world");
        verifyProperty(oraTranslator, VdbLexicon.Translator.TYPE, "oracle");
        verifyProperty(oraTranslator, "my-property", "my-value");

        /*
         *      vdb:dataRoles
         *          @jcr:primaryType=vdb:dataRoles
         */
        KomodoObject dataRoles = verify(myVdbExample, VdbLexicon.Vdb.DATA_ROLES, VdbLexicon.Vdb.DATA_ROLES);

        /*
         *          roleOne
         *              @jcr:primaryType=vdb:dataRole
         *              @vdb:anyAuthenticated=false
         *              @vdb:grantAll=true
         *              @vdb:allowCreateTemporaryTables=true
         *              @vdb:description=roleOne described
         *              @vdb:mappedRoleNames=ROLE1, ROLE2
         */
        KomodoObject dataRole1 = verify(dataRoles, "roleOne", VdbLexicon.DataRole.DATA_ROLE);
        verifyProperty(dataRole1, VdbLexicon.Translator.DESCRIPTION, "roleOne described");
        verifyProperty(dataRole1, VdbLexicon.DataRole.ANY_AUTHENTICATED, Boolean.FALSE.toString());
        verifyProperty(dataRole1, VdbLexicon.DataRole.GRANT_ALL, Boolean.TRUE.toString());
        verifyProperty(dataRole1, VdbLexicon.DataRole.ALLOW_CREATE_TEMP_TABLES, Boolean.TRUE.toString());
        verifyProperty(dataRole1, VdbLexicon.DataRole.MAPPED_ROLE_NAMES, "ROLE1", "ROLE2");

        /*
         *              vdb:permissions
         *                  @jcr:primaryType=vdb:permissions
         */
        KomodoObject permissions = verify(dataRole1, VdbLexicon.DataRole.PERMISSIONS, VdbLexicon.DataRole.PERMISSIONS);

        /*
         *                  myTable.T1
         *                      @jcr.primaryType=vdb:permission
         *                      @allowRead=true
         */
        KomodoObject permission1 = verify(permissions, "myTable.T1", VdbLexicon.DataRole.Permission.PERMISSION);
        verifyProperty(permission1, VdbLexicon.DataRole.Permission.ALLOW_READ, Boolean.TRUE.toString());

        /*
         *                  myTable.T2
         *                      @jcr.primaryType=vdb:permission
         *                      @allowCreate=true
         *                      @allowRead=false
         *                      @allowUpdate=true
         *                      @allowDelete=true
         *                      @allowExecute=true
         *                      @allowAlter=true
         */
        KomodoObject permission2 = verify(permissions, "myTable.T2", VdbLexicon.DataRole.Permission.PERMISSION);
        verifyProperty(permission2, VdbLexicon.DataRole.Permission.ALLOW_CREATE, Boolean.TRUE.toString());
        verifyProperty(permission2, VdbLexicon.DataRole.Permission.ALLOW_READ, Boolean.FALSE.toString());
        verifyProperty(permission2, VdbLexicon.DataRole.Permission.ALLOW_UPDATE, Boolean.TRUE.toString());
        verifyProperty(permission2, VdbLexicon.DataRole.Permission.ALLOW_DELETE, Boolean.TRUE.toString());
        verifyProperty(permission2, VdbLexicon.DataRole.Permission.ALLOW_EXECUTE, Boolean.TRUE.toString());
        verifyProperty(permission2, VdbLexicon.DataRole.Permission.ALLOW_ALTER, Boolean.TRUE.toString());

        /*
         *                      vdb:conditions
         *                          @jcr:primaryType=vdb:conditions
         */
        KomodoObject conditions = verify(permission2, VdbLexicon.DataRole.Permission.CONDITIONS, VdbLexicon.DataRole.Permission.CONDITIONS);

        /*
         *                          col1 = user()
         *                              @jcr:primaryType=vdb:condition
         *                              @vdb:constraint=false
         */
        KomodoObject condition = verify(conditions, "col1 = user()", VdbLexicon.DataRole.Permission.Condition.CONDITION);
        verifyProperty(condition, VdbLexicon.DataRole.Permission.Condition.CONSTRAINT, Boolean.FALSE.toString());

        /*
         *                  myTable.T2.col1
         *                      @jcr.primaryType=vdb:permission
         */
        KomodoObject permission3 = verify(permissions, "myTable.T2.col1", VdbLexicon.DataRole.Permission.PERMISSION);

        /*
         *                      vdb:masks
         *                          @jcr:primaryType=vdb:masks
         */
        KomodoObject masks = verify(permission3, VdbLexicon.DataRole.Permission.MASKS, VdbLexicon.DataRole.Permission.MASKS);

        /*
         *                          col2
         *                              @jcr:primaryType=vdb:mask
         *                              @vdb:order=1
         */
        KomodoObject mask = verify(masks, "col2", VdbLexicon.DataRole.Permission.Mask.MASK);
        verifyProperty(mask, VdbLexicon.DataRole.Permission.Mask.ORDER, Integer.toString(1));

        /*
         *                  javascript
         *                      @jcr.primaryType=vdb:permission
         *                      @allowLanguage=true
         */
        KomodoObject permission4 = verify(permissions, "javascript", VdbLexicon.DataRole.Permission.PERMISSION);
        verifyProperty(permission4, VdbLexicon.DataRole.Permission.ALLOW_LANGUAGE, Boolean.TRUE.toString());

        KomodoObject test = verify(modelTwo, "Test");

        KomodoObject testQuery = verify(test, TeiidSqlLexicon.Query.ID);
        verify(testQuery, TeiidSqlLexicon.From.ID, JcrConstants.NT_UNSTRUCTURED, TeiidSqlLexicon.From.ID);

    }

    @Test
    public void testAllElementsVdbImport() throws Exception {
        InputStream vdbStream = TestUtilities.allElementsExample();

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();
        importOptions.setOption(OptionKeys.NAME,
                                               TestUtilities.ALL_ELEMENTS_EXAMPLE_NAME + TestUtilities.ALL_ELEMENTS_EXAMPLE_SUFFIX);

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(vdbStream, ImportType.VDB, importOptions,
                                                                           importMessages);

        // Test that a vdb was created
        assertNotNull("Failed - No Vdb Created ", vdbNode);

        // Test vdb name
        String vdbName = vdbNode.getName(this.uow);
        assertEquals(importOptions.getOption(OptionKeys.NAME), vdbName);

        verifyAllElementsExampleNode(vdbNode);
    }


    @Test
    @Ignore("This will not succeed until MODE-2464 has been fixed")
    public void testBooksExample_Full_Vdb() throws Exception {
        InputStream vdbStream = TestUtilities.getResourceAsStream(getClass(),
                                                                  BOOKS_DIRECTORY, BOOKS_EXAMPLE_FULL);

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();
        importOptions.setOption(OptionKeys.NAME, BOOKS_EXAMPLE_FULL);

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(vdbStream, ImportType.VDB, importOptions,
                                                                           importMessages);

        // Test that a vdb was created
        assertNotNull("Failed - No Vdb Created ", vdbNode);

        // Test vdb name
        String vdbName = vdbNode.getName(this.uow);
        assertEquals(importOptions.getOption(OptionKeys.NAME), vdbName);

        assertNotNull(vdbNode);

        Vdb vdb = WorkspaceManager.getInstance(_repo).resolve(this.uow, vdbNode, Vdb.class);

        assertNotNull(vdb);
        String desc = vdb.getDescription(this.uow);
        assertEquals("Sample vdb that demonstrates various vdb manifest properties including data role with permissions", desc);

        assertNotNull(vdb.getProperty(this.uow, "vdb:preview"));
        assertEquals("false", vdb.getProperty(this.uow, "vdb:preview").getValue(this.uow).toString());
        assertNotNull(vdb.getProperty(this.uow, "query-timeout"));
        assertEquals("256000", vdb.getProperty(this.uow, "query-timeout").getValue(this.uow).toString());
        assertNotNull(vdb.getProperty(this.uow, "allowed-languages"));
        assertEquals("java, pascal", vdb.getProperty(this.uow, "allowed-languages").getValue(this.uow).toString());
        assertNotNull(vdb.getProperty(this.uow, "authentication-type"));
        assertEquals("USERPASSWORD", vdb.getProperty(this.uow, "authentication-type").getValue(this.uow).toString());

        assertEquals(2, vdb.getModels(this.uow).length);

        assertEquals(1, vdb.getDataRoles(this.uow).length);
        DataRole dataRole = WorkspaceManager.getInstance(_repo).resolve(this.uow,  vdb.getDataRoles(this.uow)[0], DataRole.class);
    	assertEquals("publishers-only", dataRole.getName(this.uow));
    	assertNotNull(dataRole.getProperty(this.uow, "vdb:grantAll"));
        assertEquals("true", dataRole.getProperty(this.uow, "vdb:grantAll").getValue(this.uow).toString());
        assertEquals(8, dataRole.getPermissions(this.uow).length);
        assertEquals(2, dataRole.getMappedRoles(this.uow).length);

        assertEquals(1, vdb.getTranslators(this.uow).length);
        Translator translator = WorkspaceManager.getInstance(_repo).resolve(this.uow,  vdb.getTranslators(this.uow)[0], Translator.class);
    	assertEquals("books_db2", translator.getName(this.uow));
    	assertEquals("db2", translator.getType(this.uow));
    	assertNotNull(translator.getProperty(this.uow, "requiresCriteria"));
        assertEquals("true", translator.getProperty(this.uow, "requiresCriteria").getValue(this.uow).toString());
        assertNotNull(translator.getProperty(this.uow, "supportsCommonTableExpressions"));
        assertEquals("false", translator.getProperty(this.uow, "supportsCommonTableExpressions").getValue(this.uow).toString());
        assertNotNull(translator.getProperty(this.uow, "MaxDependentInPredicates"));
        assertEquals("25", translator.getProperty(this.uow, "MaxDependentInPredicates").getValue(this.uow).toString());
    }


    @Test
    public void testBooksExample_Vdb_Properties_Only() throws Exception {
        //File vdbFile = setupWithFile(BOOKS_EXAMPLE);
        InputStream vdbStream = TestUtilities.getResourceAsStream(getClass(),
                                                                  BOOKS_DIRECTORY, BOOKS_EXAMPLE_PROPS_ONLY);

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();
        importOptions.setOption(OptionKeys.NAME, BOOKS_EXAMPLE_PROPS_ONLY);

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(vdbStream, ImportType.VDB, importOptions,
                                                                           importMessages);

        // Test that a vdb was created
        assertNotNull("Failed - No Vdb Created ", vdbNode);

        // Test vdb name
        String vdbName = vdbNode.getName(this.uow);
        assertEquals(importOptions.getOption(OptionKeys.NAME), vdbName);
        assertNotNull(vdbNode);

        Vdb vdb = WorkspaceManager.getInstance(_repo).resolve(this.uow, vdbNode, Vdb.class);

        assertNotNull(vdb);
        String desc = vdb.getDescription(this.uow);
        assertEquals("Sample vdb that demonstrates various vdb manifest properties only", desc);

        // VDB Properties
        /*
			    <property name="preview" value="false"/>
			    <property name="query-timeout" value="256000"/>
			    <property name="allowed-languages" value="java, pascal"/>
			    <property name="security-domain" value="custom-security"/>
			    <property name="gss-pattern" value="%abc&amp;a-b"/>
			    <property name="password-pattern" value="$xyz1-9"/>
			    <property name="authentication-type" value="USERPASSWORD"/>
			    <property name="validationDateTime" value="Wed Apr 22 08:36:34 CDT 2015"/>
			    <property name="validationVersion" value="8.7.1"/>
         */
        assertNotNull(vdb.getProperty(this.uow, "vdb:preview"));
        assertEquals("false", vdb.getProperty(this.uow, "vdb:preview").getValue(this.uow).toString());
        assertNotNull(vdb.getProperty(this.uow, "query-timeout"));
        assertEquals("256000", vdb.getProperty(this.uow, "query-timeout").getValue(this.uow).toString());
        assertNotNull(vdb.getProperty(this.uow, "allowed-languages"));
        assertEquals("java, pascal", vdb.getProperty(this.uow, "allowed-languages").getValue(this.uow).toString());
        assertNotNull(vdb.getProperty(this.uow, "authentication-type"));
        assertEquals("USERPASSWORD", vdb.getProperty(this.uow, "authentication-type").getValue(this.uow).toString());

        assertEquals(0, vdb.getModels(this.uow).length);

    }

    @Test
    public void testBooksExample_Source_Model_Only() throws Exception {
        InputStream vdbStream = TestUtilities.getResourceAsStream(getClass(),
                                                                  BOOKS_DIRECTORY, BOOKS_EXAMPLE_SOURCE_MODEL_ONLY);

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();
        importOptions.setOption(OptionKeys.NAME, BOOKS_EXAMPLE_SOURCE_MODEL_ONLY);

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(vdbStream, ImportType.VDB, importOptions,
                                                                           importMessages);

        // Test that a vdb was created
        assertNotNull("Failed - No Vdb Created ", vdbNode);

        // Test vdb name
        String vdbName = vdbNode.getName(this.uow);
        assertEquals(importOptions.getOption(OptionKeys.NAME), vdbName);
        assertNotNull(vdbNode);

        Vdb vdb = WorkspaceManager.getInstance(_repo).resolve(this.uow, vdbNode, Vdb.class);

        assertNotNull(vdb);
        String desc = vdb.getDescription(this.uow);
        assertEquals("Sample vdb that includes this description and a single pass-through source model", desc);

        assertEquals(1, vdb.getModels(this.uow).length);
        assertNotNull("BooksSource", vdb.getModels(this.uow)[0].getName(this.uow));

    	//System.out.println("testBooksExampleVdb_3 >> COMPLETED");

    }

    @Test
    public void testBooksExample_Source_Model_With_Roles() throws Exception {
        InputStream vdbStream = TestUtilities.getResourceAsStream(getClass(),
                                                                  BOOKS_DIRECTORY, BOOKS_EXAMPLE_SOURCE_WITH_ROLES);

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();
        importOptions.setOption(OptionKeys.NAME, BOOKS_EXAMPLE_SOURCE_WITH_ROLES);

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(vdbStream, ImportType.VDB, importOptions,
                                                                           importMessages);

        // Test that a vdb was created
        assertNotNull("Failed - No Vdb Created ", vdbNode);

        // Test vdb name
        String vdbName = vdbNode.getName(this.uow);
        assertEquals(importOptions.getOption(OptionKeys.NAME), vdbName);
        assertNotNull(vdbNode);

        Vdb vdb = WorkspaceManager.getInstance(_repo).resolve(this.uow, vdbNode, Vdb.class);

        assertNotNull(vdb);
        String desc = vdb.getDescription(this.uow);
        assertEquals("Sample vdb that includes this description, a single pass-through source model with data roles", desc);

        assertEquals(1, vdb.getModels(this.uow).length);
        assertNotNull("BooksSource", vdb.getModels(this.uow)[0].getName(this.uow));
        assertEquals(1, vdb.getDataRoles(this.uow).length);
    	/*
	        <data-role name="publishers-only" any-authenticated="false" allow-create-temporary-tables="false" grant-all="true">
		        <description>publishers can both read and update book info</description>
		        <permission>
		            <resource-name>sysadmin</resource-name>
		            <allow-create>false</allow-create>
		            <allow-read>true</allow-read>
		            <allow-update>false</allow-update>
		            <allow-delete>false</allow-delete>
		            <allow-execute>false</allow-execute>
		            <allow-alter>false</allow-alter>
		        </permission>
		        <permission>
		            <resource-name>BooksSource</resource-name>
		            <allow-create>false</allow-create>
		            <allow-read>true</allow-read>
		            <allow-update>false</allow-update>
		            <allow-delete>false</allow-delete>
		            <allow-execute>false</allow-execute>
		            <allow-alter>false</allow-alter>
		        </permission>
		        <permission>
		            <resource-name>BooksSource.AUTHORS</resource-name>
		            <allow-update>false</allow-update>
		        </permission>
		        <permission>
		            <resource-name>BooksSource.BOOK_AUTHORS</resource-name>
		            <allow-update>false</allow-update>
		        </permission>
		    </data-role>
        */

        DataRole dataRole = WorkspaceManager.getInstance(_repo).resolve(this.uow,  vdb.getDataRoles(this.uow)[0], DataRole.class);
    	assertEquals("publishers-only", dataRole.getName(this.uow));
    	assertNotNull(dataRole.getProperty(this.uow, "vdb:grantAll"));
        assertEquals("true", dataRole.getProperty(this.uow, "vdb:grantAll").getValue(this.uow).toString());
        assertEquals(8, dataRole.getPermissions(this.uow).length);
        assertEquals(2, dataRole.getMappedRoles(this.uow).length);
    }

    @Test
    public void testBooksExample_Virtual_Model_Only() throws Exception {
        InputStream vdbStream = TestUtilities.getResourceAsStream(getClass(),
                                                                  BOOKS_DIRECTORY, BOOKS_EXAMPLE_VIRTUAL_MODEL_ONLY);

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();
        importOptions.setOption(OptionKeys.NAME, BOOKS_EXAMPLE_VIRTUAL_MODEL_ONLY);

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(vdbStream, ImportType.VDB, importOptions,
                                                                           importMessages);

        // Test that a vdb was created
        assertNotNull("Failed - No Vdb Created ", vdbNode);

        // Test vdb name
        String vdbName = vdbNode.getName(this.uow);
        assertEquals(importOptions.getOption(OptionKeys.NAME), vdbName);
        assertNotNull(vdbNode);

        Vdb vdb = WorkspaceManager.getInstance(_repo).resolve(this.uow, vdbNode, Vdb.class);

        assertNotNull(vdb);
        assertEquals("BooksVirtualModelOnly", vdb.getVdbName(this.uow));
        String desc = vdb.getDescription(this.uow);
        assertEquals("Sample vdb that contains simple VIRTUAL model and single view", desc);

        assertEquals(1, vdb.getModels(this.uow).length);
        assertNotNull("BooksView", vdb.getModels(this.uow)[0].getName(this.uow));
        assertEquals(0, vdb.getDataRoles(this.uow).length);

    	//System.out.println("testBooksExampleVdb_5 >> COMPLETED");

    }

    @Test
    public void testBooksExample_Translator_Only() throws Exception {
        InputStream vdbStream = TestUtilities.getResourceAsStream(getClass(),
                                                                  BOOKS_DIRECTORY, BOOKS_EXAMPLE_TRANSLATORS_ONLY);

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();
        importOptions.setOption(OptionKeys.NAME, BOOKS_EXAMPLE_TRANSLATORS_ONLY);

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(vdbStream, ImportType.VDB, importOptions,
                                                                           importMessages);

        // Test that a vdb was created
        assertNotNull("Failed - No Vdb Created ", vdbNode);

        // Test vdb name
        String vdbName = vdbNode.getName(this.uow);
        assertEquals(importOptions.getOption(OptionKeys.NAME), vdbName);
        assertNotNull(vdbNode);

        Vdb vdb = WorkspaceManager.getInstance(_repo).resolve(this.uow, vdbNode, Vdb.class);

        /*
		    <description>Sample vdb containing only a tranlator override element</description>
		    <translator name="books_db2" type="db2" description="">
		        <property name="requiresCriteria" value="true"/>
		        <property name="supportsCommonTableExpressions" value="false"/>
		        <property name="MaxDependentInPredicates" value="25"/>
		    </translator>
         */
        assertNotNull(vdb);
        assertEquals("BooksExampleTranslatorOverride", vdb.getVdbName(this.uow));
        String desc = vdb.getDescription(this.uow);
        assertEquals("Sample vdb containing only a tranlator override element", desc);

        assertEquals(0, vdb.getModels(this.uow).length);
        assertEquals(1, vdb.getTranslators(this.uow).length);
        Translator translator = WorkspaceManager.getInstance(_repo).resolve(this.uow,  vdb.getTranslators(this.uow)[0], Translator.class);
    	assertEquals("books_db2", translator.getName(this.uow));
    	assertEquals("db2", translator.getType(this.uow));

    	assertNotNull(translator.getProperty(this.uow, "requiresCriteria"));
        assertEquals("true", translator.getProperty(this.uow, "requiresCriteria").getValue(this.uow).toString());
        assertNotNull(translator.getProperty(this.uow, "supportsCommonTableExpressions"));
        assertEquals("false", translator.getProperty(this.uow, "supportsCommonTableExpressions").getValue(this.uow).toString());
        assertNotNull(translator.getProperty(this.uow, "MaxDependentInPredicates"));
        assertEquals("25", translator.getProperty(this.uow, "MaxDependentInPredicates").getValue(this.uow).toString());

    }

    @Test
    public void testDynamicCustomerVdb() throws Exception {
        InputStream vdbStream = TestUtilities.getResourceAsStream(getClass(),
                                                                  VDB_DIRECTORY, DYNAMIC_CUSTOMER_VDB);

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();
        importOptions.setOption(OptionKeys.NAME, DYNAMIC_CUSTOMER_VDB_NAME);

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(vdbStream, ImportType.VDB, importOptions,
                                                                           importMessages);

        // Test that a vdb was created
        assertNotNull("Failed - No Vdb Created ", vdbNode);

        // Test vdb name
        String vdbName = vdbNode.getName(uow);
        assertEquals(importOptions.getOption(OptionKeys.NAME), vdbName);

        assertNotNull(vdbNode);

        Vdb vdb = WorkspaceManager.getInstance(_repo).resolve(uow, vdbNode, Vdb.class);

        assertNotNull(vdb);
        String desc = vdb.getDescription(uow);
        assertEquals("Customer Dynamic VDB", desc);

        verifyProperty(vdb, "UseConnectorMetadata", "true");

        Model[] models = vdb.getModels(uow);
        assertEquals(1, models.length);
        Model model = models[0];
        assertEquals("ProductsMySQL_Dynamic", model.getName(uow));

        ModelSource[] sources = model.getSources(uow);
        assertEquals(1, sources.length);
        ModelSource source = sources[0];
        assertEquals("CustomerAccounts", source.getName(uow));
        assertEquals("mysql", source.getTranslatorName(uow));
        assertEquals("java:/CustomerAccounts", source.getJndiName(uow));
    }

    @Test
    public void testPartsDynamicVdb() throws Exception {
        InputStream vdbStream = TestUtilities.getResourceAsStream(getClass(),
                                                                  VDB_DIRECTORY, PARTS_DYNAMIC_VDB);

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();
        importOptions.setOption(OptionKeys.NAME, PARTS_DYNAMIC_VDB_NAME);

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(vdbStream, ImportType.VDB, importOptions,
                                                                           importMessages);

        // Test that a vdb was created
        assertNotNull("Failed - No Vdb Created ", vdbNode);

        // Test vdb name
        String vdbName = vdbNode.getName(uow);
        assertEquals(importOptions.getOption(OptionKeys.NAME), vdbName);

        assertNotNull(vdbNode);

        Vdb vdb = WorkspaceManager.getInstance(_repo).resolve(uow, vdbNode, Vdb.class);

        assertNotNull(vdb);
        String connType = vdb.getConnectionType(uow);
        assertEquals("BY_VERSION", connType);

        Model[] models = vdb.getModels(uow);
        assertEquals(2, models.length);
        for (Model model : models) {
            verifyProperty(model, VdbLexicon.Model.METADATA_TYPE, "DDL");

            if ("PartsViewModel".equals(model.getName(uow))) {
                assertEquals(Type.VIRTUAL, model.getModelType(uow));
                assertEquals(PARTS_DYNAMIC_PARTSVIEW_DDL, model.getModelDefinition(uow));

                // Ddl Sequenced
                verify(model, "PartsSummary", JcrConstants.NT_UNSTRUCTURED, TeiidDdlLexicon.CreateTable.VIEW_STATEMENT);

            } else if ("PartsSS".equals(model.getName(uow))) {
                assertEquals(Type.PHYSICAL, model.getModelType(uow));
                assertEquals(PARTS_DYNAMIC_PARTSS_DDL, model.getModelDefinition(uow));

                // Ddl Sequenced
                verify(model, "PARTS", JcrConstants.NT_UNSTRUCTURED, TeiidDdlLexicon.CreateTable.TABLE_STATEMENT);
            }
        }
    }

    @Test
    public void testPorfolioVdb() throws Exception {
        InputStream vdbStream = TestUtilities.getResourceAsStream(getClass(),
                                                                  VDB_DIRECTORY, PORTFOLIO_VDB);

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();
        importOptions.setOption(OptionKeys.NAME, PORTFOLIO_VDB_NAME);

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(vdbStream, ImportType.VDB, importOptions,
                                                                           importMessages);

        // Test that a vdb was created
        assertNotNull("Failed - No Vdb Created ", vdbNode);

        // Test vdb name
        String vdbName = vdbNode.getName(uow);
        assertEquals(importOptions.getOption(OptionKeys.NAME), vdbName);

        assertNotNull(vdbNode);

        WorkspaceManager manager = WorkspaceManager.getInstance(_repo);
        Vdb vdb = manager.resolve(uow, vdbNode, Vdb.class);

        assertNotNull(vdb);
        String desc = vdb.getDescription(uow);
        assertEquals("The Portfolio Dynamic VDB", desc);

        verifyProperty(vdb, "UseConnectorMetadata", "true");

        Model[] models = vdb.getModels(uow);
        assertEquals(5, models.length);

        Model model = manager.resolve(uow, vdb.getChild(uow, "StocksMatModel"), Model.class);
        verifyProperty(model, VdbLexicon.Model.METADATA_TYPE, "DDL");
        assertEquals(Type.VIRTUAL, model.getModelType(uow));

        // Ddl Sequenced
        verify(model, "stockPricesMatView", JcrConstants.NT_UNSTRUCTURED, TeiidDdlLexicon.CreateTable.VIEW_STATEMENT);
    }

    @Test
    public void testTpchVdb() throws Exception {
        InputStream vdbStream = TestUtilities.getResourceAsStream(getClass(),
                                                                  VDB_DIRECTORY, TPCH_VDB);

        // Options for the import (default)
        ImportOptions importOptions = new ImportOptions();
        importOptions.setOption(OptionKeys.NAME, TPCH_VDB_NAME);

        // Saves Messages during import
        ImportMessages importMessages = new ImportMessages();

        KomodoObject vdbNode = executeImporter(vdbStream, ImportType.VDB, importOptions,
                                                                           importMessages);

        // Test that a vdb was created
        assertNotNull("Failed - No Vdb Created ", vdbNode);

        // Test vdb name
        String vdbName = vdbNode.getName(uow);
        assertEquals(importOptions.getOption(OptionKeys.NAME), vdbName);

        assertNotNull(vdbNode);

        WorkspaceManager manager = WorkspaceManager.getInstance(_repo);
        Vdb vdb = manager.resolve(uow, vdbNode, Vdb.class);

        assertNotNull(vdb);

        Model[] models = vdb.getModels(uow);
        assertEquals(4, models.length);

        //
        // oracle model
        //
        KomodoObject child = vdb.getChild(uow, "oracle");
        assertNotNull(child);
        Model oracleModel = manager.resolve(uow, child, Model.class);
        verifyProperty(oracleModel, "importer.schemaPattern", "TPCH");
        verifyProperty(oracleModel, "importer.tableTypes", "TABLE");
        verifyProperty(oracleModel, "importer.useFullSchemaName", "false");
        verifyProperty(oracleModel, "importer.importKeys", "false");
        verifyProperty(oracleModel, VdbLexicon.Model.METADATA_TYPE, "DDL");
        assertEquals(1, oracleModel.getSources(uow).length);

        // Ddl Sequenced
        verify(oracleModel, "CUSTOMER", JcrConstants.NT_UNSTRUCTURED, TeiidDdlLexicon.CreateTable.TABLE_STATEMENT);
        verify(oracleModel, "LINEITEM", JcrConstants.NT_UNSTRUCTURED, TeiidDdlLexicon.CreateTable.TABLE_STATEMENT);
        verify(oracleModel, "NATION", JcrConstants.NT_UNSTRUCTURED, TeiidDdlLexicon.CreateTable.TABLE_STATEMENT);
        verify(oracleModel, "ORDERS", JcrConstants.NT_UNSTRUCTURED, TeiidDdlLexicon.CreateTable.TABLE_STATEMENT);
        verify(oracleModel, "PARTS", JcrConstants.NT_UNSTRUCTURED, TeiidDdlLexicon.CreateTable.TABLE_STATEMENT);
        verify(oracleModel, "PARTSUPP", JcrConstants.NT_UNSTRUCTURED, TeiidDdlLexicon.CreateTable.TABLE_STATEMENT);
        verify(oracleModel, "REGION", JcrConstants.NT_UNSTRUCTURED, TeiidDdlLexicon.CreateTable.TABLE_STATEMENT);
        verify(oracleModel, "SUPPLIER", JcrConstants.NT_UNSTRUCTURED, TeiidDdlLexicon.CreateTable.TABLE_STATEMENT);        
    }
}
