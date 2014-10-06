/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.komodo.eclipse.sql.ui.editor.panel.configuration;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.komodo.modeshape.teiid.parser.TeiidSQLConstants;
import org.komodo.spi.constants.StringConstants;

/**
 * SqlAutoIndentStrategy
 *
 * @since 8.0
 */
public class SqlAutoIndentStrategy extends DefaultIndentLineAutoEditStrategy
    implements StringConstants, TeiidSQLConstants.Reserved {

    /**
     * A String array of these keywords
     * @since 4.0
     */
    private static final String[] KEYWORDS = new String[] { SELECT,      
                                                           FROM,        
                                                           WHERE,       
                                                           INSERT,      
                                                           VALUES,      
                                                           UPDATE,      
                                                           SET,         
                                                           DELETE,      
                                                           CREATE + SPACE + VIRTUAL + SPACE + PROCEDURE };    

    private String sLastKeywordFound = null;

    public SqlAutoIndentStrategy() {
    }

    /* (non-Javadoc)
     * Method declared on IAutoIndentStrategy
`     */
    @Override
    public void customizeDocumentCommand(IDocument d, DocumentCommand c) {
//        System.out.println( "[SqlAutoIndentStrategy.customizeDocumentCommand] c.text is: [" + c.text + "]" + " d.get() is: " + d.get() ); //$NON-NLS-1$

        /*
         * jh note (6/16/2004): The difficulty with this is that we have been supporting all text changes,
         *    including DELETE and BACKSPACE.  The jdt implementations of this (indent on newline or insert
         *    newline on curly brace) do not support delete and backspace.  So the cure is to stop attempting
         *    to support it.  If a user backspaces over "WHERE a > b", leaving "WHE", we do nothing.
         *    If they then type "RE" we will handle that, because we handle all newly typed characters.
         */

        // Solution:  do not respond to backspace or delete:
        // if DELETE or BACKSPACE case, take no action
        // (DELETE and BACKSPACE have a NON-ZERO length, and a text of EMPTY_STRING)
        if ( c.length > 0 && c.text.equals( EMPTY_STRING ) ) {
            return;
        }

        if (c.text != null && endsWithClauseKeyword(d, c)) {

            smartInsertAfterKeyword(d, c);
        }
    }


    /**
     * Returns whether or not the text ends with one of the given search strings.
     */
    private boolean endsWithClauseKeyword(IDocument d, DocumentCommand c) {
        String sDocTextUpcased = d.get();

//      System.out.println( "[SqlAutoIndentStrategy.endsWithClauseKeyword] sDocTextUpcased BEFORE trim: [" + sDocTextUpcased + "]" ); //$NON-NLS-1$
        int iLastCharIndex = sDocTextUpcased.length() - 1;

        if ( sDocTextUpcased.length() > 0
          &&  ( sDocTextUpcased.charAt( iLastCharIndex ) == NEW_LINE.charAt(0) )
           ) {
             sDocTextUpcased = sDocTextUpcased.trim();
        }
//      System.out.println( "[SqlAutoIndentStrategy.endsWithClauseKeyword] sDocTextUpcased AFTER trim: [" + sDocTextUpcased + "]" ); //$NON-NLS-1$

        // check to see if user typed a single whitespace (ie, no copying and pasting):
        boolean rv;
        if (c.text != null
         && isAllWhitespace(c.text)) {
            // yes, whitespace was typed (or pasted) in.
            //  see if previous text ended with a keyword
            rv = endsWithClauseKeyword(sDocTextUpcased);
        } else {
            // person is still typing a name; don't allow modifications
            rv =  false;
        } // endif

        return rv;
    }

    /**
     * @param text
     * @return
     */
    private boolean isAllWhitespace(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                // as soon as we find a non-whitespace, we know it is not all whitespace
                return false;
            } // endif
        } // endfor
        return true;
    }

    /**
     * @param sDocTextUpcased
     * @return
     */
    private boolean endsWithClauseKeyword(String sDocTextUpcased) {
        sDocTextUpcased = sDocTextUpcased.toUpperCase();

        for (int i= 0; i < KEYWORDS.length; i++) {
            if( sDocTextUpcased.endsWith( KEYWORDS[i] ) ) {
                sLastKeywordFound = KEYWORDS[i];

                // only return true if there is white space before this keyword
                StringBuffer sb = new StringBuffer( sDocTextUpcased );

                // calc index to char before the keyword:
                int iIndex = sb.length() - sLastKeywordFound.length() - 1;
//                System.out.println( "[SqlAutoIndentStrategy.endsWithClauseKeyword] iIndex: " + iIndex ); //$NON-NLS-1$

                if ( iIndex > -1 ) {
//                    System.out.println( "[SqlAutoIndentStrategy.endsWithClauseKeyword] iIndex > -1 is TRUE, will test for space "  ); //$NON-NLS-1$

                    char cPrecedingChar =  sb.charAt( iIndex );

                    if( cPrecedingChar == SPACE.charAt(0) || cPrecedingChar == NEW_LINE.charAt(0) || cPrecedingChar == TAB.charAt(0) ) {
//                        System.out.println( "[SqlAutoIndentStrategy.endsWithClauseKeyword] returning true A "  ); //$NON-NLS-1$
                        return true;
                    }
                } else {
//                    System.out.println( "[SqlAutoIndentStrategy.endsWithClauseKeyword] returning true B "  ); //$NON-NLS-1$
                    return true;
                }
            }
        }

//        System.out.println( "[SqlAutoIndentStrategy.endsWithClauseKeyword] About to return false C"  ); //$NON-NLS-1$
        return false;
    }

    /**
     * Set the indent of a bracket based on the command provided in the supplied document.
     * @param document - the document being parsed
     * @param command - the command being performed
     */
     protected void smartInsertAfterKeyword(IDocument document, DocumentCommand command) {
        if (command.offset == -1 || document.getLength() == 0) {
            sLastKeywordFound = null;
            return;
        }

        try {
//            String sDoc =
//                document.get();

            int p           = (command.offset == document.getLength() ? command.offset - 1 : command.offset);
            int line        = document.getLineOfOffset(p);
            int start       = document.getLineOffset(line);
            int whiteend    = findEndOfWhiteSpace(document, start, command.offset);

            String sKeyword         = getEndingKeyword( document, command );
            String sNewCommandText  = EMPTY_STRING;


            // 1. put the keyword on a new line, depending on preference
            //  (but only if there is not already a newline immidiately before it)
            int iBefore = p - ( sKeyword.length() - 1 );

            char chCharOneBeforeKeyword = ' ';
            char chCharTwoBeforeKeyword = ' ';
            if ( iBefore > 0 ) {
                chCharOneBeforeKeyword = document.getChar( iBefore );
                chCharTwoBeforeKeyword = document.getChar( iBefore -1 );
            }
            if  ( whiteend > 0
               && chCharOneBeforeKeyword != NEW_LINE.charAt(0)
               && chCharTwoBeforeKeyword != NEW_LINE.charAt(0)
               && startClausesOnNewLine() ) {
                sNewCommandText = NEW_LINE;
            }

            // 2. add in the keyword
            sNewCommandText += sKeyword
                            +  NEW_LINE;

            // 3. indent the content, depending on preference
            //    jh note: the indent makes no sense unless the newline is also requested,
            //             so I am linking them.
            if ( startClausesOnNewLine() && indentClauseContent() ) {
                sNewCommandText += TAB;
            }


            command.length  = sNewCommandText.length();

            /*  Strategy:
             *
             * command.offset should be: command.offset - (length of sKeyword, minus 1 ) =
             *                                        9                     6        1     4
             *    1234567890
             *       SELEC
             *       \nSELECT\nbbbb
             *
             * iOverage should be: sNewCommandText.length() - (length of sKeyword, minus 1)
             */
            /* PAF, 2004-12-03: The above "minus 1" clause no longer applies,
             *  since we are now waiting a character before reformatting.
             */

            command.offset  = command.offset - ( sKeyword.length() );
            int iOverage    = sNewCommandText.length() - ( sKeyword.length() );

            command.text = sNewCommandText;
            command.doit = true;

            // expand the document to fit the new content: increate doc length by iOverage spaces
            String sCurrentDocText = document.get();
            int iOrigLength = sCurrentDocText.length();
            StringBuffer replaceText= new StringBuffer( iOrigLength );
            replaceText.replace(0, iOrigLength, sCurrentDocText );

            // append just what you need, and use blanks
            for( int i = 0; i < iOverage; i++ ) {
                replaceText.append( ' ' );  // add a character blank
            }

            document.set( replaceText.toString() );

// jh: to test: force the update here so we can catch the exception...
//               Copy the 'execute' method from DocumentCommand to this class:
//          execute( document, command );

        } catch (BadLocationException excp) {
//            System.out.println("AutoIndent.error.bad_location_2"); //$NON-NLS-1$
        }

        sLastKeywordFound = null;
    }

    private String getEndingKeyword( IDocument d, DocumentCommand command ) {

        // use the keyword 'endsWithClauseKeyword' found, if it is available. Avoid duplication!
        if ( sLastKeywordFound != null ) {
            return sLastKeywordFound;
        }


        String sDocTextUpcased = d.get();

        if ( sDocTextUpcased.length() > 0 && sDocTextUpcased.charAt( sDocTextUpcased.length() - 1 ) == NEW_LINE.charAt(0) ) {
            sDocTextUpcased = sDocTextUpcased.trim();
        }

        sDocTextUpcased += command.text;
        sDocTextUpcased = sDocTextUpcased.toUpperCase();

        for (int i= 0; i < KEYWORDS.length; i++) {
            if( sDocTextUpcased.endsWith( KEYWORDS[i] ) ) {

                return KEYWORDS[i];
            }
        }
        return EMPTY_STRING;
    }


    private boolean startClausesOnNewLine() {
        return true;
    }

    private boolean indentClauseContent() {
        return true;
    }

}
