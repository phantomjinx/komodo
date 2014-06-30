/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.teiid.query.validator.v87;

import org.komodo.spi.runtime.version.ITeiidVersion;
import org.komodo.spi.runtime.version.TeiidVersion.Version;
import org.teiid.query.validator.v86.Test86UpdateValidator;

/**
 *
 */
@SuppressWarnings( "javadoc" )
public class Test87UpdateValidator extends Test86UpdateValidator {

    protected Test87UpdateValidator(ITeiidVersion teiidVersion) {
        super(teiidVersion);
    }

    public Test87UpdateValidator() {
        this(Version.TEIID_8_7.get());
    }

}
