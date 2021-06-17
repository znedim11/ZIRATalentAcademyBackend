package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.CompanyService;
import ba.com.zira.praksa.api.GameService;
import ba.com.zira.praksa.api.PlatformService;
import ba.com.zira.praksa.api.RegionService;
import ba.com.zira.praksa.api.ReleaseService;
import ba.com.zira.praksa.api.model.company.CompanyResponse;
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.api.model.platform.PlatformResponse;
import ba.com.zira.praksa.api.model.region.RegionResponse;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.dao.ReleaseDAO;
import ba.com.zira.praksa.dao.model.ReleaseEntity;
import ba.com.zira.praksa.mapper.CompanyMapper;
import ba.com.zira.praksa.mapper.GameMapper;
import ba.com.zira.praksa.mapper.PlatformMapper;
import ba.com.zira.praksa.mapper.RegionMapper;
import ba.com.zira.praksa.mapper.ReleaseMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReleaseServiceImpl implements ReleaseService
{
	final static String DEV = "DEV";
	final static String PUB = "PUB";
	RequestValidator requestValidator;
	ReleaseMapper releaseMapper;
	GameMapper gameMapper;
	PlatformMapper platformMapper;
	CompanyMapper companyMapper;
	RegionMapper regionMapper;
	GameService gameService;
	CompanyService companyService;
	PlatformService platformService;
	RegionService regionService;
	ReleaseDAO releaseDAO;

    @Override
    public PayloadResponse<String> addRelease(final EntityRequest<ReleaseRequest> request) throws ApiException {
        requestValidator.validate(request);
        ReleaseEntity entity = releaseMapper.dtoToEntity(request.getEntity());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());
        releaseDAO.persist(entity);
        ReleaseResponseLight response = releaseMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, "Release Added Successfully");
    }
	@Override
	public PayloadResponse<String> addRelease(final EntityRequest<ReleaseRequest> request) throws ApiException
	{
		requestValidator.validate(request);
		ReleaseEntity entity = releaseMapper.dtoToEntity(request.getEntity());
		entity.setCreated(LocalDateTime.now());
		entity.setCreatedBy(request.getUserId());

		SearchRequest<Long> requestGame = new SearchRequest<>();
		requestGame.setEntity(request.getEntity().getGameId());
		PayloadResponse<GameResponse> responseGamePayload = new PayloadResponse<>();
		responseGamePayload = gameService.findById(requestGame);
		GameResponse gameResponse = responseGamePayload.getPayload();
		entity.setGame(gameMapper.responseToEntity(gameResponse));

		SearchRequest<Long> requestRegion = new SearchRequest<>();
		requestRegion.setEntity(request.getEntity().getRegionId());
		PayloadResponse<RegionResponse> responseRegionPayload = new PayloadResponse<>();
		responseRegionPayload = regionService.findById(requestRegion);
		RegionResponse regionResponse = responseRegionPayload.getPayload();
		entity.setRegion(regionMapper.responseToEntity(regionResponse));

		SearchRequest<Long> requestPlatform = new SearchRequest<>();
		requestPlatform.setEntity(request.getEntity().getPlatformId());
		PayloadResponse<PlatformResponse> responsePlatformPayload = new PayloadResponse<>();
		responsePlatformPayload = platformService.findById(requestPlatform);
		PlatformResponse platformResponse = responsePlatformPayload.getPayload();
		entity.setPlatform(platformMapper.responseToEntity(platformResponse));

		String companyType = request.getEntity().getCompanyType();

		SearchRequest<Long> requestCompany = new SearchRequest<>();
		PayloadResponse<CompanyResponse> responseCompanyPayload = new PayloadResponse<>();
		if (DEV.equalsIgnoreCase(companyType))
		{
			requestCompany.setEntity(request.getEntity().getDeveloperId());
			responseCompanyPayload = companyService.findById(requestCompany);
			CompanyResponse companyResponse = responseCompanyPayload.getPayload();
			entity.setDeveloper(companyMapper.responseToEntity(companyResponse));
		}
		else if (PUB.equalsIgnoreCase(companyType))
		{
			requestCompany.setEntity(request.getEntity().getPublisherId());
			responseCompanyPayload = companyService.findById(requestCompany);
			CompanyResponse companyResponse = responseCompanyPayload.getPayload();
			entity.setPublisher(companyMapper.responseToEntity(companyResponse));
		}

		releaseDAO.persist(entity);
		return new PayloadResponse<>(request, ResponseCode.OK, "Release Added Successfully");
	}

}
