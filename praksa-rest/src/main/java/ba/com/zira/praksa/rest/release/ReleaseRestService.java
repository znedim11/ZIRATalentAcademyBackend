package ba.com.zira.praksa.rest.release;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.CompanyService;
import ba.com.zira.praksa.api.GameService;
import ba.com.zira.praksa.api.PlatformService;
import ba.com.zira.praksa.api.RegionService;
import ba.com.zira.praksa.api.ReleaseService;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.api.release.ReleaseType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "release")
@RestController
@RequestMapping(value = "release")

public class ReleaseRestService
{
	@Autowired
	private ReleaseService releaseService;
	private GameService gameService;
	private PlatformService platformService;
	private RegionService regionService;
	private CompanyService companyService;
	private ReleaseType releaseType;

	@ApiOperation(value = "Add Release", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/game/release/add")
	public PayloadResponse<String> addReleaseGame(@RequestBody final EntityRequest<ReleaseRequest> request) throws ApiException
	{
		final ReleaseRequest addReleaseRequest = request.getEntity();
		addReleaseRequest.setType(releaseType.Game.toString());
		return releaseService.addRelease(request);
	}

	@ApiOperation(value = "Add Release", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/platform/release/add")
	public PayloadResponse<String> addReleasePlatform(@RequestBody final EntityRequest<ReleaseRequest> request) throws ApiException
	{
		final ReleaseRequest addReleaseRequest = request.getEntity();
		addReleaseRequest.setType(releaseType.Platform.toString());
		return releaseService.addRelease(request);
	}

}
