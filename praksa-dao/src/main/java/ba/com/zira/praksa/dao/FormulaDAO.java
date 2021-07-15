package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.dao.model.ReviewFormulaEntity;

@Repository
public class FormulaDAO extends AbstractDAO<ReviewFormulaEntity, Long> {
    public List<String> getGradesByFormula(final Long formulaId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT g.type FROM GradeEntity g WHERE g.formulaId = :fId");

        TypedQuery<String> query = entityManager.createQuery(stringBuilder.toString(), String.class);
        query.setParameter("fId", formulaId);

        return query.getResultList();

    }

    public Long getNumberOfReviewsGamesByFormula(Long formulaId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT COUNT(r) FROM ReviewEntity r WHERE r.reviewFormula.id = :fId");

        TypedQuery<Long> query = entityManager.createQuery(stringBuilder.toString(), Long.class);
        query.setParameter("fId", formulaId);

        return query.getSingleResult();
    }

    public List<LoV> getLoVs(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("SELECT new ba.com.zira.praksa.api.model.LoV(f.id, f.name) FROM ReviewFormulaEntity f %s",
                list != null ? "WHERE f.id IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (list != null) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }
}
