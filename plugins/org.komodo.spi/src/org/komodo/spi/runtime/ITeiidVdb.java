/*************************************************************************************
 * Copyright (c) 2014 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.komodo.spi.runtime;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public interface ITeiidVdb {

    /**
     * Extension of a vdb file
     */
    static final String VDB_EXTENSION = "vdb"; //$NON-NLS-1$
    
    /**
     * Extension of a vdb file with dot appended
     */
    static final String VDB_DOT_EXTENSION = ".vdb"; //$NON-NLS-1$

    /**
     * Suffix of a dynamic vdb
     */
    static final String DYNAMIC_VDB_SUFFIX = "-vdb.xml"; //$NON-NLS-1$

    /**
     * @return the name
     */
    String getName();

    /**
     * @return deployed name
     */
    String getDeployedName();

    /**
     * @return the version
     */
    int getVersion();

    /**
     * @return <code>true</code> if this is a preview VDB
     */
    boolean isPreviewVdb();

    /**
     * @return <code>true</code> if this VDB is active
     */
    boolean isActive();

    /**
     * @return <code>true</code> if this VDB is loading
     */
    boolean isLoading();

    /**
     * @return <code>true</code> if this VDB failed
     */
    boolean hasFailed();

    /**
     * @return <code>true</code> if this VDB is removed
     */
    boolean wasRemoved();

    /**
     * @return any validity errors
     */
    List<String> getValidityErrors();

    /**
     * Does the VDB contain any models 
     * 
     * @return <code>true</code> if the vdb has any models 
     */
    boolean hasModels();

    /**
     * Get the names of all the models in this vdb
     * 
     * @return {@link Collection} of model names
     */
    Collection<String> getModelNames();

    /**
     * @param key
     * 
     * @return value of property or null
     */
    String getPropertyValue(String key);
}
