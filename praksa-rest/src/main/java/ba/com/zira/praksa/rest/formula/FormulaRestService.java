/**
 *
 */
package ba.com.zira.praksa.rest.formula;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.FormulaService;
import ba.com.zira.praksa.api.model.LoV;
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

    @ApiOperation(value = "Find Formulas", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<FormulaResponse> find(@RequestParam(required = false) final String pagination) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);

        return formulaService.find(request);
    }

    @ApiOperation(value = "Get Formula by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<FormulaResponse> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return formulaService.findById(request);
    }

    @ApiOperation(value = "Create Formula", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<FormulaResponse> create(@RequestBody EntityRequest<FormulaCreateRequest> request) throws ApiException {
        return formulaService.create(request);
    }

    @ApiOperation(value = "Update Formula", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<FormulaResponse> update(@PathVariable final Long id,
            @RequestBody final EntityRequest<FormulaUpdateRequest> request) throws ApiException {

        final FormulaUpdateRequest formula = request.getEntity();
        if (!Objects.isNull(formula)) {
            formula.setId(id);
        }

        return formulaService.update(request);
    }

    @ApiOperation(value = "Get number of Reviews by Formula.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/reviewcount")
    public PayloadResponse<Long> getNumberOfReviewsForFormula(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return formulaService.getNumberOfReviewsGamesByFormula(request);
    }

    @ApiOperation(value = "Get Formula names by Ids.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public ListPayloadResponse<LoV> getLoVs(@RequestParam(required = false) final List<Long> ids) throws ApiException {

        final ListRequest<Long> request = new ListRequest<>();
        request.setList(ids);

        return formulaService.getLoVs(request);
    }

    @ApiOperation(value = "Get grade types by Formula.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/grades")
    public ListPayloadResponse<String> getGradesByFormula(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return formulaService.getGradesByFormula(request);
    }
}
