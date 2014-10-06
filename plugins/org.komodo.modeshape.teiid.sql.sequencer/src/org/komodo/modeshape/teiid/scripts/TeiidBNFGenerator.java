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
package org.komodo.modeshape.teiid.scripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.komodo.modeshape.teiid.parser.TeiidSQLConstants;
import org.komodo.modeshape.teiid.parser.bnf.AbstractBNF;
import org.komodo.modeshape.teiid.parser.bnf.BNFConstants;
import org.komodo.modeshape.teiid.parser.bnf.clause.BracketClause;
import org.komodo.modeshape.teiid.parser.bnf.clause.Clause;
import org.komodo.modeshape.teiid.parser.bnf.clause.ClauseStack;
import org.komodo.modeshape.teiid.parser.bnf.clause.IClause;
import org.komodo.spi.constants.StringConstants;
import org.komodo.spi.runtime.version.ITeiidVersion;
import org.komodo.spi.runtime.version.TeiidVersion;
import org.komodo.utils.ArgCheck;

/**
 *
 */
@SuppressWarnings({"nls", "javadoc"})
public class TeiidBNFGenerator implements StringConstants {

    private static final String EXEC_HOME = DOT;

    private static final String SRC_DIR = EXEC_HOME + File.separator + SRC;

    private static final String JAVACC_DIR = SRC_DIR + File.separator + "javacc";
    
    private static final String BNF_DIR = SRC_DIR + File.separator +
                                                                    convertPackageToDirPath(BNFConstants.class.getPackage());

    private static final String BNF_FILENAME = "newBNF.java";

    private static final String TEIID_SQL_JJT = "TeiidSyntaxParser.jjt";

    private static final Pattern METHOD_DEC_PATTERN = Pattern.compile("[A-Za-z]+ ([a-z][a-zA-Z]+)\\([a-zA-Z ]*\\) :");

    private static final Pattern COMMENT_PATTERN = Pattern.compile("[\\s]*([\\/\\/]|[\\/\\*]|[\\*]|[\\*\\/])+.*");

    private static final Pattern TRY_PATTERN = Pattern.compile("[\\s]*try.*");

    private static final Pattern CATCH_PATTERN = Pattern.compile("[\\s]*catch.*");

    private static final Pattern BRACE_PATTERN = Pattern.compile("[\\s]*[\\{\\}][\\s]*");

    private static final String LICENSE = "" +
    "/*" + NEW_LINE +
    " * JBoss, Home of Professional Open Source." + NEW_LINE +
    " * See the COPYRIGHT.txt file distributed with this work for information" + NEW_LINE +
    " * regarding copyright ownership.  Some portions may be licensed" + NEW_LINE +
    " * to Red Hat, Inc. under one or more contributor license agreements." + NEW_LINE +
    " * " + NEW_LINE +
    " * This library is free software; you can redistribute it and/or" + NEW_LINE +
    " * modify it under the terms of the GNU Lesser General Public" + NEW_LINE +
    " * License as published by the Free Software Foundation; either" + NEW_LINE +
    " * version 2.1 of the License, or (at your option) any later version." + NEW_LINE +
    " * " + NEW_LINE +
    " * This library is distributed in the hope that it will be useful," + NEW_LINE +
    " * but WITHOUT ANY WARRANTY; without even the implied warranty of" + NEW_LINE +
    " * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU" + NEW_LINE +
    " * Lesser General Public License for more details." + NEW_LINE +
    " * " + NEW_LINE +
    " * You should have received a copy of the GNU Lesser General Public" + NEW_LINE +
    " * License along with this library; if not, write to the Free Software" + NEW_LINE +
    " * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA" + NEW_LINE +
    " * 02110-1301 USA." + NEW_LINE +
    " */" + NEW_LINE;

    private class TokenModel {

        private StringBuffer content = new StringBuffer();
        
        public void addContent(String line) {
            content.append(line);
        }

        public String getContent() {
            return content.toString();
        }
    }

    private class MethodModel {
        private final String name;

        private List<String> contents;

        private Map<String, String> appendPragmas;

        public MethodModel(String name) {
            this.name = name;
            contents = new ArrayList<String>();
        }

        private void createClauses(Map<String, String> tokenMap) throws Exception {
            appendPragmas = new LinkedHashMap<String, String>();
            // < COMMA >
            // stringVal()

            Pattern tokenPattern = Pattern.compile("<([A-Z_]+)>");
            Pattern functionPattern = Pattern.compile("([a-zA-Z]+)\\(.*\\)");
            Pattern ppPattern = Pattern.compile("pp[AS]+[a-z]+\\(bnf\\." + name + "\\((.*?)\\).*");

            StringTokenizer tokenizer = new StringTokenizer(getContent());

            String identifier = null;
            ClauseStack clauseStack = new ClauseStack();

            while(tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();

                Matcher tokenMatcher = tokenPattern.matcher(token);
                Matcher functionMatcher = functionPattern.matcher(token);
                Matcher ppMatcher = ppPattern.matcher(token);

                if (token.startsWith("LOOKAHEAD")) {
                    // Need to check if there are brackets with it
                    // If not then we need to ignore the next
                    
                    boolean hasBrackets = token.contains(OPEN_BRACKET);
                    int brackets = 0;
                    for (int i = 0; i < token.length(); ++i) {
                        char c = token.charAt(i);
                        if (c == '(')
                            brackets++;
                        else if (c == ')')
                            brackets--;
                    }

                    if (hasBrackets)
                    
                } else if (tokenMatcher.matches()) {
                    identifier = tokenMatcher.group(1);
                    ArgCheck.isNotNull(identifier);
                    identifier = identifier.trim();

                    // Ignore the <EOF> token
                    if ("EOF".equals(identifier))
                        continue;

                    String tokenValue = tokenMap.get(identifier);
                    Clause clause = new Clause(identifier);
                    clause.setValue(tokenValue);
                    clauseStack.push(clause);

//                  buf.append("append(bnf, \"" + tokenValue.toUpperCase() + "\");" + NEW_LINE);

                } else if (functionMatcher.matches()) {
                    identifier = functionMatcher.group(1);
                    ArgCheck.isNotNull(identifier);
                    identifier = identifier.trim();

                    if ("LOOKAHEAD".equals(identifier))
                        continue;

                    Clause clause = new Clause(identifier);
                    clause.setValue(identifier + "(0)");
                    clauseStack.push(clause);

//                  buf.append("append(bnf, " + identifier + "(0));" + NEW_LINE);

                } else if (ppMatcher.matches()) {
                    String ppFunction = ppMatcher.group(1);
                    IClause clause = clauseStack.peek();
                    if (clause instanceof Clause)
                        ((Clause)clause).setPPFunction(ppFunction);

                } else if (token.equals(OPEN_BRACKET)) {
                    clauseStack.push(new BracketClause());
                } else if (token.equals(CLOSE_BRACKET)) {
                    IClause clause = clauseStack.peek();
                    if (clause instanceof BracketClause) {
                        BracketClause bClause = (BracketClause)clause;
                        // The CLOSE_BRACKET may not refer to this clause
                        // but to an inner clause first
                        bClause.closeClause();
                    }
                }

            }

            Iterator<IClause> clauseIter = clauseStack.iterator();
            while(clauseIter.hasNext()) {
                System.out.println(clauseIter.next().toString());
            }
        }

        public void addContent(String line) throws Exception {
            contents.add(line);
        }

        public String getName() {
            return this.name;
        }

        public List<String> getContents() {
            return Collections.unmodifiableList(contents);
        }

        public String getContent() {
            StringBuffer buf = new StringBuffer();

            for (String line : getContents()) {
                line = line.replaceAll("[\\s]+", SPACE);

                // Replace all < KEY > with <KEY>
                line = line.replaceAll("< ([A-Z_]+) >", "<$1>");
                buf.append(line);
            }

            return buf.toString();
        }

        public Collection<String> getAppendPragmas(Map<String, String> tokenMap) throws Exception {
            if (appendPragmas == null) {
                createClauses(tokenMap);
            }

            return appendPragmas.values();
        }

        public boolean requiresSwitch() {
            return !requiresIfElse() && (
                                                        getContent().contains("ppSet(" + "bnf" + DOT + name)
                                                        || getContent().contains("ppAppend(" + "bnf" + DOT + name)
                                                        );
        }

        /**
         * Detects a ppSet or ppAppend with 2 or more parameters
         * ppAppend(bnf.alter(BNF.VIEW, BNF.id));
         * 
         * @return content has a pp method with 2+ parameters
         */
        public boolean requiresIfElse() {
            Pattern p = Pattern.compile("pp[AS]+[a-z]+\\(bnf\\." + name + "\\([A-Za-z\\.]+\\, [A-Za-z\\.]+.*");
            Matcher m = p.matcher(getContent());
            return m.find();
        }

    }

    private final BufferedReader ccReader;

    private final BufferedWriter bnfWriter;

    private static String convertPackageToDirPath(Package pkg) {
        return pkg.getName().replaceAll(DOUBLE_BACK_SLASH + DOT, File.separator);
    }

    /**
     * 
     */
    public TeiidBNFGenerator() throws Exception {

        File templateFile = new File(JAVACC_DIR, TEIID_SQL_JJT);
        ArgCheck.isTrue(templateFile.exists(), "JJT template cannot be found!");

        File bnfFile = new File(BNF_DIR, BNF_FILENAME);

        ccReader = new BufferedReader(new FileReader(templateFile));
        bnfWriter = new BufferedWriter(new FileWriter(bnfFile));
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        TeiidBNFGenerator generator = new TeiidBNFGenerator();
        generator.generate();
    }

    private void append(String line) throws Exception {
        bnfWriter.write(line);
    }

    private boolean isComment(String line) {
        Matcher m = COMMENT_PATTERN.matcher(line);
        return m.matches();
    }

    private boolean isStartComment(String line) {
        Matcher m = Pattern.compile("[\\s]*\\/\\*.*").matcher(line);
        return m.matches();
    }

    private boolean isEndComment(String line) {
        Matcher m = Pattern.compile("[\\s]*\\*\\/.*").matcher(line);
        return m.matches();
    }

    private boolean isTryOrCatch(String line) {
        Matcher mTry = TRY_PATTERN.matcher(line);
        Matcher mCatch = CATCH_PATTERN.matcher(line);
        return mTry.matches() || mCatch.matches();
    }

    private boolean isSingleBrace(String line) {
        Matcher mBrace = BRACE_PATTERN.matcher(line);
        return mBrace.matches();        
    }

    private boolean isDoubleBrace(String line) {
        return line.contains("{}");
    }

    private boolean isReturnStatement(String line) {
        return line.contains("return ");
    }

    private boolean isTokenDeclaration(String line) {
        return line.endsWith(COLON) && line.startsWith("TOKEN :");
    }

    private boolean isMethodDeclaration(String line) {
        Pattern p = Pattern.compile("[A-Za-z]+ [A-Za-z]+\\(.*\\) :");
        Matcher m = p.matcher(line);
        return m.matches();
    }

    private boolean isPPSetNullMethod(String line) {
        return line.contains("ppSet(null);");
    }

    private String extractMethodName(String line) {
        // void stringVal() :
        // Command command(ParseInfo info) :
        // void unsignedNumericLiteral() :
        
        if (! line.endsWith(COLON))
            return null;

        Matcher m = METHOD_DEC_PATTERN.matcher(line);
        if (! m.matches())
            return null;

        return m.group(1);
    }

    private void writeLicence() throws Exception {
        append(LICENSE);
    }

    private void writePackageDeclaration() throws Exception {
        Package p = BNFConstants.class.getPackage();
        append("package " + p.getName() + SEMI_COLON + NEW_LINE + NEW_LINE);
    }

    private void writeImports() throws Exception {
        String imp = "import";

        Class<?>[] klazzes = { List.class,
                                             TeiidSQLConstants.NonReserved.class,
                                             TeiidSQLConstants.Reserved.class,
                                             TeiidSQLConstants.Tokens.class,
                                             ITeiidVersion.class,
                                             TeiidVersion.Version.class
                                            };

        for (Class<?> klazz : klazzes) {
            append(imp + SPACE + klazz.getCanonicalName() + SEMI_COLON + NEW_LINE);
        }

        append(NEW_LINE);
    }

    private void writeClassDeclaration() throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append("/**" + NEW_LINE)
            .append(" *" + NEW_LINE)
            .append(" */" + NEW_LINE)
            .append("@SuppressWarnings({\"static-access\", \"nls\"})" + NEW_LINE)
            .append(PUBLIC + SPACE + CLASS + SPACE + "BNF" + SPACE + " extends ")
            .append(AbstractBNF.class.getSimpleName() + SPACE + OPEN_BRACE + NEW_LINE)
            .append(NEW_LINE)
            .append(TAB + "/**" + NEW_LINE)
            .append(TAB + " * @param version" + NEW_LINE)
            .append(TAB + " */" + NEW_LINE)
            .append(TAB + PUBLIC + SPACE + "BNF" + OPEN_BRACKET)
            .append(ITeiidVersion.class.getSimpleName() + SPACE + "version" + CLOSE_BRACKET)
            .append(SPACE + OPEN_BRACE + NEW_LINE)
            .append(TAB + TAB + "super(version)" + SEMI_COLON + NEW_LINE)
            .append(TAB + CLOSE_BRACE + NEW_LINE + NEW_LINE);

        append(buf.toString());
    }

    /*
     * Iterate over the found tokens in the map in order to substitute
     * any tokens used in other tokens for their values
     */
    private boolean removeSubstitutions(Map<String, String> tokenMap) {
        boolean subsMade = false;
        Pattern tokenPattern = Pattern.compile("<([A-Z_]+)>");

        Iterator<Entry<String, String>> iterator = tokenMap.entrySet().iterator();
        while(iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            Matcher m = tokenPattern.matcher(entry.getValue());
            if (!m.find())
                continue;

            StringBuffer buf = new StringBuffer();

            // Does contain one or more tokens
            do {
                String replacement = tokenMap.get(m.group(1));
                m.appendReplacement(buf, replacement);
            } while (m.find());

            m.appendTail(buf);

            String varValue = buf.toString();
            // Properly escape speech marks by adding backslashed then remove any extra
            // backslashes erroneously added
            varValue = varValue.replaceAll("\"", "\\\\\"");
            varValue = varValue.replaceAll("\\\\\\\\\"", "\\\\\"");

            tokenMap.put(entry.getKey(), varValue);
            subsMade = true;
        }

        return subsMade;
    }

    private Map<String, String> analyseTokens(List<TokenModel> tokens) throws Exception {
        Map<String, String> tokenMap = new HashMap<String, String>();

        for (TokenModel tokenModel : tokens) {
            String contents = tokenModel.getContent();

            // Replace all tabs and other whitespace with SPACES
            contents = contents.replaceAll("[\\s]+", SPACE);

            // Replace all < KEY > with <KEY>
            contents = contents.replaceAll("< ([A-Z_]+) >", "<$1>");

            // Replace all bracket + space with just a bracket
            contents = contents.replaceAll("\\( ", "\\(");

            Stack<Character> bStack = new Stack<Character>();
            StringBuffer vNameBuf = new StringBuffer();
            StringBuffer vValueBuf = new StringBuffer();
            StringBuffer currBuf = null;
            boolean inSpeechMarks = false;
            for (int i = 0; i < contents.length(); ++i) {
                char c = contents.charAt(i);

                if (c == '"') {
                    Character ce1 = null;
                    if (i > 0)
                        ce1 = contents.charAt(i - 1);

                    // Ignore escaped speech marks
                    if (ce1 == null || ! ce1.equals('\\'))
                        inSpeechMarks = !inSpeechMarks;
                }
                else if (c == '<' && !inSpeechMarks) {
                    bStack.push(c);

                    if (currBuf == null) {
                        currBuf = vNameBuf;
                        // Ensures no leading < in varName
                        continue;
                    }

                } else if (c == '>' && !inSpeechMarks)
                    bStack.pop();

                if (bStack.isEmpty() && currBuf == vValueBuf) {
                    currBuf = null;
                    String varName = vNameBuf.toString().trim();
                    if (varName.startsWith(HASH))
                        varName = varName.substring(1);

                    String varValue = vValueBuf.toString().trim();
                    // Properly escape speech marks by adding backslashed then remove any extra
                    // backslashes erroneously added
                    varValue = varValue.replaceAll("\"", "\\\\\"");
                    varValue = varValue.replaceAll("\\\\\\\\\"", "\\\\\"");
                    tokenMap.put(varName, varValue);

                    vNameBuf = new StringBuffer();
                    vValueBuf = new StringBuffer();
                    continue;
                }

                if (c == ':' && !inSpeechMarks) {
                    currBuf = vValueBuf;
                    continue;
                }

                if (currBuf != null)
                currBuf.append(c);
            }
        }

        boolean removeSubs = true;
        while(removeSubs) {
            removeSubs = removeSubstitutions(tokenMap);
        }

        return tokenMap;
    }

    private void analyseMethod(Map<String, String> tokenMap, MethodModel method) throws Exception {
        StringBuffer buf = new StringBuffer();

        // Append method declaration
        buf.append(TAB + PUBLIC + SPACE + String.class.getSimpleName())
            .append(OPEN_SQUARE_BRACKET + CLOSE_SQUARE_BRACKET + SPACE)
            .append(method.getName() + OPEN_BRACKET + "int... indices" + CLOSE_BRACKET + SPACE + OPEN_BRACE)
            .append(NEW_LINE);

        // List declaration
        buf.append(TAB + TAB + "List<String> bnf = newList();" + NEW_LINE + NEW_LINE);

        method.getAppendPragmas(tokenMap);

//        // Append Statements
//        if (!method.requiresSwitch() && !method.requiresIfElse()) {
//            for(String pragma : method.getAppendPragmas(tokenMap)) {
//                buf.append(TAB + TAB + pragma);
//            }
//        }
//        buf.append(NEW_LINE);

        // Return statement
        buf.append(TAB + TAB + "return array(bnf);" + NEW_LINE);
        
        // Append method closing brace
        buf.append(TAB + CLOSE_BRACE + NEW_LINE + NEW_LINE);

        append(buf.toString());
    }

    public void generate() throws Exception {
        try {

            String line = null;
            boolean startAnalysis = false;
            boolean inComment = false;
            List<MethodModel> methods = new ArrayList<MethodModel>();
            List<TokenModel> tokens = new ArrayList<TokenModel>();
            MethodModel currentMethod = null;
            TokenModel currentToken = null;

            while ((line = ccReader.readLine()) != null) {

                // Ignore everything about the parser's actual generated class
                if (line.startsWith("PARSER_END"))
                    startAnalysis = true;
                
                if (! startAnalysis)
                    continue;

                if (isStartComment(line))
                    inComment = true;

                if (isEndComment(line))
                    inComment = false;

                if (inComment)
                    continue;

                if (isTokenDeclaration(line)) {
                    currentToken = new TokenModel();
                    tokens.add(currentToken);

                    currentMethod = null;
                    continue;
                } else if (isMethodDeclaration(line)) {
                    String methodName = extractMethodName(line);
                    if (methodName == null)
                        currentMethod = null; // Not a proper method eg. TOKEN pragma
                    else {
                        currentMethod = new MethodModel(methodName);
                        methods.add(currentMethod);
                    }

                    currentToken = null;                    
                    continue;
                }

                if (currentMethod == null && currentToken == null)
                    continue;

                if (line.isEmpty())
                    continue;

                if (isComment(line))
                    // Ignore comments
                    continue;

                if (isTryOrCatch(line))
                    continue;

                if (isSingleBrace(line))
                    continue;

                if (isDoubleBrace(line))
                    continue;

                if (isReturnStatement(line))
                    continue;

                if (isPPSetNullMethod(line))
                    continue;

                if (currentMethod != null) {
                    currentMethod.addContent(line);
                    
                } else if (currentToken != null)
                    currentToken.addContent(line);
            }

            writeLicence();
            writePackageDeclaration();
            writeImports();
            writeClassDeclaration();

            Map<String, String> tokenMap = analyseTokens(tokens);
//            for (Entry<String, String> entry : tokenMap.entrySet()) {
//                System.out.println(entry.getKey() + " = " + entry.getValue());
//            }

            for (MethodModel method : methods) {
//                if (! method.getName().equals("textColumn"))
                if (! method.getName().equals("booleanPrimary"))
                    continue;

                System.out.println(method.getName());
                System.out.println("Requires Switch: " + method.requiresSwitch());
                System.out.println("Requires IfElse: " + method.requiresIfElse());

                System.out.println(TAB + method.getContent());

                analyseMethod(tokenMap, method);

                System.out.println("===");
            }

            append(NEW_LINE + CLOSE_BRACE);

        } finally {
            bnfWriter.close();
            ccReader.close();
        }
    }

}
