package ba.com.zira.praksa.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
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
@Table(name = "hus_company_mapping")
@NamedQuery(name = "TransferCompanyEntity.findAll", query = "SELECT c FROM TransferCompanyEntity c")
public class TransferCompanyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUS_COMPANY_MAPPING_ID_GENERATOR", sequenceName = "HUS_COMPANY_MAPPING_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUS_COMPANY_MAPPING_ID_GENERATOR")
    private Long id;

    @Column(name = "root_name")
    private String rootName;

    @Column(name = "all_names")
    private String allNames;
}