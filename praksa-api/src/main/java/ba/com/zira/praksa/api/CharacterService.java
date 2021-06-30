package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterSearchRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchResponse;

/**
 * * Methods used to manipulate Character data. <br>
 * List of APIs implemented in this class with links:
 * <ul>
 * <li>{@link #searchCharacters}</li>
 * </ul>
 * 
 * @author zira
 *
 */
public interface CharacterService {

    /**
     * Retrieve All {@link CharacterSearchResponse}s from database.
     *
     * @param request
     *            {@link EntityRequest} containing search data.
     * @return {@link PagedPayloadResponse} for {@link CharacterSearchResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PagedPayloadResponse<CharacterSearchResponse> searchCharacters(final EntityRequest<CharacterSearchRequest> request)
            throws ApiException;

    ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException;
}
