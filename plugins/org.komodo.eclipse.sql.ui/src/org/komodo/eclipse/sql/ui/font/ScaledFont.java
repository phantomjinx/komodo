/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.komodo.eclipse.sql.ui.font;


/**
 * ScaledFont
 *
 * @since 8.0
 */
public interface ScaledFont {
       
    void increase();
    
    void decrease();
    
    boolean canIncrease();
    
    boolean canDecrease();

    void addFontChangeListener(IFontChangeListener listener);

    void removeFontChangeListener(IFontChangeListener listener);

    void fireFontChanged();

}
