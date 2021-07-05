package ba.com.zira.praksa.dao;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.dao.model.GradeEntity;

@Repository
public class GradeDAO extends AbstractDAO<GradeEntity, Long> {
    public GradeEntity getGradeByName(final Long formulaId, final String type) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT g FROM GradeEntity g WHERE g.formulaId = :fId AND g.type = :type");

        TypedQuery<GradeEntity> query = entityManager.createQuery(stringBuilder.toString(), GradeEntity.class);
        query.setParameter("fId", formulaId);
        query.setParameter("type", type);

        return query.getSingleResult();

    }
}
