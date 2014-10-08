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
public class BNF  extends AbstractBNF {

	/**
	 * @param version
	 */
	public BNF(ITeiidVersion version) {
		super(version);
	}

	public String[] stringVal(int... indices) {
		List<String> bnf = newList();

		append(bnf, "((N | E )? ' (('') | ~ [ ' ] )* ' )");
		return array(bnf);
	}

	public String[] nonReserved(int... indices) {
		List<String> bnf = newList();

		append(bnf, "INSTEAD");
		append(bnf, "VIEW");
		append(bnf, "ENABLED");
		append(bnf, "DISABLED");
		append(bnf, "KEY");
		append(bnf, "SERIAL");
		append(bnf, "TEXTAGG");
		append(bnf, "COUNT");
		append(bnf, "ROW_NUMBER");
		append(bnf, "RANK");
		append(bnf, "DENSE_RANK");
		append(bnf, "SUM");
		append(bnf, "AVG");
		append(bnf, "MIN");
		append(bnf, "MAX");
		append(bnf, "EVERY");
		append(bnf, "STDDEV_POP");
		append(bnf, "STDDEV_SAMP");
		append(bnf, "VAR_SAMP");
		append(bnf, "VAR_POP");
		append(bnf, "DOCUMENT");
		append(bnf, "CONTENT");
		append(bnf, "TRIM");
		append(bnf, "EMPTY");
		append(bnf, "ORDINALITY");
		append(bnf, "PATH");
		append(bnf, "FIRST");
		append(bnf, "LAST");
		append(bnf, "NEXT");
		append(bnf, "SUBSTRING");
		append(bnf, "EXTRACT");
		append(bnf, "TO_CHARS");
		append(bnf, "TO_BYTES");
		append(bnf, "TIMESTAMPADD");
		append(bnf, "TIMESTAMPDIFF");
		append(bnf, "QUERYSTRING");
		append(bnf, "NAMESPACE");
		append(bnf, "RESULT");
		append(bnf, "INDEX");
		append(bnf, "ACCESSPATTERN");
		append(bnf, "AUTO_INCREMENT");
		append(bnf, "WELLFORMED");
		append(bnf, "SQL_TSI_FRAC_SECOND");
		append(bnf, "SQL_TSI_SECOND");
		append(bnf, "SQL_TSI_MINUTE");
		append(bnf, "SQL_TSI_HOUR");
		append(bnf, "SQL_TSI_DAY");
		append(bnf, "SQL_TSI_WEEK");
		append(bnf, "SQL_TSI_MONTH");
		append(bnf, "SQL_TSI_QUARTER");
		append(bnf, "SQL_TSI_YEAR");
		append(bnf, "TEXTTABLE");
		append(bnf, "ARRAYTABLE");
		append(bnf, "SELECTOR");
		append(bnf, "SKIP");
		append(bnf, "WIDTH");
		append(bnf, "PASSING");
		append(bnf, "NAME");
		append(bnf, "ENCODING");
		append(bnf, "COLUMNS");
		append(bnf, "DELIMITER");
		append(bnf, "QUOTE");
		append(bnf, "HEADER");
		append(bnf, "NULLS");
		append(bnf, "OBJECTTABLE");
		append(bnf, "VERSION");
		append(bnf, "INCLUDING");
		append(bnf, "EXCLUDING");
		append(bnf, "XMLDECLARATION");
		append(bnf, "VARIADIC");
		append(bnf, "RAISE");
		append(bnf, "EXCEPTION");
		append(bnf, "CHAIN");
		append(bnf, "JSONARRAY_AGG");
		append(bnf, "JSONOBJECT");
		return array(bnf);
	}

	public String[] id(int... indices) {
		List<String> bnf = newList();

		append(bnf, "((@ | # | ([ A-Z, A-Z ] | [ Œ-� ] ) ) (([ A-Z, A-Z ] | [ Œ-� ] ) | _ | [ 0-9 ] )* ) | ( (() | ~ [  ] )+  ) (. ((@ | # | ([ A-Z, A-Z ] | [ Œ-� ] ) ) (([ A-Z, A-Z ] | [ Œ-� ] ) | _ | [ 0-9 ] )* ) | ( (() | ~ [  ] )+  ))*");
		append(bnf, nonReserved(0));
		return array(bnf);
	}

	public String[] command(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, createProcedure(0));
append(bnf, userCommand(0));
append(bnf, callableStatement(0));
				break;
			case BNF.createProcedure:
				append(bnf, ";");
				break;
			case BNF.userCommand:
				break;
			case BNF.callableStatement:
				break;
		}

		return array(bnf);
	}

	public String[] designerCommand(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, createProcedure(0));
append(bnf, forEachRowTriggerAction(0));
append(bnf, userCommand(0));
				break;
			case BNF.createProcedure:
				append(bnf, ";");
				break;
			case BNF.forEachRowTriggerAction:
				break;
			case BNF.userCommand:
				break;
		}

		return array(bnf);
	}

	public String[] createTrigger(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "CREATE");
				break;
			case BNF.CREATE:
				append(bnf, "TRIGGER");
				break;
			case BNF.TRIGGER:
				append(bnf, "ON");
				break;
			case BNF.ON:
				append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, "INSTEAD");
				break;
			case BNF.INSTEAD:
				append(bnf, "OF");
				break;
			case BNF.OF:
				append(bnf, "INSERT");
				append(bnf, "UPDATE");
				append(bnf, "DELETE");
				break;
			case BNF.INSERT:
				append(bnf, "AS");
				break;
			case BNF.UPDATE:
			    
			    ERROR ERROR WILL ROBINSON!!!
			    
				break;
			case BNF.DELETE:

			    ERROR ERROR WILL ROBINSON!!!

				break;
			case BNF.AS:
				append(bnf, forEachRowTriggerAction(0));
				break;
		}

		return array(bnf);
	}

	public String[] alter(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "ALTER");
				break;
			case BNF.ALTER:
				append(bnf, "VIEW");
				append(bnf, "PROCEDURE");
				append(bnf, "TRIGGER");
				break;
			case BNF.VIEW:
				append(bnf, id(0));
				break;
				
				ERROR VIEW/id and PROCEDURE/id are missing and this should be an if statement!!!!!
				
				
				
			case BNF.PROCEDURE:
				append(bnf, id(0));
				break;
			case BNF.TRIGGER:
				append(bnf, "ON");
				break;
			case BNF.ON:
				append(bnf, id(0));
				break;
			case BNF.INSTEAD:
				append(bnf, "OF");
				break;
			case BNF.OF:
				append(bnf, "INSERT");
				append(bnf, "UPDATE");
				append(bnf, "DELETE");
				break;
			case BNF.INSERT:
				append(bnf, "AS");
				append(bnf, "ENABLED");
				append(bnf, "DISABLED");
				break;
			case BNF.UPDATE:
				break;
			case BNF.DELETE:
				break;
		}

		return array(bnf);
	}

	public String[] forEachRowTriggerAction(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "FOR");
				break;
			case BNF.FOR:
				append(bnf, "EACH");
				break;
			case BNF.EACH:
				append(bnf, "ROW");
				break;
			case BNF.ROW:
				append(bnf, "BEGIN");
				append(bnf, statement(0));
				break;
			case BNF.BEGIN:
				append(bnf, "ATOMIC");
				append(bnf, statement(0));
				break;
			case BNF.ATOMIC:
				append(bnf, statement(0));
				break;
			case BNF.statement:
				append(bnf, "END");
				break;
		}

		return array(bnf);
	}

	public String[] userCommand(int... indices) {
		List<String> bnf = newList();

		append(bnf, queryExpression(0));
		append(bnf, storedProcedure(0));
		append(bnf, insert(0));
		append(bnf, update(0));
		append(bnf, delete(0));
		append(bnf, alter(0));
		append(bnf, createTrigger(0));
		append(bnf, compoundStatement(0));
		return array(bnf);
	}

	public String[] errorStatement(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "ERROR");
				break;
			case BNF.ERROR:
				append(bnf, expression(0));
				break;
		}

		return array(bnf);
	}

	public String[] raiseStatement(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "RAISE");
				break;
			case BNF.RAISE:
				append(bnf, "SQLWARNING");
				append(bnf, exceptionReference(0));
				break;
			case BNF.SQLWARNING:
				append(bnf, exceptionReference(0));
				break;
		}

		return array(bnf);
	}

	public String[] exceptionReference(int... indices) {
		List<String> bnf = newList();

		append(bnf, id(0));
		append(bnf, exception(0));
		return array(bnf);
	}

	public String[] exception(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "SQLEXCEPTION");
				break;
			case BNF.SQLEXCEPTION:
				append(bnf, commonValueExpression(0));
				break;
			case BNF.SQLSTATE:
				append(bnf, commonValueExpression(0));
				break;
			case BNF.CHAIN:
				append(bnf, exceptionReference(0));
				break;
			case BNF.COMMA:
				append(bnf, commonValueExpression(0));
				break;
		}

		return array(bnf);
	}

	public String[] statement(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, id(0));
append(bnf, ifStatement(0));
append(bnf, delimitedStatement(0));
				break;
			case BNF.id:
				append(bnf, ":");
				break;
			case BNF.COLON:
				append(bnf, loopStatement(0));
				append(bnf, whileStatement(0));
				append(bnf, compoundStatement(0));
				break;
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
				append(bnf, ";");
				break;
			case BNF.sqlStatement:
				break;
			case BNF.errorStatement:
				break;
			case BNF.raiseStatement:
				break;
			case BNF.declareStatement:
				break;
			case BNF.branchingStatement:
				break;
			case BNF.returnStatement:
				break;
		}

		return array(bnf);
	}

	public String[] compoundStatement(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "BEGIN");
				break;
			case BNF.BEGIN:
				append(bnf, "NOT");
				append(bnf, statement(0));
				break;
			case BNF.NOT:
				append(bnf, "ATOMIC");
				break;
			case BNF.ATOMIC:
				append(bnf, statement(0));
				break;
			case BNF.EXCEPTION:
				append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, statement(0));
				break;
		}

		return array(bnf);
	}

	public String[] branchingStatement(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "BREAK");
append(bnf, "CONTINUE");
append(bnf, "LEAVE");
				break;
			case BNF.BREAK:
				append(bnf, id(0));
				break;
			case BNF.CONTINUE:
				append(bnf, id(0));
				break;
			case BNF.LEAVE:
				append(bnf, id(0));
				break;
		}

		return array(bnf);
	}

	public String[] returnStatement(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "RETURN");
				break;
			case BNF.RETURN:
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
append(bnf, "WHILE");
				break;
			case BNF.WHILE:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, criteria(0));
				break;
			case BNF.criteria:
				append(bnf, ")");
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
append(bnf, "LOOP");
				break;
			case BNF.LOOP:
				append(bnf, "ON");
				break;
			case BNF.ON:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, queryExpression(0));
				break;
			case BNF.queryExpression:
				append(bnf, ")");
				break;
			case BNF.RPAREN:
				append(bnf, "AS");
				break;
			case BNF.AS:
				append(bnf, id(0));
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
append(bnf, "IF");
				break;
			case BNF.IF:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, criteria(0));
				break;
			case BNF.criteria:
				append(bnf, ")");
				break;
			case BNF.RPAREN:
				append(bnf, statement(0));
				break;
			case BNF.statement:
				append(bnf, "ELSE");
				break;
			case BNF.ELSE:
				append(bnf, statement(0));
				break;
		}

		return array(bnf);
	}

	public String[] declareStatement(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "DECLARE");
				break;
			case BNF.DECLARE:
				append(bnf, parseDataType(0));
				append(bnf, "EXCEPTION");
				break;
			case BNF.parseDataType:
				append(bnf, id(0));
				break;
			case BNF.EXCEPTION:
				append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, "=");
				break;
			case BNF.EQ:
				append(bnf, assignStatementOperand(0));
				break;
		}

		return array(bnf);
	}

	public String[] assignStatement(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, "=");
				break;
			case BNF.EQ:
				append(bnf, assignStatementOperand(0));
				append(bnf, storedProcedure(0));
				break;
			case BNF.storedProcedure:
				append(bnf, "WITH");
				append(bnf, "WITHOUT");
				break;
			case BNF.WITH:
				append(bnf, "RETURN");
				break;
			case BNF.WITHOUT:
				append(bnf, "RETURN");
				break;
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
append(bnf, userCommand(0));
append(bnf, dynamicCommand(0));
				break;
			case BNF.userCommand:
				append(bnf, "WITH");
				append(bnf, "WITHOUT");
				break;
			case BNF.dynamicCommand:
				append(bnf, "WITH");
				append(bnf, "WITHOUT");
				break;
			case BNF.WITH:
				append(bnf, "RETURN");
				break;
			case BNF.WITHOUT:
				append(bnf, "RETURN");
				break;
		}

		return array(bnf);
	}

	public String[] createProcedure(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "CREATE");
				break;
			case BNF.CREATE:
				append(bnf, "VIRTUAL");
				append(bnf, "PROCEDURE");
				break;
			case BNF.VIRTUAL:
				append(bnf, "PROCEDURE");
				break;
			case BNF.PROCEDURE:
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
append(bnf, "CREATE");
				break;
			case BNF.CREATE:
				append(bnf, "VIRTUAL");
				append(bnf, "PROCEDURE");
				break;
			case BNF.VIRTUAL:
				append(bnf, "PROCEDURE");
				break;
			case BNF.PROCEDURE:
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
append(bnf, "EXECUTE");
append(bnf, "EXEC");
				break;
			case BNF.EXECUTE:
				append(bnf, "STRING");
				append(bnf, "IMMEDIATE");
				append(bnf, expression(0));
				break;
			case BNF.EXEC:
				append(bnf, "STRING");
				append(bnf, "IMMEDIATE");
				append(bnf, expression(0));
				break;
			case BNF.STRING:
				append(bnf, expression(0));
				break;
			case BNF.IMMEDIATE:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, "AS");
				append(bnf, "USING");
				append(bnf, "UPDATE");
				break;
			case BNF.AS:
				append(bnf, createElementsWithTypes(0));
				break;
			case BNF.USING:
				append(bnf, setClauseList(0));
				break;
			case BNF.UPDATE:
				append(bnf, intVal(0));
				append(bnf, "*");
				break;
			case BNF.createElementsWithTypes:
				append(bnf, "INTO");
				append(bnf, "USING");
				append(bnf, "UPDATE");
				break;
			case BNF.setClauseList:
				append(bnf, "UPDATE");
				break;
			case BNF.INTO:
				append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, "USING");
				append(bnf, "UPDATE");
				break;
		}

		return array(bnf);
	}

	public String[] setClauseList(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, "=");
				break;
			case BNF.EQ:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, "=");
				break;
			case BNF.EQ:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				break;
		}

		return array(bnf);
	}

	public String[] createElementsWithTypes(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, parseDataType(0));
				break;
			case BNF.parseDataType:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, parseDataType(0));
				break;
			case BNF.parseDataType:
				break;
		}

		return array(bnf);
	}

	public String[] callableStatement(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "{");
				break;
			case BNF.LBRACE:
				append(bnf, "?");
				append(bnf, "CALL");
				break;
			case BNF.QMARK:
				append(bnf, "=");
				break;
			case BNF.CALL:
				append(bnf, id(0));
				break;
			case BNF.EQ:
				append(bnf, "CALL");
				break;
			case BNF.id:
				append(bnf, "(");
				append(bnf, "}");
				break;
			case BNF.LPAREN:
				append(bnf, executeNamedParams(0));
				append(bnf, expressionList(0));
				break;
			case BNF.RBRACE:
				append(bnf, option(0));
				break;
			case BNF.executeNamedParams:
				append(bnf, ")");
				break;
			case BNF.expressionList:
				append(bnf, ")");
				break;
			case BNF.RPAREN:
				append(bnf, "}");
				break;
		}

		return array(bnf);
	}

	public String[] storedProcedure(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "EXEC");
append(bnf, "EXECUTE");
append(bnf, "CALL");
				break;
			case BNF.EXEC:
				append(bnf, id(0));
				break;
			case BNF.EXECUTE:
				break;
			case BNF.CALL:
				break;
			case BNF.id:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, executeNamedParams(0));
				append(bnf, expressionList(0));
				break;
			case BNF.executeNamedParams:
				append(bnf, ")");
				break;
			case BNF.expressionList:
				append(bnf, ")");
				break;
			case BNF.RPAREN:
				append(bnf, option(0));
				break;
		}

		return array(bnf);
	}

	public String[] executeNamedParams(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, "=");
				break;
			case BNF.EQ:
				append(bnf, ">");
				append(bnf, expression(0));
				break;
			case BNF.GT:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, "=");
				break;
			case BNF.EQ:
				append(bnf, ">");
				append(bnf, expression(0));
				break;
			case BNF.GT:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				break;
		}

		return array(bnf);
	}

	public String[] insert(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "INSERT");
append(bnf, "MERGE");
				break;
			case BNF.INSERT:
				append(bnf, "INTO");
				break;
			case BNF.MERGE:
				append(bnf, "INTO");
				break;
			case BNF.INTO:
				append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, columnList(0));
				append(bnf, queryExpression(0));
				break;
			case BNF.columnList:
				append(bnf, queryExpression(0));
				break;
			case BNF.queryExpression:
				append(bnf, option(0));
				break;
		}

		return array(bnf);
	}

	public String[] expressionList(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				break;
		}

		return array(bnf);
	}

	public String[] update(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "UPDATE");
				break;
			case BNF.UPDATE:
				append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, "SET");
				break;
			case BNF.SET:
				append(bnf, setClauseList(0));
				break;
			case BNF.setClauseList:
				append(bnf, where(0));
				append(bnf, option(0));
				break;
			case BNF.where:
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
append(bnf, "DELETE");
				break;
			case BNF.DELETE:
				append(bnf, "FROM");
				break;
			case BNF.FROM:
				append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, where(0));
				append(bnf, option(0));
				break;
			case BNF.where:
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
append(bnf, "WITH");
				break;
			case BNF.WITH:
				append(bnf, withListElement(0));
				break;
			case BNF.withListElement:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, withListElement(0));
				break;
			case BNF.withListElement:
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
append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, columnList(0));
				append(bnf, "AS");
				break;
			case BNF.AS:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, queryExpression(0));
				break;
			case BNF.queryExpression:
				append(bnf, ")");
				break;
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
			case BNF.queryTerm:
				append(bnf, "UNION");
				append(bnf, "EXCEPT");
				break;
			case BNF.UNION:
				append(bnf, "ALL");
				append(bnf, "DISTINCT");
				append(bnf, queryTerm(0));
				break;
			case BNF.EXCEPT:
				append(bnf, "ALL");
				append(bnf, "DISTINCT");
				append(bnf, queryTerm(0));
				break;
			case BNF.ALL:
				append(bnf, queryTerm(0));
				break;
			case BNF.DISTINCT:
				append(bnf, queryTerm(0));
				break;
			case BNF.queryTerm:
				append(bnf, orderby(0));
				append(bnf, limit(0));
				append(bnf, option(0));
				break;
			case BNF.orderby:
				append(bnf, limit(0));
				append(bnf, option(0));
				break;
			case BNF.limit:
				append(bnf, option(0));
				break;
		}

		return array(bnf);
	}

	public String[] queryTerm(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, queryPrimary(0));
				break;
			case BNF.queryPrimary:
				append(bnf, "INTERSECT");
				break;
			case BNF.INTERSECT:
				append(bnf, "ALL");
				append(bnf, "DISTINCT");
				append(bnf, queryPrimary(0));
				break;
			case BNF.ALL:
				append(bnf, queryPrimary(0));
				break;
			case BNF.DISTINCT:
				append(bnf, queryPrimary(0));
				break;
			case BNF.queryPrimary:
				break;
		}

		return array(bnf);
	}

	public String[] queryPrimary(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, query(0));
append(bnf, "VALUES");
append(bnf, "TABLE");
append(bnf, "(");
				break;
			case BNF.VALUES:
				append(bnf, "(");
				break;
			case BNF.TABLE:
				append(bnf, id(0));
				break;
			case BNF.LPAREN:
				append(bnf, queryExpressionBody(0));
				break;
			case BNF.queryExpressionBody:
				append(bnf, ")");
				break;
			case BNF.expressionList:
				append(bnf, ")");
				break;
			case BNF.COMMA:
				append(bnf, "(");
				break;
			case BNF.expressionList:
				append(bnf, ")");
				break;
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
			case BNF.select:
				append(bnf, into(0));
				append(bnf, from(0));
				break;
			case BNF.into:
				append(bnf, from(0));
				break;
			case BNF.from:
				append(bnf, where(0));
				append(bnf, groupBy(0));
				append(bnf, having(0));
				break;
			case BNF.where:
				append(bnf, groupBy(0));
				append(bnf, having(0));
				break;
			case BNF.groupBy:
				append(bnf, having(0));
				break;
			case BNF.having:
				break;
		}

		return array(bnf);
	}

	public String[] into(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "INTO");
				break;
			case BNF.INTO:
				append(bnf, id(0));
				break;
		}

		return array(bnf);
	}

	public String[] select(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "SELECT");
				break;
			case BNF.SELECT:
				append(bnf, "ALL");
				append(bnf, "DISTINCT");
				append(bnf, "*");
				append(bnf, selectSymbol(0));
				break;
			case BNF.ALL:
				append(bnf, "*");
				append(bnf, selectSymbol(0));
				break;
			case BNF.DISTINCT:
				append(bnf, "*");
				append(bnf, selectSymbol(0));
				break;
			case BNF.selectSymbol:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, selectSymbol(0));
				break;
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
			case BNF.expression:
				append(bnf, "AS");
				break;
			case BNF.AS:
				append(bnf, id(0));
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
			case BNF.expression:
				append(bnf, "AS");
				break;
			case BNF.AS:
				append(bnf, id(0));
				break;
		}

		return array(bnf);
	}

	public String[] allInGroupSymbol(int... indices) {
		List<String> bnf = newList();

		append(bnf, "((@ | # | ([ A-Z, A-Z ] | [ Œ-� ] ) ) (([ A-Z, A-Z ] | [ Œ-� ] ) | _ | [ 0-9 ] )* ) | ( (() | ~ [  ] )+  ) (. ((@ | # | ([ A-Z, A-Z ] | [ Œ-� ] ) ) (([ A-Z, A-Z ] | [ Œ-� ] ) | _ | [ 0-9 ] )* ) | ( (() | ~ [  ] )+  ))* . *");
		return array(bnf);
	}

	public String[] orderedAgg(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "XMLAGG");
				break;
			case BNF.XMLAGG:
				append(bnf, "ARRAY_AGG");
				break;
			case BNF.ARRAY_AGG:
				append(bnf, "JSONARRAY_AGG");
				break;
			case BNF.JSONARRAY_AGG:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, orderby(0));
				append(bnf, ")");
				break;
			case BNF.orderby:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] textAgg(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "TEXTAGG");
				break;
			case BNF.TEXTAGG:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, "FOR");
				append(bnf, derivedColumn(0));
				break;
			case BNF.FOR:
				append(bnf, derivedColumn(0));
				break;
			case BNF.derivedColumn:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, derivedColumn(0));
				break;
			case BNF.derivedColumn:
				append(bnf, "DELIMITER");
				append(bnf, "QUOTE");
				append(bnf, "HEADER");
				append(bnf, "ENCODING");
				append(bnf, orderby(0));
				append(bnf, ")");
				break;
			case BNF.DELIMITER:
				append(bnf, "QUOTE");
				append(bnf, "HEADER");
				append(bnf, "ENCODING");
				append(bnf, orderby(0));
				append(bnf, ")");
				break;
			case BNF.QUOTE:
				append(bnf, "HEADER");
				append(bnf, "ENCODING");
				append(bnf, orderby(0));
				append(bnf, ")");
				break;
			case BNF.HEADER:
				append(bnf, "ENCODING");
				append(bnf, orderby(0));
				append(bnf, ")");
				break;
			case BNF.ENCODING:
				append(bnf, id(0));
				break;
			case BNF.orderby:
				append(bnf, ")");
				break;
			case BNF.id:
				append(bnf, orderby(0));
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] aggregateSymbol(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "COUNT");
append(bnf, "COUNT");
append(bnf, "SUM");
append(bnf, "AVG");
append(bnf, "MIN");
append(bnf, "MAX");
append(bnf, "EVERY");
append(bnf, "STDDEV_POP");
append(bnf, "STDDEV_SAMP");
append(bnf, "VAR_SAMP");
append(bnf, "VAR_POP");
append(bnf, "SOME");
append(bnf, "ANY");
				break;
			case BNF.COUNT:
				append(bnf, "(");
				break;
			case BNF.COUNT:
				append(bnf, "(");
				break;
			case BNF.SUM:
				break;
			case BNF.AVG:
				break;
			case BNF.MIN:
				break;
			case BNF.MAX:
				break;
			case BNF.EVERY:
				break;
			case BNF.STDDEV_POP:
				break;
			case BNF.STDDEV_SAMP:
				break;
			case BNF.VAR_SAMP:
				break;
			case BNF.VAR_POP:
				break;
			case BNF.SOME:
				break;
			case BNF.ANY:
				break;
			case BNF.LPAREN:
				append(bnf, "*");
				break;
			case BNF.LPAREN:
				append(bnf, "DISTINCT");
				append(bnf, "ALL");
				append(bnf, expression(0));
				break;
			case BNF.STAR:
				append(bnf, ")");
				break;
			case BNF.DISTINCT:
				append(bnf, expression(0));
				break;
			case BNF.ALL:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] analyticAggregateSymbol(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "ROW_NUMBER");
append(bnf, "RANK");
append(bnf, "DENSE_RANK");
				break;
			case BNF.ROW_NUMBER:
				append(bnf, "(");
				break;
			case BNF.RANK:
				break;
			case BNF.DENSE_RANK:
				break;
			case BNF.LPAREN:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] filterClause(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "FILTER");
				break;
			case BNF.FILTER:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, "WHERE");
				break;
			case BNF.WHERE:
				append(bnf, booleanPrimary(0));
				break;
			case BNF.booleanPrimary:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] from(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "FROM");
				break;
			case BNF.FROM:
				append(bnf, tableReference(0));
				break;
			case BNF.tableReference:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, tableReference(0));
				break;
			case BNF.tableReference:
				break;
		}

		return array(bnf);
	}

	public String[] tableReference(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "{ OJ");
append(bnf, joinedTable(0));
				break;
			case BNF.ESCAPEDJOIN:
				append(bnf, joinedTable(0));
				break;
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
			case BNF.tablePrimary:
				append(bnf, crossJoin(0));
				append(bnf, qualifiedJoin(0));
				break;
			case BNF.crossJoin:
				break;
			case BNF.qualifiedJoin:
				break;
		}

		return array(bnf);
	}

	public String[] crossJoin(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "CROSS");
append(bnf, "UNION");
				break;
			case BNF.CROSS:
				append(bnf, "JOIN");
				break;
			case BNF.UNION:
				append(bnf, "JOIN");
				break;
			case BNF.JOIN:
				append(bnf, tablePrimary(0));
				break;
		}

		return array(bnf);
	}

	public String[] qualifiedJoin(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "RIGHT");
append(bnf, "LEFT");
append(bnf, "FULL");
append(bnf, "INNER");
				break;
			case BNF.RIGHT:
				append(bnf, "OUTER");
				append(bnf, "JOIN");
				break;
			case BNF.LEFT:
				append(bnf, "OUTER");
				break;
			case BNF.FULL:
				append(bnf, "OUTER");
				break;
			case BNF.INNER:
				break;
			case BNF.OUTER:
				append(bnf, "JOIN");
				break;
			case BNF.JOIN:
				append(bnf, tableReference(0));
				break;
			case BNF.OUTER:
				break;
			case BNF.OUTER:
				break;
			case BNF.tableReference:
				append(bnf, "ON");
				break;
			case BNF.ON:
				append(bnf, criteria(0));
				break;
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
append(bnf, "(");
				break;
			case BNF.textTable:
				append(bnf, "MAKEDEP");
				append(bnf, "MAKENOTDEP");
				break;
			case BNF.arrayTable:
				break;
			case BNF.xmlTable:
				break;
			case BNF.objectTable:
				break;
			case BNF.unaryFromClause:
				break;
			case BNF.subqueryFromClause:
				break;
			case BNF.LPAREN:
				append(bnf, joinedTable(0));
				break;
			case BNF.joinedTable:
				append(bnf, ")");
				break;
			case BNF.RPAREN:
				break;
		}

		return array(bnf);
	}

	public String[] xmlSerialize(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "XMLSERIALIZE");
				break;
			case BNF.XMLSERIALIZE:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, "DOCUMENT");
				append(bnf, "CONTENT");
				append(bnf, expression(0));
				break;
			case BNF.DOCUMENT:
				append(bnf, expression(0));
				break;
			case BNF.CONTENT:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, "AS");
				append(bnf, "ENCODING");
				append(bnf, "VERSION");
				append(bnf, "INCLUDING");
				append(bnf, "EXCLUDING");
				append(bnf, ")");
				break;
			case BNF.AS:
				append(bnf, "STRING");
				append(bnf, "VARCHAR");
				append(bnf, "CLOB");
				append(bnf, "VARBINARY");
				append(bnf, "BLOB");
				break;
			case BNF.ENCODING:
				append(bnf, id(0));
				break;
			case BNF.VERSION:
				append(bnf, stringVal(0));
				break;
			case BNF.INCLUDING:
				append(bnf, "XMLDECLARATION");
				break;
			case BNF.EXCLUDING:
				append(bnf, "XMLDECLARATION");
				break;
			case BNF.STRING:
				append(bnf, "ENCODING");
				append(bnf, "VERSION");
				append(bnf, "INCLUDING");
				append(bnf, "EXCLUDING");
				append(bnf, ")");
				break;
			case BNF.VARCHAR:
				break;
			case BNF.CLOB:
				break;
			case BNF.VARBINARY:
				break;
			case BNF.BLOB:
				break;
			case BNF.id:
				append(bnf, "VERSION");
				append(bnf, "INCLUDING");
				append(bnf, "EXCLUDING");
				append(bnf, ")");
				break;
			case BNF.stringVal:
				append(bnf, "INCLUDING");
				append(bnf, "EXCLUDING");
				append(bnf, ")");
				break;
			case BNF.XMLDECLARATION:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] arrayTable(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "ARRAYTABLE");
				break;
			case BNF.ARRAYTABLE:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, valueExpressionPrimary(0));
				break;
			case BNF.valueExpressionPrimary:
				append(bnf, "COLUMNS");
				break;
			case BNF.COLUMNS:
				append(bnf, createElementsWithTypes(0));
				break;
			case BNF.createElementsWithTypes:
				append(bnf, ")");
				break;
			case BNF.RPAREN:
				append(bnf, "AS");
				append(bnf, id(0));
				break;
			case BNF.AS:
				append(bnf, id(0));
				break;
		}

		return array(bnf);
	}

	public String[] textTable(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "TEXTTABLE");
				break;
			case BNF.TEXTTABLE:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, commonValueExpression(0));
				break;
			case BNF.commonValueExpression:
				append(bnf, "SELECTOR");
				append(bnf, "COLUMNS");
				break;
			case BNF.SELECTOR:
				append(bnf, stringVal(0));
				break;
			case BNF.COLUMNS:
				append(bnf, textColumn(0));
				break;
			case BNF.stringVal:
				append(bnf, "COLUMNS");
				break;
			case BNF.textColumn:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, textColumn(0));
				break;
			case BNF.textColumn:
				append(bnf, "NO");
				append(bnf, "DELIMITER");
				append(bnf, "ESCAPE");
				append(bnf, "QUOTE");
				append(bnf, "HEADER");
				append(bnf, "SKIP");
				append(bnf, ")");
				break;
			case BNF.NO:
				append(bnf, "ROW");
				break;
			case BNF.DELIMITER:
				append(bnf, "ESCAPE");
				append(bnf, "QUOTE");
				append(bnf, "HEADER");
				append(bnf, "SKIP");
				append(bnf, ")");
				break;
			case BNF.ESCAPE:
				append(bnf, "HEADER");
				append(bnf, "SKIP");
				append(bnf, ")");
				break;
			case BNF.QUOTE:
				append(bnf, "HEADER");
				append(bnf, "SKIP");
				append(bnf, ")");
				break;
			case BNF.HEADER:
				append(bnf, intVal(0));
				append(bnf, "SKIP");
				append(bnf, ")");
				break;
			case BNF.SKIP_KEYWORD:
				append(bnf, intVal(0));
				break;
			case BNF.RPAREN:
				append(bnf, "AS");
				append(bnf, id(0));
				break;
			case BNF.ROW:
				append(bnf, "DELIMITER");
				break;
			case BNF.intVal:
				append(bnf, "SKIP");
				append(bnf, ")");
				break;
			case BNF.intVal:
				append(bnf, ")");
				break;
			case BNF.AS:
				append(bnf, id(0));
				break;
		}

		return array(bnf);
	}

	public String[] textColumn(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, "FOR");
				append(bnf, parseDataType(0));
				break;
			case BNF.FOR:
				append(bnf, "ORDINALITY");
				break;
			case BNF.WIDTH:
				append(bnf, intVal(0));
				break;
			case BNF.SELECTOR:
				append(bnf, stringVal(0));
				break;
			case BNF.intVal:
				append(bnf, "NO");
				append(bnf, "SELECTOR");
				break;
			case BNF.stringVal:
				append(bnf, intVal(0));
				break;
			case BNF.NO:
				append(bnf, "TRIM");
				break;
			case BNF.intVal:
				break;
			case BNF.TRIM:
				append(bnf, "SELECTOR");
				break;
		}

		return array(bnf);
	}

	public String[] xmlQuery(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "XMLQUERY");
				break;
			case BNF.XMLQUERY:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, xmlNamespaces(0));
				append(bnf, stringVal(0));
				break;
			case BNF.xmlNamespaces:
				append(bnf, ",");
				break;
			case BNF.stringVal:
				append(bnf, "PASSING");
				append(bnf, "NULL");
				append(bnf, "EMPTY");
				append(bnf, ")");
				break;
			case BNF.COMMA:
				append(bnf, stringVal(0));
				break;
			case BNF.PASSING:
				append(bnf, derivedColumn(0));
				break;
			case BNF.NULL:
				append(bnf, "ON");
				break;
			case BNF.EMPTY:
				append(bnf, "ON");
				break;
			case BNF.derivedColumn:
				append(bnf, ",");
				break;
			case BNF.ON:
				append(bnf, "EMPTY");
				break;
			case BNF.COMMA:
				append(bnf, derivedColumn(0));
				break;
			case BNF.derivedColumn:
				append(bnf, "NULL");
				append(bnf, "EMPTY");
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] objectTable(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "OBJECTTABLE");
				break;
			case BNF.OBJECTTABLE:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, "LANGUAGE");
				append(bnf, stringVal(0));
				break;
			case BNF.LANGUAGE:
				append(bnf, stringVal(0));
				break;
			case BNF.stringVal:
				append(bnf, "PASSING");
				append(bnf, "COLUMNS");
				break;
			case BNF.PASSING:
				append(bnf, derivedColumn(0));
				break;
			case BNF.COLUMNS:
				append(bnf, objectColumn(0));
				break;
			case BNF.derivedColumn:
				append(bnf, ",");
				break;
			case BNF.objectColumn:
				append(bnf, ",");
				break;
			case BNF.derivedColumn:
				append(bnf, "COLUMNS");
				break;
			case BNF.objectColumn:
				append(bnf, ")");
				break;
			case BNF.RPAREN:
				append(bnf, "AS");
				append(bnf, id(0));
				break;
			case BNF.AS:
				append(bnf, id(0));
				break;
		}

		return array(bnf);
	}

	public String[] objectColumn(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, parseDataType(0));
				break;
			case BNF.parseDataType:
				append(bnf, stringVal(0));
				break;
			case BNF.stringVal:
				append(bnf, "DEFAULT");
				break;
			case BNF.DEFAULT_KEYWORD:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				break;
		}

		return array(bnf);
	}

	public String[] xmlTable(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "XMLTABLE");
				break;
			case BNF.XMLTABLE:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, xmlNamespaces(0));
				append(bnf, stringVal(0));
				break;
			case BNF.xmlNamespaces:
				append(bnf, ",");
				break;
			case BNF.stringVal:
				append(bnf, "PASSING");
				append(bnf, "COLUMNS");
				append(bnf, ")");
				break;
			case BNF.PASSING:
				append(bnf, derivedColumn(0));
				break;
			case BNF.COLUMNS:
				append(bnf, xmlColumn(0));
				break;
			case BNF.RPAREN:
				append(bnf, "AS");
				append(bnf, id(0));
				break;
			case BNF.derivedColumn:
				append(bnf, ",");
				break;
			case BNF.xmlColumn:
				append(bnf, ",");
				break;
			case BNF.AS:
				append(bnf, id(0));
				break;
			case BNF.COMMA:
				append(bnf, derivedColumn(0));
				break;
			case BNF.derivedColumn:
				append(bnf, "COLUMNS");
				append(bnf, ")");
				break;
			case BNF.xmlColumn:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] xmlColumn(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, "FOR");
				append(bnf, parseDataType(0));
				break;
			case BNF.FOR:
				append(bnf, "ORDINALITY");
				break;
			case BNF.parseDataType:
				append(bnf, "DEFAULT");
				append(bnf, "PATH");
				break;
			case BNF.DEFAULT_KEYWORD:
				append(bnf, expression(0));
				break;
			case BNF.PATH:
				append(bnf, stringVal(0));
				break;
			case BNF.expression:
				append(bnf, "PATH");
				break;
		}

		return array(bnf);
	}

	public String[] intVal(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "+");
append(bnf, "-");
				break;
			case BNF.PLUS:
				append(bnf, "([ 0-9 ])+");
				break;
			case BNF.MINUS:
				append(bnf, "([ 0-9 ])+");
				break;
		}

		return array(bnf);
	}

	public String[] subqueryFromClause(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "TABLE");
append(bnf, "LATERAL");
				break;
			case BNF.TABLE:
				append(bnf, "(");
				break;
			case BNF.LATERAL:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, queryExpression(0));
				append(bnf, storedProcedure(0));
				break;
			case BNF.queryExpression:
				append(bnf, ")");
				break;
			case BNF.storedProcedure:
				append(bnf, ")");
				break;
			case BNF.RPAREN:
				append(bnf, "AS");
				append(bnf, id(0));
				break;
			case BNF.AS:
				append(bnf, id(0));
				break;
		}

		return array(bnf);
	}

	public String[] unaryFromClause(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, "AS");
				break;
			case BNF.AS:
				append(bnf, id(0));
				break;
		}

		return array(bnf);
	}

	public String[] where(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "WHERE");
				break;
			case BNF.WHERE:
				append(bnf, criteria(0));
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
append(bnf, compoundCritAnd(0));
				break;
			case BNF.compoundCritAnd:
				append(bnf, "OR");
				break;
			case BNF.OR:
				append(bnf, compoundCritAnd(0));
				break;
			case BNF.compoundCritAnd:
				break;
		}

		return array(bnf);
	}

	public String[] compoundCritAnd(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, notCrit(0));
				break;
			case BNF.notCrit:
				append(bnf, "AND");
				break;
			case BNF.AND:
				append(bnf, notCrit(0));
				break;
			case BNF.notCrit:
				break;
		}

		return array(bnf);
	}

	public String[] notCrit(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "NOT");
				break;
			case BNF.NOT:
				append(bnf, booleanPrimary(0));
				break;
		}

		return array(bnf);
	}

	public String[] booleanPrimary(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, commonValueExpression(0));
append(bnf, existsCriteria(0));
				break;
			case BNF.commonValueExpression:
				append(bnf, betweenCrit(0));
				append(bnf, matchCrit(0));
				append(bnf, regexMatchCrit(0));
				append(bnf, setCrit(0));
				append(bnf, isNullCrit(0));
				append(bnf, subqueryCompareCriteria(0));
				append(bnf, compareCrit(0));
				break;
		}

		return array(bnf);
	}

	public String[] operator(int... indices) {
		List<String> bnf = newList();

		append(bnf, "=");
		append(bnf, "<>");
		append(bnf, "!=");
		append(bnf, "<");
		append(bnf, "<=");
		append(bnf, ">");
		append(bnf, ">=");
		return array(bnf);
	}

	public String[] compareCrit(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, operator(0));
				break;
			case BNF.operator:
				append(bnf, commonValueExpression(0));
				break;
		}

		return array(bnf);
	}

	public String[] subquery(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, queryExpression(0));
				append(bnf, storedProcedure(0));
				break;
			case BNF.queryExpression:
				append(bnf, ")");
				break;
			case BNF.storedProcedure:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] subqueryCompareCriteria(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, operator(0));
				break;
			case BNF.operator:
				append(bnf, "ANY");
				append(bnf, "SOME");
				append(bnf, "ALL");
				break;
			case BNF.ANY:
				append(bnf, subquery(0));
				break;
			case BNF.SOME:
				break;
			case BNF.ALL:
				break;
		}

		return array(bnf);
	}

	public String[] matchCrit(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "NOT");
				break;
			case BNF.NOT:
				append(bnf, "LIKE");
				append(bnf, "SIMILAR");
				break;
			case BNF.LIKE:
				append(bnf, commonValueExpression(0));
				break;
			case BNF.SIMILAR:
				append(bnf, "TO");
				break;
			case BNF.commonValueExpression:
				append(bnf, "ESCAPE");
				append(bnf, "{");
				break;
			case BNF.TO:
				append(bnf, commonValueExpression(0));
				break;
			case BNF.ESCAPE:
				break;
			case BNF.LBRACE:
				append(bnf, "ESCAPE");
				break;
			case BNF.charVal:
				append(bnf, "}");
				break;
		}

		return array(bnf);
	}

	public String[] regexMatchCrit(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "NOT");
				break;
			case BNF.NOT:
				append(bnf, "LIKE_REGEX");
				break;
			case BNF.LIKE_REGEX:
				append(bnf, commonValueExpression(0));
				break;
		}

		return array(bnf);
	}

	public String[] betweenCrit(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "NOT");
				break;
			case BNF.NOT:
				append(bnf, "BETWEEN");
				break;
			case BNF.BETWEEN:
				append(bnf, commonValueExpression(0));
				break;
			case BNF.commonValueExpression:
				append(bnf, "AND");
				break;
			case BNF.AND:
				append(bnf, commonValueExpression(0));
				break;
		}

		return array(bnf);
	}

	public String[] isNullCrit(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "IS");
				break;
			case BNF.IS:
				append(bnf, "NOT");
				append(bnf, "NULL");
				break;
			case BNF.NOT:
				append(bnf, "NULL");
				break;
		}

		return array(bnf);
	}

	public String[] setCrit(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "NOT");
				break;
			case BNF.NOT:
				append(bnf, "IN");
				break;
			case BNF.IN:
				append(bnf, subquery(0));
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, commonValueExpression(0));
				break;
			case BNF.commonValueExpression:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, commonValueExpression(0));
				break;
			case BNF.commonValueExpression:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] existsCriteria(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "EXISTS");
				break;
			case BNF.EXISTS:
				append(bnf, subquery(0));
				break;
		}

		return array(bnf);
	}

	public String[] groupBy(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "GROUP");
				break;
			case BNF.GROUP:
				append(bnf, "BY");
				break;
			case BNF.BY:
				append(bnf, "ROLLUP");
				break;
			case BNF.ROLLUP:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, expressionList(0));
				break;
			case BNF.expressionList:
				append(bnf, ")");
				append(bnf, expressionList(0));
				break;
			case BNF.RPAREN:
				break;
		}

		return array(bnf);
	}

	public String[] having(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "HAVING");
				break;
			case BNF.HAVING:
				append(bnf, criteria(0));
				break;
			case BNF.criteria:
				break;
		}

		return array(bnf);
	}

	public String[] orderby(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "ORDER");
				break;
			case BNF.ORDER:
				append(bnf, "BY");
				break;
			case BNF.BY:
				append(bnf, sortSpecification(0));
				break;
			case BNF.sortSpecification:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, sortSpecification(0));
				break;
			case BNF.sortSpecification:
				break;
		}

		return array(bnf);
	}

	public String[] sortSpecification(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, sortKey(0));
				break;
			case BNF.sortKey:
				append(bnf, "ASC");
				append(bnf, "DESC");
				append(bnf, "NULLS");
				break;
			case BNF.ASC:
				append(bnf, "NULLS");
				break;
			case BNF.DESC:
				append(bnf, "NULLS");
				break;
			case BNF.NULLS:
				append(bnf, "FIRST");
				append(bnf, "LAST");
				break;
		}

		return array(bnf);
	}

	public String[] sortKey(int... indices) {
		List<String> bnf = newList();

		append(bnf, expression(0));
		return array(bnf);
	}

	public String[] intParam(int... indices) {
		List<String> bnf = newList();

		append(bnf, intVal(0));
		append(bnf, unsignedValueExpressionPrimary(0));
		return array(bnf);
	}

	public String[] limit(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "LIMIT");
append(bnf, "OFFSET");
append(bnf, fetchLimit(0));
				break;
			case BNF.LIMIT:
				append(bnf, intParam(0));
				break;
			case BNF.OFFSET:
				append(bnf, intParam(0));
				break;
			case BNF.fetchLimit:
				break;
			case BNF.intParam:
				append(bnf, ",");
				break;
			case BNF.intParam:
				append(bnf, "ROW");
				append(bnf, "ROWS");
				break;
			case BNF.COMMA:
				append(bnf, intParam(0));
				break;
			case BNF.ROW:
				append(bnf, fetchLimit(0));
				break;
			case BNF.ROWS:
				append(bnf, fetchLimit(0));
				break;
			case BNF.intParam:
				break;
			case BNF.fetchLimit:
				break;
		}

		return array(bnf);
	}

	public String[] fetchLimit(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "FETCH");
				break;
			case BNF.FETCH:
				append(bnf, "FIRST");
				append(bnf, "NEXT");
				break;
			case BNF.FIRST:
				append(bnf, intParam(0));
				append(bnf, "ROW");
				append(bnf, "ROWS");
				break;
			case BNF.NEXT:
				append(bnf, intParam(0));
				append(bnf, "ROW");
				append(bnf, "ROWS");
				break;
			case BNF.intParam:
				append(bnf, "ROW");
				append(bnf, "ROWS");
				break;
			case BNF.ROW:
				append(bnf, "ONLY");
				break;
			case BNF.ROWS:
				append(bnf, "ONLY");
				break;
		}

		return array(bnf);
	}

	public String[] option(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "MAKEDEP");
append(bnf, "NOCACHE");
				break;
			case BNF.MAKEDEP:
				append(bnf, id(0));
				break;
			case BNF.id:
				break;
			case BNF.id:
				append(bnf, ",");
				append(bnf, "MAKENOTDEP");
				break;
			case BNF.COMMA:
				append(bnf, id(0));
				break;
			case BNF.id:
				break;
			case BNF.id:
				break;
		}

		return array(bnf);
	}

	public String[] expression(int... indices) {
		List<String> bnf = newList();

		append(bnf, criteria(0));
		return array(bnf);
	}

	public String[] commonValueExpression(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, plusExpression(0));
				break;
			case BNF.plusExpression:
				append(bnf, "||");
				break;
			case BNF.CONCAT_OP:
				append(bnf, plusExpression(0));
				break;
			case BNF.plusExpression:
				break;
		}

		return array(bnf);
	}

	public String[] plusExpression(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, timesExpression(0));
				break;
			case BNF.timesExpression:
				append(bnf, plusMinus(0));
				break;
			case BNF.plusMinus:
				append(bnf, timesExpression(0));
				break;
			case BNF.timesExpression:
				break;
		}

		return array(bnf);
	}

	public String[] plusMinus(int... indices) {
		List<String> bnf = newList();

		append(bnf, "+");
		append(bnf, "-");
		return array(bnf);
	}

	public String[] timesExpression(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, valueExpressionPrimary(0));
				break;
			case BNF.valueExpressionPrimary:
				append(bnf, timesOperator(0));
				break;
			case BNF.timesOperator:
				append(bnf, valueExpressionPrimary(0));
				break;
			case BNF.valueExpressionPrimary:
				break;
		}

		return array(bnf);
	}

	public String[] timesOperator(int... indices) {
		List<String> bnf = newList();

		append(bnf, "*");
		append(bnf, "/");
		return array(bnf);
	}

	public String[] valueExpressionPrimary(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, nonNumericLiteral(0));
append(bnf, plusMinus(0));
append(bnf, unsignedValueExpressionPrimary(0));
				break;
			case BNF.plusMinus:
				break;
			case BNF.unsignedValueExpressionPrimary:
				append(bnf, "[");
				break;
			case BNF.LSBRACE:
				append(bnf, plusExpression(0));
				break;
			case BNF.plusExpression:
				append(bnf, "]");
				break;
			case BNF.RSBRACE:
				break;
		}

		return array(bnf);
	}

	public String[] parameterReference(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "?");
append(bnf, "$");
				break;
			case BNF.DOLLAR:
				append(bnf, intVal(0));
				break;
		}

		return array(bnf);
	}

	public String[] unescapedFunction(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, textAgg(0));
append(bnf, aggregateSymbol(0));
append(bnf, orderedAgg(0));
append(bnf, analyticAggregateSymbol(0));
append(bnf, function(0));
				break;
			case BNF.textAgg:
				append(bnf, filterClause(0));
				append(bnf, windowSpecification(0));
				break;
			case BNF.aggregateSymbol:
				break;
			case BNF.orderedAgg:
				break;
			case BNF.analyticAggregateSymbol:
				append(bnf, filterClause(0));
				append(bnf, windowSpecification(0));
				break;
			case BNF.function:
				append(bnf, windowSpecification(0));
				break;
			case BNF.filterClause:
				append(bnf, windowSpecification(0));
				break;
			case BNF.windowSpecification:
				break;
			case BNF.filterClause:
				append(bnf, windowSpecification(0));
				break;
			case BNF.windowSpecification:
				break;
			case BNF.windowSpecification:
				break;
		}

		return array(bnf);
	}

	public String[] nestedExpression(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, expression(0));
				append(bnf, ",");
				append(bnf, ")");
				break;
			case BNF.expression:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, ")");
				break;
			case BNF.COMMA:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, ",");
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] unsignedValueExpressionPrimary(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, parameterReference(0));
append(bnf, "{ FN");
append(bnf, unescapedFunction(0));
append(bnf, "((@ | # | ([ A-Z, A-Z ] | [ Œ-� ] ) ) (([ A-Z, A-Z ] | [ Œ-� ] ) | _ | [ 0-9 ] )* ) | ( (() | ~ [  ] )+  ) (. ((@ | # | ([ A-Z, A-Z ] | [ Œ-� ] ) ) (([ A-Z, A-Z ] | [ Œ-� ] ) | _ | [ 0-9 ] )* ) | ( (() | ~ [  ] )+  ))*");
append(bnf, nonReserved(0));
append(bnf, subquery(0));
append(bnf, nestedExpression(0));
append(bnf, searchedCaseExpression(0));
append(bnf, caseExpression(0));
				break;
			case BNF.parameterReference:
				break;
			case BNF.ESCAPEDFUNCTION:
				append(bnf, function(0));
				break;
			case BNF.unescapedFunction:
				break;
			case BNF.ID:
				break;
			case BNF.subquery:
				break;
			case BNF.nestedExpression:
				break;
			case BNF.searchedCaseExpression:
				break;
			case BNF.caseExpression:
				break;
			case BNF.function:
				append(bnf, "}");
				break;
		}

		return array(bnf);
	}

	public String[] windowSpecification(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "OVER");
				break;
			case BNF.OVER:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, "PARTITION");
				append(bnf, orderby(0));
				append(bnf, ")");
				break;
			case BNF.PARTITION:
				append(bnf, "BY");
				break;
			case BNF.orderby:
				append(bnf, ")");
				break;
			case BNF.BY:
				append(bnf, expressionList(0));
				break;
			case BNF.expressionList:
				append(bnf, orderby(0));
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] caseExpression(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "CASE");
				break;
			case BNF.CASE:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, "WHEN");
				break;
			case BNF.WHEN:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, "THEN");
				break;
			case BNF.THEN:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, "ELSE");
				append(bnf, "END");
				break;
			case BNF.ELSE:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, "END");
				break;
		}

		return array(bnf);
	}

	public String[] searchedCaseExpression(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "CASE");
				break;
			case BNF.CASE:
				append(bnf, "WHEN");
				break;
			case BNF.WHEN:
				append(bnf, criteria(0));
				break;
			case BNF.criteria:
				append(bnf, "THEN");
				break;
			case BNF.THEN:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, "ELSE");
				append(bnf, "END");
				break;
			case BNF.ELSE:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, "END");
				break;
		}

		return array(bnf);
	}

	public String[] function(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "CONVERT");
append(bnf, "CAST");
append(bnf, "SUBSTRING");
append(bnf, "EXTRACT");
append(bnf, "TRIM");
append(bnf, "TO_CHARS");
append(bnf, "TO_BYTES");
append(bnf, "TIMESTAMPADD");
append(bnf, "TIMESTAMPDIFF");
append(bnf, queryString(0));
append(bnf, "LEFT");
append(bnf, "RIGHT");
append(bnf, "CHAR");
append(bnf, "USER");
append(bnf, "YEAR");
append(bnf, "MONTH");
append(bnf, "HOUR");
append(bnf, "MINUTE");
append(bnf, "SECOND");
append(bnf, "XMLCONCAT");
append(bnf, "XMLCOMMENT");
append(bnf, "TRANSLATE");
append(bnf, "INSERT");
append(bnf, xmlParse(0));
append(bnf, xmlElement(0));
append(bnf, "XMLPI");
append(bnf, xmlForest(0));
append(bnf, jsonObject(0));
append(bnf, xmlSerialize(0));
append(bnf, xmlQuery(0));
append(bnf, id(0));
				break;
			case BNF.CONVERT:
				append(bnf, "(");
				break;
			case BNF.CAST:
				append(bnf, "(");
				break;
			case BNF.SUBSTRING:
				append(bnf, "(");
				break;
			case BNF.EXTRACT:
				append(bnf, "(");
				break;
			case BNF.TRIM:
				append(bnf, "(");
				break;
			case BNF.TO_CHARS:
				append(bnf, "(");
				break;
			case BNF.TO_BYTES:
				append(bnf, "(");
				break;
			case BNF.TIMESTAMPADD:
				append(bnf, "(");
				break;
			case BNF.TIMESTAMPDIFF:
				append(bnf, "(");
				break;
			case BNF.queryString:
				break;
			case BNF.LEFT:
				append(bnf, "(");
				break;
			case BNF.RIGHT:
				break;
			case BNF.CHAR:
				break;
			case BNF.USER:
				break;
			case BNF.YEAR:
				break;
			case BNF.MONTH:
				break;
			case BNF.HOUR:
				break;
			case BNF.MINUTE:
				break;
			case BNF.SECOND:
				break;
			case BNF.XMLCONCAT:
				break;
			case BNF.XMLCOMMENT:
				break;
			case BNF.TRANSLATE:
				append(bnf, "(");
				break;
			case BNF.INSERT:
				append(bnf, "(");
				break;
			case BNF.xmlParse:
				break;
			case BNF.xmlElement:
				break;
			case BNF.XMLPI:
				append(bnf, "(");
				break;
			case BNF.jsonObject:
				break;
			case BNF.xmlSerialize:
				break;
			case BNF.xmlQuery:
				break;
			case BNF.id:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, expression(0));
				break;
			case BNF.LPAREN:
				append(bnf, expression(0));
				break;
			case BNF.LPAREN:
				append(bnf, expression(0));
				break;
			case BNF.LPAREN:
				append(bnf, "YEAR");
				append(bnf, "MONTH");
				append(bnf, "DAY");
				append(bnf, "HOUR");
				append(bnf, "MINUTE");
				append(bnf, "SECOND");
				break;
			case BNF.LPAREN:
				append(bnf, "LEADING");
				append(bnf, "TRAILING");
				append(bnf, "BOTH");
				append(bnf, expression(0));
				append(bnf, expression(0));
				break;
			case BNF.LPAREN:
				append(bnf, expression(0));
				break;
			case BNF.LPAREN:
				append(bnf, intervalType(0));
				break;
			case BNF.LPAREN:
				append(bnf, expressionList(0));
				append(bnf, ")");
				break;
			case BNF.LPAREN:
				append(bnf, expressionList(0));
				append(bnf, ")");
				break;
			case BNF.LPAREN:
				append(bnf, "NAME");
				break;
			case BNF.LPAREN:
				append(bnf, "ALL");
				append(bnf, "DISTINCT");
				append(bnf, expressionList(0));
				append(bnf, orderby(0));
				append(bnf, ")");
				break;
			case BNF.expression:
				append(bnf, ",");
				break;
			case BNF.expression:
				append(bnf, "AS");
				break;
			case BNF.expression:
				append(bnf, "FROM");
				append(bnf, ",");
				break;
			case BNF.YEAR:
				append(bnf, "FROM");
				break;
			case BNF.MONTH:
				break;
			case BNF.DAY:
				break;
			case BNF.HOUR:
				break;
			case BNF.MINUTE:
				break;
			case BNF.SECOND:
				break;
			case BNF.LEADING:
				append(bnf, expression(0));
				append(bnf, "FROM");
				break;
			case BNF.TRAILING:
				break;
			case BNF.BOTH:
				break;
			case BNF.expression:
				append(bnf, "FROM");
				break;
			case BNF.expression:
				append(bnf, ")");
				break;
			case BNF.expression:
				append(bnf, ",");
				break;
			case BNF.intervalType:
				append(bnf, ",");
				break;
			case BNF.expressionList:
				append(bnf, ")");
				break;
			case BNF.expressionList:
				append(bnf, ")");
				break;
			case BNF.NAME:
				append(bnf, id(0));
				break;
			case BNF.ALL:
				append(bnf, expressionList(0));
				append(bnf, orderby(0));
				append(bnf, ")");
				break;
			case BNF.DISTINCT:
				append(bnf, expressionList(0));
				append(bnf, orderby(0));
				append(bnf, ")");
				break;
			case BNF.expressionList:
				append(bnf, orderby(0));
				append(bnf, ")");
				break;
			case BNF.orderby:
				append(bnf, ")");
				break;
			case BNF.RPAREN:
				append(bnf, filterClause(0));
				break;
			case BNF.COMMA:
				append(bnf, parseDataType(0));
				break;
			case BNF.AS:
				append(bnf, parseDataType(0));
				break;
			case BNF.FROM:
				append(bnf, expression(0));
				break;
			case BNF.COMMA:
				append(bnf, expressionList(0));
				break;
			case BNF.FROM:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, "FROM");
				break;
			case BNF.FROM:
				append(bnf, expression(0));
				break;
			case BNF.COMMA:
				append(bnf, stringVal(0));
				break;
			case BNF.COMMA:
				append(bnf, expression(0));
				break;
			case BNF.id:
				append(bnf, ",");
				append(bnf, ")");
				break;
			case BNF.parseDataType:
				append(bnf, ")");
				break;
			case BNF.parseDataType:
				append(bnf, ")");
				break;
			case BNF.expression:
				append(bnf, "FOR");
				append(bnf, ")");
				break;
			case BNF.expressionList:
				append(bnf, ")");
				break;
			case BNF.expression:
				append(bnf, ")");
				break;
			case BNF.stringVal:
				append(bnf, ",");
				append(bnf, ")");
				break;
			case BNF.expression:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, expression(0));
				break;
			case BNF.FOR:
				append(bnf, expression(0));
				break;
			case BNF.COMMA:
				append(bnf, expression(0));
				break;
			case BNF.COMMA:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, ")");
				break;
			case BNF.expression:
				append(bnf, ")");
				break;
			case BNF.expression:
				append(bnf, ")");
				break;
			case BNF.expression:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] xmlParse(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "XMLPARSE");
				break;
			case BNF.XMLPARSE:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, "DOCUMENT");
				append(bnf, "CONTENT");
				break;
			case BNF.DOCUMENT:
				append(bnf, expression(0));
				break;
			case BNF.CONTENT:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, "WELLFORMED");
				append(bnf, ")");
				break;
			case BNF.WELLFORMED:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] queryString(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "QUERYSTRING");
				break;
			case BNF.QUERYSTRING:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, expression(0));
				break;
			case BNF.expression:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, derivedColumn(0));
				break;
			case BNF.derivedColumn:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] xmlElement(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "XMLELEMENT");
				break;
			case BNF.XMLELEMENT:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, "NAME");
				break;
			case BNF.NAME:
				append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, ",");
				append(bnf, ",");
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, xmlNamespaces(0));
				break;
			case BNF.COMMA:
				append(bnf, xmlAttributes(0));
				break;
			case BNF.COMMA:
				append(bnf, expression(0));
				break;
			case BNF.xmlNamespaces:
				append(bnf, ",");
				append(bnf, ",");
				break;
			case BNF.xmlAttributes:
				append(bnf, ",");
				break;
			case BNF.expression:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] xmlAttributes(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "XMLATTRIBUTES");
				break;
			case BNF.XMLATTRIBUTES:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, derivedColumn(0));
				break;
			case BNF.derivedColumn:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, derivedColumn(0));
				break;
			case BNF.derivedColumn:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] jsonObject(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "JSONOBJECT");
				break;
			case BNF.JSONOBJECT:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, derivedColumnList(0));
				break;
			case BNF.derivedColumnList:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] derivedColumnList(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, derivedColumn(0));
				break;
			case BNF.derivedColumn:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, derivedColumn(0));
				break;
			case BNF.derivedColumn:
				break;
		}

		return array(bnf);
	}

	public String[] xmlForest(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "XMLFOREST");
				break;
			case BNF.XMLFOREST:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, xmlNamespaces(0));
				append(bnf, derivedColumnList(0));
				break;
			case BNF.xmlNamespaces:
				append(bnf, ",");
				break;
			case BNF.derivedColumnList:
				append(bnf, ")");
				break;
			case BNF.COMMA:
				append(bnf, derivedColumnList(0));
				break;
		}

		return array(bnf);
	}

	public String[] xmlNamespaces(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "XMLNAMESPACES");
				break;
			case BNF.XMLNAMESPACES:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, namespaceItem(0));
				break;
			case BNF.namespaceItem:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, namespaceItem(0));
				break;
			case BNF.namespaceItem:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] namespaceItem(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, stringVal(0));
append(bnf, "NO");
append(bnf, "DEFAULT");
				break;
			case BNF.stringVal:
				append(bnf, "AS");
				break;
			case BNF.NO:
				append(bnf, "DEFAULT");
				break;
			case BNF.DEFAULT_KEYWORD:
				append(bnf, stringVal(0));
				break;
			case BNF.AS:
				append(bnf, id(0));
				break;
			case BNF.stringVal:
				break;
			case BNF.id:
				break;
		}

		return array(bnf);
	}

	public String[] parseDataTypePrimary(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "STRING");
append(bnf, "VARCHAR");
append(bnf, "BOOLEAN");
append(bnf, "BYTE");
append(bnf, "TINYINT");
append(bnf, "SHORT");
append(bnf, "SMALLINT");
append(bnf, "CHAR");
append(bnf, "INTEGER");
append(bnf, "LONG");
append(bnf, "BIGINT");
append(bnf, "BIGINTEGER");
append(bnf, "FLOAT");
append(bnf, "REAL");
append(bnf, "DOUBLE");
append(bnf, "BIGDECIMAL");
append(bnf, "DECIMAL");
append(bnf, "DATE");
append(bnf, "TIME");
append(bnf, "TIMESTAMP");
append(bnf, "OBJECT");
append(bnf, "BLOB");
append(bnf, "CLOB");
append(bnf, "VARBINARY");
append(bnf, "XML");
				break;
			case BNF.STRING:
				append(bnf, "(");
				break;
			case BNF.VARCHAR:
				append(bnf, "(");
				break;
			case BNF.CHAR:
				append(bnf, "(");
				break;
			case BNF.BIGINTEGER:
				append(bnf, "(");
				break;
			case BNF.BIGDECIMAL:
				append(bnf, "(");
				break;
			case BNF.DECIMAL:
				append(bnf, "(");
				break;
			case BNF.OBJECT:
				append(bnf, "(");
				break;
			case BNF.BLOB:
				append(bnf, "(");
				break;
			case BNF.CLOB:
				append(bnf, "(");
				break;
			case BNF.VARBINARY:
				append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, intVal(0));
				break;
			case BNF.LPAREN:
				append(bnf, intVal(0));
				break;
			case BNF.LPAREN:
				append(bnf, intVal(0));
				break;
			case BNF.LPAREN:
				append(bnf, intVal(0));
				break;
			case BNF.LPAREN:
				append(bnf, intVal(0));
				break;
			case BNF.LPAREN:
				append(bnf, intVal(0));
				break;
			case BNF.LPAREN:
				append(bnf, intVal(0));
				break;
			case BNF.LPAREN:
				append(bnf, intVal(0));
				break;
			case BNF.LPAREN:
				append(bnf, intVal(0));
				break;
			case BNF.LPAREN:
				append(bnf, intVal(0));
				break;
			case BNF.intVal:
				append(bnf, ")");
				break;
			case BNF.intVal:
				append(bnf, ")");
				break;
			case BNF.intVal:
				append(bnf, ")");
				break;
			case BNF.intVal:
				append(bnf, ")");
				break;
			case BNF.intVal:
				append(bnf, ",");
				append(bnf, ")");
				break;
			case BNF.intVal:
				append(bnf, ",");
				append(bnf, ")");
				break;
			case BNF.intVal:
				append(bnf, ")");
				break;
			case BNF.intVal:
				append(bnf, ")");
				break;
			case BNF.intVal:
				append(bnf, ")");
				break;
			case BNF.intVal:
				append(bnf, ")");
				break;
			case BNF.RPAREN:
				break;
			case BNF.COMMA:
				append(bnf, intVal(0));
				break;
			case BNF.COMMA:
				append(bnf, intVal(0));
				break;
			case BNF.intVal:
				append(bnf, ")");
				break;
			case BNF.intVal:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}

	public String[] parseDataType(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, parseDataTypePrimary(0));
				break;
			case BNF.parseDataTypePrimary:
				append(bnf, "[");
				break;
			case BNF.LSBRACE:
				append(bnf, "]");
				break;
			case BNF.RSBRACE:
				break;
		}

		return array(bnf);
	}

	public String[] intervalType(int... indices) {
		List<String> bnf = newList();

		append(bnf, "SQL_TSI_FRAC_SECOND");
		append(bnf, "SQL_TSI_SECOND");
		append(bnf, "SQL_TSI_MINUTE");
		append(bnf, "SQL_TSI_HOUR");
		append(bnf, "SQL_TSI_DAY");
		append(bnf, "SQL_TSI_WEEK");
		append(bnf, "SQL_TSI_MONTH");
		append(bnf, "SQL_TSI_QUARTER");
		append(bnf, "SQL_TSI_YEAR");
		return array(bnf);
	}

	public String[] nonNumericLiteral(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, stringVal(0));
append(bnf, "(X | X ' (([ A-F, A-F ] | [ 0-9 ] ) ([ A-F, A-F ] | [ 0-9 ] ))+ ' )");
append(bnf, "FALSE");
append(bnf, "TRUE");
append(bnf, "UNKNOWN");
append(bnf, "NULL");
append(bnf, "{ (D | T | TS | B )");
				break;
			case BNF.stringVal:
				break;
			case BNF.ESCAPEDTYPE:
				append(bnf, stringVal(0));
				break;
		}

		return array(bnf);
	}

	public String[] unsignedNumericLiteral(int... indices) {
		List<String> bnf = newList();

		append(bnf, "([ 0-9 ])+");
		append(bnf, "[ 0-9 ] . ([ 0-9 ])+ [ E, E ] (+ | - )? ([ 0-9 ])+");
		append(bnf, "([ 0-9 ])* . ([ 0-9 ])+");
		return array(bnf);
	}

	public String[] columnList(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		switch (index) {
			case 0:
append(bnf, "(");
				break;
			case BNF.LPAREN:
				append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, ",");
				break;
			case BNF.COMMA:
				append(bnf, id(0));
				break;
			case BNF.id:
				append(bnf, ")");
				break;
		}

		return array(bnf);
	}


}