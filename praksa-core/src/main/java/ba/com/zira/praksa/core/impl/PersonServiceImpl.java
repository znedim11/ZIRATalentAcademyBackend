package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.PersonService;
import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.api.model.person.PersonCreateRequest;
import ba.com.zira.praksa.api.model.person.PersonUpdateRequest;
import ba.com.zira.praksa.core.validation.PersonRequestValidation;
import ba.com.zira.praksa.dao.PersonDAO;
import ba.com.zira.praksa.dao.model.PersonEntity;
import ba.com.zira.praksa.mapper.PersonMapper;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

	private RequestValidator requestValidator;
	private PersonRequestValidation personRequestValidation;
	private PersonDAO personDAO;
	private PersonMapper personMapper;
	private static final String VALIDATION_RULE = "validateAbstractRequest";

	@Override
	public PagedPayloadResponse<Person> find(final SearchRequest<String> request) throws ApiException {
		requestValidator.validate(request);

		PagedData<PersonEntity> personModelEntities = personDAO.findAll(request.getFilter());
		final List<PersonEntity> personEntityList = personModelEntities.getRecords();

		final List<Person> personList = personMapper.entityListToDtoList(personEntityList);

		PagedData<Person> data = new PagedData<>();
		data.setNumberOfPages(personModelEntities.getNumberOfPages());
		data.setRecords(personList);
		data.setNumberOfRecords(personModelEntities.getNumberOfRecords());
		data.setPage(personModelEntities.getPage());
		data.setRecordsPerPage(personModelEntities.getRecordsPerPage());
		data.setHasMoreRecords(personModelEntities.hasMoreRecords());

		return new PagedPayloadResponse<>(request, ResponseCode.OK, data);
	}

	@Override
	public PayloadResponse<Person> findById(final SearchRequest<Long> request) throws ApiException {
		EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
		personRequestValidation.validatePersonRequest(entityRequest, VALIDATION_RULE);

		final PersonEntity personEntity = personDAO.findByPK(request.getEntity());

		return new PayloadResponse<>(request, ResponseCode.OK, personMapper.entityToDto(personEntity));
	}

	@Override
	@Transactional(rollbackFor = ApiException.class)
	public PayloadResponse<Person> create(EntityRequest<PersonCreateRequest> request) throws ApiException {
		personRequestValidation.validatePersonLastName(request, VALIDATION_RULE);

		PersonEntity personEntity = personMapper.personCreateToPersonEntity(request.getEntity());
		personEntity.setCreated(LocalDateTime.now());
		personEntity.setCreatedBy(request.getUserId());

		personDAO.persist(personEntity);
		return new PayloadResponse<>(request, ResponseCode.OK, personMapper.entityToDto(personEntity));
	}

	@Override
	@Transactional(rollbackFor = ApiException.class)
	public PayloadResponse<Person> update(final EntityRequest<PersonUpdateRequest> request) throws ApiException {
		EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity().getId(), request);
		personRequestValidation.validatePersonRequest(entityRequest, VALIDATION_RULE);

		final PersonUpdateRequest requestEntity = request.getEntity();
		final PersonEntity personEntity = personMapper.personUpdateToPersonEntity(requestEntity);
		personEntity.setModified(LocalDateTime.now());
		personEntity.setModifiedBy(request.getUserId());

		personDAO.merge(personEntity);

		return new PayloadResponse<>(request, ResponseCode.OK, personMapper.entityToDto(personEntity));
	}

	@Override
	@Transactional(rollbackFor = ApiException.class)
	public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
		EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
		personRequestValidation.validatePersonRequest(entityRequest, VALIDATION_RULE);

		personDAO.removeByPK(request.getEntity());
		return new PayloadResponse<>(request, ResponseCode.OK, "Person deleted!");
	}

}