package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.ReleaseService;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.enums.TimeSegment;
import ba.com.zira.praksa.api.model.release.IntervalHelper;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.api.model.release.ReleaseResponse;
import ba.com.zira.praksa.api.model.release.ReleaseResponseDetails;
import ba.com.zira.praksa.api.model.release.ReleaseResponseLight;
import ba.com.zira.praksa.api.model.release.ReleasesByTimetableRequest;
import ba.com.zira.praksa.api.model.release.ReleasesByTimetableResponse;
import ba.com.zira.praksa.core.utils.LookupService;
import ba.com.zira.praksa.core.validation.ReleaseRequestValidation;
import ba.com.zira.praksa.dao.CompanyDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.PlatformDAO;
import ba.com.zira.praksa.dao.RegionDAO;
import ba.com.zira.praksa.dao.ReleaseDAO;
import ba.com.zira.praksa.dao.model.ReleaseEntity;
import ba.com.zira.praksa.mapper.ReleaseMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReleaseServiceImpl implements ReleaseService {
    RequestValidator requestValidator;
    ReleaseRequestValidation releaseRequestValidation;
    ReleaseMapper releaseMapper;
    ReleaseDAO releaseDAO;
    PlatformDAO platformDAO;
    CompanyDAO companyDAO;
    GameDAO gameDAO;
    RegionDAO regionDAO;
    LookupService lookupService;

    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";
    static final String BASIC_NOT_NULL = "basicNotNull";

    public ReleaseServiceImpl(RequestValidator requestValidator, ReleaseRequestValidation releaseRequestValidation,
            ReleaseMapper releaseMapper, ReleaseDAO releaseDAO, PlatformDAO platformDAO, CompanyDAO companyDAO, GameDAO gameDAO,
            RegionDAO regionDAO, LookupService lookupService) {
        super();
        this.requestValidator = requestValidator;
        this.releaseRequestValidation = releaseRequestValidation;
        this.releaseMapper = releaseMapper;
        this.releaseDAO = releaseDAO;
        this.companyDAO = companyDAO;
        this.gameDAO = gameDAO;
        this.platformDAO = platformDAO;
        this.regionDAO = regionDAO;
        this.lookupService = lookupService;
    }

    @Override
    public PagedPayloadResponse<ReleaseResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<ReleaseEntity> releaseEntities = releaseDAO.findAll(request.getFilter());
        final List<ReleaseEntity> releaseEntityList = releaseEntities.getRecords();

        final List<ReleaseResponse> releasesList = releaseMapper.entityListToResponseList(releaseEntityList);

        PagedData<ReleaseResponse> data = new PagedData<>();
        data.setNumberOfPages(releaseEntities.getNumberOfPages());
        data.setRecords(releasesList);
        data.setNumberOfRecords(releaseEntities.getNumberOfRecords());
        data.setPage(releaseEntities.getPage());
        data.setRecordsPerPage(releaseEntities.getRecordsPerPage());
        data.setHasMoreRecords(releaseEntities.hasMoreRecords());
        return new PagedPayloadResponse<>(request, ResponseCode.OK, data);
    }

    @Override
    public PayloadResponse<ReleaseResponseLight> findByUuid(final EntityRequest<String> request) throws ApiException {
        releaseRequestValidation.validateReleaseRequest(request, VALIDATE_ABSTRACT_REQUEST);

        final ReleaseEntity releaseEntity = releaseDAO.findByPK(request.getEntity());

        return new PayloadResponse<>(request, ResponseCode.OK, releaseMapper.releaseEntityToRelease(releaseEntity));
    }

    @Override
    public PayloadResponse<String> addRelease(final EntityRequest<ReleaseRequest> request) throws ApiException {
        requestValidator.validate(request);
        releaseRequestValidation.validateEntityExistsInRequest(request, VALIDATE_ABSTRACT_REQUEST);
        releaseRequestValidation.validateRequiredFields(request, BASIC_NOT_NULL);

        ReleaseEntity entity = new ReleaseEntity();
        entity.setUuid(UUID.randomUUID().toString());

        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());
        entity.setRegion(regionDAO.findByPK(request.getEntity().getRegionId()));
        entity.setType(request.getEntity().getType());
        entity.setReleaseDate(LocalDateTime.parse(request.getEntity().getReleaseDate()));
        if (request.getEntity().getGameId() != null) {
            entity.setGame(gameDAO.findByPK(request.getEntity().getGameId()));
        }

        if (request.getEntity().getPlatformId() != null) {
            entity.setPlatform(platformDAO.findByPK(request.getEntity().getPlatformId()));
        }

        if (request.getEntity().getDeveloperId() != null) {
            entity.setDeveloper(companyDAO.findByPK(request.getEntity().getDeveloperId()));
        }
        if (request.getEntity().getPublisherId() != null) {
            entity.setPublisher(companyDAO.findByPK(request.getEntity().getPublisherId()));
        }

        releaseDAO.merge(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, "Release Added Successfully");
    }

    @Override
    public PayloadResponse<ReleasesByTimetableResponse> getReleasesByTimetable(final EntityRequest<ReleasesByTimetableRequest> request)
            throws ApiException {
        EntityRequest<ReleasesByTimetableRequest> entityRequest = new EntityRequest<>(request.getEntity(), request);
        releaseRequestValidation.validateReleaseByTimetableRequest(entityRequest, VALIDATE_ABSTRACT_REQUEST);
        releaseRequestValidation.validateDatesInRequest(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        ReleasesByTimetableResponse releasesByTimetableResponse = new ReleasesByTimetableResponse();

        LocalDateTime localDate = LocalDateTime.now();
        LocalDateTime startDate = request.getEntity().getStartDate() == null ? localDate : request.getEntity().getStartDate();
        LocalDateTime endDate = request.getEntity().getEndDate() == null ? localDate : request.getEntity().getEndDate();
        String releaseType = request.getEntity().getReleaseType();

        List<ReleaseEntity> listOfReleasesInRange = releaseDAO.getReleasesPerTimetable(startDate, endDate, releaseType);

        List<IntervalHelper> helpers = createSplitHelpers(request.getEntity().getTimeSegment(), startDate, endDate);

        releasesByTimetableResponse.setMapOfReleasesByIntervals(mapReleaseEntitiesToIntervals(helpers, listOfReleasesInRange));
        releasesByTimetableResponse.setStartDate(startDate);
        releasesByTimetableResponse.setEndDate(endDate);

        return new PayloadResponse<>(request, ResponseCode.OK, releasesByTimetableResponse);
    }

    private Predicate<? super ReleaseEntity> isBetween(final LocalDateTime startOfSegment, final LocalDateTime endOfSegment) {
        return a -> (a.getReleaseDate().isAfter(startOfSegment) || a.getReleaseDate().isEqual(startOfSegment))
                && (a.getReleaseDate().isBefore(endOfSegment) || a.getReleaseDate().isEqual(endOfSegment));
    }

    private List<IntervalHelper> createSplitHelpers(final String typeOfSplit, final LocalDateTime startDate, final LocalDateTime endDate) {

        List<IntervalHelper> helpers = new ArrayList<>();
        Period diff = Period.between(startDate.toLocalDate().withDayOfMonth(1), endDate.toLocalDate().withDayOfMonth(1));
        Period diffWeek = Period.ofDays(7);

        createWeeklyHelpers(typeOfSplit, startDate, endDate, helpers, diffWeek, diff);
        createMonthlyHelpers(typeOfSplit, startDate, endDate, helpers, diff);
        createQuartalHelpers(typeOfSplit, startDate, endDate, helpers, diff);
        createYearlyHelpers(typeOfSplit, startDate, endDate, helpers, diff);
        createAlltimeHelper(typeOfSplit, startDate, endDate, helpers);

        return helpers;
    }

    private void createAlltimeHelper(final String typeOfSplit, final LocalDateTime startDate, final LocalDateTime endDate,
            final List<IntervalHelper> helpers) {
        if (TimeSegment.ALLTIME.getValue().equalsIgnoreCase(typeOfSplit)) {
            helpers.add(new IntervalHelper(startDate, endDate));
        }
    }

    private void createWeeklyHelpers(final String typeOfSplit, final LocalDateTime startDate, final LocalDateTime endDate,
            final List<IntervalHelper> helpers, final Period diffWeek, final Period diff) {
        if (TimeSegment.WEEK.getValue().equalsIgnoreCase(typeOfSplit)) {
            if (diffWeek.getDays() == 0) {
                helpers.add(new IntervalHelper(startDate, endDate));
            } else {
                for (int i = 0; i < (diffWeek.getDays() + diff.getMonths() * 30 + diff.getYears() * 365) / 7; i++) {
                    helpers.add(new IntervalHelper(startDate.plusDays(i * 7L), startDate.plusDays(7 * i + 7L)));
                }
            }
        }

    }

    private void createYearlyHelpers(final String typeOfSplit, final LocalDateTime startDate, final LocalDateTime endDate,
            final List<IntervalHelper> helpers, final Period diff) {

        if (TimeSegment.YEAR.getValue().equalsIgnoreCase(typeOfSplit)) {
            if (diff.getYears() == 0) {
                helpers.add(new IntervalHelper(startDate, endDate));
            } else {
                for (int i = 0; i < diff.getYears(); i++) {
                    helpers.add(new IntervalHelper(startDate.plusYears(i), startDate.plusYears(i + 1L)));
                }
            }
        }
    }

    private void createMonthlyHelpers(final String typeOfSplit, final LocalDateTime startDate, final LocalDateTime endDate,
            final List<IntervalHelper> helpers, final Period diff) {
        if (TimeSegment.MONTH.getValue().equalsIgnoreCase(typeOfSplit)) {
            if (diff.getMonths() < 0) {
                helpers.add(new IntervalHelper(startDate, endDate));
            } else {
                for (int i = 0; i < diff.getMonths() + diff.getYears() * 12; i++) {
                    helpers.add(new IntervalHelper(startDate.plusMonths(i), startDate.plusMonths(i + 1L)));
                }
            }
        }

    }

    private void createQuartalHelpers(final String typeOfSplit, final LocalDateTime startDate, final LocalDateTime endDate,
            final List<IntervalHelper> helpers, final Period diff) {
        if (TimeSegment.QUARTAL.getValue().equalsIgnoreCase(typeOfSplit)) {
            if (diff.getMonths() < 0) {
                helpers.add(new IntervalHelper(startDate, endDate));
            } else {
                for (int i = 0; i < (diff.getMonths() / 4 + diff.getYears() * 12); i += 3) {
                    helpers.add(new IntervalHelper(startDate.plusMonths(i), startDate.plusMonths(i + 3L)));
                }
            }
        }

    }

    private Map<String, List<ReleaseResponseDetails>> mapReleaseEntitiesToIntervals(List<IntervalHelper> intervals,
            List<ReleaseEntity> listOfReleaseEntities) throws ApiException {

        Map<String, List<ReleaseResponseDetails>> map = new LinkedHashMap<>();

        for (IntervalHelper interval : intervals) {

            LocalDateTime startOfSegment = interval.getStartOfSegment();
            LocalDateTime endOfSegment = interval.getEndOfSegment();

            List<ReleaseEntity> releasesTemp = listOfReleaseEntities.stream().filter(isBetween(startOfSegment, endOfSegment))
                    .collect(Collectors.toList());

            List<ReleaseResponseDetails> responseList = releaseMapper.entityListToDtoList(releasesTemp);
            lookupService.lookupCoverImage(responseList, ReleaseResponseDetails::getGameId, ObjectType.GAME.getValue(),
                    ReleaseResponseDetails::setImageUrl, ReleaseResponseDetails::getImageUrl);
            interval.setReleaseCount(Long.valueOf(releasesTemp.size()));

            map.put(interval.toString(), responseList);

        }

        return map;
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> delete(EntityRequest<String> request) throws ApiException {
        EntityRequest<String> entityRequest = new EntityRequest<>(request.getEntity(), request);
        releaseRequestValidation.validateReleaseRequest(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        releaseDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Release deleted!");
    }

}
