package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.ReleaseService;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.dao.CompanyDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.PlatformDAO;
import ba.com.zira.praksa.dao.RegionDAO;
import ba.com.zira.praksa.dao.ReleaseDAO;
import ba.com.zira.praksa.dao.model.ReleaseEntity;
import ba.com.zira.praksa.mapper.ReleaseMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReleaseServiceImpl implements ReleaseService {
    RequestValidator requestValidator;
    ReleaseMapper releaseMapper;
    ReleaseDAO releaseDAO;
    PlatformDAO platformDAO;
    CompanyDAO companyDAO;
    GameDAO gameDAO;
    RegionDAO regionDAO;

    @Override
    public PayloadResponse<String> addRelease(final EntityRequest<ReleaseRequest> request) throws ApiException {
        requestValidator.validate(request);
        ReleaseEntity entity = releaseMapper.dtoToEntity(request.getEntity());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());
        entity.setGame(gameDAO.findByPK(request.getEntity().getGameId()));
        entity.setRegion(regionDAO.findByPK(request.getEntity().getRegionId()));
        entity.setPlatform(platformDAO.findByPK(request.getEntity().getPlatformId()));

        if (request.getEntity().getDeveloperId() != null) {
            entity.setDeveloper(companyDAO.findByPK(request.getEntity().getDeveloperId()));
        } else if (request.getEntity().getPublisherId() != null) {
            entity.setPublisher(companyDAO.findByPK(request.getEntity().getPublisherId()));
        }

        releaseDAO.persist(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, "Release Added Successfully");
    }

}
