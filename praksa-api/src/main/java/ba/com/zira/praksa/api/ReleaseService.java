package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.api.model.release.ReleaseResponse;
import ba.com.zira.praksa.api.model.release.ReleaseResponseLight;
import ba.com.zira.praksa.api.model.release.ReleasesByTimetableRequest;
import ba.com.zira.praksa.api.model.release.ReleasesByTimetableResponse;

/**
 * * Methods used to manipulate {@link ReleaseResponse} data. <br>
 * List of APIs implemented in this class with links:
 * <ul>
 * <li>{@link #find}</li>
 * <li>{@link #findByUuid}</li>
 * <li>{@link #addRelease}</li>
 * <li>{@link #delete}</li>
 * </ul>
 *
 * @author zira
 *
 */

public interface ReleaseService {

    /**
     * Retrieve All {@link ReleaseResponse}s from database.
     *
     * @param request
     *            {@link SearchRequest} containing pagination and sorting
     *            information.
     * @return {@link PagedPayloadResponse} for {@link ReleaseResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PagedPayloadResponse<ReleaseResponse> find(final SearchRequest<String> request) throws ApiException;

    /**
     * Retrieve {@link ReleaseResponseLight} by Id.
     *
     * @param request
     *            {@link SearchRequest} for Sample Id and additional pagination
     *            and sorting information.
     * @return {@link PayloadResponse} for {@link ReleaseResponseLight}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PayloadResponse<ReleaseResponseLight> findByUuid(EntityRequest<String> request) throws ApiException;

    /**
     * Delete {@link ReleaseResponse} from the database. <br>
     * If {@link ReleaseResponse} with the given Id does not exist a validation
     * exception will be thrown.
     *
     * @param request
     *            {@link ReleaseResponse} for Person Id.
     * @return {@link PayloadResponse} confirming deletion.
     * @throws ApiException
     *             If there was a problem during API invocation then
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PayloadResponse<String> delete(EntityRequest<String> request) throws ApiException;

    /**
     * Create {@link ReleaseResponse}. <br>
     * Method creates Sample if the request is valid.
     *
     * @param request
     *            {@link EntityRequest} for {@link ReleaseResponse}
     * @return {@link PayloadResponse} holding created {@link ReleaseResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PayloadResponse<String> addRelease(final EntityRequest<ReleaseRequest> request) throws ApiException;

    public PayloadResponse<ReleasesByTimetableResponse> getReleasesByTimetable(final EntityRequest<ReleasesByTimetableRequest> request)
            throws ApiException;

}
