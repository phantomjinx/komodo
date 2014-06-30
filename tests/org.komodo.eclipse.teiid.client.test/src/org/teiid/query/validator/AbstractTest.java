/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.teiid.query.validator;

import org.teiid.core.types.DataTypeManagerService;
import org.komodo.spi.runtime.version.ITeiidVersion;
import org.teiid.query.parser.QueryParser;
import org.teiid.query.parser.TeiidParser;
import org.teiid.query.sql.AbstractTestFactory;
import org.teiid.query.unittest.RealMetadataFactory;

/**
 *
 */
public abstract class AbstractTest {

    private final ITeiidVersion teiidVersion;

    private final DataTypeManagerService dataTypeManager;

    private final QueryParser queryParser;

    private final RealMetadataFactory metadataFactory;

    /**
     * @param teiidVersion
     */
    public AbstractTest(ITeiidVersion teiidVersion) {
        this.teiidVersion = teiidVersion;
        this.dataTypeManager = DataTypeManagerService.getInstance(teiidVersion);
        this.queryParser = new QueryParser(teiidVersion);
        this.metadataFactory = new RealMetadataFactory(teiidVersion);
    }

    /**
     * @return the teiidVersion
     */
    public ITeiidVersion getTeiidVersion() {
        return this.teiidVersion;
    }

    /**
     * @return the queryParser
     */
    public QueryParser getQueryParser() {
        return this.queryParser;
    }

    /**
     * @return the teiid parser
     */
    public TeiidParser getTeiidParser() {
        return getQueryParser().getTeiidParser();
    }

    /**
     * @return the metadataFactory
     */
    public RealMetadataFactory getMetadataFactory() {
        return this.metadataFactory;
    }

    /**
     * @return the dataTypeManager
     */
    public DataTypeManagerService getDataTypeManager() {
        return this.dataTypeManager;
    }

    protected abstract AbstractTestFactory getFactory();
}
