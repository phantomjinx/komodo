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
package org.komodo.relational.workspace.internal;

import org.komodo.relational.model.Model;
import org.komodo.relational.model.Schema;
import org.komodo.relational.model.internal.RelationalObjectImpl;
import org.komodo.relational.workspace.Workspace;
import org.komodo.relational.workspace.WorkspaceManager;
import org.komodo.spi.KException;
import org.komodo.spi.repository.Repository;
import org.komodo.spi.repository.Repository.UnitOfWork;
import org.komodo.utils.ArgCheck;

/**
 *
 */
public final class WorkspaceImpl extends RelationalObjectImpl implements Workspace {

    /**
     * @param repository the repository the workspace belongs to
     * @throws KException if error occurs
     */
    public WorkspaceImpl(Repository repository) throws KException {
        super(repository, repository.komodoWorkspace(null).getAbsolutePath());
    }

    @Override
    public Model addModel(UnitOfWork uow, String modelName) throws KException {
        ArgCheck.isNotEmpty(modelName, "modelName"); //$NON-NLS-1$
        UnitOfWork transaction = uow;

        if (transaction == null) {
            transaction = getRepository().createTransaction("workspaceimpl-addModel", false, null); //$NON-NLS-1$
        }

        assert (transaction != null);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addModel: transaction = '{0}', model = '{1}'", //$NON-NLS-1$
                         transaction.getName(),
                         modelName);
        }

        try {
            final Model result = WorkspaceManager.createModel(transaction,
                                                                           getRepository(),
                                                                           this,
                                                                           modelName);

            if (uow == null) {
                transaction.commit();
            }

            return result;
        } catch (final Exception e) {
            throw handleError(uow, transaction, e);
        }
    }

    @Override
    public Schema addSchema(UnitOfWork uow, String schemaName, String schemaFragment) throws KException {
        ArgCheck.isNotEmpty(schemaName, "schemaName"); //$NON-NLS-1$
        ArgCheck.isNotEmpty(schemaFragment, "schemaFragment"); //$NON-NLS-1$

        UnitOfWork transaction = uow;

        if (transaction == null) {
            transaction = getRepository().createTransaction("workspaceimpl-addSchema", false, null); //$NON-NLS-1$
        }

        assert (transaction != null);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addModel: transaction = '{0}', schema = '{1}', contents = '{2}'", //$NON-NLS-1$
                         transaction.getName(),
                         schemaName,
                         schemaFragment);
        }

        try {
            final Schema result = WorkspaceManager.createSchema(transaction,
                                                                           getRepository(),
                                                                           this,
                                                                           schemaName);
            result.setRendition(transaction, schemaFragment);
            
            if (uow == null) {
                transaction.commit();
            }

            return result;
        } catch (final Exception e) {
            throw handleError(uow, transaction, e);
        }        
    }

    Teiid addTeiid(UnitOfWork uow, String id) {

    }
}
