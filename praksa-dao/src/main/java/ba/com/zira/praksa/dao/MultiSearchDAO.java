package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.praksa.api.model.MultiSearchResponse;
import ba.com.zira.praksa.dao.model.MultiSearchViewEntity;

@Repository
public class MultiSearchDAO extends AbstractDAO<MultiSearchViewEntity, Long> {

    public List<MultiSearchResponse> getResponseForMultiSearchForName(final SearchRequest<String> request) {
        String jqpl = "select new ba.com.zira.praksa.api.model.MultiSearchResponse(v.id, v.name, v.type) from MultiSearchViewEntity v where v.name like :searchTerm";
        TypedQuery<MultiSearchResponse> response = entityManager.createQuery(jqpl, MultiSearchResponse.class)
                .setParameter("searchTerm", "%" + request.getEntity() + "%")
                .setMaxResults(request.getFilter().getPaginationFilter().getEntitiesPerPage());
        return response.getResultList();
    }

    void refreshView() {
        String nativeQuery = "create or replace table HUW_MULTI_SEARCH as select   hg.id,   hg.full_name,"
                + "    'GAME' as \"type\" from   hut_game hg union select    hcc.id ,   hcc.\"name\",     'CHARACTER'"
                + "from   hut_character hcc union select   hc.id,   hc.\"name\",     'COMPANY' from   hut_company hc"
                + "union select   ho.id,   ho.\"name\" ,   'OBJECT' from   hut_object ho union"
                + "select   hc2.id,   hc2.\"name\" ,   'CONCEPT' from     hut_concept hc2 union"
                + "select   hl.id ,   hl.\"name\" ,   'LOCATION' from     hut_location hl union"
                + "select   hf.id ,   hf.\"name\" ,   'FRANCHISE' from     hut_franchise hf union"
                + "select   hr.id ,   hr.\"name\" ,   'REGION' from     hut_region hr union"
                + "select   hp.id ,   hp.full_name ,   'PLATFORM' from     hut_platform hp union"
                + "select   hp2.id ,   hp2.first_name || ' ' || hp2.last_name ,     'PERSON' from   hut_person hp2 union"
                + "select   hf.id ,   hf.\"name\" ,   'FEATURE' from   hut_feature hf;";
        Query q = entityManager.createNativeQuery(nativeQuery);
        q.executeUpdate();
    }

}
