package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.review.CompleteReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewSearchRequest;
import ba.com.zira.praksa.dao.model.ReviewEntity;

@Repository
public class ReviewDAO extends AbstractDAO<ReviewEntity, Long> {

    public List<ReviewResponse> searchReviews(final ReviewSearchRequest searchRequest) {

        StringBuilder jpql = new StringBuilder();
        jpql.append(
                "SELECT DISTINCT new ba.com.zira.praksa.api.model.review.ReviewResponse(g.fullName, g.id, p.abbriviation, p.id, r.title, r.createdBy, rg.grade, r.id)");
        jpql.append(" FROM ReviewEntity r");
        jpql.append(" INNER JOIN GameEntity g on g.id=r.game.id");
        jpql.append(" INNER JOIN ReleaseEntity re on re.game.id=g.id");
        jpql.append(" INNER JOIN PlatformEntity p on p.id =re.platform.id");
        jpql.append(" INNER JOIN ReviewGradeEntity rg on rg.review.id =r.id");
        jpql.append(" WHERE 1=1");

        if (searchRequest.getGameId() != null) {
            jpql.append(" AND g.id= :gameId");
        }
        if (searchRequest.getPlatformId() != null) {
            jpql.append(" AND p.id= :platformId");
        }
        if (searchRequest.getReviewerId() != null) {
            jpql.append(" AND r.createdBy= :reviewerId");
        }

        if (searchRequest.getLowestRating() != null && searchRequest.getHighestRating() != null) {
            jpql.append(" AND rg.grade BETWEEN :lowestRating AND :highestRating");
        }
        if (searchRequest.getLowestRating() == null && searchRequest.getHighestRating() != null) {
            jpql.append(" AND rg.grade < :highestRating");
        }
        if (searchRequest.getLowestRating() != null && searchRequest.getHighestRating() == null) {
            jpql.append(" AND rg.grade > :lowestRating");
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

    public List<CompleteReviewResponse> getTopGame(final ReviewSearchRequest searchRequest) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT DISTINCT new ba.com.zira.praksa.api.model.review.CompleteReviewResponse( g.fullName, 'top')");
        jpql.append(" FROM ReviewEntity r ");
        jpql.append(" LEFT OUTER JOIN  GameEntity g on g.id =r.game.id");
        jpql.append(" LEFT OUTER JOIN  ReleaseEntity rl on rl.game.id =g.id");
        jpql.append(" LEFT OUTER JOIN  PlatformEntity p on p.id =rl.platform.id");
        jpql.append(" LEFT OUTER JOIN  ReviewGradeEntity rg on rg.review.id =r.id");
        jpql.append(" WHERE 1=1");

        if (searchRequest.getGameId() != null) {
            jpql.append(" AND g.id= :gameId");
        }
        if (searchRequest.getPlatformId() != null) {
            jpql.append(" AND p.id= :platformId");
        }
        if (searchRequest.getReviewerId() != null) {
            jpql.append(" AND r.createdBy= :reviewerId");
        }

        if (searchRequest.getLowestRating() != null && searchRequest.getHighestRating() != null) {
            jpql.append(" AND rg.grade BETWEEN :lowestRating AND :highestRating");
        }
        if (searchRequest.getLowestRating() == null && searchRequest.getHighestRating() != null) {
            jpql.append(" AND rg.grade < :highestRating");
        }
        if (searchRequest.getLowestRating() != null && searchRequest.getHighestRating() == null) {
            jpql.append(" AND rg.grade > :lowestRating");
        }

        jpql.append(" AND rg.grade=(SELECT MAX(rg1.grade) FROM ReviewGradeEntity rg1 WHERE rg1.id=rg.id)");

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
        if (searchRequest.getLowestRating() != null) {
            query.setParameter("lowestRating", searchRequest.getLowestRating());
        }
        if (searchRequest.getHighestRating() != null) {
            query.setParameter("highestRating", searchRequest.getHighestRating());
        }

        return query.getResultList();

    }

    public List<CompleteReviewResponse> getFlopGame(final ReviewSearchRequest searchRequest) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT DISTINCT new ba.com.zira.praksa.api.model.review.CompleteReviewResponse( g.fullName, 'flop')");
        jpql.append(" FROM ReviewEntity r ");
        jpql.append(" LEFT OUTER JOIN  GameEntity g on g.id =r.game.id");
        jpql.append(" LEFT OUTER JOIN  ReleaseEntity rl on rl.game.id =g.id");
        jpql.append(" LEFT OUTER JOIN  PlatformEntity p on p.id =rl.platform.id");
        jpql.append(" LEFT OUTER JOIN  ReviewGradeEntity rg on rg.review.id =r.id");
        jpql.append(" WHERE 1=1");

        if (searchRequest.getGameId() != null) {
            jpql.append(" AND g.id= :gameId");
        }
        if (searchRequest.getPlatformId() != null) {
            jpql.append(" AND p.id= :platformId");
        }
        if (searchRequest.getReviewerId() != null) {
            jpql.append(" AND r.createdBy= :reviewerId");
        }

        if (searchRequest.getLowestRating() != null && searchRequest.getHighestRating() != null) {
            jpql.append(" AND rg.grade BETWEEN :lowestRating AND :highestRating");
        }
        if (searchRequest.getLowestRating() == null && searchRequest.getHighestRating() != null) {
            jpql.append(" AND rg.grade < :highestRating");
        }
        if (searchRequest.getLowestRating() != null && searchRequest.getHighestRating() == null) {
            jpql.append(" AND rg.grade > :lowestRating");
        }

        jpql.append(" AND rg.grade=(SELECT MIN(rg1.grade) FROM ReviewGradeEntity rg1 WHERE rg1.id=rg.id)");

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
        jpql.append("SELECT DISTINCT new ba.com.zira.praksa.api.model.review.CompleteReviewResponse( p.abbriviation, COUNT(p.id))");
        jpql.append(" FROM ReviewEntity r ");
        jpql.append(" LEFT OUTER JOIN  GameEntity g on g.id =r.game.id");
        jpql.append(" LEFT OUTER JOIN  ReleaseEntity rl on rl.game.id =g.id");
        jpql.append(" LEFT OUTER JOIN  PlatformEntity p on p.id =rl.platform.id");
        jpql.append(" LEFT OUTER JOIN  ReviewGradeEntity rg on rg.review.id =r.id");
        jpql.append(" WHERE 1=1");

        if (searchRequest.getGameId() != null) {
            jpql.append(" AND g.id= :gameId");
        }
        if (searchRequest.getPlatformId() != null) {
            jpql.append(" AND p.id= :platformId");
        }
        if (searchRequest.getReviewerId() != null) {
            jpql.append(" AND r.createdBy= :reviewerId");
        }

        if (searchRequest.getLowestRating() != null && searchRequest.getHighestRating() != null) {
            jpql.append(" AND rg.grade BETWEEN :lowestRating AND :highestRating");
        }
        if (searchRequest.getLowestRating() == null && searchRequest.getHighestRating() != null) {
            jpql.append(" AND rg.grade < :highestRating");
        }
        if (searchRequest.getLowestRating() != null && searchRequest.getHighestRating() == null) {
            jpql.append(" AND rg.grade > :lowestRating");
        }

        jpql.append(" GROUP BY p.abbriviation");
        jpql.append(" ORDER BY 2 DESC");

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
        if (searchRequest.getLowestRating() != null) {
            query.setParameter("lowestRating", searchRequest.getLowestRating());
        }
        if (searchRequest.getHighestRating() != null) {
            query.setParameter("highestRating", searchRequest.getHighestRating());
        }

        return query.getResultList();

    }

}
