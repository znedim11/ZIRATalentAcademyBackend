package ba.com.zira.praksa.api.model.reviewgrade;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReviewGradeResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Double grade;

    private String type;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ReviewGradeResponse other = (ReviewGradeResponse) obj;
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

}
