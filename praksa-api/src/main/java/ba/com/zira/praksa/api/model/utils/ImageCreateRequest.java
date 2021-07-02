package ba.com.zira.praksa.api.model.utils;

import java.io.Serializable;

import lombok.Data;

@Data
public class ImageCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String imageData;
    private String imageName;
}