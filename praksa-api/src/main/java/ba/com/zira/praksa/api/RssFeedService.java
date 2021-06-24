package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.model.rssfeed.RssFeed;

/**
 * * Methods used to manipulate {@link RssFeedService} data. * Methods used to
 * manipulate RssFeedService data. <br>
 * List of APIs implemented in this class with links:
 * <ul>
 * <li>{@link #rssFeedReader}</li>
 * </ul>
 *
 * @author zira
 *
 */

public interface RssFeedService {
    /**
     * Read {@link RssFeed}s by Id.
     *
     * @param request
     *            {@link RssFeedService}
     * @return {@link PagedPayloadResponse} for {@link RssFeed}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PayloadResponse<String> rssFeedReader(final EntityRequest<Long> request) throws ApiException;
}
