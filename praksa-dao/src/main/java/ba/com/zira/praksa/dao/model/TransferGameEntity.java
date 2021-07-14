package ba.com.zira.praksa.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the hut_platform database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hus_game")
public class TransferGameEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUS_GAME_ID_GENERATOR", sequenceName = "HUS_GAME_SEQ", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUS_GAME_ID_GENERATOR")
    private Long id;

    private String name;

    private String overview;

    private String cooperative;

    @Column(name = "database_id")
    private String databaseId;

    @Column(name = "community_rating")
    private String communityRating;

    private String platform;

    @Column(name = "community_rating_count")
    private String communityRatingCount;

    private String genres;

    private String developer;

    private String publisher;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "release_year")
    private String releaseYear;

    private String esrb;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "max_players")
    private String maxPlayers;

    @Column(name = "release_type")
    private String releaseType;

    @Column(name = "wikipedia_url")
    private String wikipediaUrl;

}