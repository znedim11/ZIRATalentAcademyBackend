package ba.com.zira.praksa.api;

import java.util.Map;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.praksa.api.model.utils.ImageUploadRequest;

@FunctionalInterface
public interface FileUploadService {
    public Map<String, String> uploadImage(EntityRequest<ImageUploadRequest> imageUploadRequest) throws ApiException;
}