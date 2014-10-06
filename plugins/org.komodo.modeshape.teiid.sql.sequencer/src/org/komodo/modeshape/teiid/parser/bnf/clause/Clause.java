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
package org.komodo.modeshape.teiid.parser.bnf.clause;

import org.komodo.spi.constants.StringConstants;

/**
 *
 */
public class Clause implements IClause, StringConstants {

    private final String identifier;

    private String value;

    private String ppFunction;

    /**
     * @param identifier
     */
    public Clause(String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the ppFunction
     */
    public String getPPFunction() {
        return this.ppFunction;
    }

    /**
     * @param ppFunction
     */
    public void setPPFunction(String ppFunction) {
        this.ppFunction = ppFunction;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getIdentifier() + SPACE);
        buf.append("{ " + getValue() + " }" + SPACE);
        buf.append("ppSet|Append " + getPPFunction() + SPACE);
        return buf.toString();
    }
}