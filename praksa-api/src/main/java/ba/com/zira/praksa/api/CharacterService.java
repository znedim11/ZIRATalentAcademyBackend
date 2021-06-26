package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterResponse;

/**
 * * Methods used to manipulate {@link CharacterResponse} data.
 *
 * @author zira
 *
 */

public interface CharacterService {
    ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException;
}
