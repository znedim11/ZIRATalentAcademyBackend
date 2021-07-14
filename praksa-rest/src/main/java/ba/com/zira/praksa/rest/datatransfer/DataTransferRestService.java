/**
 *
 */
package ba.com.zira.praksa.rest.datatransfer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EmptyRequest;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.DataTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author zira
 *
 */

@Api(tags = "data-transfer")
@RestController
@RequestMapping(value = "datatransfer")
public class DataTransferRestService {

    @Autowired
    DataTransferService dataTransferService;

    @ApiOperation(value = "Transfer platforms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/platform")
    public PayloadResponse<String> platformHUSToPlatformHUT() throws ApiException {
        final EmptyRequest request = new EmptyRequest();

        return dataTransferService.platformHUSToPlatformHUT(request);
    }

    @ApiOperation(value = "Transfer games", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/game")
    public PayloadResponse<String> gameHUSToGameHUT(@RequestParam(required = false) final Long numOfRecords) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(numOfRecords);

        return dataTransferService.gameHUSToGameHUTWrapper(request);
    }
}
