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
package org.komodo.repository.test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.ObservationManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.komodo.core.KomodoLexicon;
import org.komodo.repository.LocalRepository;
import org.komodo.repository.LocalRepository.LocalRepositoryId;
import org.komodo.repository.RepositoryImpl;
import org.komodo.repository.RepositoryImpl.UnitOfWorkImpl;
import org.komodo.spi.KException;
import org.komodo.spi.constants.StringConstants;
import org.komodo.spi.repository.KomodoObject;
import org.komodo.spi.repository.Property;
import org.komodo.spi.repository.Repository.State;
import org.komodo.spi.repository.Repository.UnitOfWork;
import org.komodo.spi.repository.RepositoryClient;
import org.komodo.spi.repository.RepositoryClientEvent;
import org.komodo.test.utils.AbstractLoggingTest;
import org.komodo.test.utils.LocalRepositoryObserver;
import org.komodo.test.utils.NodePathListener;
import org.komodo.utils.FileUtils;
import org.modeshape.jcr.api.observation.Event.Sequencing;

/**
 *
 */
@SuppressWarnings( {"javadoc", "nls"} )
public class TestLocalRepositoryPersistence extends AbstractLoggingTest
                                                                       implements StringConstants, Sequencing {

    private static final String TEST_FILE_DATABASE = System.getProperty("java.io.tmpdir") + 
                                                                                    File.separator
                                                                                    + "TestLocalFileRepoPersistence";

    private static final String TEST_LEVELDB_DATABASE = System.getProperty("java.io.tmpdir") + 
                                                                                    File.separator
                                                                                    + "TestLocalLevelDBRepoPersistence";

    private static final String TEST_FILE_REPOSITORY_CONFIG = "test-local-repository-on-file-config.json";

    private static final String TEST_LEVELDB_REPOSITORY_CONFIG = "test-local-repository-on-leveldb-config.json";

    private static final String PRODUCTION_REPOSITORY_CONFIG = "local-repository-config.json";

    protected LocalRepository _repo = null;

    protected LocalRepositoryObserver _repoObserver = null;

    private File testDb(String dbPath) {
        return new File(dbPath);
    }

    private boolean dbExists(String dbPath) {
        File testDb = testDb(dbPath);
        return testDb.exists();
    }

    private void deleteDbDir(String dbPath) {
        File testDb = testDb(dbPath);
        if (testDb.exists())
            FileUtils.removeDirectoryAndChildren(testDb);
    }

    private void initLocalRepository(Class<?> loaderClass, String configFile) throws Exception {

        URL configUrl = loaderClass.getResource(configFile);
        assertNotNull(configUrl);

        LocalRepositoryId id = new LocalRepositoryId(configUrl, DEFAULT_LOCAL_WORKSPACE_NAME);
        _repo = new LocalRepository(id);
        assertThat(_repo.getState(), is(State.NOT_REACHABLE));
        assertThat(_repo.ping(), is(false));

        _repoObserver = new LocalRepositoryObserver();
        assertNotNull(_repoObserver);
        _repo.addObserver(_repoObserver);

        // Start the repository
        final RepositoryClient client = mock(RepositoryClient.class);
        final RepositoryClientEvent event = RepositoryClientEvent.createStartedEvent(client);
        _repo.notify(event);

        // Wait for the starting of the repository or timeout of 5 minutes
        if (!_repoObserver.getLatch().await(5, TimeUnit.MINUTES)) {
            throw new RuntimeException("Local repository did not start");
        }
    }

    private void initLocalRepository(String configFile) throws Exception {
        initLocalRepository(TestLocalRepositoryPersistence.class, configFile);
    }

    private Session session(UnitOfWork uow) throws Exception {
        if (!(uow instanceof UnitOfWorkImpl))
            throw new Exception("Attempt to extract session from unit of work which is not a UnitOfWorkImpl");

        Session session = ((UnitOfWorkImpl)uow).getSession();
        return session;
    }

    /**
     * @param countdown equivalent to number of sql query expressions to be sequenced
     * @param pathsToBeSequenced wilcarded patterns against which to compare the sequenced nodes
     * @return the latch for awaiting the sequencing
     * @throws Exception
     */
    private CountDownLatch addNodeCreatedListener(UnitOfWork uow, final String... pathsCreated) throws Exception {
        Session session = session(uow);
        ObservationManager manager = session.getWorkspace().getObservationManager();
        assertNotNull(manager);

        final CountDownLatch updateLatch = new CountDownLatch(pathsCreated.length);
        List<String> paths = Arrays.asList(pathsCreated);
        NodePathListener nodePathListener = new NodePathListener(paths, updateLatch);
        manager.addEventListener(nodePathListener, Event.NODE_ADDED, null, true, null, null, false);
        return updateLatch;
    }

    /**
     * Shutdown and destroy repo
     *
     * @throws Exception
     */
    private void destroyLocalRepository() throws Exception {
        assertNotNull(_repo);
        assertNotNull(_repoObserver);

        if (State.REACHABLE.equals(_repo.getState())) {
            _repoObserver.resetLatch();

            RepositoryClient client = mock(RepositoryClient.class);
            RepositoryClientEvent event = RepositoryClientEvent.createShuttingDownEvent(client);
            _repo.notify(event);
        }

        if (! _repoObserver.getLatch().await(1, TimeUnit.MINUTES))
            throw new RuntimeException("Local repository was not stopped");

        _repo.removeObserver(_repoObserver);

        _repoObserver = null;
        _repo = null;
    }

    @Before
    public void setup() {
        deleteDbDir(TEST_FILE_DATABASE);
        assertFalse(dbExists(TEST_FILE_DATABASE));

        deleteDbDir(TEST_LEVELDB_DATABASE);
        assertFalse(dbExists(TEST_LEVELDB_DATABASE));
    }

    @After
    public void cleanup() throws Exception {
        destroyLocalRepository();

        deleteDbDir(TEST_FILE_DATABASE);
        deleteDbDir(TEST_LEVELDB_DATABASE);
    }

    private void helpTestPersistenceWorkspace(String dbPath, String config) throws Exception, KException {
        // Ensure the file store does not already exist
        assertFalse(dbExists(dbPath));
        assertNull(_repo);

        // Initialise the repository
        initLocalRepository(config);
        assertNotNull(_repo);

        UnitOfWork uow = _repo.createTransaction("test-persistence-workspace", false, null);

        Session session = session(uow);
        CountDownLatch latch = addNodeCreatedListener(uow, "/tko:komodo/tko:workspace");

        // Create the komodo workspace
        KomodoObject workspace = _repo.komodoWorkspace(uow);
        assertNotNull(workspace);

        //
        // Save the session to ensure the node created listener fires
        // to let us know that the workspace has been created.
        //
        session.save();

        //
        // Wait for the latch to countdown
        //
        assertTrue(latch.await(3, TimeUnit.MINUTES));

        //
        // Commit transaction to logout out of the session
        //
        uow.commit();

        // Find the workspace to confirm what we expect to happen
        List<KomodoObject> results = _repo.searchByType(null, KomodoLexicon.Workspace.NODE_TYPE);
        assertEquals(1, results.size());
        assertEquals(RepositoryImpl.WORKSPACE_ROOT, results.iterator().next().getAbsolutePath());

        // Shutdown the repository
        destroyLocalRepository();

        // Restart the repository
        initLocalRepository(config);
        assertNotNull(_repo);

        // Find the root and workspace to confirm repo was persisted
        results = _repo.searchByType(null, KomodoLexicon.Komodo.NODE_TYPE);
        assertEquals(1, results.size());

        results = _repo.searchByType(null, KomodoLexicon.Workspace.NODE_TYPE);
        assertEquals(1, results.size());
        assertEquals(RepositoryImpl.WORKSPACE_ROOT, results.iterator().next().getAbsolutePath());
    }

    @Test
    public void testFilePersistenceWorkspace() throws Exception {        
        helpTestPersistenceWorkspace(TEST_FILE_DATABASE, TEST_FILE_REPOSITORY_CONFIG);
    }

    @Test
    public void testLevelDBPersistenceWorkspace() throws Exception {
        helpTestPersistenceWorkspace(TEST_LEVELDB_DATABASE, TEST_LEVELDB_REPOSITORY_CONFIG);
    }

    private void helpTestPersistenceObjects(String dbPath, String config) throws Exception, KException {
        int testObjectCount = 5;

        // Ensure the file store does not already exist
        assertFalse(dbExists(dbPath));
        assertNull(_repo);

        // Initialise the repository
        initLocalRepository(config);
        assertNotNull(_repo);

        setLoggingLevel(Level.FINE);

        UnitOfWork uow = _repo.createTransaction("test-persistence-objects", false, null);

        Session session = session(uow);

        String[] nodePaths = new String[testObjectCount + 1];
        nodePaths[0] = "/tko:komodo/tko:workspace";
        for (int i = 1; i <= testObjectCount; ++i)
            nodePaths[i] = "/tko:komodo/tko:workspace/test" + i;

        CountDownLatch latch = addNodeCreatedListener(uow, nodePaths);

        // Create the komodo workspace
        KomodoObject workspace = _repo.komodoWorkspace(uow);
        assertNotNull(workspace);

        for (int i = 1; i <= testObjectCount; ++i) {
            KomodoObject child = workspace.addChild(uow, "test" + i, KomodoLexicon.VdbModel.NODE_TYPE);
            child.setProperty(uow, KomodoLexicon.VdbModel.METADATA_TYPE, "DDL");
        }

        //
        // Save the session to ensure the node created listener fires
        // to let us know that the workspace has been created.
        //
        session.save();

        //
        // Wait for the latch to countdown
        //
        assertTrue(latch.await(3, TimeUnit.MINUTES));

        //
        // Commit transaction to logout out of the session
        //
        uow.commit();

        // Find the objects to confirm what we expect to happen
        List<KomodoObject> results = _repo.searchByType(null, KomodoLexicon.VdbModel.NODE_TYPE);
        assertEquals(testObjectCount, results.size());

        // Shutdown the repository
        destroyLocalRepository();

        // Restart the repository
        initLocalRepository(config);
        assertNotNull(_repo);

        // Find the test nodes to confirm repo was persisted
        results = _repo.searchByType(null, KomodoLexicon.VdbModel.NODE_TYPE);
        assertEquals(testObjectCount, results.size());

        for (KomodoObject result : results) {
            String name = result.getName(null);
            assertTrue(name.startsWith("test"));

            Property property = result.getProperty(null, KomodoLexicon.VdbModel.METADATA_TYPE);
            assertEquals("DDL", property.getStringValue(null));
        }
    }

    @Test
    public void testFilePersistenceObjects() throws Exception {
        helpTestPersistenceObjects(TEST_FILE_DATABASE, TEST_FILE_REPOSITORY_CONFIG);
    }

    @Test
    public void testLevelDBPersistenceObjects() throws Exception {
        helpTestPersistenceObjects(TEST_LEVELDB_DATABASE, TEST_LEVELDB_REPOSITORY_CONFIG);
    }

    /**
     * Test to ensure that the production repository
     * configuration files are valid and the repository
     * can be successfully started
     *
     * @throws Exception
     */
    @Test
    public void testProductionRepositoryConfiguration() throws Exception {
        initLocalRepository(LocalRepository.class, PRODUCTION_REPOSITORY_CONFIG);
        assertNotNull(_repo);
    }
}
