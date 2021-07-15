package ba.com.zira.praksa.core.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.praksa.api.model.users.UserCodeDisplay;

@Service
@FeignClient(name = "${uaa.service.location}", decode404 = true)
public interface UAAFeignClient {

    @GetMapping(value = "/user/getallusercodes")
    public PagedPayloadResponse<UserCodeDisplay> getUserDetails() throws ApiException;
}
