/**
 *
 */
package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

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
import ba.com.zira.praksa.api.FormulaService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.formula.FormulaCreateRequest;
import ba.com.zira.praksa.api.model.formula.FormulaResponse;
import ba.com.zira.praksa.api.model.formula.FormulaUpdateRequest;
import ba.com.zira.praksa.core.validation.FormulaRequestValidation;
import ba.com.zira.praksa.dao.FormulaDAO;
import ba.com.zira.praksa.dao.GradeDAO;
import ba.com.zira.praksa.dao.ReviewDAO;
import ba.com.zira.praksa.dao.model.GradeEntity;
import ba.com.zira.praksa.dao.model.ReviewFormulaEntity;
import ba.com.zira.praksa.mapper.FormulaMapper;

/**
 * @author zira
 *
 */

@Service
public class FormulaServiceImpl implements FormulaService {

    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";
    static final String BASIC_NOT_NULL = "basicNotNull";

    RequestValidator requestValidator;
    FormulaRequestValidation formulaRequestValidation;
    FormulaDAO formulaDAO;
    FormulaMapper formulaMapper;

    GradeDAO gradeDAO;
    ReviewDAO reviewDAO;

    public FormulaServiceImpl(RequestValidator requestValidator, FormulaRequestValidation formulaRequestValidation, FormulaDAO formulaDAO,
            FormulaMapper formulaMapper, GradeDAO gradeDAO, ReviewDAO reviewDAO) {
        super();
        this.requestValidator = requestValidator;
        this.formulaRequestValidation = formulaRequestValidation;
        this.formulaDAO = formulaDAO;
        this.formulaMapper = formulaMapper;
        this.gradeDAO = gradeDAO;
        this.reviewDAO = reviewDAO;
    }

    @Override
    public PagedPayloadResponse<FormulaResponse> find(SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<ReviewFormulaEntity> conceptEntitesData = formulaDAO.findAll(request.getFilter());
        List<ReviewFormulaEntity> conceptEntities = conceptEntitesData.getRecords();

        final List<FormulaResponse> conceptList = formulaMapper.entityListToResponseList(conceptEntities);

        PagedData<FormulaResponse> pagedData = new PagedData<>();
        pagedData.setNumberOfPages(conceptEntitesData.getNumberOfPages());
        pagedData.setNumberOfRecords(conceptEntitesData.getNumberOfRecords());
        pagedData.setPage(conceptEntitesData.getPage());
        pagedData.setRecords(conceptList);
        pagedData.setRecordsPerPage(conceptEntitesData.getRecordsPerPage());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, pagedData);
    }

    @Override
    public PayloadResponse<FormulaResponse> findById(SearchRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        formulaRequestValidation.validateFormulaExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        final ReviewFormulaEntity formulaEntity = formulaDAO.findByPK(request.getEntity());

        final FormulaResponse formulaResponse = formulaMapper.entityToResponse(formulaEntity);

        formulaResponse.setGrades(formulaDAO.getGradesByFormula(formulaResponse.getId()));

        return new PayloadResponse<>(request, ResponseCode.OK, formulaResponse);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<FormulaResponse> create(EntityRequest<FormulaCreateRequest> request) throws ApiException {
        formulaRequestValidation.validateEntityExistsInCreateRequest(request, BASIC_NOT_NULL);
        formulaRequestValidation.validateRequiredAttributesExistInCreateRequest(request, BASIC_NOT_NULL);

        ReviewFormulaEntity entity = formulaMapper.createRequestToEntity(request.getEntity());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());

        ReviewFormulaEntity createdEntity = formulaDAO.persist(entity);
        List<GradeEntity> grades = new ArrayList<>();

        if (request.getEntity().getGrades() != null) {
            for (String grade : request.getEntity().getGrades()) {
                GradeEntity gradeEntity = new GradeEntity();
                gradeEntity.setType(grade);
                gradeEntity.setFormulaId(createdEntity.getId());
                gradeEntity.setCreated(LocalDateTime.now());
                gradeEntity.setCreatedBy(request.getUserId());

                grades.add(gradeEntity);
            }

            gradeDAO.persistCollection(grades);
        }

        FormulaResponse response = formulaMapper.entityToResponse(entity);
        response.setGrades(grades.stream().map(GradeEntity::getType).collect(Collectors.toList()));

        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<FormulaResponse> update(EntityRequest<FormulaUpdateRequest> request) throws ApiException {
        formulaRequestValidation.validateEntityExistsInUpdateRequest(request, BASIC_NOT_NULL);

        EntityRequest<Long> entityRequestId = new EntityRequest<>(request.getEntity().getId(), request);
        formulaRequestValidation.validateFormulaExists(entityRequestId, VALIDATE_ABSTRACT_REQUEST);

        formulaRequestValidation.validateRequiredAttributesExistInUpdateRequest(request, BASIC_NOT_NULL);

        final FormulaUpdateRequest formulaRequest = request.getEntity();

        ReviewFormulaEntity formulaEntity = formulaDAO.findByPK(request.getEntity().getId());
        formulaEntity = formulaMapper.updateRequestToEntity(formulaRequest, formulaEntity);
        formulaEntity.setModified(LocalDateTime.now());
        formulaEntity.setModifiedBy(request.getUserId());

        formulaDAO.merge(formulaEntity);

        List<String> currentGrades = formulaDAO.getGradesByFormula(formulaEntity.getId());
        List<String> newGrades = formulaRequest.getGrades();

        final List<String> gradesToDelete = new ArrayList<>(Sets.difference(new HashSet<>(currentGrades), new HashSet<>(newGrades)));
        final List<String> gradesToAdd = new ArrayList<>(Sets.difference(new HashSet<>(newGrades), new HashSet<>(currentGrades)));

        List<GradeEntity> grades = new ArrayList<>();

        if (!gradesToAdd.isEmpty()) {
            for (String grade : gradesToAdd) {
                GradeEntity gradeEntity = new GradeEntity();
                gradeEntity.setType(grade);
                gradeEntity.setFormulaId(formulaEntity.getId());
                gradeEntity.setCreated(LocalDateTime.now());
                gradeEntity.setCreatedBy(request.getUserId());

                grades.add(gradeEntity);
            }

            gradeDAO.persistCollection(grades);
        }

        if (!gradesToDelete.isEmpty()) {
            for (String grade : gradesToDelete) {
                GradeEntity gradeEntity = gradeDAO.getGradeByName(formulaEntity.getId(), grade);

                gradeDAO.remove(gradeEntity);
            }
        }

        final FormulaResponse formulaResponse = formulaMapper.entityToResponse(formulaEntity);
        formulaResponse.setGrades(formulaDAO.getGradesByFormula(formulaEntity.getId()));

        return new PayloadResponse<>(request, ResponseCode.OK, formulaResponse);
    }

    @Override
    public PayloadResponse<Long> getNumberOfReviewsGamesByFormula(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        formulaRequestValidation.validateFormulaExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        Long numberofReviews = formulaDAO.getNumberOfReviewsGamesByFormula(request.getEntity());

        return new PayloadResponse<>(request, ResponseCode.OK, numberofReviews);
    }

    @Override
    public ListPayloadResponse<LoV> getLoVs(ListRequest<Long> request) throws ApiException {
        if (request.getList() != null) {
            for (Long item : request.getList()) {
                EntityRequest<Long> longRequest = new EntityRequest<>(item, request);
                formulaRequestValidation.validateFormulaExists(longRequest, VALIDATE_ABSTRACT_REQUEST);
            }
        }

        List<LoV> loVs = formulaDAO.getLoVs(request.getList());

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }

    @Override
    public ListPayloadResponse<String> getGradesByFormula(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        formulaRequestValidation.validateFormulaExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<String> grades = formulaDAO.getGradesByFormula(request.getEntity());

        return new ListPayloadResponse<>(request, ResponseCode.OK, grades);
    }

}
