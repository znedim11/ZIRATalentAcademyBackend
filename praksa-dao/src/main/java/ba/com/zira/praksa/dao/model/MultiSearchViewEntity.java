package ba.com.zira.praksa.dao.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "HUW_MULTI_SEARCH")
@NamedQuery(name = "MultiSearchViewEntity.findAll", query = "SELECT c FROM MultiSearchViewEntity c")
public class MultiSearchViewEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    Long id;
    String name;
    String type;
}