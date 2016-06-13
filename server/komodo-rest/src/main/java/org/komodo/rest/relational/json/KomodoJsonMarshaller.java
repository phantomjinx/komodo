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

import java.util.HashMap;
import java.util.Map;
import org.komodo.rest.KRestEntity;
import org.komodo.rest.RestBasicEntity;
import org.komodo.rest.RestLink;
import org.komodo.rest.RestProperty;
import org.komodo.rest.json.LinkSerializer;
import org.komodo.rest.json.RestPropertySerializer;
import org.komodo.rest.relational.ImportExportStatus;
import org.komodo.rest.relational.KomodoSavedSearcher;
import org.komodo.rest.relational.KomodoSearcherAttributes;
import org.komodo.rest.relational.KomodoStatusObject;
import org.komodo.rest.relational.KomodoStorageAttributes;
import org.komodo.rest.relational.KomodoTeiidAttributes;
import org.komodo.rest.relational.RestStorageType;
import org.komodo.rest.relational.RestStorageTypeAttribute;
import org.komodo.rest.relational.RestTeiid;
import org.komodo.rest.relational.RestTeiidStatus;
import org.komodo.rest.relational.RestTeiidVdbStatus;
import org.komodo.rest.relational.RestTeiidVdbStatusVdb;
import org.komodo.rest.relational.RestVdb;
import org.komodo.rest.relational.RestVdbCondition;
import org.komodo.rest.relational.RestVdbDataRole;
import org.komodo.rest.relational.RestVdbImport;
import org.komodo.rest.relational.RestVdbMask;
import org.komodo.rest.relational.RestVdbModel;
import org.komodo.rest.relational.RestVdbModelSource;
import org.komodo.rest.relational.RestVdbPermission;
import org.komodo.rest.relational.RestVdbTranslator;
import org.komodo.rest.relational.dataservice.DataServiceSchema;
import org.komodo.rest.relational.dataservice.RestDataservice;
import org.komodo.rest.relational.datasource.DSSPropertyPairProperty;
import org.komodo.rest.relational.datasource.DataSourceSchema;
import org.komodo.rest.relational.datasource.DataSourceSchemaProperty;
import org.komodo.rest.relational.datasource.RestDataSource;
import org.komodo.rest.relational.json.datasource.DSSPropertyListSerializer;
import org.komodo.rest.relational.json.datasource.DSSPropertyPairPropertySerializer;
import org.komodo.rest.relational.json.datasource.DSSPropertySerializer;
import org.komodo.rest.relational.json.datasource.DSSSerializer;
import org.komodo.rest.relational.json.datasource.DataSourceSerializer;
import org.komodo.rest.schema.json.TeiidXsdReader;
import org.komodo.spi.repository.KomodoType;
import org.komodo.utils.ArgCheck;
import org.komodo.utils.KLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A JSON serializer and deserializer for {@link RestBasicEntity Komodo REST objects}.
 */
public final class KomodoJsonMarshaller {

    private static final KLog LOGGER = KLog.getLogger();

    /**
     * The shared JSON serialier/deserializer for {@link RestBasicEntity} objects.
     */
    public static final Gson BUILDER;

    public static final Gson PRETTY_BUILDER;

    static {
        final GsonBuilder temp = new GsonBuilder().registerTypeAdapter( RestLink.class, new LinkSerializer() )
                                                  .registerTypeAdapter(KomodoStatusObject.class, new StatusObjectSerializer())
                                                  .registerTypeAdapter(KomodoSavedSearcher.class, new SavedSearcherSerializer())
                                                  .registerTypeAdapter(KomodoSearcherAttributes.class, new SearcherAttributesSerializer())
                                                  .registerTypeAdapter(KomodoTeiidAttributes.class, new TeiidAttributesSerializer())
                                                  .registerTypeAdapter( RestProperty.class, new RestPropertySerializer() )
                                                  .registerTypeAdapter( RestVdb.class, new VdbSerializer() )
                                                  .registerTypeAdapter( RestVdbModel.class, new VdbModelSerializer() )
                                                  .registerTypeAdapter( RestVdbModelSource.class, new VdbModelSourceSerializer() )
                                                  .registerTypeAdapter( RestVdbDataRole.class, new VdbDataRoleSerializer() )
                                                  .registerTypeAdapter( RestVdbImport.class, new VdbImportSerializer() )
                                                  .registerTypeAdapter( RestVdbPermission.class, new VdbPermissionSerializer() )
                                                  .registerTypeAdapter( RestVdbCondition.class, new VdbConditionSerializer() )
                                                  .registerTypeAdapter( RestVdbMask.class, new VdbMaskSerializer() )
                                                  .registerTypeAdapter( RestVdbTranslator.class, new VdbTranslatorSerializer() )
                                                  .registerTypeAdapter( RestDataservice.class, new DataserviceSerializer() )
                                                  .registerTypeAdapter( RestDataSource.class, new DataSourceSerializer() )
                                                  .registerTypeAdapter( RestBasicEntity.class, new BasicEntitySerializer<RestBasicEntity>() )
                                                  .registerTypeAdapter( RestTeiid.class, new TeiidSerializer() )
                                                  .registerTypeAdapter( RestTeiidStatus.class, new TeiidStatusSerializer() )
                                                  .registerTypeAdapter( RestTeiidVdbStatus.class, new TeiidVdbStatusSerializer() )
                                                  .registerTypeAdapter( RestTeiidVdbStatusVdb.class, new TeiidVdbStatusVdbSerializer())
                                                  .registerTypeAdapter( DataSourceSchema.class, new DSSSerializer())
                                                  .registerTypeAdapter( DSSPropertyListSerializer.class, new DSSPropertyListSerializer())
                                                  .registerTypeAdapter( DSSPropertyPairProperty.class, new DSSPropertyPairPropertySerializer())
                                                  .registerTypeAdapter( DataSourceSchemaProperty.class, new DSSPropertySerializer())
                                                  .registerTypeAdapter( KomodoStorageAttributes.class, new StorageAttributesSerializer())
                                                  .registerTypeAdapter( ImportExportStatus.class, new ImportExportStatusSerializer())
                                                  .registerTypeAdapter( RestStorageType.class, new StorageTypeSerializer())
                                                  .registerTypeAdapter( RestStorageTypeAttribute.class, new StorageTypeAttributeSerializer());
        BUILDER = temp.create();
        PRETTY_BUILDER = temp.setPrettyPrinting().create();
    }

    /**
     * Cached teiid element schema. Unlikely to ever change
     * and this reduces the work required.
     */
    private static Map<Object, String> teiidElementSchemaCache = new HashMap<>();

    public static String teiidElementSchema(KomodoType kType) throws Exception {
        String schema;

        //
        // Check the cache first
        //
        Object key = kType;
        if (kType == null)
            key = Void.class;

        schema = teiidElementSchemaCache.get(key);
        if (schema != null)
            return schema;

        //
        // Not in cache so create
        //
        TeiidXsdReader reader = new TeiidXsdReader();

        if (kType == null) {
            schema = reader.read();

            JsonParser parser = new JsonParser();
            JsonElement shellElement = parser.parse(schema);
            JsonObject shell = shellElement.getAsJsonObject();
            JsonElement schema1Element = shell.get("schema-1");
            JsonObject schema1 = schema1Element.getAsJsonObject();

            DataSourceSchema dsSchema = new DataSourceSchema();
            schema1.add(DataSourceSchema.NAME_LABEL, BUILDER.toJsonTree(dsSchema));

            schema = PRETTY_BUILDER.toJson(shell);

        } else if (KomodoType.DATASOURCE.equals(kType)) {
            // Data sources do not have a mandated schema set out in the vdb-deployer.xsd
            // so need to construct our own. At this point should be pretty simple

            DataSourceSchema dsSchema = new DataSourceSchema();
            schema = marshall(dsSchema);

        } else if (KomodoType.DATASERVICE.equals(kType)) {
            DataServiceSchema dataserviceSchema = new DataServiceSchema();
            schema = marshall(dataserviceSchema);

        } else {
            schema = reader.schemaByKType(kType);
        }

        teiidElementSchemaCache.put(key, schema);
        return schema;
    }

    /**
     * Outputs a non-pretty printed JSON representation.
     *
     * @param entity
     *        the entity whose JSON representation is being requested (cannot be <code>null</code>)
     * @return the JSON representation (never empty)
     */
    public static String marshall( final KRestEntity entity ) {
        return marshall( entity, true );
    }

    /**
     * @param entity
     *        the entity whose JSON representation is being requested (cannot be <code>null</code>)
     * @param prettyPrint
     *        <code>true</code> if JSON output should be pretty printed
     * @return the JSON representation (never empty)
     */
    public static String marshall( final KRestEntity entity,
                                   final boolean prettyPrint ) {
        ArgCheck.isNotNull( entity, "entity" ); //$NON-NLS-1$

        String json = null;

        if ( prettyPrint ) {
            json = PRETTY_BUILDER.toJson( entity );
        } else {
            json = BUILDER.toJson( entity );
        }

        LOGGER.debug( "marshall: {0}", json ); //$NON-NLS-1$
        return json;
    }

    /**
     * @param entities
     *        the entities whose JSON representation is being requested (cannot be <code>null</code>)
     * @param prettyPrint
     *        <code>true</code> if JSON output should be pretty printed
     * @return the JSON representation (never empty)
     */
    public static String marshallArray( final KRestEntity[] entities,
                                   final boolean prettyPrint ) {
        ArgCheck.isNotNull( entities, "entities" ); //$NON-NLS-1$

        String json = null;

        if ( prettyPrint ) {
            json = PRETTY_BUILDER.toJson( entities );
        } else {
            json = BUILDER.toJson( entities );
        }

        LOGGER.debug( "marshall: {0}", json ); //$NON-NLS-1$
        return json;
    }

    /**
     * @param <T>
     *        the {@link RestBasicEntity} type of the output
     * @param json
     *        the JSON representation being converted to a {@link RestBasicEntity} (cannot be empty)
     * @param entityClass
     *        the type of {@link RestBasicEntity} the JSON will be converted to (cannot be <code>null</code>)
     * @return the {@link RestBasicEntity} (never <code>null</code>)
     */
    public static < T extends KRestEntity > T unmarshall( final String json,
                                                               final Class< T > entityClass ) {
        final T entity = BUILDER.fromJson( json, entityClass );
        LOGGER.debug( "unmarshall: class = {0}, entity = {1}", entityClass, entity ); //$NON-NLS-1$
        return entity;
    }

    /**
     * @param <T>
     *        the {@link RestBasicEntity} type of the output
     * @param json
     *        the JSON representation being converted to a {@link RestBasicEntity} (cannot be empty)
     * @param entityClass
     *        the type of {@link RestBasicEntity} the JSON will be converted to (cannot be <code>null</code>)
     * @return the {@link RestBasicEntity} (never <code>null</code>)
     */
    public static < T extends KRestEntity > T[] unmarshallArray( final String json,
                                                               final Class< T[] > entityClass ) {
        final T[] entity = BUILDER.fromJson( json, entityClass );
        LOGGER.debug( "unmarshall: class = {0}, entity = {1}", entityClass, entity ); //$NON-NLS-1$
        return entity;
    }

    /**
     * Don't allow construction outside of this class.
     */
    private KomodoJsonMarshaller() {
        // nothing to do
    }

}
