/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.teiid.query.sql.v85;

import org.komodo.spi.runtime.version.ITeiidVersion;
import org.komodo.spi.runtime.version.TeiidVersion.Version;
import org.teiid.query.sql.v8.Test8Cloning;

/**
 *
 */
@SuppressWarnings( "javadoc" )
public class Test85Cloning extends Test8Cloning {

    protected Test85Cloning(ITeiidVersion teiidVersion) {
        super(teiidVersion);
    }

    public Test85Cloning() {
        this(Version.TEIID_8_5.get());
    }

}
