package ba.com.zira.praksa.api.model.company;

import java.io.Serializable;

import lombok.Data;

@Data
public class CompanySearchResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String outlineText;
    private String imageUrl;
    private Long id;

    public CompanySearchResponse() {

    }

    public CompanySearchResponse(final Long id, final String name, final String outlineText, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.outlineText = outlineText;
        this.imageUrl = imageUrl;
    }

}