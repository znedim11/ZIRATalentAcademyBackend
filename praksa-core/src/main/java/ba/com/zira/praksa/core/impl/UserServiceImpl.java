package ba.com.zira.praksa.core.impl;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.UserService;
import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.users.UserCodeDisplay;
import ba.com.zira.praksa.core.clients.UAAFeignClient;

/**
 * @author zira
 *
 */

@Service
@ComponentScan
public class UserServiceImpl implements UserService {

    UAAFeignClient uaaFeignClient;

    public UserServiceImpl(UAAFeignClient uaaFeignClient) {
        this.uaaFeignClient = uaaFeignClient;
    }

    @Override
    public PagedPayloadResponse<UserCodeDisplay> getUsers(EntityRequest<ReviewResponse> request) throws ApiException {

        List<UserCodeDisplay> lista = uaaFeignClient.getUserDetails().getPayload();

        return new PagedPayloadResponse<>(request, ResponseCode.OK, lista.size(), 1, 1, lista.size(), lista);

    }

}
