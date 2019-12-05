package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.Request;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.model.SampleModel;
import ba.com.zira.praksa.api.request.SampleRequest;
import ba.com.zira.praksa.api.response.SampleResponse;

/**
 * * Methods used to manipulate {@link SampleModel} data. <br>
 * List of APIs implemented in this class with links:
 * <ul>
 * <li>{@link #find}</li>
 * </ul>
 * 
 * @author zira
 *
 */
public interface SampleService {

    /**
     * Retrieve All {@link SampleResponse}s from database.
     * 
     * @param request
     *            {@link Request} containing pagination and sorting information.
     * @return {@link PagedPayloadResponse} for {@link SampleResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PagedPayloadResponse<SampleResponse> find(final Request request) throws ApiException;
    
    
    /**
     * Retrieve {@link SampleResponse} by Id.
     * 
     * @param request
     *            {@link SearchRequest} for Sample Id and additional pagination
     *            and sorting information.
     * @return {@link PayloadResponse} for {@link SampleResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<SampleResponse> findById(SearchRequest<String> request) throws ApiException;

    /**
     * Create {@link SampleResponse}. <br>
     * Method creates Sample if the request is valid.
     * 
     * @param request
     *            {@link EntityRequest} for {@link SampleResponse}
     * @return {@link PayloadResponse} holding created {@link SampleResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<SampleResponse> createSample(EntityRequest<SampleRequest> request) throws ApiException;
    
    /**
     * Update existing {@link SampleResponse}. <br>
     * Method validates if Sample exists and if the request is valid update
     * database.
     * 
     * @param request
     *            {@link EntityRequest} for {@link SampleResponse}
     * @return {@link PayloadResponse} holding created {@link SampleResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<SampleResponse> update(final EntityRequest<SampleRequest> request) throws ApiException;
    
    /**
     * Delete {@link SampleResponse} from the database. <br>
     * If {@link SampleResponse} with the given Id does not exist a validation
     * exception will be thrown.
     * 
     * @param request
     *            {@link SampleRequest} for Sample Id.
     * @throws ApiException
     *             If there was a problem during API invocation then
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    void delete(EntityRequest<String> request) throws ApiException;
}
