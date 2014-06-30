/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.teiid.query.resolver.v85;

import org.komodo.spi.runtime.version.ITeiidVersion;
import org.komodo.spi.runtime.version.TeiidVersion.Version;
import org.teiid.query.resolver.v8.Test8XMLResolver;

/**
 *
 */
@SuppressWarnings( "javadoc" )
public class Test85XMLResolver extends Test8XMLResolver {

    protected Test85XMLResolver(ITeiidVersion teiidVersion) {
        super(teiidVersion);
    }

    public Test85XMLResolver() {
        this(Version.TEIID_8_5.get());
    }

}
