package ba.com.zira.praksa.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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
    
    SampleModelEntity dtoToEntity(SampleRequest sample);

    @InheritInverseConfiguration(name = "dtoToEntity")
    SampleResponse entityToDto(SampleModelEntity billingPeriodEntity);

}