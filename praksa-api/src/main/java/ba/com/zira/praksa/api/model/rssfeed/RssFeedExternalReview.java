package ba.com.zira.praksa.api.model.rssfeed;

import java.io.Serializable;

import lombok.Data;

@Data
public class RssFeedExternalReview implements Serializable {

    private static final long serialVersionUID = 1L;
    Long rssFeedId;
    String origin;
    String information;
    String createdBy;
    String gameName;
}
