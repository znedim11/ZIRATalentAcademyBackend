package ba.com.zira.praksa.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.company.CompanyUpdateRequest;
import ba.com.zira.praksa.api.model.company.CompanyCreateRequest;
import ba.com.zira.praksa.api.model.company.CompanyResponse;
import ba.com.zira.praksa.api.model.franchise.FranchiseUpdateRequest;
import ba.com.zira.praksa.dao.model.CompanyEntity;
import ba.com.zira.praksa.dao.model.FranchiseEntity;


@Mapper(componentModel = "spring")
public interface CompanyMapper
{
	CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    @Mapping(source = "name", target = "name")
    CompanyResponse companyEntityToCompany(CompanyEntity sampleModelEntity);

    @Mapping(source = "name", target = "name")
    CompanyEntity companyToCompanyEntity(CompanyCreateRequest sampleModel);

    
    @Mapping(target="created",ignore=true)
    @Mapping(target="createdBy",ignore=true)
    void updateForCompanyUpdate(CompanyUpdateRequest companyModel,@MappingTarget CompanyEntity companyEntity);
    
    
    
    
    CompanyEntity dtoToEntity(CompanyCreateRequest sample);

    @InheritInverseConfiguration(name = "dtoToEntity")
    CompanyResponse entityToDto(CompanyEntity billingPeriodEntity);
}