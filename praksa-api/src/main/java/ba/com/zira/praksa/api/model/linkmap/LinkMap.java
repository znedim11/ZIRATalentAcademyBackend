package ba.com.zira.praksa.api.model.linkmap;

import java.io.Serializable;
import java.time.LocalDateTime;

import ba.com.zira.praksa.api.model.character.Character;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.game.Game;
import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.api.model.object.Object;
import ba.com.zira.praksa.api.model.person.Person;
import lombok.Data;

@Data
public class LinkMap implements Serializable {
    private static final long serialVersionUID = 1L;

    private String uuid;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime modified;
    private String modifiedBy;
    private Character character;
    private ConceptResponse concept;
    private Game game;
    private Location location;
    private Object object;
    private Person person;
}
