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
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.core.validation.ReleaseRequestValidation;
import ba.com.zira.praksa.dao.ReleaseDAO;
import ba.com.zira.praksa.dao.model.ReleaseEntity;
import ba.com.zira.praksa.mapper.GameMapper;
import ba.com.zira.praksa.mapper.ReleaseMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReleaseServiceImpl implements ReleaseService
{
	RequestValidator requestValidator;
	ReleaseDAO releaseDAO;
	ReleaseMapper releaseMapper;
	ReleaseRequestValidation releaseRequestValidation;
	GameMapper gameMapper;
	GameService gameService;
	CompanyService companyService;
	PlatformService platformService;
	RegionService regionService;

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
		Long ID = request.getEntity().getGameId();
		requestGame.setEntity(ID);

		PayloadResponse<GameResponse> responseGame = new PayloadResponse<>();
		responseGame = gameService.findById(requestGame);
		// entity.setGame(gameMapper.responseToEntity(responseGame));

		return new PayloadResponse<>(request, ResponseCode.OK, "Release Added Successfully");
	}

}
