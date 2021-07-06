package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.model.media.MediaRetrivalRequest;
import ba.com.zira.praksa.api.model.mediastore.MediaStoreCreateRequest;
import ba.com.zira.praksa.api.model.mediastore.MediaStoreResponse;
import ba.com.zira.praksa.api.model.mediastore.MediaStoreUpdateRequest;

/**
 * * Methods used to manipulate {@link MediaStoreResponse} data. <br>
 * List of APIs implemented in this class with links:
 * <ul>
 * <li>{@link #find}</li>
 * <li>{@link #findByUuid}</li>
 * <li>{@link #create}</li>
 * <li>{@link #update}</li>
 * <li>{@link #delete}</li>
 * </ul>
 *
 * @author zira
 *
 */
public interface MediaStoreService {

    /**
     * Retrieve All {@link MediaStoreResponse}s from database.
     *
     * @param request
     *            {@link SearchRequest} containing pagination and sorting
     *            information.
     * @return {@link PagedPayloadResponse} for {@link MediaStoreResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PagedPayloadResponse<MediaStoreResponse> find(final SearchRequest<String> request) throws ApiException;

    /**
     * Retrieve {@link MediaStoreResponse} by Uuid.
     *
     * @param request
     *            {@link SearchRequest} for Sample Uuid and additional
     *            pagination and sorting information.
     * @return {@link PayloadResponse} for {@link MediaStoreResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<MediaStoreResponse> findByUuid(EntityRequest<String> request) throws ApiException;

    /**
     * Create {@link MediaStoreResponse}. <br>
     * Method creates Sample if the request is valid.
     *
     * @param request
     *            {@link EntityRequest} for {@link MediaStoreResponse}
     * @return {@link PayloadResponse} holding created
     *         {@link MediaStoreResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<MediaStoreResponse> create(EntityRequest<MediaStoreCreateRequest> request) throws ApiException;

    /**
     * Update existing {@link MediaStoreResponse}. <br>
     * Method validates if Sample exists and if the request is valid update
     * database.
     *
     * @param request
     *            {@link EntityRequest} for {@link MediaStoreResponse}
     * @return {@link PayloadResponse} holding created
     *         {@link MediaStoreResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<MediaStoreResponse> update(final EntityRequest<MediaStoreUpdateRequest> request) throws ApiException;

    /**
     * Delete {@link MediaStoreResponse} from the database. <br>
     * If {@link MediaStoreResponse} with the given Uuid does not exist a
     * validation exception will be thrown.
     *
     * @param request
     *            {@link MediaStoreResponse} for MediaStore Uuid.
     * @return {@link PayloadResponse} confirming deletion.
     * @throws ApiException
     *             If there was a problem during API invocation then
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<String> delete(EntityRequest<String> request) throws ApiException;

    ListPayloadResponse<String> getImageUrl(final EntityRequest<MediaRetrivalRequest> request) throws ApiException;

}
