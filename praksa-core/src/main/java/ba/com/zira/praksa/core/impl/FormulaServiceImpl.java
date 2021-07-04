/**
 *
 */
package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.FormulaService;
import ba.com.zira.praksa.api.model.formula.FormulaCreateRequest;
import ba.com.zira.praksa.api.model.formula.FormulaResponse;
import ba.com.zira.praksa.api.model.formula.FormulaUpdateRequest;
import ba.com.zira.praksa.core.validation.FormulaRequestValidation;
import ba.com.zira.praksa.dao.FormulaDAO;
import ba.com.zira.praksa.dao.GradeDAO;
import ba.com.zira.praksa.dao.ReviewDAO;
import ba.com.zira.praksa.dao.model.ReviewFormulaEntity;
import ba.com.zira.praksa.dao.model.ReviewGradeEntity;
import ba.com.zira.praksa.mapper.FormulaMapper;

/**
 * @author zira
 *
 */

@Service
public class FormulaServiceImpl implements FormulaService {

    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";
    static final String BASIC_NOT_NULL = "basicNotNull";

    FormulaRequestValidation formulaRequestValidation;
    FormulaDAO formulaDAO;
    FormulaMapper formulaMapper;

    GradeDAO gradeDAO;
    ReviewDAO reviewDAO;

    public FormulaServiceImpl(FormulaRequestValidation formulaRequestValidation, FormulaDAO formulaDAO, FormulaMapper formulaMapper,
            GradeDAO gradeDAO, ReviewDAO reviewDAO) {
        super();
        this.formulaRequestValidation = formulaRequestValidation;
        this.formulaDAO = formulaDAO;
        this.formulaMapper = formulaMapper;
        this.gradeDAO = gradeDAO;
        this.reviewDAO = reviewDAO;
    }

    @Override
    public PayloadResponse<FormulaResponse> findById(SearchRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        formulaRequestValidation.validateFormulaExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        final ReviewFormulaEntity formulaEntity = formulaDAO.findByPK(request.getEntity());

        final FormulaResponse formulaResponse = formulaMapper.entityToResponse(formulaEntity);

        // GET GRADES??

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
        List<ReviewGradeEntity> grades = new ArrayList<>();

        if (request.getEntity().getGrades() != null) {
            for (String grade : request.getEntity().getGrades()) {
                ReviewGradeEntity gradeEntity = new ReviewGradeEntity();
                gradeEntity.setUuid(UUID.randomUUID().toString());
                gradeEntity.setType(grade);
                gradeEntity.setFormulaId(createdEntity.getId());

                grades.add(gradeEntity);
                gradeDAO.merge(gradeEntity);
            }
        }

        FormulaResponse response = formulaMapper.entityToResponse(entity);

        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<FormulaResponse> update(EntityRequest<FormulaUpdateRequest> request) throws ApiException {
        // TODO Auto-generated method stub
        return null;
    }

}
