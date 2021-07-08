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
import ba.com.zira.praksa.api.model.formula.FormulaCreateRequest;
import ba.com.zira.praksa.api.model.formula.FormulaResponse;
import ba.com.zira.praksa.api.model.formula.FormulaUpdateRequest;

/**
 * * Methods used to manipulate {@link FormulaResponse} data. <br>
 * List of APIs implemented in this class with links:
 * <ul>
 * <li>{@link #findById}</li>
 * </ul>
 *
 * @author zira
 *
 */

public interface FormulaService {
    /**
     * Retrieve All {@link FormulaResponse}s from database.
     *
     * @param request
     *            {@link SearchRequest} containing pagination and sorting
     *            information.
     * @return {@link PagedPayloadResponse} for {@link FormulaResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PagedPayloadResponse<FormulaResponse> find(final SearchRequest<String> request) throws ApiException;

    /**
     * Retrieve {@link FormulaResponse} by Id.
     *
     * @param request
     *            {@link SearchRequest} for ConceptResponse Id and additional
     *            pagination and sorting information.
     * @return {@link PayloadResponse} for {@link FormulaResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<FormulaResponse> findById(SearchRequest<Long> request) throws ApiException;

    /**
     * Create {@link FormulaResponse}. <br>
     * Method creates Sample if the request is valid.
     *
     * @param request
     *            {@link EntityRequest} for {@link FormulaResponse}
     * @return {@link PayloadResponse} holding created {@link FormulaResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<FormulaResponse> create(EntityRequest<FormulaCreateRequest> request) throws ApiException;

    /**
     * Update existing {@link FormulaResponse}. <br>
     * Method validates if Sample exists and if the request is valid update
     * database.
     *
     * @param request
     *            {@link EntityRequest} for {@link FormulaResponse}
     * @return {@link PayloadResponse} holding created {@link FormulaResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<FormulaResponse> update(final EntityRequest<FormulaUpdateRequest> request) throws ApiException;

    PayloadResponse<Long> getNumberOfReviewsGamesByFormula(final EntityRequest<Long> request) throws ApiException;

    ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException;

    ListPayloadResponse<String> getGradesByFormula(final EntityRequest<Long> request) throws ApiException;
}
