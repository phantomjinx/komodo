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

import org.komodo.relational.model.Model;
import org.komodo.relational.model.Schema;
import org.komodo.spi.KException;
import org.komodo.spi.repository.Repository.UnitOfWork;

/**
 *
 */
public interface Workspace {

    /**
     * @param uow the transaction
     * @param modelName the name of the model
     * @return the new model
     * @throws KException if error occurs
     */
    Model addModel(UnitOfWork uow, String modelName) throws KException;

    /**
     * @param uow the transaction
     * @param schemaName name of the schema
     * @param schemaFragment fragment of schema
     * @return the new schema
     * @throws KException if error occurs
     */
    Schema addSchema(UnitOfWork uow, String schemaName, String schemaFragment) throws KException;

}
