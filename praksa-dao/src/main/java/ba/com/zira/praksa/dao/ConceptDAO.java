package ba.com.zira.praksa.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.concept.ConceptSearchRequest;
import ba.com.zira.praksa.dao.model.CharacterEntity;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.LocationEntity;
import ba.com.zira.praksa.dao.model.MediaStoreEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.PersonEntity;

@Repository
public class ConceptDAO extends AbstractDAO<ConceptEntity, Long> {
    public List<GameEntity> getGamesByConcept(final Long conceptId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT g FROM GameEntity g, LinkMapEntity lm WHERE lm.concept.id = :cId AND lm.game.id = g.id");

        TypedQuery<GameEntity> query = entityManager.createQuery(stringBuilder.toString(), GameEntity.class);
        query.setParameter("cId", conceptId);

        return query.getResultList();

    }

    public List<PersonEntity> getPersonsByConcept(final Long conceptId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT p FROM PersonEntity p, LinkMapEntity lm WHERE lm.concept.id = :cId AND lm.person.id = p.id");

        TypedQuery<PersonEntity> query = entityManager.createQuery(stringBuilder.toString(), PersonEntity.class);
        query.setParameter("cId", conceptId);

        return query.getResultList();

    }

    public List<LoV> getLoVs(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("SELECT new ba.com.zira.praksa.api.model.LoV(c.id, c.name) FROM ConceptEntity c %s",
                list != null ? "WHERE c.id IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (list != null) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }

    public List<ObjectEntity> getObjectsByConcept(final Long conceptId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT o FROM ObjectEntity o, LinkMapEntity lm WHERE lm.concept.id = :cId AND lm.object.id = o.id");

        TypedQuery<ObjectEntity> query = entityManager.createQuery(stringBuilder.toString(), ObjectEntity.class);
        query.setParameter("cId", conceptId);

        return query.getResultList();
    }

    public List<CharacterEntity> getCharactersByConcept(final Long conceptId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT c FROM CharacterEntity c, LinkMapEntity lm WHERE lm.concept.id = :cId AND lm.character.id = c.id");

        TypedQuery<CharacterEntity> query = entityManager.createQuery(stringBuilder.toString(), CharacterEntity.class);
        query.setParameter("cId", conceptId);

        return query.getResultList();
    }

    public List<LocationEntity> getLocationsByConcept(final Long conceptId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT l FROM LocationEntity l, LinkMapEntity lm WHERE lm.concept.id = :cId AND lm.location.id = l.id");

        TypedQuery<LocationEntity> query = entityManager.createQuery(stringBuilder.toString(), LocationEntity.class);
        query.setParameter("cId", conceptId);

        return query.getResultList();
    }

    public Long getNumberOfGamesByConcept(final Long conceptId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT COUNT(g) FROM GameEntity g, LinkMapEntity lm WHERE lm.concept.id = :cId AND lm.game.id = g.id");

        TypedQuery<Long> query = entityManager.createQuery(stringBuilder.toString(), Long.class);
        query.setParameter("cId", conceptId);

        return query.getSingleResult();
    }

    public List<ConceptEntity> searchConcepts(final ConceptSearchRequest request) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT DISTINCT c FROM ConceptEntity c ");
        stringBuilder.append("LEFT JOIN LinkMapEntity lm ON c.id = lm.concept.id ");
        stringBuilder.append("LEFT JOIN GameEntity g ON lm.game.id = g.id ");
        stringBuilder.append("LEFT JOIN CharacterEntity ch ON lm.character.id = ch.id ");
        stringBuilder.append("WHERE 1 = 1 ");

        if (request.getName() != null && !request.getName().equals("")) {
            stringBuilder.append("AND LOWER(c.name) LIKE LOWER(CONCAT('%',:name,'%')) ");
        }
        if (request.getGameIds() != null) {
            stringBuilder.append("AND g.id IN :gameIds ");
        }
        if (request.getCharacterIds() != null) {
            stringBuilder.append("AND ch.id IN :characterIds ");
        }

        if (request.getSortBy() != null && request.getSortBy().equals("Alphabetical")) {
            stringBuilder.append("ORDER BY c.name");
        } else if (request.getSortBy() != null && request.getSortBy().equals("Last edit")) {
            stringBuilder.append("ORDER BY c.modified DESC");
        } else if (request.getSortBy() != null && request.getSortBy().equals("Most games")) {
            stringBuilder.append("");
        }

        TypedQuery<ConceptEntity> query = entityManager.createQuery(stringBuilder.toString(), ConceptEntity.class);

        if (request.getName() != null && !request.getName().equals("")) {
            query.setParameter("name", request.getName());
        }
        if (request.getGameIds() != null) {
            query.setParameter("gameIds", request.getGameIds());
        }
        if (request.getCharacterIds() != null) {
            query.setParameter("characterIds", request.getCharacterIds());
        }

        return query.getResultList();

    }

    public LocalDateTime getFirstReleaseDateByConcept(final Long conceptId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "SELECT r.releaseDate FROM LinkMapEntity lm JOIN GameEntity g ON lm.game.id = g.id JOIN ReleaseEntity r on g.id = r.game.id WHERE lm.concept.id = :cId ORDER BY r.releaseDate ASC");

        TypedQuery<LocalDateTime> query = entityManager.createQuery(stringBuilder.toString(), LocalDateTime.class);
        query.setParameter("cId", conceptId);

        return query.getResultList().isEmpty() ? null : query.getResultList().get(0);
    }

    public MediaStoreEntity getCoverByConcept(final Long conceptId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "SELECT mse FROM MediaStoreEntity mse, MediaEntity me WHERE me.id = mse.media.id AND me.objectId = :objectId AND me.objectType = :objectType AND mse.type = :type");

        TypedQuery<MediaStoreEntity> query = entityManager.createQuery(stringBuilder.toString(), MediaStoreEntity.class);
        query.setParameter("objectId", conceptId);
        query.setParameter("objectType", "CONCEPT");
        query.setParameter("type", "COVER_IMAGE");

        return query.getResultList().stream().findFirst().orElse(null);
    }

    public List<LoV> getLoVsNotConnectedTo(String field, String idField, Long id) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format(
                "SELECT DISTINCT c.id FROM ConceptEntity c LEFT OUTER JOIN LinkMapEntity lm ON lm.concept.id= c.id WHERE lm.%s.%s = :id",
                field, idField));

        TypedQuery<Long> listQuery = entityManager.createQuery(stringBuilder.toString(), Long.class);
        listQuery.setParameter("id", id);
        List<Long> list = listQuery.getResultList();

        stringBuilder.setLength(0);
        stringBuilder.append(String.format("SELECT new ba.com.zira.praksa.api.model.LoV(c.id, c.name) FROM ConceptEntity c %s",
                !list.isEmpty() ? "WHERE c.id NOT IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (!list.isEmpty()) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }

}
