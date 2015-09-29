/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.komodo.relational.commands.permission;

import java.util.ResourceBundle;
import org.komodo.relational.vdb.Permission;
import org.komodo.spi.constants.StringConstants;

/**
 * Localized messages for {@link Permission}-related shell commands.
 */
public class PermissionCommandMessages implements StringConstants {

    private static final String BUNDLE_NAME = ( PermissionCommandMessages.class.getPackage().getName() + DOT + PermissionCommandMessages.class.getSimpleName().toLowerCase() );

    /**
     * The resource bundle for localized messages.
     */
    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

    @SuppressWarnings( "javadoc" )
    public enum General {
        INVALID_DIRECTION_PROPERTY_VALUE,
        INVALID_NULLABLE_PROPERTY_VALUE,
        MISSING_CONDITION_NAME,
        MISSING_MASK_NAME;

        @Override
        public String toString() {
            return getEnumName(this) + DOT + name();
        }
    }

    @SuppressWarnings( "javadoc" )
    public enum AddConditionCommand {
        ADD_CONDITION_ERROR,
        CONDITION_ADDED;

        @Override
        public String toString() {
            return getEnumName(this) + DOT + name();
        }
    }

    @SuppressWarnings( "javadoc" )
    public enum DeleteConditionCommand {
        CONDITION_DELETED,
        DELETE_CONDITION_ERROR;

        @Override
        public String toString() {
            return getEnumName(this) + DOT + name();
        }
    }

    @SuppressWarnings( "javadoc" )
    public enum AddMaskCommand {
        ADD_MASK_ERROR,
        MASK_ADDED;

        @Override
        public String toString() {
            return getEnumName(this) + DOT + name();
        }
    }

    @SuppressWarnings( "javadoc" )
    public enum DeleteMaskCommand {
        DELETE_MASK_ERROR,
        MASK_DELETED;

        @Override
        public String toString() {
            return getEnumName(this) + DOT + name();
        }
    }

    private static String getEnumName(Enum<?> enumValue) {
        String className = enumValue.getClass().getName();
        String[] components = className.split("\\$"); //$NON-NLS-1$
        return components[components.length - 1];
    }

}
