/*
* JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.komodo.rest.relational;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.net.URI;
import java.util.List;
import javax.ws.rs.client.Entity;
import org.junit.Test;
import org.komodo.rest.RestBasicEntity;
import org.komodo.rest.relational.json.KomodoJsonMarshaller;
import org.komodo.spi.repository.KomodoObject;
import org.komodo.spi.repository.KomodoType;
import org.komodo.spi.repository.Repository;
import org.komodo.spi.repository.Repository.UnitOfWork;
import org.modeshape.sequencer.ddl.dialect.teiid.TeiidDdlLexicon;
import org.modeshape.sequencer.teiid.lexicon.VdbLexicon;

@SuppressWarnings( {"javadoc", "nls"} )
public final class KomodoSearchServiceTest extends AbstractKomodoServiceTest {

    private final static String PORTFOLIO_DATA_PATH = "/tko:komodo/tko:workspace/Portfolio";

    @Test
    public void shouldSearchForAnythingContainingView() throws Exception {
        loadVdbs();

        // get
        KomodoProperties properties = new KomodoProperties();
        properties.addProperty(SEARCH_CONTAINS_PARAMETER, "view");
        URI uri = _uriBuilder.generateSearchUri(properties);

        this.response = request(uri).get();
        final String entity = this.response.readEntity(String.class);
        System.out.println("Response:\n" + entity);

        RestBasicEntity[] entities = KomodoJsonMarshaller.unmarshallArray(entity, RestBasicEntity[].class);
        assertEquals(19, entities.length);

        for (RestBasicEntity e : entities) {
            assertNotNull(e.getId());
            assertNotNull(e.getBaseUri());
            assertNotNull(e.getDataPath());
            assertNotNull(e.getkType());
            assertNotNull(e.hasChildren());
            assertNotEquals(KomodoType.UNKNOWN, e.getkType());
        }
    }

    @Test
    public void shouldSearchForAnyModelContainingView() throws Exception {
        loadVdbs();

        // get
        KomodoProperties properties = new KomodoProperties();
        properties.addProperty(SEARCH_TYPE_PARAMETER, VdbLexicon.Vdb.DECLARATIVE_MODEL);
        properties.addProperty(SEARCH_CONTAINS_PARAMETER, "view");
        URI uri = _uriBuilder.generateSearchUri(properties);

        this.response = request(uri).get();
        final String entity = this.response.readEntity(String.class);
        System.out.println("Response:\n" + entity);

        RestBasicEntity[] entities = KomodoJsonMarshaller.unmarshallArray(entity, RestBasicEntity[].class);
        assertEquals(5, entities.length);

        for (RestBasicEntity e : entities) {
            assertNotNull(e.getId());
            assertNotNull(e.getBaseUri());
            assertNotNull(e.getDataPath());
            assertNotNull(e.getkType());
            assertNotNull(e.hasChildren());
            assertEquals(KomodoType.MODEL, e.getkType());
        }
    }

    @Test
    public void shouldSearchForAnyModelUnderPortfolioContainingView() throws Exception {
        loadVdbs();

        // get
        KomodoProperties properties = new KomodoProperties();
        properties.addProperty(SEARCH_TYPE_PARAMETER, VdbLexicon.Vdb.DECLARATIVE_MODEL);
        properties.addProperty(SEARCH_PARENT_PARAMETER, PORTFOLIO_DATA_PATH);
        properties.addProperty(SEARCH_CONTAINS_PARAMETER, "view");
        URI uri = _uriBuilder.generateSearchUri(properties);

        this.response = request(uri).get();
        final String entity = this.response.readEntity(String.class);
        System.out.println("Response:\n" + entity);

        RestBasicEntity[] entities = KomodoJsonMarshaller.unmarshallArray(entity, RestBasicEntity[].class);
        assertEquals(2, entities.length);

        for (RestBasicEntity e : entities) {
            assertNotNull(e.getId());
            assertNotNull(e.getBaseUri());
            assertNotNull(e.getDataPath());
            assertNotNull(e.getkType());
            assertNotNull(e.hasChildren());
            assertEquals(KomodoType.MODEL, e.getkType());
            assertTrue(e.getDataPath().startsWith(PORTFOLIO_DATA_PATH));
        }
    }

    @Test
    public void shouldSearchByPath() throws Exception {
        loadVdbs();

        // get
        KomodoProperties properties = new KomodoProperties();
        properties.addProperty(SEARCH_PATH_PARAMETER, PORTFOLIO_DATA_PATH);
        URI uri = _uriBuilder.generateSearchUri(properties);

        this.response = request(uri).get();
        final String entity = this.response.readEntity(String.class);
        System.out.println("Response:\n" + entity);
        RestBasicEntity[] entities = KomodoJsonMarshaller.unmarshallArray(entity, RestBasicEntity[].class);
        assertEquals(1, entities.length);

        RestBasicEntity basicEntity = entities[0];
        RestVdb vdb = RestEntityFactory.resolve(basicEntity, RestVdb.class);
        assertNotNull(vdb);

        assertPortfolio(vdb);
    }

    @Test
    public void shouldSearchByParent() throws Exception {
        loadVdbs();

        // get
        KomodoProperties properties = new KomodoProperties();
        properties.addProperty(SEARCH_PARENT_PARAMETER, PORTFOLIO_DATA_PATH);
        URI uri = _uriBuilder.generateSearchUri(properties);

        this.response = request(uri).get();
        final String entity = this.response.readEntity(String.class);
        System.out.println("Response:\n" + entity);
        RestBasicEntity[] entities = KomodoJsonMarshaller.unmarshallArray(entity, RestBasicEntity[].class);
        assertEquals(5, entities.length);

        for (RestBasicEntity basicEntity : entities) {
            RestVdbModel model = RestEntityFactory.resolve(basicEntity, RestVdbModel.class);
            assertNotNull(model);
        }
    }

    @Test
    public void shouldSearchByAncestor() throws Exception {
        loadVdbs();

        // get
        KomodoProperties properties = new KomodoProperties();
        properties.addProperty(SEARCH_ANCESTOR_PARAMETER, PORTFOLIO_DATA_PATH);
        URI uri = _uriBuilder.generateSearchUri(properties);

        this.response = request(uri).get();
        final String entity = this.response.readEntity(String.class);
        System.out.println("Response:\n" + entity);
        RestBasicEntity[] entities = KomodoJsonMarshaller.unmarshallArray(entity, RestBasicEntity[].class);
        assertEquals(94, entities.length);

        for (RestBasicEntity basicEntity : entities) {
            System.out.println(basicEntity.getDataPath());
        }
    }

    @Test
    public void shouldSearchByType() throws Exception {
        loadVdbs();

        // get
        KomodoProperties properties = new KomodoProperties();
        properties.addProperty(SEARCH_TYPE_PARAMETER, TeiidDdlLexicon.CreateTable.TABLE_ELEMENT);
        URI uri = _uriBuilder.generateSearchUri(properties);

        this.response = request(uri).get();
        final String entity = this.response.readEntity(String.class);
        System.out.println("Response:\n" + entity);
        RestBasicEntity[] entities = KomodoJsonMarshaller.unmarshallArray(entity, RestBasicEntity[].class);
        assertEquals(38, entities.length);

        for (RestBasicEntity basicEntity : entities) {
            KomodoType kType = basicEntity.getkType();
            assertEquals(KomodoType.COLUMN, kType);
        }
    }

    @Test
    public void shouldSearchByKType() throws Exception {
        loadVdbs();

        // get
        KomodoProperties properties = new KomodoProperties();
        properties.addProperty(SEARCH_TYPE_PARAMETER, KomodoType.COLUMN.getType());
        URI uri = _uriBuilder.generateSearchUri(properties);

        this.response = request(uri).get();
        final String entity = this.response.readEntity(String.class);
        System.out.println("Response:\n" + entity);
        RestBasicEntity[] entities = KomodoJsonMarshaller.unmarshallArray(entity, RestBasicEntity[].class);
        assertEquals(38, entities.length);

        for (RestBasicEntity basicEntity : entities) {
            KomodoType kType = basicEntity.getkType();
            assertEquals(KomodoType.COLUMN, kType);
        }
    }

    @Test
    public void shouldSearchByKTypeAndLocalNameParameter() throws Exception {
        loadVdbs();

        // get
        KomodoProperties properties = new KomodoProperties();
        properties.addProperty(SEARCH_TYPE_PARAMETER, KomodoType.COLUMN.getType());
        properties.addProperty(SEARCH_OBJECT_NAME_PARAMETER, "%ID%");
        URI uri = _uriBuilder.generateSearchUri(properties);

        this.response = request(uri).get();
        final String entity = this.response.readEntity(String.class);
        System.out.println("Response:\n" + entity);
        RestBasicEntity[] entities = KomodoJsonMarshaller.unmarshallArray(entity, RestBasicEntity[].class);
        assertEquals(12, entities.length);

        for (RestBasicEntity basicEntity : entities) {
            KomodoType kType = basicEntity.getkType();
            assertEquals(KomodoType.COLUMN, kType);
            assertTrue(basicEntity.getId().contains("ID"));
            System.out.println(basicEntity.getDataPath());
        }
    }

    @Test
    public void shouldExecuteSavedSearch()  throws Exception {
        loadVdbs();
        List<String> searchNames = loadSampleSearches();

        // get
        KomodoProperties properties = new KomodoProperties();
        properties.addProperty(SEARCH_SAVED_NAME_PARAMETER, searchNames.get(0));
        URI uri = _uriBuilder.generateSearchUri(properties);

        this.response = request(uri).get();
        final String entity = this.response.readEntity(String.class);
        System.out.println("Response:\n" + entity);
        RestBasicEntity[] entities = KomodoJsonMarshaller.unmarshallArray(entity, RestBasicEntity[].class);
        assertEquals(4, entities.length);

        for (RestBasicEntity basicEntity : entities) {
            KomodoType kType = basicEntity.getkType();
            assertEquals(KomodoType.VDB, kType);
            System.out.println(basicEntity.getDataPath());
        }
    }

    @Test
    public void shouldReturnSavedSearches() throws Exception {
        loadVdbs();
        List<String> searchNames = loadSampleSearches();

        // get
        KomodoProperties properties = new KomodoProperties();
        URI uri = _uriBuilder.generateSavedSearchCollectionUri(properties);

        this.response = request(uri).get();
        final String entity = this.response.readEntity(String.class);
        System.out.println("Response:\n" + entity);
        KomodoSavedSearcher[] entities = KomodoJsonMarshaller.unmarshallArray(entity, KomodoSavedSearcher[].class);
        assertEquals(searchNames.size(), entities.length);

        for (KomodoSavedSearcher kso : entities) {
            searchNames.contains(kso.getName());
        }
    }

    @Test
    public void shouldSaveSearch() throws Exception {
        loadVdbs();

        // post
        URI uri = _uriBuilder.generateSavedSearchCollectionUri(new KomodoProperties());

        KomodoSearcherAttributes searchAttr = new KomodoSearcherAttributes();
        String searchName = "Vdbs Search";
        searchAttr.setSearchName(searchName);
        searchAttr.setType(VdbLexicon.Vdb.VIRTUAL_DATABASE);

        this.response = request(uri).post(Entity.json(searchAttr));

        Repository repository = getRestApp().getDefaultRepository();
        UnitOfWork uow = repository.createTransaction(
                                                      getClass().getSimpleName() + COLON + "FindSavedSearches" + COLON + System.currentTimeMillis(),
                                                      true, null);

        KomodoObject searches = repository.komodoSearches(uow);
        KomodoObject[] children = searches.getChildren(uow);
        assertEquals(1, children.length);
        assertEquals(searchName, children[0].getName(uow));
    }

}
