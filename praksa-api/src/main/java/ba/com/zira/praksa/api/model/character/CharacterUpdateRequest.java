package ba.com.zira.praksa.api.model.character;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ba.com.zira.praksa.api.model.utils.ImageCreateRequest;
import lombok.Data;

@Data
public class CharacterUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private Long id;
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