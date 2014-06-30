/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.teiid.query.resolver.v7;

import org.komodo.spi.runtime.version.TeiidVersion.Version;
import org.teiid.query.resolver.AbstractTestAccessPattern;
import org.teiid.query.sql.AbstractTestFactory;
import org.teiid.query.sql.v7.Test7Factory;

/**
 *
 */
public class Test7AccessPattern extends AbstractTestAccessPattern {

    private Test7Factory factory;

    /**
     *
     */
    public Test7AccessPattern() {
        super(Version.TEIID_7_7.get());
    }

    @Override
    protected AbstractTestFactory getFactory() {
        if (factory == null)
            factory = new Test7Factory(getQueryParser());

        return factory;
    }
}
