package ba.com.zira.praksa.rest.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.praksa.api.UserService;
import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.users.UserCodeDisplay;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "user")
@RestController
@RequestMapping(value = "/user")
public class UserRestService {

    @Autowired
    UserService userService;

    @ApiOperation(value = "Get all user codes and ids", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/getallusercodes")
    public PagedPayloadResponse<UserCodeDisplay> getUsers() throws ApiException {

        EntityRequest<ReviewResponse> request = new EntityRequest<>();
        return userService.getUsers(request);
    }

}
