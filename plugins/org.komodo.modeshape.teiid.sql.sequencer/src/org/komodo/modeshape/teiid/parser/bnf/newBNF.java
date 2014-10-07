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

		return array(bnf);
	}

	public String[] nonReserved(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] id(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] command(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] designerCommand(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] createTrigger(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] alter(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] forEachRowTriggerAction(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] userCommand(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] errorStatement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] raiseStatement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] exceptionReference(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] exception(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] statement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] delimitedStatement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] compoundStatement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] branchingStatement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] returnStatement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] whileStatement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] loopStatement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] ifStatement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] declareStatement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] assignStatement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] assignStatementOperand(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] sqlStatement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] createProcedure(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] procedureBodyCommand(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] dynamicCommand(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] setClauseList(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] createElementsWithTypes(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] callableStatement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] storedProcedure(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] executeNamedParams(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] insert(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] expressionList(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] update(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] delete(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] queryExpression(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] withListElement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] queryExpressionBody(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] queryTerm(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] queryPrimary(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] query(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] into(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] select(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] selectSymbol(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] selectExpression(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] derivedColumn(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] allInGroupSymbol(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] orderedAgg(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] textAgg(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] aggregateSymbol(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] analyticAggregateSymbol(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] filterClause(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] from(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] tableReference(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] joinedTable(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] crossJoin(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] qualifiedJoin(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] tablePrimary(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] xmlSerialize(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] arrayTable(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] textTable(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] textColumn(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] xmlQuery(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] objectTable(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] objectColumn(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] xmlTable(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] xmlColumn(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] intVal(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] subqueryFromClause(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] unaryFromClause(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] where(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] criteria(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] compoundCritOr(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] compoundCritAnd(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] notCrit(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] booleanPrimary(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] operator(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] compareCrit(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] subquery(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] subqueryCompareCriteria(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] matchCrit(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] regexMatchCrit(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] betweenCrit(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] isNullCrit(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] setCrit(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] existsCriteria(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] groupBy(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] having(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] orderby(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] sortSpecification(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] sortKey(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] intParam(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] limit(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] fetchLimit(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] option(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] expression(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] commonValueExpression(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] plusExpression(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] plusMinus(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] timesExpression(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] timesOperator(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] valueExpressionPrimary(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] parameterReference(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] unescapedFunction(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] nestedExpression(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] unsignedValueExpressionPrimary(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] windowSpecification(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] caseExpression(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] searchedCaseExpression(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] function(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] xmlParse(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] queryString(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] xmlElement(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] xmlAttributes(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] jsonObject(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] derivedColumnList(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] xmlForest(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] xmlNamespaces(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] namespaceItem(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] parseDataTypePrimary(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] parseDataType(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] intervalType(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] nonNumericLiteral(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] unsignedNumericLiteral(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}

	public String[] columnList(int... indices) {
		List<String> bnf = newList();

		return array(bnf);
	}


}