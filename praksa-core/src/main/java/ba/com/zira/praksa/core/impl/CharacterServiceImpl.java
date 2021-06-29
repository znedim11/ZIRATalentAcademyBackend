package ba.com.zira.praksa.core.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.CharacterService;
import ba.com.zira.praksa.api.model.character.CharacterSearchRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchResponse;
import ba.com.zira.praksa.dao.CharacterDAO;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CharacterServiceImpl implements CharacterService {

    RequestValidator requestValidator;
    CharacterDAO characterDAO;

    public CharacterServiceImpl(RequestValidator requestValidator, CharacterDAO characterDAO) {
        super();
        this.requestValidator = requestValidator;
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

}