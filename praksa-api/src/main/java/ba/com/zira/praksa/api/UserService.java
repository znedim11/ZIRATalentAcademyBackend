package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.users.UserCodeDisplay;

/**
 *
 * @author zira
 *
 */

public interface UserService {

    public PagedPayloadResponse<UserCodeDisplay> getUsers(EntityRequest<ReviewResponse> request) throws ApiException;

}
