package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.dao.model.ReviewGradeEntity;

@Repository
public class ReviewGradeDAO extends AbstractDAO<ReviewGradeEntity, String> {
    public List<ReviewGradeEntity> getGradesByReview(final Long reviewId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT g FROM ReviewGradeEntity g WHERE g.review.id = :rId");

        TypedQuery<ReviewGradeEntity> query = entityManager.createQuery(stringBuilder.toString(), ReviewGradeEntity.class);
        query.setParameter("rId", reviewId);

        return query.getResultList();

    }
}
