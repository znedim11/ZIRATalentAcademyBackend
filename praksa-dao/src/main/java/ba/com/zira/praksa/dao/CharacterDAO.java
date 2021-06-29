package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterSearchRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchResponse;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.dao.model.CharacterEntity;

@Repository
public class CharacterDAO extends AbstractDAO<CharacterEntity, Long> {

    public List<CharacterSearchResponse> searchCharacters(CharacterSearchRequest searchRequest) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(
                " SELECT new ba.com.zira.praksa.api.model.character.CharacterSearchResponse(c.id, c.name, c.information, ms.url, (SELECT COUNT(*) FROM LinkMapEntity lm WHERE lm.character.id = c.id AND lm.game.id IS NOT NULL))");
        jpql.append(" FROM CharacterEntity c");
        jpql.append(" LEFT OUTER JOIN MediaEntity m ON m.objectId = c.id");
        jpql.append(" LEFT OUTER JOIN MediaStoreEntity ms ON ms.media.id = m.id");
        jpql.append(" WHERE m.objectType = :objectType");

        if (searchRequest.getName() != null && !searchRequest.getName().isEmpty()) {
            jpql.append(" AND UPPER(c.name) like :name");
        }

        if (searchRequest.getGender() != null && !searchRequest.getGender().isEmpty()) {
            jpql.append(" AND c.gender = :gender");
        }

        if (searchRequest.getDob() != null && searchRequest.getDobCondition() != null && !searchRequest.getDobCondition().isEmpty()) {
            if (searchRequest.getDobCondition().toUpperCase().equals("BEFORE")) {
                System.out.println("BEFORE");
                jpql.append(" AND c.dob < :dob");
            } else if (searchRequest.getDobCondition().toUpperCase().equals("AFTER")) {
                System.out.println("AFTER");
                jpql.append(" AND c.dob > :dob");
            } else {
                System.out.println("EQUAL");
                jpql.append(" AND c.dob = :dob");
            }
        }

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            if (searchRequest.getSortBy().toUpperCase().equals("ALPHABETICAL ORDER")) {
                jpql.append(" ORDER BY c.name ASC");
            } else if (searchRequest.getSortBy().toUpperCase().equals("LAST EDIT")) {
                jpql.append(" ORDER BY c.modified DESC");
            } else if (searchRequest.getSortBy().toUpperCase().equals("MOST APPEARANCES")) {
                jpql.append(" ORDER BY 5 DESC ");
            }
        }

        TypedQuery<CharacterSearchResponse> query = entityManager.createQuery(jpql.toString(), CharacterSearchResponse.class);
        query.setParameter("objectType", ObjectType.CHARACTER.getValue());

        if (searchRequest.getName() != null && !searchRequest.getName().isEmpty()) {
            query.setParameter("name", "%" + searchRequest.getName().toUpperCase() + "%");
        }

        if (searchRequest.getGender() != null && !searchRequest.getGender().isEmpty()) {
            query.setParameter("gender", searchRequest.getGender());
        }

        if (searchRequest.getDob() != null) {
            query.setParameter("dob", searchRequest.getDob());

        }

        return query.getResultList();
    }

    public List<LoV> getLoVs(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("SELECT new ba.com.zira.praksa.api.model.LoV(c.id, c.name) FROM CharacterEntity c %s",
                list != null ? "WHERE c.id IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (list != null) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }
}
