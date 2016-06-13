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
package org.komodo.rest.relational;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.komodo.rest.KRestEntity;
import org.komodo.spi.storage.StorageConnector.Attribute;

public class RestStorageType implements KRestEntity {

    public static final String NAME_LABEL = "name";

    public static final String ATTRIBUTES_LABEL = "attributes";

    private String name;

    private List<RestStorageTypeAttribute> attributes;

    /**
     * Constructor for use when deserializing
     */
    public RestStorageType() {
        super();
        this.attributes = Collections.emptyList();
    }

    public RestStorageType(String name, Collection<Attribute> attributes) {
        this.name = name;

        if (attributes == null || attributes.isEmpty())
            this.attributes = Collections.emptyList();
        else {
            this.attributes = new ArrayList<>();
            for (Attribute attribute : attributes) {
                this.attributes.add(new RestStorageTypeAttribute(attribute));
            }
        }
    }

    @Override
    public Object getXml() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean supports(MediaType mediaType) {
        return MediaType.APPLICATION_JSON_TYPE.equals(mediaType)
        || MediaType.APPLICATION_XML_TYPE.equals(mediaType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RestStorageTypeAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(RestStorageTypeAttribute[] attributes) {
        if (attributes == null || attributes.length == 0)
            this.attributes = Collections.emptyList();

        this.attributes = new ArrayList<>();
        for (RestStorageTypeAttribute attribute : attributes) {
            this.attributes.add(attribute);
        }
    }
}
