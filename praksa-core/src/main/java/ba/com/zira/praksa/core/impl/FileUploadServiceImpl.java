package ba.com.zira.praksa.core.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.FileUploadService;
import ba.com.zira.praksa.api.model.utils.ImageUploadRequest;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Value("${image.folder.location://opt//zira//cdn//vigor//img}")
    private String imagePath;

    @Value("${image.server.location:http://172.20.20.45:82//vigor//img}")
    private String imageServerUrl;

    @Override
    public Map<String, String> uploadImage(EntityRequest<ImageUploadRequest> imageUploadRequest) throws ApiException {
        Map<String, String> result = new HashMap<>();
        try {
            String url = imageServerUrl + "/" + imageUploadRequest.getEntity().getImageName();
            String base64Image = imageUploadRequest.getEntity().getImageData().split(",")[1];
            byte[] decodedImage = Base64.getDecoder().decode(base64Image.getBytes(StandardCharsets.UTF_8));
            Path destinationFile = Paths.get(imagePath, imageUploadRequest.getEntity().getImageName());
            String basename = FilenameUtils.getBaseName(imageUploadRequest.getEntity().getImageName());
            String extension = FilenameUtils.getExtension(imageUploadRequest.getEntity().getFileType());
            Files.write(destinationFile, decodedImage);
            result.put("url", url);
            result.put("baseName", basename);
            result.put("extension", extension);

            return result;
        } catch (IOException e) {
            throw ApiException.createFrom(imageUploadRequest, ResponseCode.REQUEST_INVALID, e.getMessage());
        }
    }
}