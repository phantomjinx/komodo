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
import java.util.List;
import org.komodo.spi.constants.StringConstants;

/**
 *
 */
public class OrClause implements ICompoundClause, StringConstants {

    private IClause leftClause;

    private IClause rightClause;

    private ClauseStack owningStack;

    @Override
    public ClauseStack getOwningStack() {
        return owningStack;
    }

    @Override
    public void setOwningStack(ClauseStack clauseStack) {
        this.owningStack = clauseStack;
    }

    @Override
    public IClause nextClause() {
        return getOwningStack().nextClause(this);
    }

    /**
     * @return the leftClause
     */
    public IClause getLeftClause() {
        return this.leftClause;
    }

    /**
     * @param leftClause the leftClause to set
     */
    public void setLeftClause(IClause leftClause) {
        if (leftClause == this)
            throw new RuntimeException("Cannot add an orClause as its own left operand"); //$NON-NLS-1$

        this.leftClause = leftClause;
        this.leftClause.setOwningStack(getOwningStack());
    }

    /**
     * @return the rightClause
     */
    public IClause getRightClause() {
        return this.rightClause;
    }

    /**
     * @param rightClause the rightClause to set
     */
    public void setRightClause(IClause rightClause) {
        if (rightClause == this)
            throw new RuntimeException("Cannot add an orClause as its own right operand"); //$NON-NLS-1$

        if (this.rightClause == null) {
            this.rightClause = rightClause;
            this.rightClause.setOwningStack(getOwningStack());
            return;
        }

        if (this.rightClause instanceof IGroupClause && ((IGroupClause) this.rightClause).isOpen()) {
            ((IGroupClause) this.rightClause).addClause(rightClause);
            return;
        }

        if (this.rightClause instanceof OrClause) {
            ((OrClause) this.rightClause).setRightClause(rightClause);
            this.rightClause.setOwningStack(getOwningStack());
            return;
        }

        if (rightClause instanceof OrClause) {
            //
            // Replace the current right clause with this or clause
            // and set the current right clause to be its left clause
            //
            OrClause orClause = (OrClause) rightClause;
            orClause.setLeftClause(this.rightClause);
            this.rightClause = orClause;
            this.rightClause.setOwningStack(getOwningStack());
        }
    }

    @Override
    public <T extends IClause> T getLastClause(Class<T> clauseClass) {
        T lastClause = rightClause.getLastClause(clauseClass);
        if (lastClause != null)
            return lastClause;
        else if (clauseClass.isInstance(this))
          return (T) this;
        else
            return null;
    }

    @Override
    public <T extends IGroupClause> T findLatestOpenGroupClause(Class<T> groupClass) {
        if (rightClause == null)
            return null;

        return rightClause.findLatestOpenGroupClause(groupClass);
    }

    @Override
    public List<String> getAppendStatements() {
        List<String> appendStatements = new ArrayList<String>();

        appendStatements.addAll(leftClause.getAppendStatements());
        appendStatements.addAll(rightClause.getAppendStatements());

        return appendStatements;
    }

    @Override
    public List<TokenClause> getFirstTokenClauses() {
        List<TokenClause> tokenClauses = new ArrayList<TokenClause>();
        tokenClauses.addAll(leftClause.getFirstTokenClauses());
        tokenClauses.addAll(rightClause.getFirstTokenClauses());
        return tokenClauses;
    }

    @Override
    public boolean hasPPFunction() {
        return leftClause.hasPPFunction() || rightClause.hasPPFunction();
    }

    @Override
    public boolean hasMultiParameterPPFunction() {
        return leftClause.hasMultiParameterPPFunction() || rightClause.hasMultiParameterPPFunction();
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();

        buf.append(getLeftClause() + NEW_LINE);
        buf.append(PIPE + NEW_LINE);
        buf.append(getRightClause());

        return buf.toString();
    }
}
