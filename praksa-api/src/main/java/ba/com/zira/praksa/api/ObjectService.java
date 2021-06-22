package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.model.object.ObjectRequest;
import ba.com.zira.praksa.api.model.object.ObjectResponse;

/**
 * * Methods used to manipulate {@link ObjectRequest} data. <br>
 * List of APIs implemented in this class with links:
 * <ul>
 * <li>{@link #find}</li>
 * </ul>
 *
 * @author zira
 *
 */
public interface ObjectService {

    /**
     * Retrieve All {@link ObjectRequest}s from database.
     *
     * @param request
     *            {@link SearchRequest} containing pagination and sorting
     *            information.
     * @return {@link PagedPayloadResponse} for {@link ObjectRequest}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PagedPayloadResponse<ObjectResponse> find(final SearchRequest<String> request) throws ApiException;

    /**
     * Retrieve {@link ObjectRequest} by Id.
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
     * Create {@link ObjectRequest}. <br>
     * Method creates Sample if the request is valid.
     *
     * @param request
     *            {@link EntityRequest} for {@link ObjectResponse}
     * @return {@link PayloadResponse} holding created {@link ObjectRequest}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<ObjectResponse> create(EntityRequest<ObjectRequest> request) throws ApiException;

    /**
     * Update existing {@link ObjectRequest}. <br>
     * Method validates if Sample exists and if the request is valid update
     * database.
     *
     * @param request
     *            {@link EntityRequest} for {@link ObjectRequest}
     * @return {@link PayloadResponse} holding created {@link ObjectResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<ObjectRequest> update(final EntityRequest<ObjectRequest> request) throws ApiException;

    /**
     * Delete {@link ObjectRequest} from the database. <br>
     * If {@link ObjectRequest} with the given Id does not exist a validation
     * exception will be thrown.
     *
     * @param request
     *            {@link ObjectRequest} for Object Id.
     * @return {@link PayloadResponse} confirming deletion.
     * @throws ApiException
     *             If there was a problem during API invocation then
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;
}
