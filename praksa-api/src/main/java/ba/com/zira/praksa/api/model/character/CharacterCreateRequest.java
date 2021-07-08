package ba.com.zira.praksa.api.model.character;

import java.io.Serializable;
import java.util.List;

import ba.com.zira.praksa.api.model.utils.ImageCreateRequest;
import lombok.Data;

@Data
public class CharacterCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String realName;
    private String gender;
    private String dob;
    private String dod;
    private String outlineText;
    private String information;
    private List<Long> gamesIds;
    private String aliases;
    private ImageCreateRequest imageCreateRequest;
}
