package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
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

}