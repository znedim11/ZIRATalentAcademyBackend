package ba.com.zira.praksa.core.impl;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EmptyRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.MultiSearchService;
import ba.com.zira.praksa.api.model.MultiSearchResponse;
import ba.com.zira.praksa.api.model.WikiStatsResponse;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.core.utils.LookupService;
import ba.com.zira.praksa.dao.MultiSearchDAO;

@Service
public class MultiSearchServiceImpl implements MultiSearchService {

    MultiSearchDAO multiSearchDAO;

    LookupService lookupService;

    public MultiSearchServiceImpl(MultiSearchDAO multiSearchDAO, LookupService lookupService) {
        super();
        this.multiSearchDAO = multiSearchDAO;
        this.lookupService = lookupService;
    }

    @Override
    public PagedPayloadResponse<MultiSearchResponse> findByName(SearchRequest<String> request) throws ApiException {
        List<MultiSearchResponse> responses = multiSearchDAO.getResponseForMultiSearchForName(request);
        lookupService.lookupCoverImage(responses.stream().filter(isType(ObjectType.GAME)).collect(Collectors.toList()),
                MultiSearchResponse::getObjectId, ObjectType.GAME.getValue(), MultiSearchResponse::setImageUrl,
                MultiSearchResponse::getImageUrl);
        lookupService.lookupCoverImage(responses.stream().filter(isType(ObjectType.GAME)).collect(Collectors.toList()),
                MultiSearchResponse::getObjectId, ObjectType.COMPANY.getValue(), MultiSearchResponse::setImageUrl,
                MultiSearchResponse::getImageUrl);
        lookupService.lookupCoverImage(responses.stream().filter(isType(ObjectType.PLATFORM)).collect(Collectors.toList()),
                MultiSearchResponse::getObjectId, ObjectType.PLATFORM.getValue(), MultiSearchResponse::setImageUrl,
                MultiSearchResponse::getImageUrl);
        lookupService.lookupCoverImage(responses.stream().filter(isType(ObjectType.OBJECT)).collect(Collectors.toList()),
                MultiSearchResponse::getObjectId, ObjectType.OBJECT.getValue(), MultiSearchResponse::setImageUrl,
                MultiSearchResponse::getImageUrl);
        lookupService.lookupCoverImage(responses.stream().filter(isType(ObjectType.PERSON)).collect(Collectors.toList()),
                MultiSearchResponse::getObjectId, ObjectType.PERSON.getValue(), MultiSearchResponse::setImageUrl,
                MultiSearchResponse::getImageUrl);
        lookupService.lookupCoverImage(responses.stream().filter(isType(ObjectType.LOCATION)).collect(Collectors.toList()),
                MultiSearchResponse::getObjectId, ObjectType.LOCATION.getValue(), MultiSearchResponse::setImageUrl,
                MultiSearchResponse::getImageUrl);
        lookupService.lookupCoverImage(responses.stream().filter(isType(ObjectType.CONCEPT)).collect(Collectors.toList()),
                MultiSearchResponse::getObjectId, ObjectType.CONCEPT.getValue(), MultiSearchResponse::setImageUrl,
                MultiSearchResponse::getImageUrl);
        return new PagedPayloadResponse<>(request, ResponseCode.OK, responses.size(), 1, 1, responses.size(), responses);

    }

    private Predicate<? super MultiSearchResponse> isType(ObjectType game) {
        return s -> game.getValue().equalsIgnoreCase(s.getObjectType());
    }

    @Override
    public ListPayloadResponse<WikiStatsResponse> findWikiStats(EmptyRequest request) {
        List<WikiStatsResponse> responses = multiSearchDAO.findWikiStats();
        return new ListPayloadResponse<>(request, ResponseCode.OK, responses);

    }

}
