/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.komodo.relational.commands.virtualprocedure;

import java.util.HashMap;
import java.util.Map;
import org.komodo.relational.model.VirtualProcedure;
import org.komodo.relational.model.internal.VirtualProcedureImpl;
import org.komodo.shell.api.ShellCommand;
import org.komodo.shell.api.ShellCommandProvider;
import org.komodo.shell.api.WorkspaceStatus;
import org.komodo.spi.KException;
import org.komodo.spi.repository.KomodoObject;
import org.komodo.spi.repository.Repository;

/**
 * A shell command provider for VirtualProcedures.
 */
public class VirtualProcedureCommandProvider implements ShellCommandProvider {

    /**
     * Constructs a command provider for VirtualProcedure shell commands.
     */
    public VirtualProcedureCommandProvider() {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.api.ShellCommandProvider#provideCommands()
     */
    @Override
    public Map< String, Class< ? extends ShellCommand >> provideCommands() {
        final Map< String, Class< ? extends ShellCommand >> result = new HashMap<>();

        result.put( AddParameterCommand.NAME, AddParameterCommand.class );

        result.put( DeleteParameterCommand.NAME, DeleteParameterCommand.class );

//        result.put( SetTablePropertyCommand.NAME, SetTablePropertyCommand.class );

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.api.ShellCommandProvider#resolve(org.komodo.spi.repository.Repository.UnitOfWork,
     *      org.komodo.spi.repository.KomodoObject)
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public VirtualProcedure resolve ( final Repository.UnitOfWork uow, final KomodoObject kObj ) throws KException {
        if(VirtualProcedureImpl.RESOLVER.resolvable(uow, kObj)) {
            return VirtualProcedureImpl.RESOLVER.resolve(uow, kObj);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.api.ShellCommandProvider#isRoot(org.komodo.spi.repository.Repository.UnitOfWork,
     *      org.komodo.spi.repository.KomodoObject)
     */
    @Override
    public boolean isRoot ( final Repository.UnitOfWork uow, final KomodoObject kObj ) throws KException {
        if(VirtualProcedureImpl.RESOLVER.resolvable(uow, kObj)) {
            return false;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.api.ShellCommandProvider#getTypeDisplay(org.komodo.spi.repository.Repository.UnitOfWork,
     *      org.komodo.spi.repository.KomodoObject)
     */
    @Override
    public String getTypeDisplay ( final Repository.UnitOfWork uow, final KomodoObject kObj ) throws KException {
        if(VirtualProcedureImpl.RESOLVER.resolvable(uow, kObj)) {
            VirtualProcedure proc = VirtualProcedureImpl.RESOLVER.resolve(uow, kObj);
            return proc.getTypeDisplayName();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.api.ShellCommandProvider#getStatusMessage(org.komodo.spi.repository.Repository.UnitOfWork,
     *      org.komodo.spi.repository.KomodoObject)
     */
    @Override
    public String getStatusMessage ( final Repository.UnitOfWork uow, final KomodoObject kObj ) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.api.ShellCommandProvider#initWorkspaceState(org.komodo.shell.api.WorkspaceStatus)
     */
    @Override
    public void initWorkspaceState(WorkspaceStatus wsStatus) {
        // Init any workspace state
    }

}
