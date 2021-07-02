package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.FranchiseService;
import ba.com.zira.praksa.api.model.franchise.FranchiseCreateRequest;
import ba.com.zira.praksa.api.model.franchise.FranchiseResponse;
import ba.com.zira.praksa.api.model.franchise.FranchiseUpdateRequest;
import ba.com.zira.praksa.dao.FranchiseDAO;
import ba.com.zira.praksa.dao.model.FranchiseEntity;
import ba.com.zira.praksa.mapper.FranchiseMapper;

@Service
public class FranchiseServiceImpl implements FranchiseService {

    private RequestValidator requestValidator;
    private FranchiseDAO franchiseDAO;
    private FranchiseMapper franchiseMapper;

    public FranchiseServiceImpl(final RequestValidator requestValidator, FranchiseDAO franchiseDAO, FranchiseMapper franchiseMapper) {
        this.requestValidator = requestValidator;
        this.franchiseDAO = franchiseDAO;
        this.franchiseMapper = franchiseMapper;
    }

    @Override
    public PagedPayloadResponse<FranchiseResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<FranchiseEntity> franchiseModelEntities = franchiseDAO.findAll(request.getFilter());
        final List<FranchiseResponse> franchiseList = new ArrayList<>();

        for (final FranchiseEntity FranchiseEntity : franchiseModelEntities.getRecords()) {
            franchiseList.add(franchiseMapper.entityToDto(FranchiseEntity));
        }
        return new PagedPayloadResponse<>(request, ResponseCode.OK, franchiseList.size(), 1, 1, franchiseList.size(), franchiseList);
    }

    @Override
    public PayloadResponse<FranchiseResponse> create(EntityRequest<FranchiseCreateRequest> request) throws ApiException {
        requestValidator.validate(request);
        FranchiseEntity entity = franchiseMapper.dtoToEntity(request.getEntity());
        franchiseDAO.persist(entity);
        FranchiseResponse response = franchiseMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<FranchiseResponse> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final FranchiseEntity franchiseEntity = franchiseDAO.findByPK(request.getEntity());

        final FranchiseResponse franchise = franchiseMapper.entityToDto(franchiseEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, franchise);
    }

    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<FranchiseResponse> update(final EntityRequest<FranchiseUpdateRequest> request) throws ApiException {
        requestValidator.validate(request);

        FranchiseEntity existingFranchiseEntity = franchiseDAO.findByPK(request.getEntity().getId());

        final LocalDateTime date = LocalDateTime.now();
        final FranchiseUpdateRequest franchise = request.getEntity();

        franchiseMapper.updateForFranchiseUpdate(franchise, existingFranchiseEntity);

        existingFranchiseEntity.setModified(date);
        existingFranchiseEntity.setModifiedBy(request.getUserId());

        franchiseDAO.merge(existingFranchiseEntity);

        final FranchiseResponse response = franchiseMapper.entityToDto(existingFranchiseEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);

    }

    @Override
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        franchiseDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Franchise deleted!");
    }
}
