/**
 *
 */
package ba.com.zira.praksa.core.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.CharacterService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.core.validation.CharacterRequestValidation;
import ba.com.zira.praksa.dao.CharacterDAO;

/**
 * @author zira
 *
 */

@Service
public class CharacterServiceImpl implements CharacterService {

    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";

    CharacterRequestValidation characterRequestValidation;
    CharacterDAO characterDAO;

    public CharacterServiceImpl(CharacterRequestValidation characterRequestValidation, CharacterDAO characterDAO) {
        super();
        this.characterRequestValidation = characterRequestValidation;
        this.characterDAO = characterDAO;
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
