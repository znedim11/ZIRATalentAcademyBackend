package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.dao.model.CharacterEntity;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.LocationEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.PersonEntity;

@Repository
public class GameDAO extends AbstractDAO<GameEntity, Long> {

    public List<ConceptEntity> getConceptsByGame(Long gameId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT c FROM ConceptEntity c, LinkMapEntity lm WHERE lm.game.id = :gId AND lm.concept.id = c.id");

        TypedQuery<ConceptEntity> query = entityManager.createQuery(stringBuilder.toString(), ConceptEntity.class);
        query.setParameter("gId", gameId);

        return query.getResultList();
    }

    public List<PersonEntity> getPersonsByGame(Long gameId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT p FROM PersonEntity p, LinkMapEntity lm WHERE lm.game.id = :gId AND lm.person.id = p.id");

        TypedQuery<PersonEntity> query = entityManager.createQuery(stringBuilder.toString(), PersonEntity.class);
        query.setParameter("gId", gameId);

        return query.getResultList();
    }

    public List<LoV> getLoVs(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("SELECT new ba.com.zira.praksa.api.model.LoV(g.id, g.name) FROM GameEntity g %s",
                list != null ? "WHERE g.id IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (list != null) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }

    public List<ObjectEntity> getObjectsByGame(Long gameId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT o FROM ObjectEntity o, LinkMapEntity lm WHERE lm.game.id = :gId AND lm.object.id = o.id");

        TypedQuery<ObjectEntity> query = entityManager.createQuery(stringBuilder.toString(), ObjectEntity.class);
        query.setParameter("gId", gameId);

        return query.getResultList();
    }

    public List<CharacterEntity> getCharactersByGame(Long gameId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT c FROM CharacterEntity c, LinkMapEntity lm WHERE lm.game.id = :gId AND lm.character.id = c.id");

        TypedQuery<CharacterEntity> query = entityManager.createQuery(stringBuilder.toString(), CharacterEntity.class);
        query.setParameter("gId", gameId);

        return query.getResultList();
    }

    public List<LocationEntity> getLocationsByGame(Long gameId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT l FROM LocationEntity l, LinkMapEntity lm WHERE lm.game.id = :gId AND lm.location.id = l.id");

        TypedQuery<LocationEntity> query = entityManager.createQuery(stringBuilder.toString(), LocationEntity.class);
        query.setParameter("gId", gameId);

        return query.getResultList();
    }

    public Long getNumberOfReleasesByGame(Long gameId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT COUNT(r) FROM ReleaseEntity r WHERE r.game.id = :gId");

        TypedQuery<Long> query = entityManager.createQuery(stringBuilder.toString(), Long.class);
        query.setParameter("gId", gameId);

        return query.getSingleResult();
    }

    public PagedData<GameEntity> getGamesByFeature(final Long featureId) {
        String jpql = "SELECT g FROM GameEntity g, GameFeatureEntity gf WHERE gf.feature.id = :featureId AND gf.game.id = g.id";

        TypedQuery<GameEntity> query = entityManager.createQuery(jpql, GameEntity.class).setParameter("featureId", featureId);

        PagedData<GameEntity> gamesPagedData = new PagedData<>();
        gamesPagedData.setRecords(query.getResultList());

        return gamesPagedData;
    }

    public List<GameEntity> findbyFeatures(final List<Long> features) {
        String jpql = "SELECT DISTINCT g FROM GameEntity g, GameFeatureEntity gf WHERE gf.feature.id IN :ids AND gf.game.id = game.id";

        TypedQuery<GameEntity> query = entityManager.createQuery(jpql, GameEntity.class).setParameter("ids", features);

        return query.getResultList();
    }
}