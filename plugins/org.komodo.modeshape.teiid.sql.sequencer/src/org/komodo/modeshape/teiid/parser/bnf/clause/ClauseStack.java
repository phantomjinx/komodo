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

import java.util.Iterator;
import java.util.Stack;

/**
 *
 */
public class ClauseStack extends Stack<IClause> {

    private static final long serialVersionUID = 1L;

    private final IClause parent;

    public ClauseStack(IClause parent) {
        this.parent = parent;
    }
    
    public IClause getParent() {
        return parent;
    }

    @Override
    public IClause push(IClause clause) {
        if(isEmpty()) {
            clause.setOwningStack(this);
            return super.push(clause);
        }

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
            clause.setOwningStack(this);
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

            orClause.setOwningStack(this);
            super.push(orClause);

            // Must set the owning stack prior to setting the left clause
            // since the left clause takes it owning stack from its parent
            // or clause
            orClause.setLeftClause(topClause);
            return orClause;
        }

        clause.setOwningStack(this);
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

//    public List<TokenClause> getNextTokenClauses(TokenClause tokenClause) {
//        if (isEmpty())
//            return Collections.emptyList();
//
//        Iterator<IClause> iterator = iterator();
//        while (iterator.hasNext()) {
//            IClause stackClause = iterator.next();
//
//            if (stackClause == tokenClause) {
//
//                if (iterator.hasNext()) {
//                    IClause nextClause = iterator.next();
//                    return nextClause.getFirstTokenClauses();
//                } else {
//                    // This is the last clause in this stack but maybe this is
//                    // a stack within a stack.
//                    IClause parent = getParent();
//                    if (parent == IClause.ROOT_CLAUSE)
//                        return Collections.emptyList(); // already as high as we can go
//
//                    ClauseStack parentStack = parent.getOwningStack();
//                    IClause nextClause = parentStack.get(0);
//                    return nextClause.getFirstTokenClauses();
//                }
//
//            } else if (stackClause instanceof ICompoundClause) {
//                ICompoundClause ccStackClause = (ICompoundClause) stackClause;
//                List<TokenClause> nxtTknClauses = ccStackClause.findNextTokenClauses(tokenClause);
//                if (nxtTknClauses != null && !nxtTknClauses.isEmpty()) {
//                    // Found the tokenClause in the compound and 
//                    // returned the next clause token clauses
//                    return nxtTknClauses;
//                }
//            }
//            
//            // Failed to find tokenClause in any part of stackClause
//        }
//
//        return Collections.emptyList();
//    }

    private IClause nextFromIterator(IClause currentClause, Iterator<IClause> iterator) {
        if (iterator.hasNext()) {
            IClause nextClause = iterator.next();
            return nextClause;
        } else {
            // This is the last clause in this stack but maybe this is
            // a stack within a compound clause inside another stack.
            IClause parent = getParent();
            if (parent == IClause.ROOT_CLAUSE)
                return null; // already as high as we can go so definitely the end of the sequence

            ClauseStack parentStack = parent.getOwningStack();
            IClause nextClause = parentStack.nextClause(parent); // Get the next clause following the parent
            return nextClause;
        }
    }

    /**
     * @param tokenClause
     * @return
     */
    public IClause nextClause(IClause searchClause) {
        Iterator<IClause> iterator = iterator();
        while(iterator.hasNext()) {
            IClause iterClause = iterator.next();
            if (iterClause == searchClause) {
                return nextFromIterator(iterClause, iterator);

            } else if (iterClause instanceof OrClause) {
                // OrClauses are a little unusual as their left/right are not visible in any stack
                OrClause iterOrClause = (OrClause) iterClause;
                if (iterOrClause.getLeftClause() == searchClause)
                    return nextFromIterator(iterOrClause, iterator);
                else if (iterOrClause.getRightClause() == searchClause)
                    return nextFromIterator(iterOrClause, iterator);
            }
        }

        return null;
    }
}
