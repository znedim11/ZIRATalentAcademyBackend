package ba.com.zira.praksa.api.model.datatransfer;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransferGameHelper implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String name;

    private String overview;

    private String cooperative;

    private String databaseId;

    private String communityRating;

    private String platform;

    private String communityRatingCount;

    private String genres;

    private String developer;

    private String publisher;

    private String releaseDate;

    private String releaseYear;

    private String esrb;

    private String videoUrl;

    private String maxPlayers;

    private String releaseType;

    private String wikipediaUrl;

    public TransferGameHelper(String name, String overview, String cooperative, String communityRating, String platform, String genres,
            String developer, String publisher, String releaseDate, String releaseYear, String videoUrl, String maxPlayers,
            String releaseType, String wikipediaUrl) {
        super();
        this.name = name;
        this.overview = overview;
        this.cooperative = cooperative;
        this.communityRating = communityRating;
        this.platform = platform;
        this.genres = genres;
        this.developer = developer;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
        this.releaseYear = releaseYear;
        this.videoUrl = videoUrl;
        this.maxPlayers = maxPlayers;
        this.releaseType = releaseType;
        this.wikipediaUrl = wikipediaUrl;
    }

}