package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.ReleaseService;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
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

    // @Override
    // public PayloadResponse<GameReleaseResponse>
    // getReleasesPerTimetable(String startDate, String endDate, String segment)
    // throws ApiException {

    // List<ReleaseEntity> releaseEntityList =
    // releaseDAO.getReleasesPerTimetable(startDate, endDate).stream().sorted()
    // .collect(Collectors.toList());
    //
    // LocalDate startDate1 = LocalDate.parse(startDate);
    // LocalDate endDate1 = LocalDate.parse(endDate);
    //
    // List<String> listOfSegments = divideRangeToSegments(startDate1, endDate1,
    // segment);
    // Map<String, List<ReleaseEntity>> mapOfEntitiesBySegments =
    // mapEntitiesToSegments(releaseEntityList, listOfSegments);
    //
    // return new PayloadResponse<>();
    //
    // }
    //
    // // helper functions to divide date range into intervals and map entities
    // to
    // // intervals
    //
    // public List<String> divideRangeToSegments(LocalDate startDate, LocalDate
    // endDate, String segment) {
    //
    // List<String> segmentList = new ArrayList<>();
    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    //
    // switch (segment) {
    //
    // case "WEEK":
    // LocalDate firstMonday =
    // startDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    // LocalDate lastSunday =
    // endDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    // while (firstMonday.compareTo(lastSunday) <= 0) {
    // LocalDate startWeek = firstMonday;
    // LocalDate endWeek = startWeek.plusDays(6);
    // firstMonday = endWeek.plusDays(1);
    // segmentList.add(startWeek.format(formatter) + "-" +
    // endWeek.format(formatter));
    // }
    // break;
    //
    // case "MONTH":
    // LocalDate firstDayOfMonth =
    // startDate.with(TemporalAdjusters.firstDayOfMonth());
    // LocalDate lastDayOfEndMonth =
    // endDate.with(TemporalAdjusters.lastDayOfMonth());
    // while (firstDayOfMonth.compareTo(lastDayOfEndMonth) <= 0) {
    // LocalDate monthStart = firstDayOfMonth;
    // LocalDate monthEnd =
    // monthStart.plusDays(monthStart.lengthOfMonth()).minusDays(1);
    // firstDayOfMonth = monthEnd.plusDays(1);
    // segmentList.add(monthStart.format(formatter) + "-" +
    // monthEnd.format(formatter));
    //
    // }
    // break;
    //
    // case "YEAR":
    // LocalDate firstDayOfYear =
    // startDate.with(TemporalAdjusters.firstDayOfYear());
    // LocalDate lastDayOfLastYear =
    // endDate.with(TemporalAdjusters.lastDayOfYear());
    // while (firstDayOfYear.compareTo(lastDayOfLastYear) <= 0) {
    // LocalDate yearStart = firstDayOfYear;
    // LocalDate yearEnd = yearStart.plusYears(1).minusDays(1);
    // firstDayOfYear = yearEnd.plusDays(1);
    // segmentList.add(yearStart.format(formatter) + "-" +
    // yearEnd.format(formatter));
    // }
    //
    // break;
    //
    // case "QUARTER":
    //
    // // TO-DO
    //
    // break;
    //
    // case "ALLTIME":
    //
    // if (startDate.compareTo(endDate) <= 0) {
    // segmentList.add(startDate.format(formatter) + "-" +
    // endDate.format(formatter));
    // }
    // break;
    // default:
    // }
    //
    // return segmentList;
    //
    // }
    //
    // public Map<String, List<ReleaseEntity>>
    // mapEntitiesToSegments(List<ReleaseEntity> list, List<String> segmentList)
    // {
    //
    // List<ReleaseEntity> listOfEntities = new ArrayList<>();
    // Map<String, List<ReleaseEntity>> mapOfSegments = new HashMap<>();
    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    //
    // ENTITY: for (ReleaseEntity releaseEntity : list) {
    // LocalDate releaseDate = releaseEntity.getReleaseDate().toLocalDate();
    //
    // for (String range : segmentList) {
    // String[] se = range.split("-");
    // LocalDate start = LocalDate.parse(se[0], formatter);
    // LocalDate end = LocalDate.parse(se[1], formatter);
    //
    // if ((releaseDate.compareTo(start) >= 0) && (releaseDate.compareTo(end) <=
    // 0)) {
    // if (!mapOfSegments.containsKey(range)) {
    // listOfEntities = new ArrayList<>();
    //
    // }
    // listOfEntities.add(releaseEntity);
    // continue ENTITY;
    // }
    //
    // mapOfSegments.put(range, listOfEntities);
    //
    // }
    //
    // }
    // return mapOfSegments;
    // }

}
