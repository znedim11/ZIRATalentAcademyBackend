package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.ReleaseService;
import ba.com.zira.praksa.api.model.enums.TimeSegment;
import ba.com.zira.praksa.api.model.release.IntervalHelper;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.api.model.release.ReleaseResponseDetails;
import ba.com.zira.praksa.api.model.release.ReleasesByTimetableRequest;
import ba.com.zira.praksa.api.model.release.ReleasesByTimetableResponse;
import ba.com.zira.praksa.dao.CompanyDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.PlatformDAO;
import ba.com.zira.praksa.dao.RegionDAO;
import ba.com.zira.praksa.dao.ReleaseDAO;
import ba.com.zira.praksa.dao.model.ReleaseEntity;
import ba.com.zira.praksa.mapper.ReleaseMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReleaseServiceImpl implements ReleaseService {
    RequestValidator requestValidator;
    ReleaseMapper releaseMapper;
    ReleaseDAO releaseDAO;
    PlatformDAO platformDAO;
    CompanyDAO companyDAO;
    GameDAO gameDAO;
    RegionDAO regionDAO;

    @Override
    public PayloadResponse<String> addRelease(final EntityRequest<ReleaseRequest> request) throws ApiException {
        requestValidator.validate(request);
        ReleaseEntity entity = releaseMapper.dtoToEntity(request.getEntity());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());
        entity.setGame(gameDAO.findByPK(request.getEntity().getGameId()));
        entity.setRegion(regionDAO.findByPK(request.getEntity().getRegionId()));
        entity.setPlatform(platformDAO.findByPK(request.getEntity().getPlatformId()));

        if (request.getEntity().getDeveloperId() != null) {
            entity.setDeveloper(companyDAO.findByPK(request.getEntity().getDeveloperId()));
        } else if (request.getEntity().getPublisherId() != null) {
            entity.setPublisher(companyDAO.findByPK(request.getEntity().getPublisherId()));
        }

        releaseDAO.persist(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, "Release Added Successfully");
    }

    @Override
    public PayloadResponse<ReleasesByTimetableResponse> getReleasesByTimetable(EntityRequest<ReleasesByTimetableRequest> request)
            throws ApiException {
        requestValidator.validate(request);

        ReleasesByTimetableResponse releasesByTimetableResponse = new ReleasesByTimetableResponse();
        List<ReleaseEntity> listOfReleasesInRange = releaseDAO.getReleasesPerTimetable(request.getEntity().getStartDate(),
                request.getEntity().getEndDate());

        LocalDateTime localDate = LocalDateTime.now();
        LocalDateTime startDate = request.getEntity().getStartDate() == null ? localDate : request.getEntity().getStartDate();
        LocalDateTime endDate = request.getEntity().getEndDate() == null ? localDate : request.getEntity().getEndDate();

        List<IntervalHelper> helpers = createSplitHelpes(request.getEntity().getTimeSegment(), startDate, endDate);

        releasesByTimetableResponse.setMapOfReleasesByIntervals(mapReleaseEntitiesToIntervals(helpers, listOfReleasesInRange));
        releasesByTimetableResponse.setStartDate(startDate);
        releasesByTimetableResponse.setEndDate(endDate);

        return new PayloadResponse<>(request, ResponseCode.OK, releasesByTimetableResponse);
    }

    private Predicate<? super ReleaseEntity> isBetween(final LocalDateTime startOfSegment, final LocalDateTime endOfSegment) {
        return a -> (a.getReleaseDate().isAfter(startOfSegment) || a.getReleaseDate().isEqual(startOfSegment))
                && (a.getReleaseDate().isBefore(endOfSegment) || a.getReleaseDate().isEqual(endOfSegment));
    }

    private List<IntervalHelper> createSplitHelpes(final String typeOfSplit, final LocalDateTime startDate, final LocalDateTime endDate) {

        List<IntervalHelper> helpers = new ArrayList<>();
        Period diff = Period.between(startDate.toLocalDate().withDayOfMonth(1), endDate.toLocalDate().withDayOfMonth(1));

        createYearlyHelpers(typeOfSplit, startDate, endDate, helpers, diff);
        createMonthlyHelpers(typeOfSplit, startDate, endDate, helpers, diff);
        createQuartalHelpers(typeOfSplit, startDate, endDate, helpers, diff);

        Period diffWeek = Period.ofDays(7);
        if (TimeSegment.WEEK.getValue().equalsIgnoreCase(typeOfSplit)) {
            diff = Period.between(startDate.toLocalDate(), endDate.toLocalDate());

            if (diffWeek.getDays() == 0) {
                helpers.add(new IntervalHelper(startDate, endDate));
            } else {
                for (int i = 0; i < (diffWeek.getDays() + diff.getMonths() * 30 + diff.getYears() * 365 / 7); i++) {
                    helpers.add(new IntervalHelper(startDate.plusDays(i * 7), startDate.plusYears(i * 7 + 7)));
                }
            }
        }

        if (TimeSegment.ALLTIME.getValue().equalsIgnoreCase(typeOfSplit)) {
            helpers.add(new IntervalHelper(startDate, endDate));
        }

        return helpers;
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
            if (diff.getMonths() == 0) {
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
            if (diff.getMonths() == 0) {
                helpers.add(new IntervalHelper(startDate, endDate));
            } else {
                for (int i = 0; i < diff.getMonths() / 4 + diff.getYears() * 4; i++) {
                    helpers.add(new IntervalHelper(startDate.plusMonths(i), startDate.plusMonths(i + 3L)));
                }
            }
        }

    }

    private Map<IntervalHelper, List<ReleaseResponseDetails>> mapReleaseEntitiesToIntervals(List<IntervalHelper> intervals,
            List<ReleaseEntity> listOfReleaseEntities) {

        Map<IntervalHelper, List<ReleaseResponseDetails>> map = new LinkedHashMap<>();

        for (IntervalHelper interval : intervals) {

            LocalDateTime startOfSegment = interval.getStartOfSegment();
            LocalDateTime endOfSegment = interval.getEndOfSegment();

            List<ReleaseEntity> releasesTemp = listOfReleaseEntities.stream().filter(isBetween(startOfSegment, endOfSegment))
                    .collect(Collectors.toList());

            List<ReleaseResponseDetails> responseLightList = releaseMapper.entityListToDtoList(releasesTemp);

            interval.setReleaseCount(Long.valueOf(releasesTemp.size()));
            interval.setListOfReleases(responseLightList);
            map.put(interval, responseLightList);

        }

        return map;
    }
}
