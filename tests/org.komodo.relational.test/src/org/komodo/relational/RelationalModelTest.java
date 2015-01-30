/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.komodo.relational;

import org.junit.Rule;
import org.junit.rules.TestName;
import org.komodo.relational.workspace.WorkspaceManager;
import org.komodo.test.utils.AbstractLocalRepositoryTest;

@SuppressWarnings( {"javadoc"} )
public abstract class RelationalModelTest extends AbstractLocalRepositoryTest {

    private static WorkspaceManager _wsMgr;

    protected static WorkspaceManager getWorkspaceManager() {
        if (_wsMgr == null) {
            _wsMgr = WorkspaceManager.getInstance(_repo);
        }

        return _wsMgr;
    }

    @Rule
    public TestName name = new TestName();

}
