package ba.com.zira.praksa.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ba.com.zira.praksa.api.model.franchise.FranchiseCreateRequest;
import ba.com.zira.praksa.api.model.franchise.FranchiseResponse;
import ba.com.zira.praksa.api.model.franchise.FranchiseUpdateRequest;
import ba.com.zira.praksa.dao.model.FranchiseEntity;

@Mapper(componentModel = "spring")
public interface FranchiseMapper {

    @Mapping(source = "name", target = "name")
    FranchiseResponse franchiseEntityToFranchise(FranchiseEntity sampleModelEntity);

    @Mapping(source = "name", target = "name")
    FranchiseEntity franchiseToFranchiseEntity(FranchiseCreateRequest sampleModel);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateForFranchiseUpdate(FranchiseUpdateRequest franchiseModel, @MappingTarget FranchiseEntity franchiseEntity);

    FranchiseEntity dtoToEntity(FranchiseCreateRequest sample);

    @InheritInverseConfiguration(name = "dtoToEntity")
    FranchiseResponse entityToDto(FranchiseEntity billingPeriodEntity);

}
