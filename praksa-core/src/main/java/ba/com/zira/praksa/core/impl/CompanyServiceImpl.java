package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.CompanyService;
import ba.com.zira.praksa.api.MediaService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.company.CompanyCreateRequest;
import ba.com.zira.praksa.api.model.company.CompanyResponse;
import ba.com.zira.praksa.api.model.company.CompanyUpdateRequest;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.media.CreateMediaRequest;
import ba.com.zira.praksa.dao.CompanyDAO;
import ba.com.zira.praksa.dao.model.CompanyEntity;
import ba.com.zira.praksa.mapper.CompanyMapper;

@Service
public class CompanyServiceImpl implements CompanyService {

    private RequestValidator requestValidator;
    private CompanyDAO companyDAO;
    private CompanyMapper companyMapper;
    private MediaService mediaService;

    public CompanyServiceImpl(final RequestValidator requestValidator, CompanyDAO companyDAO, CompanyMapper companyMapper) {
        this.requestValidator = requestValidator;
        this.companyDAO = companyDAO;
        this.companyMapper = companyMapper;
    }

    @Override
    public PagedPayloadResponse<CompanyResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<CompanyEntity> companyModelEntities = companyDAO.findAll(request.getFilter());
        final List<CompanyResponse> companyList = new ArrayList<>();

        for (final CompanyEntity CompanyEntity : companyModelEntities.getRecords()) {
            companyList.add(companyMapper.entityToDto(CompanyEntity));
        }
        return new PagedPayloadResponse<>(request, ResponseCode.OK, companyList.size(), 1, 1, companyList.size(), companyList);
    }

    @Override
    public PayloadResponse<CompanyResponse> create(EntityRequest<CompanyCreateRequest> request) throws ApiException {
        requestValidator.validate(request);

        CompanyEntity entity = companyMapper.dtoToEntity(request.getEntity());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());
        companyDAO.persist(entity);

        if (request.getEntity().getImageCreateRequest() != null && request.getEntity().getImageCreateRequest().getImageData() != null
                && request.getEntity().getImageCreateRequest().getImageName() != null) {
            CreateMediaRequest mediaRequest = new CreateMediaRequest(ObjectType.COMPANY.getValue(), entity.getId(),
                    request.getEntity().getImageCreateRequest().getImageData(), request.getEntity().getImageCreateRequest().getImageName(),
                    "IMAGE", "COVER_IMAGE");

            mediaService.saveMedia(new EntityRequest<>(mediaRequest, request));
        }

        CompanyResponse response = companyMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<CompanyResponse> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final CompanyEntity companyEntity = companyDAO.findByPK(request.getEntity());

        final CompanyResponse company = companyMapper.entityToDto(companyEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, company);
    }

    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<CompanyResponse> update(final EntityRequest<CompanyUpdateRequest> request) throws ApiException {
        requestValidator.validate(request);

        CompanyEntity existingCompanyEntity = companyDAO.findByPK(request.getEntity().getId());

        final LocalDateTime date = LocalDateTime.now();
        final CompanyUpdateRequest company = request.getEntity();

        companyMapper.updateForCompanyUpdate(company, existingCompanyEntity);

        existingCompanyEntity.setModified(date);
        existingCompanyEntity.setModifiedBy(request.getUserId());

        companyDAO.merge(existingCompanyEntity);

        if (request.getEntity().getImageCreateRequest() != null && request.getEntity().getImageCreateRequest().getImageData() != null
                && request.getEntity().getImageCreateRequest().getImageName() != null) {
            CreateMediaRequest mediaRequest = new CreateMediaRequest(ObjectType.COMPANY.getValue(), existingCompanyEntity.getId(),
                    request.getEntity().getImageCreateRequest().getImageData(), request.getEntity().getImageCreateRequest().getImageName(),
                    "IMAGE", "COVER_IMAGE");

            mediaService.saveMedia(new EntityRequest<>(mediaRequest, request));
        }

        final CompanyResponse response = companyMapper.entityToDto(existingCompanyEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);

    }

    @Override
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        companyDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Company deleted!");
    }

    @Override
    public ListPayloadResponse<LoV> lovs(final ListRequest<Long> request) throws ApiException {

        List<LoV> loVs = companyDAO.getLoVs(request.getList());

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }
}
