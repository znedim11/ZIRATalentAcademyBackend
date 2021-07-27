package ba.com.zira.praksa.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.commons.model.Filter;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.PaginationFilter;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.game.GameCharacterResponse;
import ba.com.zira.praksa.api.model.game.GameSearchRequest;
import ba.com.zira.praksa.api.model.game.dlc.DlcGame;
import ba.com.zira.praksa.dao.model.CharacterEntity;
import ba.com.zira.praksa.dao.model.CompanyEntity;
import ba.com.zira.praksa.dao.model.CompanyEntity_;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.FeatureEntity;
import ba.com.zira.praksa.dao.model.FeatureEntity_;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.GameEntity_;
import ba.com.zira.praksa.dao.model.GameFeatureEntity;
import ba.com.zira.praksa.dao.model.GameFeatureEntity_;
import ba.com.zira.praksa.dao.model.LinkMapEntity;
import ba.com.zira.praksa.dao.model.LocationEntity;
import ba.com.zira.praksa.dao.model.MediaStoreEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.PersonEntity;
import ba.com.zira.praksa.dao.model.PlatformEntity;
import ba.com.zira.praksa.dao.model.RegionEntity;
import ba.com.zira.praksa.dao.model.RegionEntity_;
import ba.com.zira.praksa.dao.model.ReleaseEntity;
import ba.com.zira.praksa.dao.model.ReleaseEntity_;

@Repository
public class GameDAO extends AbstractDAO<GameEntity, Long> {

    LoVDAO loVDAO;

    public GameDAO(LoVDAO loVDAO) {
        super();
        this.loVDAO = loVDAO;
    }

    public List<GameCharacterResponse> getGamesForCharacter(final Long characterId) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT DISTINCT new ba.com.zira.praksa.api.model.game.GameCharacterResponse(g.id, g.fullName, p.code, r.releaseDate) "
                + "FROM GameEntity g ");
        jpql.append("LEFT JOIN ReleaseEntity r ON g.id = r.game.id ");
        jpql.append("LEFT JOIN PlatformEntity p ON r.platform.id = p.id ");
        jpql.append("JOIN LinkMapEntity lm ON g.id = lm.game.id ");
        jpql.append("WHERE lm.character.id = :characterId "
                + "AND (r.releaseDate IS NULL OR r.releaseDate = (SELECT MIN(r1.releaseDate) FROM ReleaseEntity r1 WHERE r1.game.id = g.id))");

        TypedQuery<GameCharacterResponse> query = entityManager.createQuery(jpql.toString(), GameCharacterResponse.class)
                .setParameter("characterId", characterId);

        List<GameCharacterResponse> games = query.getResultList();

        return games;
    }

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

    public PagedData<LoV> getLoVs(Filter filter) {
        CriteriaQuery<LoV> criteriaQuery = builder.createQuery(LoV.class);
        Root<GameEntity> root = criteriaQuery.from(GameEntity.class);

        criteriaQuery.multiselect(root.get(GameEntity_.id), root.get(GameEntity_.fullName))
                .orderBy(builder.asc(root.get(GameEntity_.fullName)));

        loVDAO.handleFilterExpressions(filter, criteriaQuery);
        return loVDAO.handlePaginationFilter(filter, criteriaQuery, GameEntity.class);
    }

    public List<LoV> getMainGames() {
        String jpql = "SELECT new ba.com.zira.praksa.api.model.LoV(g1.id, g1.fullName) FROM GameEntity g1 where g1.dlc='0' order by 2";
        TypedQuery<LoV> mainGames = entityManager.createQuery(jpql, LoV.class);
        return mainGames.getResultList();
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

    public List<ReleaseEntity> getFirstReleaseByGame(Long gameId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT r FROM ReleaseEntity r WHERE r.game.id = :gId ORDER BY r.releaseDate");

        TypedQuery<ReleaseEntity> query = entityManager.createQuery(stringBuilder.toString(), ReleaseEntity.class);
        query.setParameter("gId", gameId);

        return query.getResultList();
    }

    public List<PlatformEntity> getPlatformsByGame(Long gameId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT DISTINCT p FROM ReleaseEntity r JOIN PlatformEntity p ON r.platform.id = p.id WHERE r.game.id = :gId");

        TypedQuery<PlatformEntity> query = entityManager.createQuery(stringBuilder.toString(), PlatformEntity.class);
        query.setParameter("gId", gameId);

        return query.getResultList();
    }

    public Map<Long, String> getGameName(final List<Long> ids) {
        String jpql = "select new ba.com.zira.praksa.api.model.LoV(g.id, g.fullName) from GameEntity g where g.id in :ids";
        TypedQuery<LoV> query = entityManager.createQuery(jpql, LoV.class).setParameter("ids", ids);
        List<LoV> lovs = query.getResultList();
        return lovs.stream().collect(Collectors.toMap(LoV::getId, LoV::getName));
    }

    public List<DlcGame> getDlcGames(String dlc) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT new ba.com.zira.praksa.api.model.game.dlc.DlcGame(game.id, game.fullName, ");
        jpql.append("(SELECT COUNT(*) FROM GameEntity dlc WHERE dlc.parentGame = game.id), MIN(r.releaseDate)) ");
        jpql.append("FROM GameEntity game ");
        jpql.append("LEFT JOIN ReleaseEntity r ON game.id = r.game.id ");
        jpql.append("WHERE game.dlc like :dlc AND (SELECT COUNT(*) FROM GameEntity dlc WHERE dlc.parentGame = game.id) <> 0 ");
        jpql.append("GROUP BY 1");

        TypedQuery<DlcGame> query = entityManager.createQuery(jpql.toString(), DlcGame.class);
        query.setParameter("dlc", dlc);

        return query.getResultList();
    }

    public Long getCountOfDlcs() {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT COUNT(*) ");
        jpql.append("FROM GameEntity game ");
        jpql.append("WHERE game.dlc like '1' AND game.parentGame IS NOT NULL");

        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
        return query.getSingleResult();
    }

    public MediaStoreEntity getCoverByGame(final Long gameId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "SELECT mse FROM MediaStoreEntity mse, MediaEntity me WHERE me.id = mse.media.id AND me.objectId = :objectId AND me.objectType = :objectType AND mse.type = :type");

        TypedQuery<MediaStoreEntity> query = entityManager.createQuery(stringBuilder.toString(), MediaStoreEntity.class);
        query.setParameter("objectId", gameId);
        query.setParameter("objectType", "GAME");
        query.setParameter("type", "COVER_IMAGE");

        return query.getResultList().stream().findFirst().orElse(null);
    }

    public PagedData<LoV> getLoVsNotConnectedTo(Filter filter, String field, String idField, Long id) {
        CriteriaQuery<LoV> criteriaQuery = builder.createQuery(LoV.class);
        Root<GameEntity> root = criteriaQuery.from(GameEntity.class);

        CriteriaQuery<Long> subquery = builder.createQuery(Long.class);
        Root<GameEntity> subqueryRoot = subquery.from(GameEntity.class);
        Join<GameEntity, LinkMapEntity> subqueryJoin = subqueryRoot.join(GameEntity_.linkMaps);

        subquery.where(builder.equal(subqueryJoin.get(field).get(idField), id)).select(subqueryRoot.get(GameEntity_.id));

        List<Long> list = entityManager.createQuery(subquery).getResultList();

        if (!list.isEmpty()) {
            criteriaQuery.where(builder.not(root.get(GameEntity_.id).in(list)));
        }

        criteriaQuery.multiselect(root.get(GameEntity_.id), root.get(GameEntity_.fullName))
                .orderBy(builder.asc(root.get(GameEntity_.fullName)));

        loVDAO.handleFilterExpressions(filter, criteriaQuery);
        return loVDAO.handlePaginationFilter(filter, criteriaQuery, GameEntity.class);

    }

    public PagedData<GameEntity> searchGames(Filter filter, GameSearchRequest entity) {
        CriteriaQuery<GameEntity> criteriaQuery = builder.createQuery(GameEntity.class);
        Root<GameEntity> root = criteriaQuery.from(GameEntity.class);
        Join<GameEntity, ReleaseEntity> releaseJoin = root.join(GameEntity_.releases, JoinType.LEFT);
        Join<ReleaseEntity, RegionEntity> regionJoin = releaseJoin.join(ReleaseEntity_.region);
        Join<ReleaseEntity, CompanyEntity> developerJoin = releaseJoin.join(ReleaseEntity_.developer);
        Join<ReleaseEntity, CompanyEntity> publisherJoin = releaseJoin.join(ReleaseEntity_.publisher);
        Join<GameEntity, GameFeatureEntity> gameFeatureJoin = root.join(GameEntity_.gameFeatures, JoinType.LEFT);
        Join<GameFeatureEntity, FeatureEntity> featureJoin = gameFeatureJoin.join(GameFeatureEntity_.feature, JoinType.LEFT);

        Predicate namePredicate = builder.and();
        Predicate genrePredicate = builder.and();

        if (entity.getName() != null && !entity.getName().equals("")) {
            namePredicate = builder.like(builder.lower(root.get(GameEntity_.fullName)),
                    String.format("%%%s%%", entity.getName()).toLowerCase());
        }
        if (entity.getGenre() != null && !entity.getGenre().equals("")) {
            genrePredicate = builder.like(builder.lower(root.get(GameEntity_.genre)),
                    String.format("%%%s%%", entity.getGenre()).toLowerCase());
        }
        if (entity.getReleasedAfter() != null && !entity.getReleasedAfter().equals("")) {
            Predicate releasedAfterPredicate = builder.greaterThanOrEqualTo(releaseJoin.get(ReleaseEntity_.releaseDate),
                    LocalDate.parse(entity.getReleasedAfter(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay());
            releaseJoin.on(releasedAfterPredicate);
        }
        if (entity.getReleasedBefore() != null && !entity.getReleasedBefore().equals("")) {
            Predicate releasedBeforePredicate = builder.lessThanOrEqualTo(releaseJoin.get(ReleaseEntity_.releaseDate),
                    LocalDate.parse(entity.getReleasedBefore(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay());
            releaseJoin.on(releasedBeforePredicate);
        }
        if (entity.getRegionIds() != null && !entity.getRegionIds().isEmpty()) {
            Predicate regionPredicate = regionJoin.get(RegionEntity_.id).in(entity.getRegionIds());
            regionJoin.on(regionPredicate);
        }
        if (entity.getFeatureIds() != null && !entity.getFeatureIds().isEmpty()) {
            Predicate featurePredicate = featureJoin.get(FeatureEntity_.id).in(entity.getFeatureIds());
            featureJoin.on(featurePredicate);
        }
        if (entity.getDeveloperId() != null) {
            Predicate developerPredicate = builder.equal(developerJoin.get(CompanyEntity_.id), entity.getDeveloperId());
            developerJoin.on(developerPredicate);
        }
        if (entity.getPublisherId() != null) {
            Predicate publisherPredicate = builder.equal(publisherJoin.get(CompanyEntity_.id), entity.getPublisherId());
            publisherJoin.on(publisherPredicate);
        }

        criteriaQuery.where(builder.and(namePredicate, genrePredicate));
        criteriaQuery.select(root).orderBy(builder.asc(root.get(GameEntity_.fullName))).distinct(true);

        root.alias("query");

        return handlePaginationFilterGame(filter, criteriaQuery);
    }

    public List<String> getGenres() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT DISTINCT g.genre FROM GameEntity g WHERE g.genre IS NOT NULL");

        TypedQuery<String> query = entityManager.createQuery(stringBuilder.toString(), String.class);

        return query.getResultList();
    }

    public PagedData<GameEntity> handlePaginationFilterGame(final Filter filter, final CriteriaQuery<GameEntity> criteriaQuery) {
        TypedQuery<GameEntity> query = entityManager.createQuery(criteriaQuery);

        PagedData<GameEntity> pagedData = new PagedData<>();
        int numberOfRecords = countAllGame(criteriaQuery);

        if (filter != null) {
            PaginationFilter paginationFilter = filter.getPaginationFilter();
            if (paginationFilter != null && paginationFilter.getPage() >= 0 && paginationFilter.getEntitiesPerPage() > 0) {
                pagedData.setPage(paginationFilter.getPage());
                pagedData.setRecordsPerPage(paginationFilter.getEntitiesPerPage());
                pagedData.setNumberOfPages((int) Math.ceil((float) numberOfRecords / paginationFilter.getEntitiesPerPage()));
                pagedData.setNumberOfRecords(numberOfRecords);
                query.setFirstResult((pagedData.getPage() - 1) * pagedData.getRecordsPerPage());
                query.setMaxResults(pagedData.getRecordsPerPage());
            } else {
                pagedData.setPage(1);
                pagedData.setRecordsPerPage(numberOfRecords);
                pagedData.setNumberOfPages(1);
                pagedData.setNumberOfRecords(numberOfRecords);
            }
        } else {
            pagedData.setPage(1);
            pagedData.setRecordsPerPage(numberOfRecords);
            pagedData.setNumberOfPages(1);
            pagedData.setNumberOfRecords(numberOfRecords);
        }
        pagedData.setRecords(query.getResultList());
        return pagedData;
    }

    public int countAllGame(final CriteriaQuery<GameEntity> criteriaQuery) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<?> root = countQuery.from(criteriaQuery.getResultType());

        root.alias("query");
        countQuery.select(builder.count(root));

        Predicate restriction = criteriaQuery.getRestriction();

        if (restriction != null) {
            countQuery.where(restriction);
        }

        return entityManager.createQuery(countQuery).getSingleResult().intValue();
    }
}
