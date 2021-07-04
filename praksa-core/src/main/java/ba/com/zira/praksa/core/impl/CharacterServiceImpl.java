package ba.com.zira.praksa.core.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.CharacterService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterSearchRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchResponse;
import ba.com.zira.praksa.api.model.character.CompleteCharacterResponse;
import ba.com.zira.praksa.api.model.game.GameCharacterResponse;
import ba.com.zira.praksa.core.validation.CharacterRequestValidation;
import ba.com.zira.praksa.dao.CharacterDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.mapper.CharacterMapper;

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
    GameDAO gameDAO;
    CharacterMapper characterMapper;

    public CharacterServiceImpl(RequestValidator requestValidator, CharacterRequestValidation characterRequestValidation,
            CharacterDAO characterDAO, GameDAO gameDAO, CharacterMapper characterMapper) {
        super();
        this.requestValidator = requestValidator;
        this.characterRequestValidation = characterRequestValidation;
        this.characterDAO = characterDAO;
        this.gameDAO = gameDAO;
        this.characterMapper = characterMapper;
    }

    @Override
    public PagedPayloadResponse<CharacterSearchResponse> searchCharacters(final EntityRequest<CharacterSearchRequest> request)
            throws ApiException {
        requestValidator.validate(request);

        CharacterSearchRequest searchRequest = request.getEntity();

        List<CharacterSearchResponse> characterList = characterDAO.searchCharacters(searchRequest);

        return new PagedPayloadResponse<>(request, ResponseCode.OK, characterList.size(), 1, 1, characterList.size(), characterList);
    }

    @Override
    public PayloadResponse<CompleteCharacterResponse> findById(SearchRequest<Long> request) throws ApiException {
        characterRequestValidation.validateCharacterExists(new EntityRequest<>(request.getEntity(), request), VALIDATE_ABSTRACT_REQUEST);

        CompleteCharacterResponse characterResponse = characterMapper
                .characterEntityToCompleteCharacter(characterDAO.findByPK(request.getEntity()));

        return new PayloadResponse<>(request, ResponseCode.OK, characterResponse);
    }

    @Override
    public PagedPayloadResponse<GameCharacterResponse> getGamesForCharacter(final EntityRequest<Long> request) throws ApiException {
        characterRequestValidation.validateCharacterExists(new EntityRequest<>(request.getEntity(), request), VALIDATE_ABSTRACT_REQUEST);

        List<GameCharacterResponse> gamesList = gameDAO.getGamesForCharacter(request.getEntity());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, gamesList.size(), 1, 1, gamesList.size(), gamesList);
    }

    @Override
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
