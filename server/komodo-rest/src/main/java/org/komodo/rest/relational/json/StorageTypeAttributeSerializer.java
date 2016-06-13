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
package org.komodo.rest.relational.json;

import static org.komodo.rest.Messages.Error.UNEXPECTED_JSON_TOKEN;
import java.io.IOException;
import org.komodo.rest.Messages;
import org.komodo.rest.relational.RestStorageTypeAttribute;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * A GSON serializer/deserializer for {@status RestStorageTypeAttribute}.
 */
public class StorageTypeAttributeSerializer extends TypeAdapter<RestStorageTypeAttribute> {

    @Override
    public RestStorageTypeAttribute read(JsonReader in) throws IOException {
        final RestStorageTypeAttribute storageTypeAttribute = new RestStorageTypeAttribute();
        in.beginObject();

        while ( in.hasNext() ) {
            final String name = in.nextName();

            switch ( name ) {
                case RestStorageTypeAttribute.NAME_LABEL:
                    storageTypeAttribute.setName(in.nextString());
                    break;
                case RestStorageTypeAttribute.DESCRIPTION_LABEL:
                    storageTypeAttribute.setDescription(in.nextString());
                    break;
                case RestStorageTypeAttribute.REQUIRED_LABEL:
                    storageTypeAttribute.setRequired(in.nextBoolean());
                    break;
                default:
                    throw new IOException( Messages.getString( UNEXPECTED_JSON_TOKEN, name ) );
            }
        }

        in.endObject();

        return storageTypeAttribute;
    }

    @Override
    public void write(JsonWriter out, RestStorageTypeAttribute value) throws IOException {
        out.beginObject();

        out.name(RestStorageTypeAttribute.NAME_LABEL);
        out.value(value.getName());

        out.name(RestStorageTypeAttribute.DESCRIPTION_LABEL);
        out.value(value.getDescription());

        out.name(RestStorageTypeAttribute.REQUIRED_LABEL);
        out.value(value.isRequired());

        out.endObject();
    }
}
