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
package org.komodo.relational.model.internal;

import org.komodo.core.KomodoLexicon;
import org.komodo.relational.model.Schema;
import org.komodo.spi.KException;
import org.komodo.spi.repository.Repository;
import org.komodo.spi.repository.Repository.UnitOfWork;

/**
 * A named schema fragment
 */
public class SchemaImpl extends RelationalObjectImpl implements Schema {

    /**
     * @param repository
     *        the repository where the relational object exists (cannot be <code>null</code>)
     * @param workspacePath
     *        the workspace relative path (cannot be empty)
     * @throws KException
     *         if an error occurs
     */
    public SchemaImpl( final Repository repository, final String workspacePath ) throws KException {
        super(repository, workspacePath);
    }

    @Override
    public String getRendition(UnitOfWork uow) throws KException {
        String rendition = getObjectProperty(uow,
                                       String.class,
                                       "getRendition", //$NON-NLS-1$
                                       KomodoLexicon.Schema.RENDITION);
        
        return rendition == null ? EMPTY_STRING : rendition;
    }
    
    @Override
    public void setRendition(UnitOfWork uow, String rendition) throws KException {
        setObjectProperty(uow, "setRendition", KomodoLexicon.Schema.RENDITION, rendition); //$NON-NLS-1$
    }

}
