package ba.com.zira.praksa.api.model.datatransfer;

import java.io.Serializable;

import lombok.Data;

@Data
public class GamePlatformCompanyHelper implements Serializable {
    private static final long serialVersionUID = 1L;

    private String gameName;

    private String platformName;

    private String developerName;

    private String publisherName;

    private String releaseDate;

    private String releaseYear;

    public GamePlatformCompanyHelper() {
        super();
    }

    public GamePlatformCompanyHelper(String gameName, String platformName, String developerName, String publisherName, String releaseDate,
            String releaseYear) {
        super();
        this.gameName = gameName;
        this.platformName = platformName;
        this.developerName = developerName;
        this.publisherName = publisherName;
        this.releaseDate = releaseDate;
        this.releaseYear = releaseYear;
    }

}
