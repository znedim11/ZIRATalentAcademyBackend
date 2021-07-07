package ba.com.zira.praksa.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.media.CoverImageHelper;
import ba.com.zira.praksa.dao.model.MediaStoreEntity;

@Repository
public class MediaStoreDAO extends AbstractDAO<MediaStoreEntity, String> {

    public List<String> getUrl(final Long objectId, final String objectType, final String type) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "SELECT mse.url FROM MediaStoreEntity mse, MediaEntity me WHERE me.id = mse.media.id AND me.objectId = :objectId AND me.objectType = :objectType AND mse.type = :type");

        TypedQuery<String> query = entityManager.createQuery(stringBuilder.toString(), String.class);
        query.setParameter("objectId", objectId);
        query.setParameter("objectType", objectType);
        query.setParameter("type", type);

        return query.getResultList();

    }

    public Map<Long, String> getUrlsForList(final List<Long> objectIds, final String objectType, final String type) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "SELECT new ba.com.zira.praksa.api.model.media.CoverImageHelper(me.objectId, me.objectType,mse.url) FROM MediaStoreEntity mse, MediaEntity me WHERE me.id = mse.media.id AND me.objectId IN :objectIds AND me.objectType = :objectType AND mse.type = :type");

        TypedQuery<CoverImageHelper> query = entityManager.createQuery(stringBuilder.toString(), CoverImageHelper.class);
        query.setParameter("objectIds", objectIds);
        query.setParameter("objectType", objectType);
        query.setParameter("type", type);

        return query.getResultList().stream().collect(Collectors.toMap(CoverImageHelper::getObjectId, CoverImageHelper::getUrl));

    }

}