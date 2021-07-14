package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EmptyRequest;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;

public interface DataTransferService {
    public PayloadResponse<String> platformHUSToPlatformHUT(final EmptyRequest request) throws ApiException;

    public PayloadResponse<String> gameHUSToGameHUTWrapper(final EntityRequest<Long> request) throws ApiException;
}
