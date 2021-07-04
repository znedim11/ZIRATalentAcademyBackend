/**
 *
 */
package ba.com.zira.praksa.rest.formula;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.FormulaService;
import ba.com.zira.praksa.api.model.formula.FormulaCreateRequest;
import ba.com.zira.praksa.api.model.formula.FormulaResponse;
import ba.com.zira.praksa.api.model.formula.FormulaUpdateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author zira
 *
 */

@Api(tags = "review-formula")
@RestController
@RequestMapping(value = "formula")
public class FormulaRestService {

    @Autowired
    FormulaService formulaService;

    @ApiOperation(value = "Get Concept by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<FormulaResponse> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return formulaService.findById(request);
    }

    @ApiOperation(value = "Create Concept", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<FormulaResponse> create(@RequestBody EntityRequest<FormulaCreateRequest> request) throws ApiException {
        return formulaService.create(request);
    }

    @ApiOperation(value = "Update Concept", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<FormulaResponse> update(@PathVariable final Long id,
            @RequestBody final EntityRequest<FormulaUpdateRequest> request) throws ApiException {

        final FormulaUpdateRequest formula = request.getEntity();
        if (!Objects.isNull(formula)) {
            formula.setId(id);
        }

        return formulaService.update(request);
    }
}
