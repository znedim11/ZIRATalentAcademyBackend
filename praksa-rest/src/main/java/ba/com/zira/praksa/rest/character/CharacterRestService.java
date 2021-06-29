package ba.com.zira.praksa.rest.character;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.praksa.api.CharacterService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterSearchRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchResponse;
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
    private CharacterService characterService;

    @ApiOperation(value = "Search characters.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/search")
    public PagedPayloadResponse<CharacterSearchResponse> searchCharacters(@RequestParam(required = false) final String name,
            @RequestParam(required = false) final String gender, @RequestParam(required = false) final String dob,
            @RequestParam(required = false) final String dobCondition, @RequestParam(required = false) final String sortBy)
            throws ApiException {
        CharacterSearchRequest characterSearchRequest = new CharacterSearchRequest();

        characterSearchRequest.setName(name);
        characterSearchRequest.setDobCondition(dobCondition);
        characterSearchRequest.setGender(gender);
        characterSearchRequest.setSortBy(sortBy);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        characterSearchRequest.setDob(dob != null ? LocalDate.parse(dob, formatter).atStartOfDay() : null);

        final EntityRequest<CharacterSearchRequest> request = new EntityRequest<>();
        request.setEntity(characterSearchRequest);

        return characterService.searchCharacters(request);
    }

    @ApiOperation(value = "Get Character names by Ids.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public ListPayloadResponse<LoV> getLoVs(@RequestParam(required = false) final List<Long> ids) throws ApiException {

        final ListRequest<Long> request = new ListRequest<>();
        request.setList(ids);

        return characterService.getLoVs(request);
    }
}
