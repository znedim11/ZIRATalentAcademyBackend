package ba.com.zira.praksa.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.SampleModel;
import ba.com.zira.praksa.api.request.SampleRequest;
import ba.com.zira.praksa.api.response.SampleResponse;
import ba.com.zira.praksa.dao.model.SampleModelEntity;

/**
 * Defined mapper interface for mapping a DTO to Entity model class
 * 
 * @author zira
 *
 */
@Mapper(componentModel = "spring")
public interface SampleModelMapper {

    SampleModelMapper INSTANCE = Mappers.getMapper(SampleModelMapper.class);
    
    @Mapping(source = "documentName", target = "documentName")
    SampleModel sampleModelEntityToSampleModel(SampleModelEntity sampleModelEntity);

    @Mapping(source = "documentName", target = "documentName")
    SampleModelEntity sampleModelToSampleModelEntity(SampleModel sampleModel);
    
    SampleModelEntity dtoToEntity(SampleRequest sample);

    @InheritInverseConfiguration(name = "dtoToEntity")
    SampleResponse entityToDto(SampleModelEntity billingPeriodEntity);

}