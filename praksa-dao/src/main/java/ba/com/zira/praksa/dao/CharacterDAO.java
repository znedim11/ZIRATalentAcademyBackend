package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterSearchRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchResponse;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.dao.model.CharacterEntity;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.LinkMapEntity;
import ba.com.zira.praksa.dao.model.LocationEntity;
import ba.com.zira.praksa.dao.model.MediaStoreEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.PersonEntity;

@Repository
public class CharacterDAO extends AbstractDAO<CharacterEntity, Long> {

    public List<CharacterSearchResponse> searchCharacters(CharacterSearchRequest searchRequest) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(
                " SELECT new ba.com.zira.praksa.api.model.character.CharacterSearchResponse(c.id, c.name, c.outlineText, ms.url, (SELECT COUNT(*) FROM LinkMapEntity lm WHERE lm.character.id = c.id AND lm.game.id IS NOT NULL))");
        jpql.append(" FROM CharacterEntity c");
        jpql.append(" LEFT OUTER JOIN MediaEntity m ON m.objectId = c.id");
        jpql.append(" LEFT OUTER JOIN MediaStoreEntity ms ON ms.media.id = m.id");
        jpql.append(" WHERE 1=1");

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

    public void removeAllGames(Long characterId) {
        CriteriaDelete<LinkMapEntity> criteriaDelete = builder.createCriteriaDelete(LinkMapEntity.class);
        Root<LinkMapEntity> root = criteriaDelete.from(LinkMapEntity.class);

        criteriaDelete.where(builder.equal(root.get("character"), characterId), builder.isNotNull(root.get("game")));

        entityManager.createQuery(criteriaDelete).executeUpdate();
    }

    public void deleteRelations(final Long characterId) {
        CriteriaDelete<LinkMapEntity> criteriaDelete = builder.createCriteriaDelete(LinkMapEntity.class);
        Root<LinkMapEntity> root = criteriaDelete.from(LinkMapEntity.class);

        criteriaDelete.where(builder.equal(root.get("character"), characterId));

        entityManager.createQuery(criteriaDelete).executeUpdate();
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

    public MediaStoreEntity getCoverImage(final Long characterId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "SELECT mse FROM MediaStoreEntity mse, MediaEntity me WHERE me.id = mse.media.id AND me.objectId = :objectId AND me.objectType = :objectType AND mse.type = :type");

        TypedQuery<MediaStoreEntity> query = entityManager.createQuery(stringBuilder.toString(), MediaStoreEntity.class);
        query.setParameter("objectId", characterId);
        query.setParameter("objectType", ObjectType.CHARACTER.getValue());
        query.setParameter("type", "COVER_IMAGE");

        return query.getResultList().stream().findFirst().orElse(null);
    }

    public List<ConceptEntity> getConceptsByCharacter(final Long characterId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("SELECT c FROM ConceptEntity c, LinkMapEntity lm WHERE lm.character.id = :characterId AND lm.concept.id = c.id");

        TypedQuery<ConceptEntity> query = entityManager.createQuery(stringBuilder.toString(), ConceptEntity.class);
        query.setParameter("characterId", characterId);

        return query.getResultList();
    }

    public List<LocationEntity> getLocationsByCharacter(final Long characterId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("SELECT l FROM LocationEntity l, LinkMapEntity lm WHERE lm.character.id = :characterId AND lm.location.id = l.id");

        TypedQuery<LocationEntity> query = entityManager.createQuery(stringBuilder.toString(), LocationEntity.class);
        query.setParameter("characterId", characterId);

        return query.getResultList();
    }

    public List<ObjectEntity> getObjectsByCharacter(final Long characterId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT o FROM ObjectEntity o, LinkMapEntity lm WHERE lm.character.id = :characterId AND lm.object.id = o.id");

        TypedQuery<ObjectEntity> query = entityManager.createQuery(stringBuilder.toString(), ObjectEntity.class);
        query.setParameter("characterId", characterId);

        return query.getResultList();
    }

    public List<PersonEntity> getPersonsByCharacter(final Long characterId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT p FROM PersonEntity p, LinkMapEntity lm WHERE lm.character.id = :characterId AND lm.person.id = p.id");

        TypedQuery<PersonEntity> query = entityManager.createQuery(stringBuilder.toString(), PersonEntity.class);
        query.setParameter("characterId", characterId);

        return query.getResultList();
    }

    public List<LoV> getLoVsNotConnectedTo(String field, String idField, Long id) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format(
                "SELECT DISTINCT c.id FROM CharacterEntity c LEFT OUTER JOIN LinkMapEntity lm ON lm.character.id= c.id WHERE lm.%s.%s = :id",
                field, idField));

        TypedQuery<Long> listQuery = entityManager.createQuery(stringBuilder.toString(), Long.class);
        listQuery.setParameter("id", id);
        List<Long> list = listQuery.getResultList();

        stringBuilder.setLength(0);
        stringBuilder.append(String.format("SELECT new ba.com.zira.praksa.api.model.LoV(c.id, c.name) FROM CharacterEntity c %s",
                !list.isEmpty() ? "WHERE c.id NOT IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (!list.isEmpty()) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }
}
