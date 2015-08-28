/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.komodo.rest.json.serialize;

import static org.komodo.rest.Messages.Error.INCOMPLETE_JSON;
import static org.komodo.rest.Messages.Error.UNEXPECTED_JSON_TOKEN;
import java.io.IOException;
import org.komodo.rest.Messages;
import org.komodo.rest.json.JsonConstants;
import org.komodo.rest.json.RestVdbDescriptor;
import org.komodo.utils.StringUtils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * A GSON serializer/deserializer for {@link RestVdbDescriptor}s.
 */
public final class VdbDescriptorSerializer extends KomodoRestEntitySerializer< RestVdbDescriptor > {

    private boolean isComplete( final RestVdbDescriptor descriptor ) {
        return !StringUtils.isBlank( descriptor.getName() );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.rest.json.serialize.KomodoRestEntitySerializer#read(com.google.gson.stream.JsonReader)
     */
    @Override
    public RestVdbDescriptor read( final JsonReader in ) throws IOException {
        final RestVdbDescriptor vdbDescriptor = new RestVdbDescriptor();
        boolean foundName = false;

        beginRead( in );

        while ( in.hasNext() ) {
            final String name = in.nextName();

            switch ( name ) {
                case JsonConstants.DESCRIPTION:
                    vdbDescriptor.setDescription( in.nextString() );
                    break;
                case JsonConstants.ID:
                    vdbDescriptor.setName( in.nextString() );
                    foundName = true;
                    break;
                case JsonConstants.LINKS:
                    readLinks( in, vdbDescriptor );
                    break;
                case JsonConstants.PROPERTIES:
                    readProperties( in, vdbDescriptor );
                    break;
                default:
                    throw new IOException( Messages.getString( UNEXPECTED_JSON_TOKEN, name ) );
            }
        }

        if ( !foundName ) {
            throw new IOException( Messages.getString( INCOMPLETE_JSON, RestVdbDescriptor.class.getSimpleName() ) );
        }

        endRead( in );
        return vdbDescriptor;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.rest.json.serialize.KomodoRestEntitySerializer#write(com.google.gson.stream.JsonWriter,
     *      org.komodo.rest.json.KomodoRestEntity)
     */
    @Override
    public void write( final JsonWriter out,
                       final RestVdbDescriptor value ) throws IOException {
        if ( !isComplete( value ) ) {
            throw new IOException( Messages.getString( INCOMPLETE_JSON, RestVdbDescriptor.class.getSimpleName() ) );
        }

        beginWrite( out );

        // id
        out.name( JsonConstants.ID );
        out.value( value.getName() );

        // description
        if ( !StringUtils.isBlank( value.getDescription() ) ) {
            out.name( JsonConstants.DESCRIPTION );
            out.value( value.getDescription() );
        }

        writeProperties( out, value );
        writeLinks( out, value );
        endWrite( out );
    }

}
