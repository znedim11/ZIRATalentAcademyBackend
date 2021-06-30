package ba.com.zira.praksa.api.model.character;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CharacterSearchRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String gender;

    private LocalDateTime dob;

    private String dobCondition;

    private String sortBy;

}
