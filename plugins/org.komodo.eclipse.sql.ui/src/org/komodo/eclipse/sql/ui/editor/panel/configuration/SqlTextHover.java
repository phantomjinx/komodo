/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.komodo.eclipse.sql.ui.editor.panel.configuration;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Point;

/**
 * Example implementation for an <code>ITextHover</code> which hovers over Sql text.
 *
 * @since 8.0
 */
public class SqlTextHover implements ITextHover {

    /* (non-Javadoc)
     * Method declared on ITextHover
     */
    @Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
        if (hoverRegion != null) {
            try {
                if (hoverRegion.getLength() > -1)
                    return textViewer.getDocument().get(hoverRegion.getOffset(), hoverRegion.getLength());
            } catch (BadLocationException x) {
            }
        }
        return "emptySelection"; //$NON-NLS-1$
    }
    
    /* (non-Javadoc)
     * Method declared on ITextHover
     */
    @Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
        Point selection= textViewer.getSelectedRange();
        if (selection.x <= offset && offset < selection.x + selection.y)
            return new Region(selection.x, selection.y);
        return new Region(offset, 0);
    }
}
