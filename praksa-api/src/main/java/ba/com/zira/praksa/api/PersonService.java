package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.api.model.person.PersonCreateRequest;
import ba.com.zira.praksa.api.model.person.PersonUpdateRequest;

public interface PersonService {

	public PagedPayloadResponse<Person> find(final SearchRequest<String> request) throws ApiException;

	PayloadResponse<Person> findById(SearchRequest<Long> reguest) throws ApiException;

	PayloadResponse<Person> create(EntityRequest<PersonCreateRequest> request) throws ApiException;

	PayloadResponse<Person> update(final EntityRequest<PersonUpdateRequest> request) throws ApiException;

	PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;
}
