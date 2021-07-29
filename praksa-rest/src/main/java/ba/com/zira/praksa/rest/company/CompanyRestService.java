package ba.com.zira.praksa.rest.company;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.CompanyService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.company.CompanyCreateRequest;
import ba.com.zira.praksa.api.model.company.CompanyResponse;
import ba.com.zira.praksa.api.model.company.CompanySearchRequest;
import ba.com.zira.praksa.api.model.company.CompanySearchResponse;
import ba.com.zira.praksa.api.model.company.CompanyUpdateRequest;
import ba.com.zira.praksa.api.model.company.report.CompanyRegionPlatformRequest;
import ba.com.zira.praksa.api.model.company.report.CompanyRegionPlatformResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "company")
@RestController
@RequestMapping(value = "company")
public class CompanyRestService {

    @Autowired
    private CompanyService companyService;

    @ApiOperation(value = "Find Companies", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<CompanyResponse> find(@RequestParam(required = false) final String pagination) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        return companyService.find(request);
    }

    @ApiOperation(value = "Find Companies", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public PagedPayloadResponse<LoV> lovs(@RequestParam(required = false) final String pagination,
            @RequestParam(required = false) final String filter) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setPagination(pagination);
        request.setFilterExpression(filter);

        return companyService.getLoVs(request);
    }

    @ApiOperation(value = "Get Company by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<CompanyResponse> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return companyService.findById(request);
    }

    @ApiOperation(value = "Create Company", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<CompanyResponse> createCompany(@RequestBody EntityRequest<CompanyCreateRequest> request) throws ApiException {
        return companyService.create(request);
    }

    @ApiOperation(value = "Update Company", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<CompanyResponse> update(@PathVariable final String id,
            @RequestBody final EntityRequest<CompanyUpdateRequest> request) throws ApiException {

        final CompanyUpdateRequest sample = request.getEntity();
        sample.setId(Long.decode(id));

        return companyService.update(request);
    }

    @ApiOperation(value = "Delete Company by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        companyService.delete(request);
    }

    @ApiOperation(value = "Get Prefered Region or Platform Report", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/company-region-platform-report")
    public PayloadResponse<CompanyRegionPlatformResponse> companyRegionPlatformReport(@RequestParam final List<Long> ids)
            throws ApiException {
        CompanyRegionPlatformRequest entity = new CompanyRegionPlatformRequest();
        entity.setCompanyIds(ids);
        EntityRequest<CompanyRegionPlatformRequest> request = new EntityRequest<>(entity);

        return companyService.companyRegionPlatformReport(request);
    }

    @ApiOperation(value = "Search companies.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/search")
    public PagedPayloadResponse<CompanySearchResponse> searchCompanies(@RequestParam(required = false) final String name,
            @RequestParam(required = false) final String dob, @RequestParam(required = false) final String dobCondition,
            @RequestParam(required = false) final String sortBy) throws ApiException {
        CompanySearchRequest companySearchRequest = new CompanySearchRequest();

        companySearchRequest.setName(name);
        companySearchRequest.setDobCondition(dobCondition);
        companySearchRequest.setSortBy(sortBy);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        companySearchRequest.setDob(dob != null ? LocalDate.parse(dob, formatter).atStartOfDay() : null);

        final EntityRequest<CompanySearchRequest> request = new EntityRequest<>();
        request.setEntity(companySearchRequest);

        return companyService.searchCompanies(request);
    }

}
