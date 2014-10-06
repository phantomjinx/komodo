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
package org.komodo.modeshape.teiid.parser.bnf;

import java.util.List;
import org.komodo.modeshape.teiid.parser.TeiidSQLConstants.NonReserved;
import org.komodo.modeshape.teiid.parser.TeiidSQLConstants.Reserved;
import org.komodo.modeshape.teiid.parser.TeiidSQLConstants.Tokens;
import org.komodo.spi.runtime.version.ITeiidVersion;
import org.komodo.spi.runtime.version.TeiidVersion.Version;

/**
 *
 */
@SuppressWarnings({"static-access", "nls"})
public class BNF extends AbstractBNF {

    /**
     * @param version
     */
    public BNF(ITeiidVersion version) {
        super(version);
    }

    public String escapedJoin() {
        return Tokens.OPEN_BRACE + "oj";
    }

    public String[] stringVal() {
        List<String> bnf = newList();
        append(bnf, Tokens.OPEN_SQUARE_BRACKET + Tokens.CLOSE_SQUARE_BRACKET);
        return array(bnf);
    }

    public String[] nonReserved() {
        List<String> bnf = newList();

        append(bnf, NonReserved.INSTEAD);
        append(bnf, NonReserved.VIEW);
        append(bnf, NonReserved.ENABLED);
        append(bnf, NonReserved.DISABLED);
        append(bnf, NonReserved.KEY);
        append(bnf, NonReserved.SERIAL);
        append(bnf, NonReserved.TEXTAGG);
        append(bnf, NonReserved.COUNT);
        append(bnf, NonReserved.ROW_NUMBER);
        append(bnf, NonReserved.RANK);
        append(bnf, NonReserved.DENSE_RANK);
        append(bnf, NonReserved.SUM);
        append(bnf, NonReserved.AVG);
        append(bnf, NonReserved.MIN);
        append(bnf, NonReserved.MAX);
        append(bnf, NonReserved.EVERY);
        append(bnf, NonReserved.STDDEV_POP);
        append(bnf, NonReserved.STDDEV_SAMP);
        append(bnf, NonReserved.VAR_SAMP);
        append(bnf, NonReserved.VAR_POP);
        append(bnf, NonReserved.DOCUMENT);
        append(bnf, NonReserved.CONTENT);
        append(bnf, NonReserved.TRIM);
        append(bnf, NonReserved.EMPTY);
        append(bnf, NonReserved.ORDINALITY);
        append(bnf, NonReserved.PATH);
        append(bnf, NonReserved.FIRST);
        append(bnf, NonReserved.LAST);
        append(bnf, NonReserved.NEXT);
        append(bnf, NonReserved.SUBSTRING);
        append(bnf, NonReserved.EXTRACT);
        append(bnf, NonReserved.TO_CHARS);
        append(bnf, NonReserved.TO_BYTES);
        append(bnf, NonReserved.TIMESTAMPADD);
        append(bnf, NonReserved.TIMESTAMPDIFF);
        append(bnf, NonReserved.QUERYSTRING);
        append(bnf, NonReserved.NAMESPACE);
        append(bnf, NonReserved.RESULT);
        append(bnf, NonReserved.INDEX);
        append(bnf, NonReserved.ACCESSPATTERN);
        append(bnf, NonReserved.AUTO_INCREMENT);
        append(bnf, NonReserved.WELLFORMED);
        append(bnf, NonReserved.SQL_TSI_FRAC_SECOND);
        append(bnf, NonReserved.SQL_TSI_SECOND);
        append(bnf, NonReserved.SQL_TSI_MINUTE);
        append(bnf, NonReserved.SQL_TSI_HOUR);
        append(bnf, NonReserved.SQL_TSI_DAY);
        append(bnf, NonReserved.SQL_TSI_WEEK);
        append(bnf, NonReserved.SQL_TSI_MONTH);
        append(bnf, NonReserved.SQL_TSI_QUARTER);
        append(bnf, NonReserved.SQL_TSI_YEAR);
        append(bnf, NonReserved.TEXTTABLE);
        append(bnf, NonReserved.ARRAYTABLE);
        append(bnf, NonReserved.SELECTOR);
        append(bnf, NonReserved.SKIP);
        append(bnf, NonReserved.WIDTH);
        append(bnf, NonReserved.PASSING);
        append(bnf, NonReserved.NAME);
        append(bnf, NonReserved.ENCODING);
        append(bnf, NonReserved.COLUMNS);
        append(bnf, NonReserved.DELIMITER);
        append(bnf, NonReserved.QUOTE);
        append(bnf, NonReserved.HEADER);
        append(bnf, NonReserved.NULLS);
        append(bnf, NonReserved.OBJECTTABLE);
        append(bnf, NonReserved.VERSION);
        append(bnf, NonReserved.INCLUDING);
        append(bnf, NonReserved.EXCLUDING);
        append(bnf, NonReserved.XMLDECLARATION);
        append(bnf, NonReserved.VARIADIC);
        append(bnf, NonReserved.RAISE);
        append(bnf, NonReserved.EXCEPTION);
        append(bnf, NonReserved.CHAIN);
        append(bnf, NonReserved.JSONARRAY_AGG);
        append(bnf, NonReserved.JSONOBJECT);

        return array(bnf);
    }

    public String[] id() {
        List<String> bnf = newList();

        append(bnf, Tokens.OPEN_SQUARE_BRACKET + Tokens.CLOSE_SQUARE_BRACKET +
                            Tokens.DOT +
                            Tokens.OPEN_SQUARE_BRACKET + Tokens.CLOSE_SQUARE_BRACKET);
        append(bnf, Tokens.OPEN_SQUARE_BRACKET + Tokens.CLOSE_SQUARE_BRACKET);
        append(bnf, nonReserved());

        return array(bnf);
    }

    public String[] command() {
        List<String> bnf = newList();

        append(bnf, createProcedure(0));
        append(bnf, userCommand());
        append(bnf, callableStatement(0));

        return array(bnf);
    }

    public String[] designerCommand() {
        List<String> bnf = newList();

        append(bnf, createProcedure(0));
        append(bnf, forEachRowTriggerAction(0));
        append(bnf, userCommand());

        return array(bnf);
    }

    public String[] createTrigger(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.CREATE);
                break;
            case CREATE:
                append(bnf, Reserved.TRIGGER);
                break;
            case TRIGGER:
                append(bnf, Reserved.ON);
                break;
            case ON:
                append(bnf, id());
                break;
            case BNF.id:
                append(bnf, NonReserved.INSTEAD);
                break;
            case INSTEAD:
                append(bnf, Reserved.OF);
                break;
            case OF:
                append(bnf, Reserved.INSERT);
                append(bnf, Reserved.UPDATE);
                append(bnf, Reserved.DELETE);
                break;
            case INSERT:
            case UPDATE:
            case DELETE:
                append(bnf, Reserved.AS);
                break;
            case AS:
                append(bnf, forEachRowTriggerAction(0));
        }

        return array(bnf);
    }

    public String[] alter(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        if (index == 0) {
            append(bnf, Reserved.ALTER);
        } else if (index == ALTER) {
            append(bnf, NonReserved.VIEW);
            append(bnf, Reserved.PROCEDURE);
            append(bnf, Reserved.TRIGGER);
        } else if (index == VIEW || index == PROCEDURE || index == ON) {
            append(bnf, id());
        } else if (index == concat(VIEW, id) || index == concat(PROCEDURE, id)) {
            append(bnf, Reserved.AS);
        } else if (index == concat(VIEW, AS)) {
            append(bnf, queryExpression(0));
        } else if (index == concat(PROCEDURE, AS)) {
            append(bnf, statement(0));
        } else if (index == TRIGGER) {
            append(bnf, Reserved.ON);
        } else if (index == concat(TRIGGER, id)) {
            append(bnf, NonReserved.INSTEAD);
        } else if (index == INSTEAD) {
            append(bnf, Reserved.OF);
        } else if (index == OF) {
            append(bnf, Reserved.INSERT);
            append(bnf, Reserved.UPDATE);
            append(bnf, Reserved.DELETE);
        } else if (index == INSERT || index == UPDATE || index == DELETE) {
            append(bnf, Reserved.AS);
            append(bnf, NonReserved.ENABLED);
            append(bnf, NonReserved.DISABLED);
        } else if (index == concat(OF, AS)) {
            append(bnf, forEachRowTriggerAction(0));
        }

        return array(bnf);
    }

    public String[] forEachRowTriggerAction(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.FOR);
                break;
            case FOR:
                append(bnf, Reserved.EACH);
                break;
            case EACH:
                append(bnf, Reserved.ROW);
                break;
            case ROW:
                append(bnf, Reserved.BEGIN);
                append(bnf, statement(0));
                break;
            case BEGIN:
                append(bnf, Reserved.ATOMIC);
                append(bnf, statement(0));
                break;
            case ATOMIC:
                append(bnf, statement(0));
                break;
            case BNF.statement:
                append(bnf, statement(0));
                append(bnf, Reserved.END);
        }

        return array(bnf);
    }

    public String[] userCommand() {
        List<String> bnf = newList();

        append(bnf, queryExpression(0));
        append(bnf, storedProcedure(0));
        append(bnf, insert(0));
        append(bnf, update(0));
        append(bnf, delete(0));
        append(bnf, alter(0));
        append(bnf, createTrigger(0));

        if (versionAtLeast(Version.TEIID_8_4))
            append(bnf, compoundStatement(0));

        return array(bnf);
    }

    public String[] errorStatement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.ERROR);
                break;
            case ERROR:
                append(bnf, expression(0));
        }

        return array(bnf);
    }

    public String[] raiseStatement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, NonReserved.RAISE);
                break;
            case RAISE:
                append(bnf, Reserved.SQLWARNING);
                append(bnf, exceptionReference(0));
                break;
            case SQLWARNING:
                append(bnf, exceptionReference(0));
        }

        return array(bnf);
    }

    public String[] exceptionReference(int... indices) {
        List<String> bnf = newList();

        append(bnf, id());
        append(bnf, exception(0));

        return array(bnf);
    }

    public String[] exception(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        if (index == 0) {
            append(bnf, Reserved.SQLEXCEPTION);
        } else if (index == SQLEXCEPTION || index == SQLSTATE || index == COMMA) {
            append(bnf, commonValueExpression(0));
        } else if (index == concat(BNF.SQLEXCEPTION, BNF.commonValueExpression)) {
            append(bnf, Reserved.SQLSTATE);
            append(bnf, NonReserved.CHAIN);
        } else if (index == concat(BNF.SQLSTATE, BNF.commonValueExpression)) {
            append(bnf, Tokens.COMMA);
            append(bnf, NonReserved.CHAIN);
        } else if (index == concat(BNF.COMMA, BNF.commonValueExpression)) {
            append(bnf, NonReserved.CHAIN);
        } else if (index == CHAIN) {
            append(bnf, exceptionReference(0));
        }

        return array(bnf);
    }

    public String[] statement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, id());
                append(bnf, loopStatement(0));
                append(bnf, whileStatement(0));
                append(bnf, compoundStatement(0));
                append(bnf, ifStatement(0));
                append(bnf, delimitedStatement(0));
                break;
            case BNF.id:
                append(bnf, Tokens.COLON);
                break;
            case COLON:
                append(bnf, loopStatement(0));
                append(bnf, whileStatement(0));
                append(bnf, compoundStatement(0));
        }

        return array(bnf);
    }

    public String[] delimitedStatement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, assignStatement(0));
                append(bnf, sqlStatement(0));
                append(bnf, errorStatement(0));
                append(bnf, raiseStatement(0));
                append(bnf, declareStatement(0));
                append(bnf, branchingStatement(0));
                append(bnf, returnStatement(0));
                break;
            case BNF.assignStatement:
            case BNF.sqlStatement:
            case BNF.errorStatement:
            case BNF.raiseStatement:
            case BNF.declareStatement:
            case BNF.branchingStatement:
            case BNF.returnStatement:
                append(bnf, Tokens.SEMI_COLON);
                break;
        }

        return array(bnf);
    }

    public String[] compoundStatement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        if (index == 0) {
            append(bnf, Reserved.BEGIN);
        } else if (index == BEGIN) {
            append(bnf, Reserved.NOT);
            append(bnf, Reserved.ATOMIC);
            append(bnf, statement(0));
            append(bnf, NonReserved.EXCEPTION);
            append(bnf, Reserved.END);
        } else if (index == NOT) {
            append(bnf, Reserved.ATOMIC);
        } else if (index == ATOMIC) {
            append(bnf, statement(0));
            append(bnf, NonReserved.EXCEPTION);
            append(bnf, Reserved.END);
        } else if (index == concat(BNF.BEGIN, BNF.statement)) {
            append(bnf, statement(0));
            append(bnf, NonReserved.EXCEPTION);
            append(bnf, Reserved.END);
        } else if (index == EXCEPTION) {
            append(bnf, id());
        } else if (index == BNF.id) {
            append(bnf, statement(0));
        } else if (index == concat(BNF.EXCEPTION, BNF.statement)) {
            append(bnf, statement(0));
            append(bnf, Reserved.END);
        }

        return array(bnf);
    }

    public String[] branchingStatement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.BREAK);
                append(bnf, Reserved.CONTINUE);
                append(bnf, Reserved.LEAVE);
                break;
            case BREAK:
            case CONTINUE:
            case LEAVE:
                append(bnf, id());
                break;
        }

        return array(bnf);
    }

    public String[] returnStatement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.RETURN);
                break;
            case RETURN:
                append(bnf, expression(0));
                break;
        }

        return array(bnf);
    }

    public String[] whileStatement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.WHILE);
                break;
            case BNF.WHILE:
                append(bnf, Tokens.LPAREN);
                break;
            case BNF.LPAREN:
                append(bnf, criteria(0));
                break;
            case BNF.criteria:
                append(bnf, Tokens.RPAREN);
                break;
            case BNF.RPAREN:
                append(bnf, statement(0));
                break;
        }
 
        return array(bnf);
    }

    public String[] loopStatement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.LOOP);
                break;
            case BNF.LOOP:
                append(bnf, Reserved.ON);
                break;
            case BNF.ON:
                append(bnf, Tokens.LPAREN);
                break;
            case BNF.LPAREN:
                append(bnf, queryExpression(0));
                break;
            case BNF.queryExpression:
                append(bnf, Tokens.RPAREN);
                break;
            case BNF.RPAREN:
                append(bnf, Reserved.AS);
                break;
            case BNF.AS:
                append(bnf, id());
                break;
            case BNF.id:
                append(bnf, statement(0));
                break;
        }

        return array(bnf);
    }

    public String[] ifStatement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.IF);
                break;
            case BNF.IF:
                append(bnf, Tokens.LPAREN);
                break;
            case BNF.LPAREN:
                append(bnf, criteria(0));
                break;
            case BNF.criteria:
                append(bnf, Tokens.RPAREN);
                break;
            case BNF.RPAREN:
            case BNF.ELSE:
                append(bnf, statement(0));
                break;
            case BNF.statement:
                append(bnf, Reserved.ELSE);
                break;
        }

        return array(bnf);
    }

    public String[] declareStatement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.DECLARE);
                break;
            case DECLARE:
                append(bnf, parseDataType(0));
                append(bnf, NonReserved.EXCEPTION);
                break;
            case parseDataType:
            case EXCEPTION:
                append(bnf, id());
                break;
            case id:
                append(bnf, Tokens.EQ);
                break;
            case EQ:
                append(bnf, assignStatement(0));
        }

        return array(bnf);
    }

    public String[] assignStatement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, id());
                break;
            case id:
                append(bnf, Tokens.EQ);
                break;
            case EQ:
                append(bnf, assignStatementOperand(0));
                append(bnf, storedProcedure(0));
                break;
            case storedProcedure:
                append(bnf, Reserved.WITH);
                append(bnf, Reserved.WITHOUT);
                break;
            case WITH:
            case WITHOUT:
                append(bnf, Reserved.RETURN);
        }
        
        return array(bnf);
    }

    public String[] assignStatementOperand(int... indices) {
        List<String> bnf = newList();

        append(bnf, insert(0));
        append(bnf, update(0));
        append(bnf, delete(0));
        append(bnf, expression(0));
        append(bnf, queryExpression(0));
        append(bnf, exception(0));

        return array(bnf);
    }

    public String[] sqlStatement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, userCommand());
                append(bnf, dynamicCommand(0));
                break;
            case userCommand:
            case dynamicCommand:
                append(bnf, Reserved.WITH);
                append(bnf, Reserved.WITHOUT);
                break;
            case WITH:
            case WITHOUT:
                append(bnf, Reserved.RETURN);
        }

        return array(bnf);
    }

    public String[] createProcedure(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.CREATE);
                break;
            case CREATE:
                append(bnf, Reserved.VIRTUAL);
                break;
            case VIRTUAL:
                append(bnf, Reserved.PROCEDURE);
                break;
            case PROCEDURE:
                append(bnf, statement(0));
                break;
        }

        return array(bnf);
    }

    public String[] procedureBodyCommand(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.CREATE);
                break;
            case CREATE:
                append(bnf, Reserved.VIRTUAL);
                append(bnf, Reserved.PROCEDURE);
                break;
            case VIRTUAL:
                append(bnf, Reserved.PROCEDURE);
                break;
            case PROCEDURE:
                append(bnf, statement(0));
                break;
        }

        return array(bnf);
    }

    public String[] dynamicCommand(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.EXECUTE);
                append(bnf, Reserved.EXEC);
                break;
            case EXECUTE:
            case EXEC:
                append(bnf, Reserved.STRING);
                append(bnf, Reserved.IMMEDIATE);
                append(bnf, expression(0));
                break;
            case STRING:
            case IMMEDIATE:
                append(bnf, expression(0));
                break;
            case expression:
                append(bnf, Reserved.AS);
                append(bnf, Reserved.USING);
                append(bnf, Reserved.UPDATE);
                break;
            case AS:
                append(bnf, createElementsWithTypes(0));
                break;
            case createElementsWithTypes:
                append(bnf, Reserved.INTO);
                append(bnf, Reserved.USING);
                append(bnf, Reserved.UPDATE);
                break;
            case INTO:
                append(bnf, id());
                break;
            case id:
                append(bnf, Reserved.USING);
                append(bnf, Reserved.UPDATE);
                break;
            case USING:
                append(bnf, setClauseList(0));
                break;
            case setClauseList:
                append(bnf, Reserved.UPDATE);
                break;
            case UPDATE:
                append(bnf, intVal(0));
                append(bnf, Tokens.STAR);
        }

        return array(bnf);
    }

    public String[] setClauseList(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, id());
                break;
            case id:
                append(bnf, Tokens.EQ);
                break;
            case EQ:
                append(bnf, expression(0));
                break;
            case expression:
                append(bnf, Tokens.COMMA);
                break;
            case COMMA:
                append(bnf, id());
                break;
        }

        return array(bnf);
    }

    public String[] createElementsWithTypes(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, id());
                break;
            case id:
                append(bnf, parseDataType(0));
                break;
            case parseDataType:
                append(bnf, Tokens.COMMA);
                break;
            case COMMA:
                append(bnf, id());
                break;
        }
        return array(bnf);
    }

    public String[] callableStatement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Tokens.LBRACE);
                break;
            case LBRACE:
                append(bnf, Tokens.QUOTE_MARK);
                append(bnf, Reserved.CALL);
                break;
            case QMARK:
                append(bnf, Tokens.EQ);
                break;
            case EQ:
                append(bnf, Reserved.CALL);
                break;
            case CALL:
                append(bnf, id());
                break;
            case id:
                append(bnf, Tokens.LPAREN);
                append(bnf, Tokens.RBRACE);
                break;
            case LPAREN:
                append(bnf, executeNamedParams(0));
                append(bnf, expressionList(0));
                break;
            case executeNamedParams:
            case expressionList:
                append(bnf, Tokens.RPAREN);
                break;
            case RPAREN:
                append(bnf, Tokens.RBRACE);
                break;
            case RBRACE:
                append(bnf, option(0));
        }

        return array(bnf);
    }

    public String[] storedProcedure(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.EXEC);
                append(bnf, Reserved.EXECUTE);
                append(bnf, Reserved.CALL);
                break;
            case EXEC:
            case EXECUTE:
            case CALL:
                append(bnf, id());
                break;
            case id:
                append(bnf, Tokens.LPAREN);
                break;
            case LPAREN:
                append(bnf, executeNamedParams(0));
                append(bnf, expressionList(0));
                break;
            case executeNamedParams:
            case expressionList:
                append(bnf, Tokens.RPAREN);
                break;
            case RPAREN:
                append(bnf, option(0));
        }

        return array(bnf);
    }

    public String[] executeNamedParams(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
            case COMMA:
                append(bnf, id());
                break;
            case id:
                append(bnf, Tokens.EQ);
                break;
            case EQ:
                append(bnf, Tokens.GT);
                append(bnf, expression(0));
                break;
            case GT:
                append(bnf, expression(0));
                break;
            case expression:
                append(bnf, Tokens.COMMA);
        }

        return array(bnf);
    }

    public String[] insert(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.INSERT);
                append(bnf, Reserved.MERGE);
                break;
            case INSERT:
            case MERGE:
                append(bnf, Reserved.INTO);
                break;
            case INTO:
                append(bnf, id());
                break;
            case id:
                append(bnf, columnList(0));
                append(bnf, queryExpression(0));
                break;
            case columnList:
                append(bnf, queryExpression(0));
                break;
            case queryExpression:
                append(bnf, option(0));
        }

        return array(bnf);
    }

    public String[] expressionList(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
            case COMMA:
                append(bnf, expression(0));
                break;
            case expression:
                append(bnf, Tokens.COMMA);
                break;
        }

        return array(bnf);
    }

    public String[] update(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.UPDATE);
                break;
            case UPDATE:
                append(bnf, id());
                break;
            case id:
                append(bnf, Reserved.SET);
                break;
            case SET:
                append(bnf, setClauseList(0));
                break;
            case setClauseList:
                append(bnf, where(0));
                append(bnf, option(0));
                break;
            case where:
                append(bnf, option(0));
                break;
        }

        return array(bnf);
    }

    public String[] delete(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.DELETE);
                break;
            case DELETE:
                append(bnf, Reserved.FROM);
                break;
            case FROM:
                append(bnf, id());
                break;
            case id:
                append(bnf, where(0));
                append(bnf, option(0));
                break;
            case where:
                append(bnf, option(0));
                break;
        }

        return array(bnf);
    }

    public String[] queryExpression(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.WITH);
                append(bnf, queryExpressionBody(0));
                break;
            case WITH:
            case COMMA:
                append(bnf, withListElement(0));
                break;
            case withListElement:
                append(bnf, Tokens.COMMA);
                append(bnf, queryExpressionBody(0));
                break;
        }
        return array(bnf);
    }

    public String[] withListElement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, id());
                break;
            case id:
                append(bnf, columnList(0));
                append(bnf, Reserved.AS);
                break;
            case columnList:
                append(bnf, Reserved.AS);
                break;
            case AS:
                append(bnf, Tokens.LPAREN);
                break;
            case LPAREN:
                append(bnf, queryExpression(0));
                break;
            case queryExpression:
                append(bnf, Tokens.RPAREN);
        }
        return array(bnf);
    }

    public String[] queryExpressionBody(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, queryTerm(0));
                break;
            case queryTerm:
                append(bnf, Reserved.UNION);
                append(bnf, Reserved.EXCEPT);
                append(bnf, orderby(0));
                append(bnf, limit(0));
                break;
            case UNION:
            case EXCEPT:
                append(bnf, Reserved.ALL);
                append(bnf, Reserved.DISTINCT);
                append(bnf, queryTerm(0));
                break;
            case orderby:
                append(bnf, limit(0));
                append(bnf, option(0));
                break;
            case limit:
                append(bnf, option(0));
        }

        return array(bnf);
    }

    public String[] queryTerm(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
            case ALL:
            case DISTINCT:
                append(bnf, queryPrimary(0));
                break;
            case queryPrimary:
                append(bnf, Reserved.INTERSECT);
                break;
            case INTERSECT:
                append(bnf, Reserved.ALL);
                append(bnf, Reserved.DISTINCT);
                append(bnf, queryPrimary(0));
        }

        return array(bnf);
    }

    public String[] queryPrimary(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        if (index == 0) {
            append(bnf, query(0));
            append(bnf, Reserved.VALUES);
            append(bnf, Reserved.TABLE);
            append(bnf, Tokens.LPAREN);
        } else if (index == VALUES || index == COMMA) {
            append(bnf, Tokens.LPAREN);
        } else if (index == concat(VALUES, LPAREN)) {
            append(bnf, expressionList(0));
        } else if (index == expressionList) {
            append(bnf, Tokens.RPAREN);
        } else if (index == concat(VALUES, RPAREN)) {
            if (versionAtLeast(Version.TEIID_8_6)) {
                append(bnf, Tokens.COMMA);
            }
        } else if (index == TABLE) {
            append(bnf, id());
        } else if (index == LPAREN) {
            append(bnf, queryExpressionBody(0));
        } else if (index == queryExpressionBody) {
            append(bnf, Tokens.RPAREN);
        }

        return array(bnf);
    }

    public String[] query(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, select(0));
                break;
            case select:
                append(bnf, into(0));
                append(bnf, from(0));
                break;
            case into:
                append(bnf, from(0));
                break;
            case from:
                append(bnf, where(0));
                append(bnf, groupBy(0));
                append(bnf, having(0));
                break;
            case where:
                append(bnf, groupBy(0));
                append(bnf, having(0));
                break;
            case groupBy:
                append(bnf, having(0));
        }

        return array(bnf);
    }

    public String[] into(int... indices) {
        List<String> bnf = newList();


        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.INTO);
                break;
            case INTO:
                append(bnf, id());
        }

        return array(bnf);
    }

    public String[] select(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.SELECT);
                break;
            case SELECT:
                append(bnf, Reserved.ALL);
                append(bnf, Reserved.DISTINCT);
                append(bnf, Tokens.STAR);
                append(bnf, selectSymbol(0));
                break;
            case ALL:
            case DISTINCT:
                append(bnf, Tokens.STAR);
                append(bnf, selectSymbol(0));
                break;
            case BNF.selectSymbol:
                append(bnf, Tokens.COMMA);
                break;
            case COMMA:
                append(bnf, selectSymbol(0));
        }

        return array(bnf);
    }

    public String[] selectSymbol(int... indices) {
        List<String> bnf = newList();

        append(bnf, selectExpression(0));
        append(bnf, allInGroupSymbol(0));

        return array(bnf);
    }

    public String[] selectExpression(int... indices) {
        List<String> bnf = newList();

        
        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, expression(0));
                break;
            case expression:
                append(bnf, Reserved.AS);
                append(bnf, id());
                break;
            case AS:
                append(bnf, id());
                break;
        }

        return array(bnf);
    }

    public String[] derivedColumn(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, expression(0));
                break;
            case expression:
                append(bnf, Reserved.AS);
                append(bnf, id());
                break;
            case AS:
                append(bnf, id());
                break;
        }

        return array(bnf);
    }

    public String[] allInGroupSymbol(int... indices) {
        List<String> bnf = newList();

        append(bnf, Tokens.OPEN_SQUARE_BRACKET +
                           Tokens.CLOSE_SQUARE_BRACKET +
                           Tokens.DOT +
                           Tokens.STAR);

        return array(bnf);
    }

    public String[] orderedAgg(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.XMLAGG);
                break;
            case XMLAGG:
                append(bnf, Reserved.ARRAY_AGG);
                break;
            case ARRAY_AGG:
                append(bnf, NonReserved.JSONARRAY_AGG);
                break;
            case JSONARRAY_AGG:
                append(bnf, Tokens.LPAREN);
                break;
            case LPAREN:
                append(bnf, expression(0));
                break;
            case expression:
                append(bnf, orderby(0));
                append(bnf, Tokens.RPAREN);
                break;
            case orderby:
                append(bnf, Tokens.RPAREN);
        }

        return array(bnf);
    }

    public String[] textAgg(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        if (index == 0) {
            append(bnf, NonReserved.TEXTAGG);
        } else if (index == TEXTAGG) {
            append(bnf, Tokens.LPAREN);
        } else if (index == LPAREN) {
            append(bnf, Reserved.FOR);
            append(bnf, derivedColumn(0));
        } else if (index == FOR || index == COMMA) {
            append(bnf, derivedColumn(0));
        } else if (index == derivedColumn) {
            append(bnf, Tokens.COMMA);
            append(bnf, NonReserved.DELIMITER);
            append(bnf, NonReserved.QUOTE);
            append(bnf, NonReserved.HEADER);
            append(bnf, NonReserved.ENCODING);
            append(bnf, orderby(0));
            append(bnf, Tokens.RPAREN);
        } else if (index == DELIMITER || index == QUOTE) {
            append(bnf, charVal());
        } else if (index == concat(DELIMITER, charVal)) {
            append(bnf, NonReserved.QUOTE);
            append(bnf, NonReserved.HEADER);
            append(bnf, NonReserved.ENCODING);
            append(bnf, orderby(0));
            append(bnf, Tokens.RPAREN);
        } else if (index == concat(QUOTE, charVal)) {
            append(bnf, NonReserved.HEADER);
            append(bnf, NonReserved.ENCODING);
            append(bnf, orderby(0));
            append(bnf, Tokens.RPAREN);
        } else if (index == HEADER) {
            append(bnf, NonReserved.ENCODING);
            append(bnf, orderby(0));
            append(bnf, Tokens.RPAREN);
        } else if (index == ENCODING) {
            append(bnf, id());
        } else if (index == id) {
            append(bnf, orderby(0));
            append(bnf, Tokens.RPAREN);
        } else if (index == orderby) {
            append(bnf, Tokens.RPAREN);
        }
        
        return array(bnf);
    }

    public String[] aggregateSymbol(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, NonReserved.COUNT);
                append(bnf, NonReserved.SUM);
                append(bnf, NonReserved.AVG);
                append(bnf, NonReserved.MIN);
                append(bnf, NonReserved.MAX);
                append(bnf, NonReserved.EVERY);
                append(bnf, NonReserved.STDDEV_POP);
                append(bnf, NonReserved.STDDEV_SAMP);
                append(bnf, NonReserved.VAR_SAMP);
                append(bnf, NonReserved.VAR_POP);
                append(bnf, Reserved.SOME);
                append(bnf, Reserved.ANY);
                break;
            case COUNT:
            case SUM:
            case AVG:
            case MIN:
            case MAX:
            case EVERY:
            case STDDEV_POP:
            case STDDEV_SAMP:
            case VAR_SAMP:
            case VAR_POP:
            case SOME:
            case ANY:
                append(bnf, Tokens.LPAREN);
                break;
            case LPAREN:
                append(bnf, Reserved.DISTINCT);
                append(bnf, Reserved.ALL);
                append(bnf, expression(0));
                break;
            case DISTINCT:
            case ALL:
                append(bnf, expression(0));
                break;
            case expression:
                append(bnf, Tokens.RPAREN);
        }
        
        return array(bnf);
    }

    public String[] analyticAggregateSymbol(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, NonReserved.ROW_NUMBER);
                append(bnf, NonReserved.RANK);
                append(bnf, NonReserved.DENSE_RANK);
                break;
            case ROW_NUMBER:
            case RANK:
            case DENSE_RANK:
                append(bnf, Tokens.LPAREN);
                break;
            case LPAREN:
                append(bnf, Tokens.RPAREN);
        }

        return array(bnf);
    }

    public String[] filterClause(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.FILTER);
                break;
            case FILTER:
                append(bnf, Tokens.LPAREN);
                break;
            case LPAREN:
                append(bnf, Reserved.WHERE);
                break;
            case WHERE:
                append(bnf, booleanPrimary(0));
                break;
            case booleanPrimary:
                append(bnf, Tokens.RPAREN);
        }

        return array(bnf);
    }

    public String[] from(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.FROM);
                break;
            case FROM:
            case COMMA:
                append(bnf, tableReference(0));
                break;
            case tableReference:
                append(bnf, Tokens.COMMA);
        }
        
        return array(bnf);
    }

    public String[] tableReference(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        if (index == 0) {
            append(bnf, escapedJoin());
            append(bnf, joinedTable(0));
        } else if (index == ESCAPEDJOIN) {
            append(bnf, joinedTable(0));
        } else if (index == concat(ESCAPEDJOIN, joinedTable)) {
            append(bnf, Tokens.RBRACE);
        }

        return array(bnf);
    }

    public String[] joinedTable(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, tablePrimary(0));
                break;
            case tablePrimary:
            case crossJoin:
            case qualifiedJoin:
                append(bnf, crossJoin(0));
                append(bnf, qualifiedJoin(0));
                break;
        }

        return array(bnf);
    }

    public String[] crossJoin(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.CROSS);
                append(bnf, Reserved.UNION);
                break;
            case CROSS:
            case UNION:
                append(bnf, Reserved.JOIN);
                break;
            case JOIN:
                append(bnf, tablePrimary(0));
        }

        return array(bnf);
    }

    public String[] qualifiedJoin(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.RIGHT);
                append(bnf, Reserved.LEFT);
                append(bnf, Reserved.FULL);
                append(bnf, Reserved.INNER);
                break;
            case RIGHT:
            case LEFT:
            case FULL:
                append(bnf, Reserved.OUTER);
                append(bnf, Reserved.JOIN);
                break;
            case INNER:
                append(bnf, Reserved.JOIN);
                break;
            case JOIN:
                append(bnf, tableReference(0));
                break;
            case tableReference:
                append(bnf, Reserved.ON);
                break;
            case ON:
                append(bnf, criteria(0));
        }

        return array(bnf);
    }

    public String[] tablePrimary(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, textTable(0));
                append(bnf, arrayTable(0));
                append(bnf, xmlTable(0));
                append(bnf, objectTable(0));
                append(bnf, unaryFromClause(0));
                append(bnf, subqueryFromClause(0));
                append(bnf, Tokens.LPAREN);
                break;
            case LPAREN:
                append(bnf, joinedTable(0));
                break;
            case joinedTable:
                append(bnf, Tokens.RPAREN);
                break;
            case textTable:
            case arrayTable:
            case xmlTable:
            case objectTable:
            case unaryFromClause:
            case subqueryFromClause:
            case RPAREN:
                append(bnf, Reserved.MAKEDEP);
                append(bnf, Reserved.MAKENOTDEP);
                break;
        }

        return array(bnf);
    }

    public String[] xmlSerialize(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Reserved.XMLSERIALIZE);
                break;
            case XMLSERIALIZE:
                append(bnf, Tokens.LPAREN);
                break;
            case LPAREN:
                append(bnf, NonReserved.DOCUMENT);
                append(bnf, NonReserved.CONTENT);
                append(bnf, expression(0));
                break;
            case DOCUMENT:
            case CONTENT:
                append(bnf, expression(0));
                break;
            case expression:
                append(bnf, Reserved.AS);
                append(bnf, NonReserved.ENCODING);
                append(bnf, NonReserved.VERSION);
                append(bnf, NonReserved.INCLUDING);
                append(bnf, NonReserved.EXCLUDING);
                append(bnf, Tokens.RPAREN);
                break;
            case AS:
                append(bnf, Reserved.STRING);
                append(bnf, Reserved.VARCHAR);
                append(bnf, Reserved.CLOB);
                append(bnf, Reserved.VARBINARY);
                append(bnf, Reserved.BLOB);
                break;
            case STRING:
            case VARCHAR:
            case CLOB:
            case VARBINARY:
            case BLOB:
                append(bnf, NonReserved.ENCODING);
                append(bnf, NonReserved.VERSION);
                append(bnf, NonReserved.INCLUDING);
                append(bnf, NonReserved.EXCLUDING);
                append(bnf, Tokens.RPAREN);
                break;
            case ENCODING:
                append(bnf, id());
                break;
            case id:
                append(bnf, NonReserved.VERSION);
                append(bnf, NonReserved.INCLUDING);
                append(bnf, NonReserved.EXCLUDING);
                append(bnf, Tokens.RPAREN);
                break;
            case VERSION:
                append(bnf, stringVal());
                break;
            case stringVal:
                append(bnf, NonReserved.INCLUDING);
                append(bnf, NonReserved.EXCLUDING);
                append(bnf, Tokens.RPAREN);
                break;
            case INCLUDING:
            case EXCLUDING:
                append(bnf, NonReserved.XMLDECLARATION);
                break;
            case XMLDECLARATION:
                append(bnf, Tokens.RPAREN);
        }

        return array(bnf);
    }

    public String[] arrayTable(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, NonReserved.ARRAYTABLE);
                break;
            case ARRAYTABLE:
                append(bnf, Tokens.LPAREN);
                break;
            case valueExpressionPrimary:
                append(bnf, NonReserved.COLUMNS);
                break;
            case COLUMNS:
                append(bnf, createElementsWithTypes(0));
                break;
            case RPAREN:
                append(bnf, Reserved.AS);
                append(bnf, id());
                break;
            case AS:
                append(bnf, id());
        }

        return array(bnf);
    }

    public String[] textTable(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        if (index == 0) {
            append(bnf, NonReserved.TEXTTABLE);
        } else if (index == TEXTTABLE) {
            append(bnf, Tokens.LPAREN);
        } else if (index == LPAREN) {
            append(bnf, commonValueExpression(0));
        } else if (index == commonValueExpression) {
            append(bnf, NonReserved.SELECTOR);
            append(bnf, NonReserved.COLUMNS);
        } else if (index == SELECTOR) {
            append(bnf, stringVal());
        } else if (index == stringVal) {
            append(bnf, NonReserved.COLUMNS);
        } else if (index == COLUMNS || index == COMMA) {
            append(bnf, textColumn(0));
        } else if (index == textColumn) {
            append(bnf, Tokens.COMMA);
            append(bnf, Reserved.NO);
            append(bnf, NonReserved.DELIMITER);
            append(bnf, Reserved.ESCAPE);
            append(bnf, NonReserved.QUOTE);
            append(bnf, NonReserved.HEADER);
            append(bnf, NonReserved.SKIP);
            append(bnf, Tokens.RPAREN);
        } else if (index == NO) {
            append(bnf, Reserved.ROW);
        } else if (index == ROW || index == concat(NO, DELIMITER)) {
            append(bnf, NonReserved.DELIMITER);
        } else if (index == DELIMITER || index == ESCAPE || index == QUOTE) {
            append(bnf, charVal());
        } else if (index == concat(DELIMITER, charVal)) {
            append(bnf, Reserved.ESCAPE);
            append(bnf, NonReserved.QUOTE);
            append(bnf, NonReserved.HEADER);
            append(bnf, NonReserved.SKIP);
            append(bnf, Tokens.RPAREN);
        } else if (index == concat(ESCAPE, charVal)) {
            append(bnf, NonReserved.HEADER);
            append(bnf, NonReserved.SKIP);
            append(bnf, Tokens.RPAREN);
        } else if (index == concat(QUOTE, charVal)) {
            append(bnf, NonReserved.HEADER);
            append(bnf, NonReserved.SKIP);
            append(bnf, Tokens.RPAREN);
        } else if (index == HEADER) {
            append(bnf, intVal(0));
            append(bnf, NonReserved.SKIP);
            append(bnf, Tokens.RPAREN);
        } else if (index == intVal) {
            append(bnf, Tokens.RPAREN);
        } else if (index == RPAREN) {
            append(bnf, Reserved.AS);
            append(bnf, id());
        } else if (index == AS) {
            append(bnf, id());
        }

        return array(bnf);
    }

    public String[] textColumn(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] xmlQuery(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] objectTable(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] objectColumn(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] xmlTable(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] xmlColumn(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] intVal(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, Tokens.PLUS);
                append(bnf, Tokens.MINUS);
                append(bnf, "[0-9]");
                break;
            case PLUS:
            case MINUS:
                append(bnf, "[0-9]");
                break;
        }

        return array(bnf);
    }

    public String[] subqueryFromClause(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] unaryFromClause(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] where(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] criteria(int... indices) {
        List<String> bnf = newList();

        append(bnf, compoundCritOr(0));

        return array(bnf);
    }

    public String[] compoundCritOr(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] compoundCritAnd(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] notCrit(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] booleanPrimary(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] operator() {
        List<String> bnf = newList();

        append(bnf, Tokens.EQ);
        append(bnf, Tokens.NE);
        append(bnf, Tokens.NE2);
        append(bnf, Tokens.LT);
        append(bnf, Tokens.LE);
        append(bnf, Tokens.GT);
        append(bnf, Tokens.GE);

        return array(bnf);
    }

    public String[] compareCrit(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] subquery(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] subqueryCompareCriteria(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] matchCrit(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] regexMatchCrit(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] charVal() {
        return stringVal();
    }

    public String[] betweenCrit(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] isNullCrit(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] setCrit(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] existsCriteria(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] groupBy(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] having(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] orderby(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] sortSpecification(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] sortKey(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] intParam(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] limit(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] fetchLimit(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] option(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    String[] expression(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] commonValueExpression(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] plusExpression(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] plusMinus() {
        List<String> bnf = newList();

        append(bnf, Tokens.PLUS);
        append(bnf, Tokens.MINUS);

        return array(bnf);
    }

    public String[] timesExpression(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] timesOperator() {
        List<String> bnf = newList();

        append(bnf, Tokens.STAR);
        append(bnf, Tokens.FORWARD_SLASH);

        return array(bnf);
    }

    public String[] valueExpressionPrimary(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] parameterReference(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] unescapedFunction(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] nestedExpression(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] unsignedValueExpressionPrimary(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] windowSpecification(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] caseExpression(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] searchedCaseExpression(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] function(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] xmlParse(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] queryString(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] xmlElement(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] xmlAttributes(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] jsonObject(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] derivedColumnList(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] xmlForest(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] xmlNamespaces(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] namespaceItem(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] parseDataTypePrimary(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] parseDataType(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] intervalType(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] nonNumericLiteral(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] unsignedNumericLiteral(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }

    public String[] columnList(int... indices) {
        List<String> bnf = newList();

        int index = concat(indices);
        switch (index) {
            case 0:
                append(bnf, VAR1);
                break;
            case 1:
                append(bnf, VAR2);
                break;
        }

        return array(bnf);
    }
}
