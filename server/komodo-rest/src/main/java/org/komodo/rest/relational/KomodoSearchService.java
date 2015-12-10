/*
* JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.komodo.rest.relational;

import static org.komodo.rest.relational.RelationalMessages.Error.SEARCH_SERVICE_GET_SEARCH_ERROR;
import static org.komodo.rest.relational.RelationalMessages.Error.SEARCH_SERVICE_SAVE_SEARCH_ERROR;
import static org.komodo.rest.relational.RelationalMessages.Error.SEARCH_SERVICE_WKSP_SEARCHES_ERROR;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.komodo.core.KEngine;
import org.komodo.core.KomodoLexicon;
import org.komodo.relational.workspace.WorkspaceManager;
import org.komodo.repository.KomodoTypeRegistry;
import org.komodo.repository.KomodoTypeRegistry.TypeIdentifier;
import org.komodo.repository.search.ComparisonOperator;
import org.komodo.repository.search.LogicalOperator;
import org.komodo.repository.search.ObjectSearcher;
import org.komodo.rest.KomodoRestException;
import org.komodo.rest.KomodoRestV1Application.V1Constants;
import org.komodo.rest.KomodoService;
import org.komodo.rest.RestBasicEntity;
import org.komodo.rest.relational.json.KomodoJsonMarshaller;
import org.komodo.spi.KException;
import org.komodo.spi.constants.StringConstants;
import org.komodo.spi.repository.KomodoObject;
import org.komodo.spi.repository.KomodoType;
import org.komodo.spi.repository.Repository.UnitOfWork;
import org.komodo.spi.repository.Repository.UnitOfWork.State;
import org.modeshape.jcr.api.JcrConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * A Komodo REST service for obtaining VDB information from the workspace.
 */
@Path(V1Constants.WORKSPACE_SEGMENT + StringConstants.FORWARD_SLASH +
           V1Constants.SEARCH_SEGMENT)
@Api(tags = {V1Constants.SEARCH_SEGMENT})
public final class KomodoSearchService extends KomodoService {

    /**
     * @param engine
     *        the Komodo Engine (cannot be <code>null</code> and must be started)
     * @throws ServerErrorException
     *         if there is a problem obtaining the {@link WorkspaceManager workspace manager}
     */
    public KomodoSearchService( final KEngine engine ) throws ServerErrorException {
        super( engine );
    }

    /**
     * @param type
     * @return if type is ktype then return its modeshape equivalent
     */
    private String convertType(String type) {
        if (type == null)
            return JcrConstants.NT_UNSTRUCTURED;

        KomodoType kType = KomodoType.getKomodoType(type);
        if (kType == null || KomodoType.UNKNOWN.equals(kType))
            return type; // Not a komodo type

        TypeIdentifier identifier = KomodoTypeRegistry.getInstance().getIdentifier(kType);
        String lexiconType = identifier.getLexiconType();
        if (lexiconType != null)
            return lexiconType;

        return JcrConstants.NT_UNSTRUCTURED;
    }

    private ObjectSearcher createObjectSearcher(String type, String parent, String ancestor,
                                                                             String path, String contains, String name) {
        final String ALIAS = "nt";  //$NON-NLS-1$
        ObjectSearcher os = new ObjectSearcher(this.repo);

        os.addFromType(convertType(type), ALIAS);

        LogicalOperator operator = null;
        if (parent != null) {
            os.addWhereParentClause(null, ALIAS, parent, true);
            operator = LogicalOperator.AND;
        }

        if (ancestor != null) {
            os.addWhereParentClause(null, ALIAS, ancestor, false);
            operator = LogicalOperator.AND;
        }

        if (path != null) {
            os.addWherePathClause(operator, ALIAS, path);
            operator = LogicalOperator.AND;
        }

        if (contains != null) {
            os.addWhereContainsClause(operator, ALIAS, STAR, contains);
            operator = LogicalOperator.AND;
        }

        if (name != null) {
            ComparisonOperator compOperator = ComparisonOperator.EQUALS;
            if (name.contains(PERCENT))
                compOperator = ComparisonOperator.LIKE;

            os.addWhereCompareClause(operator, ALIAS, "mode:localName", compOperator, name); //$NON-NLS-1$
            operator = LogicalOperator.AND;
        }

        return os;
    }

    /**
     * @param headers
     *        the request headers (never <code>null</code>)
     * @param uriInfo
     *        the request URI information (never <code>null</code>)
     * @param seachName
     *        the request search name parameter
     * @param type
     *        the request type parameter
     * @param parent
     *        the request parent parameter
     * @param ancestor
     *        the request ancestor parameter
     * @param path
     *        the request path of specific object
     * @param contains
     *        the request contains parameter
     * @param objectName
     *        the request name parameter
     * @return a JSON document representing the results of a search in the Komodo workspace
     *                  (never <code>null</code>)
     * @throws KomodoRestException
     *         if there is a problem constructing the VDBs JSON document
     */
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes ( { MediaType.APPLICATION_JSON } )
    @ApiOperation(value = "Search the workspace using criteria",
                             response = RestBasicEntity[].class)
    @ApiResponses(value = {
        @ApiResponse(code = 406, message = "Only JSON is returned by this operation")
    })
    public Response searchWorkspace( final @Context HttpHeaders headers,
                             final @Context UriInfo uriInfo,
                             @ApiParam(value = "Execute a search already saved in the repository",
                             required = false)
                              @QueryParam(value = SEARCH_SAVED_NAME_PARAMETER) String seachName,
                             @ApiParam(value = "Type of object to search for",
                                                required = false)
                             @QueryParam(value = SEARCH_TYPE_PARAMETER) String type,
                             @ApiParam(value = "The data path of the parent object",
                                                required = false)
                             @QueryParam(value = SEARCH_PARENT_PARAMETER) String parent,
                             @ApiParam(value = "The data path of the ancestor object",
                                                required = false)
                             @QueryParam(value = SEARCH_ANCESTOR_PARAMETER) String ancestor,
                             @ApiParam(value = "The data path of a specific object",
                                                required = false)
                             @QueryParam(value = SEARCH_PATH_PARAMETER) String path,
                             @ApiParam(value = "Search term for object with a property that contains value",
                                                required = false)
                             @QueryParam(value = SEARCH_CONTAINS_PARAMETER) String contains,
                             @ApiParam(value = "The name of an object. Can use '%' as wildcards for broadening searches",
                                                required = false)
                             @QueryParam(value = SEARCH_OBJECT_NAME_PARAMETER) String objectName) throws KomodoRestException {

        List<MediaType> mediaTypes = headers.getAcceptableMediaTypes();
        if (! isAcceptable(mediaTypes, MediaType.APPLICATION_JSON_TYPE))
            return notAcceptableMediaTypesBuilder().build();

        if (seachName == null && type == null && path == null && parent == null &&
            ancestor == null && contains == null && objectName == null) {

            String errorMessage = RelationalMessages.getString(
                                                               RelationalMessages.Error.SEARCH_SERVICE_NO_PARAMETERS_ERROR);

            Object responseEntity = createErrorResponse(mediaTypes, errorMessage);
            return Response.status(Status.FORBIDDEN).entity(responseEntity).build();
        }

        if (parent != null && ancestor != null) {

            String errorMessage = RelationalMessages.getString(
                                                           RelationalMessages.Error.SEARCH_SERVICE_PARENT_ANCESTOR_EXCLUSIVE_ERROR);

            Object responseEntity = createErrorResponse(mediaTypes, errorMessage);
            return Response.status(Status.FORBIDDEN).entity(responseEntity).build();
        }

        UnitOfWork uow = null;

        try {
            uow = createTransaction("objectFromWorkspace", true); //$NON-NLS-1$

            ObjectSearcher os;
            if (seachName != null) {
                os = new ObjectSearcher(repo);
                os.read(uow, seachName);
            } else {
                os = createObjectSearcher(type, parent, ancestor, path, contains, objectName);
            }

            // Execute the search
            List<KomodoObject> searchObjects = os.searchObjects(uow);

            // Convert the results into rest objects for the response
            List<RestBasicEntity> entities = new ArrayList<>();
            for (KomodoObject kObject : searchObjects) {
                RestBasicEntity entity = entityFactory.create(kObject, uriInfo.getBaseUri(), uow);
                if (entity != null) // if kType in UNKNOWN then the entity is not created
                    entities.add(entity);
            }

            return commit( uow, mediaTypes, entities );

        } catch (final Exception e) {
            if ((uow != null) && (uow.getState() != State.ROLLED_BACK)) {
                uow.rollback();
            }

            if (e instanceof KomodoRestException) {
                throw (KomodoRestException)e;
            }

            String errorMsg = e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getSimpleName();
            throw new KomodoRestException(RelationalMessages.getString(SEARCH_SERVICE_GET_SEARCH_ERROR, errorMsg), e);
        }
    }

    /**
     * @param headers
     *        the request headers (never <code>null</code>)
     * @param uriInfo
     *        the request URI information (never <code>null</code>)
     * @return a JSON document representing the results of a search in the Komodo workspace
     *                  (never <code>null</code>)
     * @throws KomodoRestException
     *         if there is a problem constructing the VDBs JSON document
     */
    @GET
    @Path(V1Constants.SAVED_SEARCHES_SEGMENT)
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes ( { MediaType.APPLICATION_JSON } )
    @ApiOperation(value = "Fetch saved searches from the workspace",
                             response = RestBasicEntity[].class)
    @ApiResponses(value = {
        @ApiResponse(code = 406, message = "Only JSON is returned by this operation")
    })
    public Response findWorkspaceSearches( final @Context HttpHeaders headers,
                             final @Context UriInfo uriInfo) throws KomodoRestException {

        List<MediaType> mediaTypes = headers.getAcceptableMediaTypes();
        if (! isAcceptable(mediaTypes, MediaType.APPLICATION_JSON_TYPE))
            return notAcceptableMediaTypesBuilder().build();

        UnitOfWork uow = null;
        try {
            uow = createTransaction("searchesFromWorkspace", true); //$NON-NLS-1$

            String searchesGroupPath = repo.komodoSearches(uow).getAbsolutePath();
            KomodoObject searchesGroup = repo.getFromWorkspace(uow, searchesGroupPath);

            List<KomodoSavedSearcher> entities = new ArrayList<KomodoSavedSearcher>();
            if (searchesGroup != null) {
                KomodoObject[] searches = searchesGroup.getChildrenOfType(uow, KomodoLexicon.Search.NODE_TYPE);

                for (KomodoObject kObject : searches) {
                    String name = kObject.getName(uow);
                    try {
                        ObjectSearcher os = new ObjectSearcher(repo);
                        os.read(uow, name);

                        KomodoSavedSearcher kso = new KomodoSavedSearcher();
                        kso.setName(name);
                        kso.setQuery(os.toString());

                        entities.add(kso);
                    } catch (KException ex) {
                        // Ignore if this search in invalid
                    }
                }
            }

            return commit(uow, mediaTypes, entities);

        } catch (final Exception e) {
            if ((uow != null) && (uow.getState() != State.ROLLED_BACK)) {
                uow.rollback();
            }

            if (e instanceof KomodoRestException) {
                throw (KomodoRestException)e;
            }

            String errorMsg = e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getSimpleName();
            throw new KomodoRestException(RelationalMessages.getString(SEARCH_SERVICE_WKSP_SEARCHES_ERROR, errorMsg), e);
        }
    }

    /**
     * @param headers
     *        the request headers (never <code>null</code>)
     * @param uriInfo
     *        the request URI information (never <code>null</code>)
     * @param searchAttributes
     *        the Search Attributes JSON representation (cannot be <code>null</code>)
     * @return a JSON document representing the results of a search in the Komodo workspace
     *                  (never <code>null</code>)
     * @throws KomodoRestException
     *         if there is a problem constructing the VDBs JSON document
     */
    @POST
    @Path(V1Constants.SAVED_SEARCHES_SEGMENT)
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes ( { MediaType.APPLICATION_JSON } )
    @ApiOperation(value = "Save a search to the workspace")
    @ApiResponses(value = {
        @ApiResponse(code = 406, message = "Only JSON is returned by this operation")
    })
    public Response saveSearch( final @Context HttpHeaders headers,
                             final @Context UriInfo uriInfo,
                             final String searchAttributes) throws KomodoRestException {

        List<MediaType> mediaTypes = headers.getAcceptableMediaTypes();
        if (! isAcceptable(mediaTypes, MediaType.APPLICATION_JSON_TYPE))
            return notAcceptableMediaTypesBuilder().build();

        KomodoSearcherAttributes sa;
        ObjectSearcher os;
        try {
            sa = KomodoJsonMarshaller.unmarshall(searchAttributes, KomodoSearcherAttributes.class);
            os = createObjectSearcher(sa.getType(), sa.getParent(), sa.getAncestor(),
                                                      sa.getPath(), sa.getContains(), sa.getObjectName());
        } catch (Exception ex) {
            throw new KomodoRestException(ex);
        }

        UnitOfWork uow = null;
        try {
            uow = createTransaction("writeSearchToWorkspace", false); //$NON-NLS-1$
            os.write(uow, sa.getSearchName());

            return commit(uow, mediaTypes, sa);
        } catch (final Exception e) {
            if ((uow != null) && (uow.getState() != State.ROLLED_BACK)) {
                uow.rollback();
            }

            if (e instanceof KomodoRestException) {
                throw (KomodoRestException)e;
            }

            String errorMsg = e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getSimpleName();
            throw new KomodoRestException(RelationalMessages.getString(SEARCH_SERVICE_SAVE_SEARCH_ERROR, errorMsg), e);
        }
    }

}
