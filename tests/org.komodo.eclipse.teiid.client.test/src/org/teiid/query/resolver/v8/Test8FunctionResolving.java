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

package org.teiid.query.resolver.v8;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.teiid.api.exception.query.QueryResolverException;
import org.teiid.core.types.DataTypeManagerService;
import org.komodo.spi.runtime.version.ITeiidVersion;
import org.komodo.spi.runtime.version.TeiidVersion.Version;
import org.teiid.query.resolver.AbstractTestFunctionResolving;
import org.teiid.query.resolver.util.ResolverVisitor;
import org.teiid.query.sql.AbstractTestFactory;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.symbol.Function;
import org.teiid.query.sql.v8.Test8Factory;

@SuppressWarnings( {"nls", "javadoc"} )
public class Test8FunctionResolving extends AbstractTestFunctionResolving {

    private Test8Factory factory;

    protected Test8FunctionResolving(ITeiidVersion teiidVersion) {
        super(teiidVersion);
    }

    public Test8FunctionResolving() {
        this(Version.TEIID_8_0.get());
    }

    @Override
    protected AbstractTestFactory getFactory() {
        if (factory == null)
            factory = new Test8Factory(getQueryParser());

        return factory;
    }

    @Test
    public void testResolveBadConvert() throws Exception {
        Function function = getFactory().newFunction("convert",
                                                     new Expression[] {
                                                         getFactory().newConstant(new Character('a')),
                                                         getFactory().newConstant(DataTypeManagerService.DefaultDataTypes.DATE.getId())}); //$NON-NLS-1$

        try {
            ResolverVisitor visitor = new ResolverVisitor(getTeiidVersion());
            visitor.resolveLanguageObject(function, getMetadataFactory().example1Cached());
            fail("excpetion expected"); //$NON-NLS-1$
        } catch (QueryResolverException err) {
            assertEquals("TEIID30071 The conversion from char to date is not allowed.", err.getMessage()); //$NON-NLS-1$
        }
    }

    @Test
    public void testResolveAmbiguousFunction() throws Exception {
        Function function = getFactory().newFunction("LCASE",
                                                     new Expression[] {getFactory().newReference(0)}); //$NON-NLS-1$

        try {
            ResolverVisitor visitor = new ResolverVisitor(getTeiidVersion());
            visitor.resolveLanguageObject(function, getMetadataFactory().example1Cached());
            fail("excpetion expected"); //$NON-NLS-1$
        } catch (QueryResolverException err) {
            assertEquals("TEIID30069 The function 'LCASE(?)' has more than one possible signature.", err.getMessage()); //$NON-NLS-1$
        }
    }

    @Test( expected = QueryResolverException.class )
    public void testStringAggWrongTypes() throws Exception {
        String sql = "string_agg(pm1.g1.e1, pm1.g1.e2)"; //$NON-NLS-1$
        getExpression(sql);
    }

    @Test( expected = QueryResolverException.class )
    public void testStringAggWrongArgs() throws Exception {
        String sql = "string_agg(pm1.g1.e1)"; //$NON-NLS-1$
        getExpression(sql);
    }
}
