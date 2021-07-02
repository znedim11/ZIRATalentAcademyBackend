package ba.com.zira.praksa.core.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.CharacterService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterSearchRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchResponse;
import ba.com.zira.praksa.core.validation.CharacterRequestValidation;
import ba.com.zira.praksa.dao.CharacterDAO;

/**
 * @author zira
 *
 */

@Service
public class CharacterServiceImpl implements CharacterService {

    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";

    private RequestValidator requestValidator;
    CharacterRequestValidation characterRequestValidation;
    CharacterDAO characterDAO;

    public CharacterServiceImpl(RequestValidator requestValidator, CharacterRequestValidation characterRequestValidation,
            CharacterDAO characterDAO) {
        super();
        this.requestValidator = requestValidator;
        this.characterRequestValidation = characterRequestValidation;
        this.characterDAO = characterDAO;
    }

    @Override
    public PagedPayloadResponse<CharacterSearchResponse> searchCharacters(final EntityRequest<CharacterSearchRequest> request)
            throws ApiException {
        requestValidator.validate(request);

        CharacterSearchRequest searchRequest = request.getEntity();

        List<CharacterSearchResponse> characterList = characterDAO.searchCharacters(searchRequest);

        return new PagedPayloadResponse<>(request, ResponseCode.OK, characterList.size(), 1, 1, characterList.size(), characterList);
    }

    public ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException {
        if (request.getList() != null) {
            for (Long item : request.getList()) {
                EntityRequest<Long> longRequest = new EntityRequest<>(item, request);
                characterRequestValidation.validateCharacterExists(longRequest, VALIDATE_ABSTRACT_REQUEST);
            }
        }

        List<LoV> loVs = characterDAO.getLoVs(request.getList());

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }
}
