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
import ba.com.zira.praksa.api.model.character.CharacterResponse;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.api.model.person.PersonCreateRequest;
import ba.com.zira.praksa.api.model.person.PersonUpdateRequest;

/**
 * * Methods used to manipulate {@link Person} data. <br>
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

public interface PersonService {

    /**
     * Retrieve All {@link Person}s from database.
     *
     * @param request
     *            {@link SearchRequest} containing pagination and sorting
     *            information.
     * @return {@link PagedPayloadResponse} for {@link Person}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PagedPayloadResponse<Person> find(final SearchRequest<String> request) throws ApiException;

    /**
     * Retrieve {@link Person} by Id.
     *
     * @param request
     *            {@link SearchRequest} for Sample Id and additional pagination
     *            and sorting information.
     * @return {@link PayloadResponse} for {@link Person}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<Person> findById(SearchRequest<Long> request) throws ApiException;

    /**
     * Create {@link Person}. <br>
     * Method creates Sample if the request is valid.
     *
     * @param request
     *            {@link EntityRequest} for {@link Person}
     * @return {@link PayloadResponse} holding created {@link Person}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<Person> create(EntityRequest<PersonCreateRequest> request) throws ApiException;

    /**
     * Create {@link Person}. <br>
     * Method creates Sample if the request is valid.
     *
     * @param request
     *            {@link EntityRequest} for {@link Person}
     * @return {@link PayloadResponse} holding created {@link Person}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<Person> update(final EntityRequest<PersonUpdateRequest> request) throws ApiException;

    /**
     * Delete {@link Person} from the database. <br>
     * If {@link Person} with the given Id does not exist a validation exception
     * will be thrown.
     *
     * @param request
     *            {@link Person} for Person Id.
     * @return {@link PayloadResponse} confirming deletion.
     * @throws ApiException
     *             If there was a problem during API invocation then
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;

    ListPayloadResponse<GameResponse> getGamesForPerson(final EntityRequest<Long> request) throws ApiException;

    ListPayloadResponse<ConceptResponse> getConceptsForPerson(final EntityRequest<Long> request) throws ApiException;

    ListPayloadResponse<ObjectResponse> getObjectsForPerson(final EntityRequest<Long> request) throws ApiException;

    ListPayloadResponse<CharacterResponse> getCharactersForPerson(final EntityRequest<Long> request) throws ApiException;

    public ListPayloadResponse<LoV> getLoVs(ListRequest<Long> request) throws ApiException;

    ListPayloadResponse<LoV> getLoVsNotConnectedTo(EntityRequest<LoV> request) throws ApiException;
}
