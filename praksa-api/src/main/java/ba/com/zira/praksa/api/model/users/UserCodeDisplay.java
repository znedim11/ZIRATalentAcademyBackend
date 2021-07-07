package ba.com.zira.praksa.api.model.users;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserCodeDisplay implements Serializable {

    private static final long serialVersionUID = 1L;
    private String usercode;
    private String displayname;
}
