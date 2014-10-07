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

import java.util.Stack;

/**
 *
 */
public class ClauseStack extends Stack<IClause> {

    private static final long serialVersionUID = 1L;

    @Override
    public IClause push(IClause clause) {
        if(isEmpty())
            return super.push(clause);

        IClause topClause = peek();
        if (topClause == clause)
            throw new RuntimeException("Should not be adding the top of the stack to the stack a second time!"); //$NON-NLS-1$

        if (topClause instanceof IGroupClause) {
            //
            // If this group clause is open then the clause should be
            // added as an inner clause
            //
            IGroupClause gClause = (IGroupClause) topClause;
            if (gClause.isOpen()) {
                gClause.addClause(clause);
                return clause;
            }

            // group clause is closed so clause will be a sibling

        } else if (topClause instanceof OrClause) {
            //
            // An OrClause requires both a left and right clause before it is
            // complete. For an OrClause to be on top of the stack then its
            // left clause will already have been assigned and the right
            // needs to be assigned.
            //
            OrClause orClause = (OrClause) topClause;
            orClause.setRightClause(clause);
            return clause;
        }

        if (clause instanceof OrClause) {
            //
            // If the clause is an OrClause then its new and
            // needs to be populated with a left operand.
            // Take the top clause from the stack and assign
            // it to the left operand then push the OrClause
            // onto the stack in its place
            //
            OrClause orClause = (OrClause) clause;
            topClause = pop();
            orClause.setLeftClause(topClause);
            super.push(orClause);
            return orClause;
        }

        return super.push(clause);
    }

    /**
     * Get the last clause in the sequence
     * (including checking group clauses for the last inner clause)
     *
     * @param expectedType
     * @return last clause in the sequence only if it matches the expected type or null
     */
    public <T extends IClause> T expectedLastClause(Class<T> expectedType) {
        if (isEmpty())
            return null;

        IClause topClause = peek();
        T lastClause = topClause.getLastClause(expectedType);
        if (lastClause != null)
            return lastClause;
        else if (expectedType.isInstance(topClause))
          return (T) topClause;
        else
            return null;
    }

    public <T extends IGroupClause> T getLatestOpenGroupClause(Class<T> groupClass) {
        if (isEmpty())
            return null;

        IClause topClause = peek();
        return topClause.findLatestOpenGroupClause(groupClass);
    }
}
