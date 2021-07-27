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
import ba.com.zira.praksa.api.model.location.Location;

/**
 * * Methods used to manipulate {@link Location} data. <br>
 * List of APIs implemented in this class with links:
 * <ul>
 * <li>{@link #find}</li>
 * </ul>
 *
 * @author zira
 *
 */
public interface LocationService {

    /**
     * Create {@link Location}. <br>
     * Method creates Sample if the request is valid.
     *
     * @param request
     *            {@link EntityRequest} for {@link Location}
     * @return {@link PayloadResponse} holding created {@link Location}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<Location> create(EntityRequest<Location> request) throws ApiException;

    /**
     * Delete {@link Location} from the database. <br>
     * If {@link Location} with the given Id does not exist a validation
     * exception will be thrown.
     *
     * @param request
     *            {@link Location} for Location Id.
     * @return {@link PayloadResponse} confirming deletion.
     * @throws ApiException
     *             If there was a problem during API invocation then
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;

    /**
     * Retrieve All {@link Location}s from database.
     *
     * @param request
     *            {@link SearchRequest} containing pagination and sorting
     *            information.
     * @return {@link PagedPayloadResponse} for {@link Location}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PagedPayloadResponse<Location> find(final SearchRequest<String> request) throws ApiException;

    /**
     * Retrieve {@link Location} by Id.
     *
     * @param request
     *            {@link SearchRequest} for Sample Id and additional pagination
     *            and sorting information.
     * @return {@link PayloadResponse} for {@link Location}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<Location> findById(SearchRequest<Long> request) throws ApiException;

    /**
     * Update existing {@link Location}. <br>
     * Method validates if Sample exists and if the request is valid update
     * database.
     *
     * @param request
     *            {@link EntityRequest} for {@link Location}
     * @return {@link PayloadResponse} holding created {@link Location}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<Location> update(final EntityRequest<Location> request) throws ApiException;

    ListPayloadResponse<LoV> getLoVs(ListRequest<Long> request) throws ApiException;

    ListPayloadResponse<LoV> getLoVsNotConnectedTo(EntityRequest<LoV> request) throws ApiException;
}
