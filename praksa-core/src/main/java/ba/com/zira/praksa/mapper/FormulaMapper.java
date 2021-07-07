package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.formula.FormulaCreateRequest;
import ba.com.zira.praksa.api.model.formula.FormulaResponse;
import ba.com.zira.praksa.api.model.formula.FormulaUpdateRequest;
import ba.com.zira.praksa.dao.model.ReviewFormulaEntity;

/**
 * @author zira
 *
 */

@Mapper(componentModel = "spring")
public interface FormulaMapper {
    FormulaMapper INSTANCE = Mappers.getMapper(FormulaMapper.class);

    FormulaResponse entityToResponse(ReviewFormulaEntity formulaEntity);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    ReviewFormulaEntity updateRequestToEntity(FormulaUpdateRequest formulaUpdateRequest, @MappingTarget ReviewFormulaEntity formulaEntity);

    ReviewFormulaEntity createRequestToEntity(FormulaCreateRequest formulaCreateRequest);

    List<FormulaResponse> entityListToResponseList(List<ReviewFormulaEntity> conceptEntities);
}