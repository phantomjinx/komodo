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
package org.komodo.rest.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.komodo.core.KEngine;
import org.komodo.importer.ImportMessages;
import org.komodo.osgi.PluginService;
import org.komodo.rest.KomodoRestException;
import org.komodo.rest.KomodoRestV1Application.V1Constants;
import org.komodo.rest.KomodoService;
import org.komodo.rest.relational.ImportExportStatus;
import org.komodo.rest.relational.KomodoStorageAttributes;
import org.komodo.rest.relational.RelationalMessages;
import org.komodo.rest.relational.RestStorageType;
import org.komodo.rest.relational.json.KomodoJsonMarshaller;
import org.komodo.spi.repository.Exportable;
import org.komodo.spi.repository.KomodoObject;
import org.komodo.spi.repository.Repository.UnitOfWork;
import org.komodo.spi.repository.Repository.UnitOfWork.State;
import org.komodo.spi.storage.StorageConnector;
import org.komodo.spi.storage.StorageConnector.Attribute;
import org.komodo.spi.storage.StorageReference;
import org.komodo.spi.storage.StorageService;
import org.komodo.utils.FileUtils;
import org.komodo.utils.KLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * A Komodo REST service for import / export artifacts into / out of the workspace
 */
@Path( V1Constants.IMPORT_EXPORT_SEGMENT )
@Api( tags = {V1Constants.IMPORT_EXPORT_SEGMENT} )
public class KomodoImportExportService extends KomodoService {

    public KomodoImportExportService(KEngine engine) throws ServerErrorException {
        super(engine);
    }

    private String encrypt(byte[] content) {
        if (content == null)
            return null;

        return Base64.getEncoder().encodeToString(content);
    }

    private byte[] decrypt(String content) {
        if (content == null)
            return null;

        return Base64.getDecoder().decode(content);
    }

    private Response checkStorageAttributes(KomodoStorageAttributes sta,
                                                                                List<MediaType> mediaTypes) throws Exception {
        if (sta == null ||
            (sta.getStorageType() == null && sta.getArtifactPath() == null && sta.getParameters() == null)) {
            String errorMessage = RelationalMessages.getString(
                                                           RelationalMessages.Error.IMPORT_EXPORT_SERVICE_NO_PARAMETERS_ERROR);

            Object responseEntity = createErrorResponseEntity(mediaTypes, errorMessage);
            return Response.status(Status.FORBIDDEN).entity(responseEntity).build();
        }

        Set<String> supportedTypes = PluginService.getInstance().getSupportedStorageTypes();
        if (! supportedTypes.contains(sta.getStorageType())) {
            String errorMessage = RelationalMessages.getString(
                                                               RelationalMessages.Error.IMPORT_EXPORT_SERVICE_UNSUPPORTED_TYPE_ERROR);

            Object responseEntity = createErrorResponseEntity(mediaTypes, errorMessage);
            return Response.status(Status.FORBIDDEN).entity(responseEntity).build();
        }

        return Response.ok().build();
    }

    /**
     * Exports an artifact from the workspace.
     *
     * @param headers
     *        the request headers (never <code>null</code>)
     * @param uriInfo
     *        the request URI information (never <code>null</code>)
     * @param export attributes
     *        the export attributes JSON representation (cannot be <code>null</code>)
     * @return a JSON document including Base64 content of the file 
     *                  (never <code>null</code>)
     * @throws KomodoRestException
     *         if there is a problem with the export
     */
    @POST
    @Path(V1Constants.EXPORT)
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes ( { MediaType.APPLICATION_JSON } )
    @ApiOperation(value = "Exports an artifact using parameters provided in the request body",
                             notes = "Syntax of the json request body is of the form " +
                                          "{ storageType='file|git|...', parameters { prop1='z', prop2='z' } }",
                             response = ImportExportStatus.class)
    @ApiResponses(value = {
        @ApiResponse(code = 406, message = "Only JSON is returned by this operation"),
        @ApiResponse(code = 403, message = "An error has occurred.")
    })
    public Response exportArtifact( final @Context HttpHeaders headers,
                             final @Context UriInfo uriInfo,
                             final String storageAttributes) throws KomodoRestException {
        List<MediaType> mediaTypes = headers.getAcceptableMediaTypes();
        if (! isAcceptable(mediaTypes, MediaType.APPLICATION_JSON_TYPE))
            return notAcceptableMediaTypesBuilder().build();

        KomodoStorageAttributes sta;
        try {
            sta = KomodoJsonMarshaller.unmarshall(storageAttributes, KomodoStorageAttributes.class);
            Response response = checkStorageAttributes(sta, mediaTypes);
            if (response.getStatus() != Status.OK.getStatusCode())
                return response;

        } catch (Exception ex) {
            String errorMessage = RelationalMessages.getString(
                                                               RelationalMessages.Error.IMPORT_EXPORT_SERVICE_REQUEST_PARSING_ERROR, ex.getMessage());

            Object responseEntity = createErrorResponseEntity(mediaTypes, errorMessage);
            return Response.status(Status.FORBIDDEN).entity(responseEntity).build();
        }

        ImportExportStatus status = new ImportExportStatus();
        UnitOfWork uow = null;
        try {
            uow = createTransaction("exportFromWorkspace", true); //$NON-NLS-1$

            String artifactPath = sta.getArtifactPath();
            KomodoObject kObject = repo.getFromWorkspace(uow, artifactPath);
            if (kObject == null) {
                String errorMessage = RelationalMessages.getString(
                                                                   RelationalMessages.Error.IMPORT_EXPORT_SERVICE_NO_ARTIFACT_ERROR, artifactPath);
                Object responseEntity = createErrorResponseEntity(mediaTypes, errorMessage);
                return Response.status(Status.FORBIDDEN).entity(responseEntity).build();
            }

            Exportable artifact = wsMgr.resolve(uow, kObject, Exportable.class);
            if (artifact == null) {
                String errorMessage = RelationalMessages.getString(
                                                                   RelationalMessages.Error.IMPORT_EXPORT_SERVICE_ARTIFACT_NOT_EXPORTABLE_ERROR, artifactPath);
                Object responseEntity = createErrorResponseEntity(mediaTypes, errorMessage);
                return Response.status(Status.FORBIDDEN).entity(responseEntity).build();
            }

            Properties parameters = sta.convertParameters();
            if (! parameters.contains(StorageConnector.FILE_PATH_PROPERTY)) {
                parameters.setProperty(StorageConnector.FILE_PATH_PROPERTY, artifact.getName(uow));
            }

            status.setName(artifact.getName(uow));
            status.setType(artifact.getDocumentType().toString());
            String downloadable = wsMgr.exportArtifact(uow, artifact, sta.getStorageType(), parameters);

            //
            // 2 Return possibilities:
            // a) Artifact exported to storage but not returned in response
            // b) Artifact available at server file location so return as content
            //
            status.setDownloadable(downloadable != null);

            setContent(status, downloadable);

            status.setSuccess(true);

            return commit( uow, mediaTypes, status );

        } catch (final Exception e) {
            if ((uow != null) && (uow.getState() != State.ROLLED_BACK)) {
                uow.rollback();
            }

            return createErrorResponse(mediaTypes, e,
                                       RelationalMessages.Error.IMPORT_EXPORT_SERVICE_EXPORT_ERROR,
                                       sta.getArtifactPath(), sta.getStorageType());
        }
    }

    public void setContent(ImportExportStatus status, String downloadable) throws Exception {
        if (downloadable != null) {
            FileInputStream stream = null;
            try {
                File downloadableFile = new File(downloadable);
                stream = new FileInputStream(downloadableFile);

                status.setDownloadableLength1(downloadableFile.length());
                status.setDownloadableLength2(stream.available());

                byte content[] = new byte[(int)downloadableFile.length()];
                stream.read(content);
                String encContent = encrypt(content);
                status.setContent(encContent);

                KLog.getLogger().info("Encrypted content of " +
                                    downloadableFile.getAbsolutePath() +
                                    ": " + encContent +
                                    " SIZE: " + downloadableFile.length());

            } finally {
                stream.close();
            }
        }
    }

    /**
     * Imports an artifact into the workspace.
     *
     * @param headers
     *        the request headers (never <code>null</code>)
     * @param uriInfo
     *        the request URI information (never <code>null</code>)
     * @param import attributes
     *        the import attributes JSON representation (cannot be <code>null</code>)
     * @return a status object indicating success / failure of the import
     * @throws KomodoRestException
     *         if there is a problem with the import
     */
    @POST
    @Path(V1Constants.IMPORT)
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes ( { MediaType.APPLICATION_JSON } )
    @ApiOperation(value = "Imports an artifact using parameters provided in the request body",
                             notes = "Syntax of the json request body is of the form " +
                                          "{ storageType='file|git|...', documentType='xml|ddl'" +
                                          "parameters { file-path-property='file.txt', prop2='z', content=\"base64-string\" } }." +
                                          "If content is populated then the artifact has been transmitted from the client and will " +
                                          "be converted to a temporary file on the server.",
                             response = ImportExportStatus.class)
    @ApiResponses(value = {
        @ApiResponse(code = 406, message = "Only JSON is returned by this operation"),
        @ApiResponse(code = 403, message = "An error has occurred.")
    })
    public Response importArtifact( final @Context HttpHeaders headers,
                             final @Context UriInfo uriInfo,
                             final String storageAttributes) throws KomodoRestException {
        List<MediaType> mediaTypes = headers.getAcceptableMediaTypes();
        if (! isAcceptable(mediaTypes, MediaType.APPLICATION_JSON_TYPE))
            return notAcceptableMediaTypesBuilder().build();

        KomodoStorageAttributes sta;
        try {
            sta = KomodoJsonMarshaller.unmarshall(storageAttributes, KomodoStorageAttributes.class);
            Response response = checkStorageAttributes(sta, mediaTypes);
            if (response.getStatus() != Status.OK.getStatusCode())
                return response;

        } catch (Exception ex) {
            String errorMessage = RelationalMessages.getString(
                                                               RelationalMessages.Error.IMPORT_EXPORT_SERVICE_REQUEST_PARSING_ERROR, ex.getMessage());

            Object responseEntity = createErrorResponseEntity(mediaTypes, errorMessage);
            return Response.status(Status.FORBIDDEN).entity(responseEntity).build();
        }

        File cttFile = null;
        ImportExportStatus status = new ImportExportStatus();
        UnitOfWork uow = null;
        try {
            if (sta.getContent() != null) {
                //
                // Content has been provided so need to outline its location
                // for the storage connector to utilise
                //
                byte[] content = decrypt(sta.getContent());
                String tempDir = FileUtils.tempDirectory();
                String fileName = content.hashCode() + DOT + sta.getDocumentType();
                cttFile = new File(tempDir, fileName);

                FileUtils.write(content, cttFile);

                // Ensure the new location of the file is conveyed to the storage plugin
                sta.setParameter(StorageConnector.FILES_HOME_PATH_PROPERTY, tempDir);
                sta.setParameter(StorageConnector.FILE_PATH_PROPERTY, fileName);
            }

            Properties parameters = sta.convertParameters();
            if (! parameters.containsKey(StorageConnector.FILE_PATH_PROPERTY)) {
                String errorMessage = RelationalMessages.getString(
                                                                   RelationalMessages.Error.IMPORT_EXPORT_SERVICE_NO_FILE_PATH_ERROR);
                Object responseEntity = createErrorResponseEntity(mediaTypes, errorMessage);
                return Response.status(Status.FORBIDDEN).entity(responseEntity).build();
            }

            uow = createTransaction("importToWorkspace", false); //$NON-NLS-1$

            KomodoObject workspace = repo.komodoWorkspace(uow);
            StorageReference storageRef = new StorageReference(sta.getStorageType(),
                                                                                   parameters,
                                                                                   sta.getDocumentType());

            ImportMessages messages = wsMgr.importArtifact(uow, workspace, storageRef);
            if (messages.hasError())
                throw new Exception(messages.errorMessagesToString());

            status.setSuccess(true);
            status.setName(storageRef.getRelativeRef());
            status.setType(sta.getDocumentType().toString());

            return commit( uow, mediaTypes, status );

        } catch (final Exception e) {
            if ((uow != null) && (uow.getState() != State.ROLLED_BACK)) {
                uow.rollback();
            }

            return createErrorResponse(mediaTypes, e,
                                       RelationalMessages.Error.IMPORT_EXPORT_SERVICE_IMPORT_ERROR,
                                       sta.getStorageType());
        } finally {
            //
            // Clean up the temporary file if applicable
            //
            if (cttFile != null)
                cttFile.delete();
        }
    }

    /**
     * Gets the types of storage available for import/export
     *
     * @param headers
     *        the request headers (never <code>null</code>)
     * @param uriInfo
     *        the request URI information (never <code>null</code>)
     * @return the collection of the storage types
     * @throws KomodoRestException
     *         if there is a problem with the operation
     */
    @GET
    @Path(V1Constants.STORAGE_TYPES)
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes ( { MediaType.APPLICATION_JSON } )
    @ApiOperation(value = "Returns the collection of available storage types used for import/export",
                             response = RestStorageType[].class)
    @ApiResponses(value = {
        @ApiResponse(code = 406, message = "Only JSON is returned by this operation"),
        @ApiResponse(code = 403, message = "An error has occurred.")
    })
    public Response storageTypes( final @Context HttpHeaders headers,
                             final @Context UriInfo uriInfo) throws KomodoRestException {
        List<MediaType> mediaTypes = headers.getAcceptableMediaTypes();
        if (! isAcceptable(mediaTypes, MediaType.APPLICATION_JSON_TYPE))
            return notAcceptableMediaTypesBuilder().build();

        try {
            
            PluginService pluginService = PluginService.getInstance();
            Set<String> storageTypes = pluginService.getSupportedStorageTypes();
            if (storageTypes == null || storageTypes.isEmpty()) {
                return Response.noContent().build();
            }

            List<RestStorageType> restStorageTypes = new ArrayList<>(storageTypes.size());
            for (String storageType : storageTypes) {
                StorageService storageService = pluginService.getStorageService(storageType);
                Set<Attribute> attributes = storageService.getAttributes();

                RestStorageType type = new RestStorageType(storageType, attributes);
                restStorageTypes.add(type);
            }

            UnitOfWork uow = createTransaction("getStorageTypes", true); //$NON-NLS-1$
            return commit(uow, mediaTypes, restStorageTypes);

        } catch (Exception e) {
            return createErrorResponse(mediaTypes, e,
                                       RelationalMessages.Error.IMPORT_EXPORT_SERVICE_STORAGE_TYPES_ERROR);
        }
    }
}
