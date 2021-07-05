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
import ba.com.zira.praksa.api.model.character.CharacterSearchRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchResponse;
import ba.com.zira.praksa.api.model.character.CompleteCharacterResponse;
import ba.com.zira.praksa.api.model.game.GameCharacterResponse;

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

    /**
     * Retrieve {@link CompleteCharacterResponse} by Id.
     *
     * @param request
     *            {@link SearchRequest} for Character Id and additional
     *            pagination and sorting information.
     * @return {@link PayloadResponse} for {@link CompleteCharacterResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PayloadResponse<CompleteCharacterResponse> findById(SearchRequest<Long> request) throws ApiException;

    public PagedPayloadResponse<GameCharacterResponse> getGamesForCharacter(final EntityRequest<Long> request) throws ApiException;

    ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException;
}
