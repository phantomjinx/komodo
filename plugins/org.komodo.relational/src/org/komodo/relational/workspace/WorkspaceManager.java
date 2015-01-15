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
package org.komodo.relational.workspace;

import org.komodo.core.KomodoLexicon;
import org.komodo.relational.model.Model;
import org.komodo.relational.model.Schema;
import org.komodo.relational.model.internal.ModelImpl;
import org.komodo.relational.model.internal.SchemaImpl;
import org.komodo.relational.workspace.internal.WorkspaceImpl;
import org.komodo.spi.KException;
import org.komodo.spi.repository.KomodoObject;
import org.komodo.spi.repository.Repository;
import org.komodo.spi.repository.Repository.UnitOfWork;
import org.komodo.utils.ArgCheck;

/**
 *
 */
public class WorkspaceManager {

    private static WorkspaceManager instance;

    /**
     * @return singleton instance
     */
    public static WorkspaceManager getInstance() {
        if (instance == null)
            instance = new WorkspaceManager();

        return instance;
    }

    private WorkspaceManager() {}

    /**
     * Only one of the {@link UnitOfWork transactions} passed in should be non-<code>null</code>. Ensures that a transaction
     * rollback occurs if the transaction was constructed within the method.
     *
     * @param transactionParameter
     *        the transaction passed into the method (can be <code>null</code>)
     * @param transactionVariable
     *        the transaction constructed within the method (can be <code>null</code>)
     * @param e
     *        the error being handled (cannot be <code>null</code>)
     * @return the error passed in if already a {@link KException} or the error passed in wrapped in a {@link KException}
     */
    private static KException handleError( final UnitOfWork transactionParameter,
                                           final UnitOfWork transactionVariable,
                                           final Exception e ) {
        assert (e != null);
        assert ((transactionParameter == null) && (transactionVariable != null))
        || ((transactionParameter != null) && (transactionVariable == null));

        if (transactionParameter == null) {
            transactionVariable.rollback();
        }

        if (e instanceof KException) {
            return (KException)e;
        }

        return new KException(e);
    }

    /**
     * @param repository the repository
     * @return workspace for the repository
     * @throws KException if error occurs
     */
    public Workspace getWorkspace(Repository repository) throws KException {
        return new WorkspaceImpl(repository);
    }

    /**
     * @param uow
     *        the transaction (can be <code>null</code> if update should be automatically committed)
     * @param repository
     *        the repository where the model object will be created (cannot be <code>null</code>)
     * @param parent
     *        the parent of the model object being created (cannot be <code>null</code>)
     * @param modelName
     *        the name of the model to create (cannot be empty)
     * @return the model object (never <code>null</code>)
     * @throws KException
     *         if an error occurs
     */
    public static Model createModel(UnitOfWork uow, Repository repository, KomodoObject parent, String modelName) throws KException {
        ArgCheck.isNotNull(repository, "repository"); //$NON-NLS-1$
        ArgCheck.isNotNull(parent, "parent"); //$NON-NLS-1$
        ArgCheck.isNotEmpty(modelName, "modelName"); //$NON-NLS-1$

        UnitOfWork transaction = uow;

        if (uow == null) {
            transaction = repository.createTransaction("workspacemanager-createModel", false, null); //$NON-NLS-1$
        }

        assert (transaction != null);

        try {
            final KomodoObject kobject = repository.add(transaction,
                                                        parent.getAbsolutePath(),
                                                        modelName,
                                                        KomodoLexicon.VdbModel.NODE_TYPE);

            if (uow == null) {
                transaction.commit();
            }

            return new ModelImpl(repository, kobject.getAbsolutePath());
        } catch (final Exception e) {
            throw handleError(uow, transaction, e);
        }
    }

    /**
     * @param uow
     *        the transaction (can be <code>null</code> if update should be automatically committed)
     * @param repository
     *        the repository where the model object will be created (cannot be <code>null</code>)
     * @param parent
     *        the parent of the schema object being created (cannot be <code>null</code>)
     * @param schemaName
     *        the name of the schema to create (cannot be empty)
     * @return the schema object (never <code>null</code>)
     * @throws KException
     *         if an error occurs
     */    public static Schema createSchema(UnitOfWork uow, Repository repository, KomodoObject parent, String schemaName) throws KException {
        ArgCheck.isNotNull(repository, "repository"); //$NON-NLS-1$
        ArgCheck.isNotNull(parent, "parent"); //$NON-NLS-1$
        ArgCheck.isNotEmpty(schemaName, "schemaName"); //$NON-NLS-1$

        UnitOfWork transaction = uow;

        if (uow == null) {
            transaction = repository.createTransaction("workspacemanager-createSchema", false, null); //$NON-NLS-1$
        }

        assert (transaction != null);

        try {
            final KomodoObject kobject = repository.add(transaction,
                                                        parent.getAbsolutePath(),
                                                        schemaName,
                                                        KomodoLexicon.Schema.NODE_TYPE);

            if (uow == null) {
                transaction.commit();
            }

            return new SchemaImpl(repository, kobject.getAbsolutePath());
        } catch (final Exception e) {
            throw handleError(uow, transaction, e);
        }
    }
}
