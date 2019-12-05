package ba.com.zira.praksa.dao.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "SAMPLE_MODEL")
public class SampleModelEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "SAMPLE_MODEL_ID_GENERATOR", sequenceName = "SAMPLE_MODEL_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAMPLE_MODEL_ID_GENERATOR")
    private Long id;
    private String docname;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return the docname
     */
    public String getDocumentName() {
        return docname;
    }

    /**
     * @param docname
     *            the docname to set
     */
    public void setDocumentName(final String docname) {
        this.docname = docname;
    }
}