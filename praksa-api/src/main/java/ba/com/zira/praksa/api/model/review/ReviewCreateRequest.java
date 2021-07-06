package ba.com.zira.praksa.api.model.review;

import java.io.Serializable;

import ba.com.zira.praksa.api.model.utils.ImageCreateRequest;
import lombok.Data;

@Data
public class ReviewCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String aliases;

    private String information;

    private String name;

    private String outline;

    private ImageCreateRequest imageCreateRequest;
}
