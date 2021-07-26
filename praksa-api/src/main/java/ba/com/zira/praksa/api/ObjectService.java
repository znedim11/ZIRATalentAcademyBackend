package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.object.ObjectCreateRequest;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.object.ObjectUpdateRequest;

/**
 * * Methods used to manipulate Object data. <br>
 * List of APIs implemented in this class with links:
 * <ul>
 * <li>{@link #find}</li>
 * <li>{@link #findById}</li>
 * <li>{@link #create}</li>
 * <li>{@link #update}</li>
 * <li>{@link #delete}</li>
 * </ul>
 *
 * @author zira
 *
 */
public interface ObjectService {

    /**
     * Retrieve All {@link ObjectResponse}s from database.
     *
     * @param request
     *            {@link SearchRequest} containing pagination and sorting
     *            information.
     * @return {@link PagedPayloadResponse} for {@link ObjectResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PagedPayloadResponse<ObjectResponse> find(final SearchRequest<String> request) throws ApiException;

    /**
     * Retrieve {@link ObjectResponse} by Id.
     *
     * @param request
     *            {@link SearchRequest} for Sample Id and additional pagination
     *            and sorting information.
     * @return {@link PayloadResponse} for {@link ObjectResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<ObjectResponse> findById(SearchRequest<Long> request) throws ApiException;

    /**
     * Create Object. <br>
     * Method creates ObjectEntity if the request is valid.
     *
     * @param request
     *            {@link EntityRequest} for {@link ObjectCreateRequest}
     * @return {@link PayloadResponse} holding created {@link ObjectResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<ObjectResponse> create(EntityRequest<ObjectCreateRequest> request) throws ApiException;

    /**
     * Update existing Object. <br>
     * Method validates if Sample exists and if the request is valid update
     * database.
     *
     * @param request
     *            {@link EntityRequest} for {@link ObjectUpdateRequest}
     * @return {@link PayloadResponse} holding created {@link ObjectResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<ObjectResponse> update(final EntityRequest<ObjectUpdateRequest> request) throws ApiException;

    /**
     * Delete Object from the database. <br>
     * If Object with the given Id does not exist a validation exception will be
     * thrown.
     *
     * @param request
     *            {@link Long} for Object Id.
     * @return {@link PayloadResponse} confirming deletion.
     * @throws ApiException
     *             If there was a problem during API invocation then
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;

    public ListPayloadResponse<LoV> getLoVs(ListRequest<Long> request) throws ApiException;

    ListPayloadResponse<LoV> getLoVsNotConnectedTo(EntityRequest<LoV> request) throws ApiException;
}
