package ba.com.zira.praksa.api.model.datatransfer;

import java.io.Serializable;

import lombok.Data;

@Data
public class PlatformCompanyHelper implements Serializable {
    private static final long serialVersionUID = 1L;

    private String platformName;

    private String developerName;

    private String publisherName;

    private String releaseDate;

    public PlatformCompanyHelper() {
        super();
    }

    public PlatformCompanyHelper(String platformName, String developerName, String publisherName, String releaseDate) {
        super();
        this.platformName = platformName;
        this.developerName = developerName;
        this.publisherName = publisherName;
        this.releaseDate = releaseDate;
    }

}
