package ba.com.zira.praksa.api.model.game;

import java.io.Serializable;

import lombok.Data;

@Data
public class GameOverviewResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String abbriviation;
    private String fullName;
    private String information;
    private String outlineText;
    private String imageUrl;
    private String[] platformAbbreviations;
    private String platformName;
    private String firstReleaseDate;
    private String developer;
    private String publisher;
}
