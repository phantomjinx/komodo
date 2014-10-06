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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.komodo.spi.constants.StringConstants;


/**
 *
 */
public class BracketClause implements IGroupClause, StringConstants {

    private boolean open = true;

    private List<IClause> clauseChain = new ArrayList<IClause>();

    public BracketClause() {
    }

    /**
     * @param clause
     */
    @Override
    public void addClause(IClause clause) {
        if (clauseChain.isEmpty()) {
            clauseChain.add(clause);
            return;
        }

        IClause chainedClause = clauseChain.get(clauseChain.size() - 1);
        if (! (chainedClause instanceof IGroupClause)) {
            clauseChain.add(clause);
            return;
        }
        
        IGroupClause gClause = ((IGroupClause) chainedClause);

        if (gClause.isClosed()) {
            clauseChain.add(clause);
            return;
        }
        
        gClause.addClause(clause);
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public boolean isClosed() {
        return !open;
    }

    public void closeClause() {
        for (int i = clauseChain.size() - 1; i >= 0; --i) {
            IClause clause= clauseChain.get(i);
            if (! (clause instanceof IGroupClause))
                continue;

            IGroupClause gClause = (IGroupClause) clause;
            if (gClause.isOpen()) {
                gClause.closeClause();
                return;
            }
        }

        // Get to here then all the group clauses in the clause chain
        // were closed so the close call is for this
        this.open = false;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(OPEN_BRACKET + NEW_LINE);

        Iterator<IClause> clauseIter = clauseChain.iterator();
        while(clauseIter.hasNext()) {
            buf.append(clauseIter.next().toString() + NEW_LINE);
        }

        buf.append(CLOSE_BRACKET + NEW_LINE);
        return buf.toString();
    }
}
