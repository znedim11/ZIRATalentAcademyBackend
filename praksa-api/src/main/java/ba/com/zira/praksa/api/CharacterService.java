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
import ba.com.zira.praksa.api.model.character.CharacterCreateRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchResponse;
import ba.com.zira.praksa.api.model.character.CharacterUpdateRequest;
import ba.com.zira.praksa.api.model.character.CompleteCharacterResponse;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.game.GameCharacterResponse;
import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.person.Person;

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
    PagedPayloadResponse<CharacterSearchResponse> searchCharacters(final EntityRequest<CharacterSearchRequest> request) throws ApiException;

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
    PayloadResponse<CompleteCharacterResponse> findById(SearchRequest<Long> request) throws ApiException;

    PayloadResponse<CompleteCharacterResponse> create(EntityRequest<CharacterCreateRequest> request) throws ApiException;

    PayloadResponse<CompleteCharacterResponse> update(EntityRequest<CharacterUpdateRequest> request) throws ApiException;

    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException;

    ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException;

    ListPayloadResponse<GameCharacterResponse> getGamesForCharacter(final EntityRequest<Long> request) throws ApiException;

    ListPayloadResponse<ConceptResponse> getConceptsByCharacter(final EntityRequest<Long> request) throws ApiException;

    ListPayloadResponse<Person> getPersonsByCharacter(final EntityRequest<Long> request) throws ApiException;

    ListPayloadResponse<ObjectResponse> getObjectsByCharacter(final EntityRequest<Long> request) throws ApiException;

    ListPayloadResponse<Location> getLocationsByCharacter(final EntityRequest<Long> request) throws ApiException;

    ListPayloadResponse<LoV> getLoVsNotConnectedTo(EntityRequest<LoV> request) throws ApiException;
}
