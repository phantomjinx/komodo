/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.komodo.relational.commands.virtualprocedure;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.komodo.relational.commands.AbstractCommandTest;
import org.komodo.relational.model.Model;
import org.komodo.relational.model.Parameter;
import org.komodo.relational.model.Procedure;
import org.komodo.relational.model.VirtualProcedure;
import org.komodo.relational.vdb.Vdb;
import org.komodo.relational.workspace.WorkspaceManager;
import org.komodo.shell.api.CommandResult;

/**
 * Test Class to test {@link DeleteParameterCommand}.
 */
@SuppressWarnings( {"javadoc", "nls"} )
public final class DeleteParameterCommandTest extends AbstractCommandTest {

    @Test
    public void testDelete1() throws Exception {
        final String[] commands = {
            "create-vdb myVdb vdbPath",
            "cd myVdb",
            "add-model myModel",
            "cd myModel",
            "add-virtual-procedure myVirtualProcedure",
            "cd myVirtualProcedure",
            "add-parameter myParameter1",
            "add-parameter myParameter2",
            "delete-parameter myParameter1" };
        final CommandResult result = execute( commands );
        assertCommandResultOk(result);

        WorkspaceManager wkspMgr = WorkspaceManager.getInstance(_repo, getTransaction());
        Vdb[] vdbs = wkspMgr.findVdbs(getTransaction());

        assertEquals(1, vdbs.length);

        Model[] models = vdbs[0].getModels(getTransaction());
        assertEquals(1, models.length);
        assertEquals("myModel", models[0].getName(getTransaction())); //$NON-NLS-1$

        Procedure[] procs = models[0].getProcedures(getTransaction());
        assertEquals(1, procs.length);
        assertEquals(true, procs[0] instanceof VirtualProcedure);
        assertEquals("myVirtualProcedure", procs[0].getName(getTransaction())); //$NON-NLS-1$

        Parameter[] params = procs[0].getParameters(getTransaction());
        assertEquals(1, params.length);
        assertEquals("myParameter2", params[0].getName(getTransaction())); //$NON-NLS-1$
    }

    @Test
    public void testTabCompleter()throws Exception{
    	ArrayList<CharSequence> candidates=new ArrayList<>();
    	setup("commandFiles","addVirtualProcParams.cmd");

    	candidates.add("myParameter1");
    	candidates.add("myParameter1");
    	assertTabCompletion("delete-parameter myP", candidates);

    	candidates.add("MyParameter3");
    	assertTabCompletion("delete-parameter ", candidates);
    }

}
