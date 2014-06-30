/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.teiid.query.resolver.v86;

import org.komodo.spi.runtime.version.ITeiidVersion;
import org.komodo.spi.runtime.version.TeiidVersion.Version;
import org.teiid.query.resolver.v85.Test85AccessPattern;

/**
 *
 */
@SuppressWarnings( "javadoc" )
public class Test86AccessPattern extends Test85AccessPattern {

    protected Test86AccessPattern(ITeiidVersion teiidVersion) {
        super(teiidVersion);
    }

    public Test86AccessPattern() {
        this(Version.TEIID_8_6.get());
    }

}
