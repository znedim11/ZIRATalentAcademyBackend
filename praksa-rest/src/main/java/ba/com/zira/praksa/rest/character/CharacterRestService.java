/**
 *
 */
package ba.com.zira.praksa.rest.character;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.praksa.api.CharacterService;
import ba.com.zira.praksa.api.model.LoV;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author zira
 *
 */

@Api(tags = "character")
@RestController
@RequestMapping(value = "character")
public class CharacterRestService {

    @Autowired
    CharacterService characterService;

    @ApiOperation(value = "Get Character names by Ids.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public ListPayloadResponse<LoV> getLoVs(@RequestParam(required = false) final List<Long> ids) throws ApiException {

        final ListRequest<Long> request = new ListRequest<>();
        request.setList(ids);

        return characterService.getLoVs(request);
    }

}
