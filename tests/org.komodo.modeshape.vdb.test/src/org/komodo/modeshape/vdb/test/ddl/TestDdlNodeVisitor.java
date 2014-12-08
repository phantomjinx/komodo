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
package org.komodo.modeshape.vdb.test.ddl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import org.junit.Test;
import org.komodo.modeshape.teiid.parser.TeiidSQLConstants;
import org.komodo.modeshape.test.utils.AbstractSequencerTest;
import org.komodo.modeshape.visitor.DdlNodeVisitor;
import org.modeshape.jcr.api.JcrConstants;
import org.modeshape.sequencer.ddl.StandardDdlLexicon;

/**
 *
 */
@SuppressWarnings({"nls", "javadoc"})
public class TestDdlNodeVisitor extends AbstractSequencerTest {

    protected Node sequenceSql(String text) throws Exception {
        Node fileNode = prepareSequence(text, SequenceType.DDL);

        Node contentNode = fileNode.getNode(JcrConstants.JCR_CONTENT);
        assertNotNull(contentNode);

        Property content = contentNode.getProperty(JcrConstants.JCR_DATA);
        assertNotNull(content);

        // Run the sequencer manually
        boolean success = session().sequence(SequenceType.DDL.getSequencerName(), content, fileNode);
        assertTrue(success);

        session().save();
        return fileNode;
    }

    /*
     * Since the options arguments can occur in any order when the visitor
     * visits them, we need to break the line down and compare the arguments
     * themselves rather than rely on a simple options1.equals(options2)
     */
    protected void compareOptions(String visLine, String ddlLine) {
        String ddlOptions = ddlLine.substring(ddlLine.indexOf(TeiidSQLConstants.Reserved.OPTIONS));
        String visOptions = visLine.substring(visLine.indexOf(TeiidSQLConstants.Reserved.OPTIONS));

        String optionsPattern = "OPTIONS \\(([A-Z]+ [']?[a-zA-Z0-9 ]+[']?(, )?)+\\)[;|,]?";
        assertTrue("Test DDL Options do not match expected pattern: " + NEW_LINE + ddlOptions, ddlOptions.matches(optionsPattern));
        assertTrue("Visitor Options do not match expected pattern: " + NEW_LINE + visOptions, visOptions.matches(optionsPattern));

        // Remove the OPTIONS SPACE and BRACKETS
        String optionsPrefixPattern = "OPTIONS \\(";
        ddlOptions = ddlOptions.replaceAll(optionsPrefixPattern, EMPTY_STRING);
        visOptions = visOptions.replaceAll(optionsPrefixPattern, EMPTY_STRING);

        String optionsPostFixPattern = "\\)[;|,]?";
        ddlOptions = ddlOptions.replaceAll(optionsPostFixPattern, EMPTY_STRING);
        visOptions = visOptions.replaceAll(optionsPostFixPattern, EMPTY_STRING);

        // Should leave just the "key 'value', key 'value', key TRUE|FALSE        
        String[] ddlTokens = ddlOptions.split(COMMA);
        String[] visTokens = visOptions.split(COMMA);

        List<String> ddlTokenList = new ArrayList<String>();
        List<String> visTokenList = new ArrayList<String>();

        for (String ddlToken : ddlTokens) {
            ddlTokenList.add(ddlToken.trim());
        }

        for (String visToken : visTokens) {
            visTokenList.add(visToken.trim());
        }

        assertEquals("Visitor tokens do not equal the options expected from the test list:" + NEW_LINE + 
                     "DDL List -->" + ddlTokenList + NEW_LINE +
                     "Visitor List --> " + visTokenList, ddlTokenList.size(), visTokenList.size());

        assertTrue("Visitor tokens do not contain all the options expected from the test list:" + NEW_LINE + 
                   "DDL List -->" + ddlTokenList + NEW_LINE +
                   "Visitor List --> " + visTokenList, visTokenList.containsAll(ddlTokenList));
    }

    private void compare(String ddl, DdlNodeVisitor visitor) {
        assertNotNull(visitor);

        String visitorDDL = visitor.getDdl();
        assertNotNull(visitorDDL);

        String[] visLines = visitorDDL.split(NEW_LINE);
        String[] ddlLines = ddl.split(NEW_LINE);

        for (int i = 0; i < visLines.length; ++i) {
            String visLine = visLines[i];
            String ddlLine = ddlLines[i];

            if (visLine.equals(ddlLine))
                continue;

            if (! visLine.contains(TeiidSQLConstants.Reserved.OPTIONS))
                fail("Visitor output did not match ddl: DDL -->" + NEW_LINE + ddl + NEW_LINE + "VISITOR --> " + NEW_LINE + visitorDDL);

            //
            // Options are the wrong way around
            //
            compareOptions(visLine, ddlLine);
        }
    }

    private void helpTest(String ddl, String expected) throws Exception, RepositoryException {
        Node fileNode = sequenceSql(ddl);
        Node ddlStmtsNode = verify(fileNode, StandardDdlLexicon.STATEMENTS_CONTAINER);
        traverse(fileNode);

        DdlNodeVisitor visitor = new DdlNodeVisitor(getTeiidVersion());
        visitor.visit(ddlStmtsNode);

        compare(expected, visitor);
    }

    @Test
    public void testForeignTable() throws Exception {
        
        String ddl = "CREATE FOREIGN TABLE G1 (" + NEW_LINE + 
                TAB + "e1 integer," + NEW_LINE +
                TAB + "e2 string(10)," + NEW_LINE +
                TAB + "e3 date NOT NULL," + NEW_LINE + 
                TAB + "e4 bigdecimal(12,3)," + NEW_LINE + 
                TAB + "e5 integer AUTO_INCREMENT OPTIONS (UUID 'uuid', NAMEINSOURCE 'nis', SELECTABLE FALSE)," + NEW_LINE + 
                TAB + "e6 string DEFAULT 'hello'," + NEW_LINE +
                TAB + "PRIMARY KEY(e1)," + NEW_LINE +
                TAB + "UNIQUE(e2)," + NEW_LINE +
                TAB + "UNIQUE(e3)," + NEW_LINE +
                TAB + "INDEX(e5)," + NEW_LINE +
                TAB + "INDEX(e6)" + NEW_LINE +
                ") OPTIONS (ANNOTATION 'Test Table', CARDINALITY '12', FOO 'BAR', UPDATABLE 'true', UUID 'uuid2');";
        
        helpTest(ddl, ddl);
    }
    
    @Test
    public void testMultiKeyPK() throws Exception {
        String ddl = "CREATE FOREIGN TABLE G1 (" + NEW_LINE + 
                TAB + "e1 integer," + NEW_LINE + 
                TAB + "e2 varchar," + NEW_LINE + 
                TAB + "e3 date," + NEW_LINE + 
                TAB + "PRIMARY KEY(e1, e2)" + NEW_LINE + 
                ");";

        helpTest(ddl, ddl);
    }

    @Test
    public void testConstraints2() throws Exception {
        String ddl = "CREATE FOREIGN TABLE G1 (" + NEW_LINE + 
                TAB + "e1 integer," + NEW_LINE + 
                TAB + "e2 varchar," + NEW_LINE + 
                TAB + "e3 date," + NEW_LINE + 
                TAB + "ACCESSPATTERN(e1)," + NEW_LINE +
                TAB + "ACCESSPATTERN(e2, e3)," + NEW_LINE +
                TAB + "UNIQUE(e1) OPTIONS (x 'true')" + NEW_LINE + 
                ");";
        helpTest(ddl, ddl);
    }
    
    @Test
    public void testFK() throws Exception {        
        String ddl = "CREATE FOREIGN TABLE G1 (" + NEW_LINE + 
                TAB + "\"g1-e1\" integer," + NEW_LINE + 
                TAB + "g1e2 varchar," + NEW_LINE + 
                TAB + "PRIMARY KEY(\"g1-e1\", g1e2)" + NEW_LINE + 
                ");" + NEW_LINE + 
                EMPTY_STRING + NEW_LINE + 
                "CREATE FOREIGN TABLE G2 (" + NEW_LINE + 
                TAB + "g2e1 integer," + NEW_LINE + 
                TAB + "g2e2 varchar," + NEW_LINE + 
                TAB + "FOREIGN KEY(g2e1, g2e2) REFERENCES G1 (\"g1-e1\", g1e2)" + NEW_LINE + 
                ");";

        helpTest(ddl, ddl);
    }   
//    
//    @Test
//    public void testOptionalFK() throws Exception {
//        String ddl = "CREATE FOREIGN TABLE \"G1+\"(g1e1 integer, g1e2 varchar, PRIMARY KEY(g1e1, g1e2));" + NEW_LINE +
//                "CREATE FOREIGN TABLE G2( g2e1 integer, g2e2 varchar, PRIMARY KEY(g2e1, g2e2)," +
//                "FOREIGN KEY (g2e1, g2e2) REFERENCES G1)";
//        String expected = "CREATE FOREIGN TABLE \"G1+\" (" + NEW_LINE + 
//                TAB + "g1e1 integer," + NEW_LINE + 
//                TAB + "g1e2 string," + NEW_LINE + 
//                TAB + "PRIMARY KEY(g1e1, g1e2)" + NEW_LINE + 
//                ");" + NEW_LINE + 
//                "" + NEW_LINE + 
//                "CREATE FOREIGN TABLE G2 (" + NEW_LINE + 
//                TAB + "g2e1 integer," + NEW_LINE + 
//                TAB + "g2e2 string," + NEW_LINE + 
//                TAB + "PRIMARY KEY(g2e1, g2e2)," + NEW_LINE + "  FOREIGN KEY(g2e1, g2e2) REFERENCES G1 " + NEW_LINE + 
//                ");";
//        helpTest(ddl, expected);
//    }   
//
//    @Test
//    public void testFKWithOptions() throws Exception {
//        String ddl = "CREATE FOREIGN TABLE \"G1+\"(g1e1 integer, g1e2 varchar, PRIMARY KEY(g1e1, g1e2));" + NEW_LINE +
//                "CREATE FOREIGN TABLE G2( g2e1 integer, g2e2 varchar, PRIMARY KEY(g2e1, g2e2)," +
//                "FOREIGN KEY (g2e1, g2e2) REFERENCES G1 OPTIONS (NAMEINSOURCE 'g1Relationship'))  ";
//        String expected = "CREATE FOREIGN TABLE \"G1+\" (" + NEW_LINE + 
//                TAB + "g1e1 integer," + NEW_LINE + 
//                TAB + "g1e2 string," + NEW_LINE + 
//                TAB + "PRIMARY KEY(g1e1, g1e2)" + NEW_LINE + 
//                ");" + NEW_LINE + 
//                "" + NEW_LINE + 
//                "CREATE FOREIGN TABLE G2 (" + NEW_LINE + 
//                TAB + "g2e1 integer," + NEW_LINE + 
//                TAB + "g2e2 string," + NEW_LINE + 
//                TAB + "PRIMARY KEY(g2e1, g2e2)," + NEW_LINE + "  FOREIGN KEY(g2e1, g2e2) REFERENCES G1  OPTIONS (NAMEINSOURCE 'g1Relationship')" + NEW_LINE + 
//                ");";
//        helpTest(ddl, expected);
//    }       
//    
//    @Test
//    public void testMultipleCommands() throws Exception {
//        String ddl = "CREATE VIEW V1 AS SELECT * FROM PM1.G1 " +
//                "CREATE PROCEDURE FOO(P1 integer) RETURNS (e1 integer, e2 varchar) AS SELECT * FROM PM1.G1;";
//        String expected = "CREATE VIEW V1" + NEW_LINE + 
//                "AS" + NEW_LINE + 
//                "SELECT * FROM PM1.G1;" + NEW_LINE + 
//                "" + NEW_LINE + 
//                "CREATE VIRTUAL PROCEDURE FOO(IN P1 integer) RETURNS TABLE (e1 integer, e2 string)" + NEW_LINE + 
//                "AS" + NEW_LINE + 
//                "SELECT * FROM PM1.G1;";
//        helpTest(ddl, expected);
//        
//    }   
//    
//    @Test 
//    public void testView() throws Exception {
//        String ddl = "CREATE View G1( e1 integer, e2 varchar) OPTIONS (CARDINALITY 1234567890123) AS select e1, e2 from foo.bar";
//        String expected = "CREATE VIEW G1 (" + NEW_LINE + 
//                TAB + "e1 integer," + NEW_LINE + 
//                TAB + "e2 string" + NEW_LINE + 
//                ") OPTIONS (CARDINALITY 1234567954432)" + NEW_LINE + 
//                "AS" + NEW_LINE + 
//                "SELECT e1, e2 FROM foo.bar;";
//        helpTest(ddl, expected);
//    }   
//    
//    @Test
//    public void testInsteadOfTrigger() throws Exception {
//        String ddl =    "CREATE VIEW G1( e1 integer, e2 varchar) AS select * from foo;" +
//                        "CREATE TRIGGER ON G1 INSTEAD OF INSERT AS " +
//                        "FOR EACH ROW " + NEW_LINE +
//                        "BEGIN ATOMIC " + NEW_LINE +
//                        "insert into g1 (e1, e2) values (1, 'trig');" + NEW_LINE +
//                        "END;";
//
//        String expected = "CREATE VIEW G1 (" + NEW_LINE + 
//                TAB + "e1 integer," + NEW_LINE + 
//                TAB + "e2 string" + NEW_LINE + 
//                ")" + NEW_LINE + 
//                "AS" + NEW_LINE + 
//                "SELECT * FROM foo;" + NEW_LINE + 
//                "" + NEW_LINE + 
//                "CREATE TRIGGER ON G1 INSTEAD OF INSERT AS" + NEW_LINE + 
//                "FOR EACH ROW" + NEW_LINE + 
//                "BEGIN ATOMIC" + NEW_LINE + 
//                "INSERT INTO g1 (e1, e2) VALUES (1, 'trig');" + NEW_LINE + 
//                "END;";
//        helpTest(ddl, expected);
//    }   
//    
//    @Test
//    public void testSourceProcedure() throws Exception {
//        String ddl = "CREATE FOREIGN PROCEDURE myProc(OUT p1 boolean, p2 varchar, INOUT p3 decimal) " +
//                "RETURNS (r1 varchar, r2 decimal)" +
//                "OPTIONS(RANDOM 'any', UUID 'uuid', NAMEINSOURCE 'nis', ANNOTATION 'desc', UPDATECOUNT '2');";
//        
//        String expected = "CREATE FOREIGN PROCEDURE myProc(OUT p1 boolean, IN p2 string, INOUT p3 bigdecimal) RETURNS TABLE (r1 string, r2 bigdecimal)" + NEW_LINE + 
//                "OPTIONS (UUID 'uuid', ANNOTATION 'desc', NAMEINSOURCE 'nis', UPDATECOUNT 2, RANDOM 'any')";
//        helpTest(ddl, expected);        
//    }   
//    
//    @Test
//    public void testPushdownFunctionNoArgs() throws Exception {
//        String ddl = "CREATE FOREIGN FUNCTION SourceFunc() RETURNS integer OPTIONS (UUID 'hello world')";
//        String expected = "CREATE FOREIGN FUNCTION SourceFunc() RETURNS integer" + NEW_LINE + 
//                "OPTIONS (UUID 'hello world');";
//        helpTest(ddl, expected);
//    }   
//    
//    @Test
//    public void testNonPushdownFunction() throws Exception {
//        String ddl = "CREATE FUNCTION SourceFunc(p1 integer, p2 varchar) RETURNS integer OPTIONS (JAVA_CLASS 'foo', JAVA_MEHTOD 'bar')";
//        String expected = "CREATE VIRTUAL FUNCTION SourceFunc(p1 integer, p2 string) RETURNS integer" + NEW_LINE + 
//                "OPTIONS (JAVA_CLASS 'foo', JAVA_MEHTOD 'bar');";
//        helpTest(ddl, expected);
//    }
//
//    private void helpTest(String ddl, String expected) {
//        Schema s = TestDDLParser.helpParse(ddl, "model").getSchema();
//        String metadataDDL = DDLStringVisitor.getDDLString(s, null, null);
//        assertEquals(expected, metadataDDL);
//    }   
//    
//    @Test
//    public void testSourceProcedureVariadic() throws Exception {
//        String ddl = "CREATE FOREIGN PROCEDURE myProc(OUT p1 boolean, VARIADIC p3 decimal) " +
//                "RETURNS (r1 varchar, r2 decimal);";
//        
//        String expected = "CREATE FOREIGN PROCEDURE myProc(OUT p1 boolean, VARIADIC p3 bigdecimal) RETURNS TABLE (r1 string, r2 bigdecimal)";
//        helpTest(ddl, expected);        
//    }   
//    
//    @Test public void testViewFBI() throws Exception {
//        String ddl = "CREATE View G1( \"a e1\" integer, \"a e2\" varchar, INDEX (\"a e1\", upper(\"a e2\"))) AS select e1, e2 from foo.bar";
//        String expected = "CREATE VIEW G1 (" + NEW_LINE + "   \"a e1\" integer," + NEW_LINE + " \"a e2\" string," + NEW_LINE + "  INDEX(\"a e1\", upper(\"a e2\"))" + NEW_LINE + ")" + NEW_LINE + "AS" + NEW_LINE + "SELECT e1, e2 FROM foo.bar;";
//        helpTest(ddl, expected);
//    }
//    
//    @Test public void testNamespaces() throws Exception {
//        String ddl = "set namespace 'some long thing' as x; CREATE View G1(a integer, b varchar) options (\"teiid_rel:x\" false, \"x:z\" 'stringval') AS select e1, e2 from foo.bar";
//        String expected = "SET NAMESPACE 'http://www.teiid.org/ext/relational/2012' AS teiid_rel;" + NEW_LINE + "SET NAMESPACE 'some long thing' AS n1;" + NEW_LINE + "" + NEW_LINE + "CREATE VIEW G1 (" + NEW_LINE + " a integer," + NEW_LINE + "    b string" + NEW_LINE + ") OPTIONS (\"teiid_rel:x\" 'false', \"n1:z\" 'stringval')" + NEW_LINE + "AS" + NEW_LINE + "SELECT e1, e2 FROM foo.bar;";
//        helpTest(ddl, expected);
//    }
//    
//    @Test public void testGlobalTemporaryTable() throws Exception {
//        String ddl = "create global temporary table myTemp (x string, y serial, primary key (x))";
//        String expected = "CREATE GLOBAL TEMPORARY TABLE myTemp (" + NEW_LINE + " x string," + NEW_LINE + " y SERIAL," + NEW_LINE + " PRIMARY KEY(x)" + NEW_LINE + ")";
//        helpTest(ddl, expected);
//    }
//    
//    @Test public void testArrayTypes() throws Exception {
//        String ddl = "CREATE FOREIGN PROCEDURE myProc(OUT p1 boolean, p2 varchar, INOUT p3 decimal) " +
//                "RETURNS (r1 varchar(100)[], r2 decimal[][])";
//        
//        String expected = "CREATE FOREIGN PROCEDURE myProc(OUT p1 boolean, IN p2 string, INOUT p3 bigdecimal) RETURNS TABLE (r1 string(100)[], r2 bigdecimal[][])";
//        helpTest(ddl, expected);        
//    }

}
