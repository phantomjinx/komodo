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
package org.komodo.relational.teiid.internal;

import org.komodo.relational.model.internal.RelationalObjectImpl;
import org.komodo.relational.teiid.Teiid;
import org.komodo.repository.RepositoryImpl.UnitOfWorkImpl;
import org.komodo.spi.KException;
import org.komodo.spi.repository.Repository;
import org.komodo.spi.runtime.EventManager;
import org.komodo.spi.runtime.TeiidInstance;
import org.komodo.spi.runtime.TeiidParent;
import org.komodo.spi.repository.Repository.UnitOfWork;
import org.komodo.utils.KLog;

/**
 * Implementation of teiid instance model
 */
public class TeiidImpl extends RelationalObjectImpl implements Teiid, TeiidParent {

    /**
     * @param repository the repository
     * @param path the path
     * @throws KException if error occurs
     */
    protected TeiidImpl(Repository repository, String path) throws KException {
        super(repository, path);
    }

    public String getHost(UnitOfWork uow) {
        return getObjectProperty(uow, propertyType, getterName, propertyPath)
    }

    @Override
    public String getHost() {
        return getHost(null);
    }

    @Override
    public Object getParentObject() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getName() {
        try {
            return this.getName(null);
        } catch (KException ex) {
            KLog.getLogger().error(ex.getLocalizedMessage(), ex);
            return null;
        }
    }

    @Override
    public TeiidInstance getTeiidInstance() {
        return null;
    }

    @Override
    public void setTeiidInstance(TeiidInstance teiidInstance) {
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public EventManager getEventManager() {
        return null;
    }

}
