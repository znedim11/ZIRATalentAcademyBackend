package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.enums.ReviewType;
import ba.com.zira.praksa.api.model.review.CompleteReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewSearchRequest;
import ba.com.zira.praksa.dao.model.ExternalReviewEntity;

@Repository
public class ExternalReviewDAO extends AbstractDAO<ExternalReviewEntity, Long> {

    public List<ReviewResponse> searchReviews(final ReviewSearchRequest searchRequest) {
        StringBuilder jpql = new StringBuilder();
        Double grade = 0D;
        jpql.append(
                "SELECT new ba.com.zira.praksa.api.model.review.ReviewResponse(g.fullName, g.id, p.fullName, p.id, er.gameName, er.createdBy, "
                        + grade + ", er.id, ");
        jpql.append(String.format("'%s' )", ReviewType.EXTERNAL.getValue()));
        jpql.append(" FROM ExternalReviewEntity er ");
        jpql.append(" LEFT OUTER JOIN  GameEntity g on g.id =er.game.id");
        jpql.append(" LEFT OUTER JOIN  ReleaseEntity rl on rl.game.id =g.id");
        jpql.append(" LEFT OUTER JOIN  PlatformEntity p on p.id =rl.platform.id");
        jpql.append(" WHERE 1=1");
        if (searchRequest.getGameId() != null) {
            jpql.append(" AND g.id = :gameId");
        }
        if (searchRequest.getPlatformId() != null) {
            jpql.append(" AND p.id = :platformId");
        }
        if (searchRequest.getReviewerId() != null) {
            jpql.append(" AND er.createdBy=:reviewerId");
        }
        if (searchRequest.getLowestRating() != null && searchRequest.getHighestRating() != null) {
            jpql.append(" AND " + grade + " BETWEEN :lowestRating AND :highestRating");
        }
        if (searchRequest.getLowestRating() == null && searchRequest.getHighestRating() != null) {
            jpql.append(" AND " + grade + " <= :highestRating");
        }
        if (searchRequest.getLowestRating() != null && searchRequest.getHighestRating() == null) {
            jpql.append(" AND " + grade + " >= :lowestRating");
        }

        TypedQuery<ReviewResponse> query = entityManager.createQuery(jpql.toString(), ReviewResponse.class);
        if (searchRequest.getGameId() != null) {
            query.setParameter("gameId", searchRequest.getGameId());
        }
        if (searchRequest.getPlatformId() != null) {
            query.setParameter("platformId", searchRequest.getPlatformId());
        }
        if (searchRequest.getReviewerId() != null) {
            query.setParameter("reviewerId", searchRequest.getReviewerId());
        }
        if (searchRequest.getLowestRating() != null) {
            query.setParameter("lowestRating", searchRequest.getLowestRating());
        }
        if (searchRequest.getHighestRating() != null) {
            query.setParameter("highestRating", searchRequest.getHighestRating());
        }

        return query.getResultList();
    }

    public List<CompleteReviewResponse> getMostPopularPlatform(final ReviewSearchRequest searchRequest) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT new ba.com.zira.praksa.api.model.review.CompleteReviewResponse( p.fullName, p.id , COUNT(p.id))");
        jpql.append(" FROM ExternalReviewEntity er ");
        jpql.append(" LEFT OUTER JOIN  GameEntity g on g.id =er.game.id");
        jpql.append(" LEFT OUTER JOIN  ReleaseEntity rl on rl.game.id =g.id");
        jpql.append(" LEFT OUTER JOIN  PlatformEntity p on p.id =rl.platform.id");
        jpql.append(" WHERE 1=1");
        if (searchRequest.getGameId() != null) {
            jpql.append(" AND g.id = :gameId");
        }
        if (searchRequest.getPlatformId() != null) {
            jpql.append(" AND p.id = :platformId");
        }
        if (searchRequest.getReviewerId() != null) {
            jpql.append(" AND er.createdBy=:reviewerId");
        }

        jpql.append(" GROUP BY p.fullName, p.id");
        jpql.append(" ORDER BY 3 DESC");

        TypedQuery<CompleteReviewResponse> query = entityManager.createQuery(jpql.toString(), CompleteReviewResponse.class);
        if (searchRequest.getGameId() != null) {
            query.setParameter("gameId", searchRequest.getGameId());
        }
        if (searchRequest.getPlatformId() != null) {
            query.setParameter("platformId", searchRequest.getPlatformId());
        }
        if (searchRequest.getReviewerId() != null) {
            query.setParameter("reviewerId", searchRequest.getReviewerId());
        }
        return query.getResultList();

    }

}