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

	public String[] option(int... indices) {
		List<String> bnf = newList();

		int index = concat(indices);
		if (index == 0) {
			append(bnf, "OPTION");
		} else if (index == BNF.OPTION) {
			append(bnf, "MAKEDEP");
			append(bnf, "NOCACHE");
		} else if (index == BNF.MAKEDEP) {
			append(bnf, id(0));
		} else if (index == concat(BNF.NOCACHE,BNF.COMMA,BNF.id)) {
		} else if (index == concat(BNF.MAKEDEP,BNF.id)) {
			append(bnf, ",");
			append(bnf, "MAKENOTDEP");
		} else if (index == concat(BNF.MAKEDEP,BNF.COMMA)) {
			append(bnf, id(0));
		} else if (index == concat(BNF.MAKENOTDEP,BNF.COMMA,BNF.id)) {
		} else if (index == concat(BNF.MAKEDEP,BNF.COMMA,BNF.id)) {
			append(bnf, ",");
		}

		return array(bnf);
	}


}