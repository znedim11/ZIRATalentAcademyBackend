package ba.com.zira.praksa.api.model.character;

import java.io.Serializable;

import lombok.Data;

@Data
public class CharacterSearchResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String outlineText;
    private String imageUrl;
    private Long id;
    private Long numberOfAppearances;

    public CharacterSearchResponse() {

    }

    public CharacterSearchResponse(final Long id, final String name, final String outlineText, final String imageUrl, final Long count) {
        this.id = id;
        this.name = name;
        this.outlineText = outlineText;
        this.imageUrl = imageUrl;
        this.numberOfAppearances = count;
    }

}
