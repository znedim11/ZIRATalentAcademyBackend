package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import ba.com.zira.praksa.api.CompanyService;
import ba.com.zira.praksa.api.MediaService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.company.CompanyCreateRequest;
import ba.com.zira.praksa.api.model.company.CompanyResponse;
import ba.com.zira.praksa.api.model.company.CompanySearchRequest;
import ba.com.zira.praksa.api.model.company.CompanySearchResponse;
import ba.com.zira.praksa.api.model.company.CompanyUpdateRequest;
import ba.com.zira.praksa.api.model.company.report.CompanyRegionPlatform;
import ba.com.zira.praksa.api.model.company.report.CompanyRegionPlatformRequest;
import ba.com.zira.praksa.api.model.company.report.CompanyRegionPlatformResponse;
import ba.com.zira.praksa.api.model.company.report.CompanyRegionPlatformResponseDetail;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.media.CreateMediaRequest;
import ba.com.zira.praksa.core.utils.LookupService;
import ba.com.zira.praksa.dao.CompanyDAO;
import ba.com.zira.praksa.dao.PlatformDAO;
import ba.com.zira.praksa.dao.RegionDAO;
import ba.com.zira.praksa.dao.model.CompanyEntity;
import ba.com.zira.praksa.mapper.CompanyMapper;

@Service
public class CompanyServiceImpl implements CompanyService {

    private RequestValidator requestValidator;
    private CompanyDAO companyDAO;
    private RegionDAO regionDAO;
    private PlatformDAO platformDAO;
    private CompanyMapper companyMapper;
    private MediaService mediaService;
    private LookupService lookupService;

    public CompanyServiceImpl(final RequestValidator requestValidator, CompanyDAO companyDAO, RegionDAO regionDAO, PlatformDAO platformDAO,
            CompanyMapper companyMapper, LookupService lookupService) {
        this.requestValidator = requestValidator;
        this.companyDAO = companyDAO;
        this.regionDAO = regionDAO;
        this.platformDAO = platformDAO;
        this.companyMapper = companyMapper;
        this.lookupService = lookupService;
    }

    @Override
    public PagedPayloadResponse<CompanyResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<CompanyEntity> companyModelEntities = companyDAO.findAll(request.getFilter());
        final List<CompanyResponse> companyList = new ArrayList<>();

        for (final CompanyEntity CompanyEntity : companyModelEntities.getRecords()) {
            companyList.add(companyMapper.entityToDto(CompanyEntity));
        }
        return new PagedPayloadResponse<>(request, ResponseCode.OK, companyList.size(), 1, 1, companyList.size(), companyList);
    }

    @Override
    public PayloadResponse<CompanyResponse> create(EntityRequest<CompanyCreateRequest> request) throws ApiException {
        requestValidator.validate(request);

        CompanyEntity entity = companyMapper.dtoToEntity(request.getEntity());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());
        companyDAO.persist(entity);

        if (request.getEntity().getImageCreateRequest() != null && request.getEntity().getImageCreateRequest().getImageData() != null
                && request.getEntity().getImageCreateRequest().getImageName() != null) {
            CreateMediaRequest mediaRequest = new CreateMediaRequest(ObjectType.COMPANY.getValue(), entity.getId(),
                    request.getEntity().getImageCreateRequest().getImageData(), request.getEntity().getImageCreateRequest().getImageName(),
                    "IMAGE", "COVER_IMAGE");

            mediaService.saveMedia(new EntityRequest<>(mediaRequest, request));
        }

        CompanyResponse response = companyMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<CompanyResponse> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final CompanyEntity companyEntity = companyDAO.findByPK(request.getEntity());

        final CompanyResponse company = companyMapper.entityToDto(companyEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, company);
    }

    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<CompanyResponse> update(final EntityRequest<CompanyUpdateRequest> request) throws ApiException {
        requestValidator.validate(request);

        CompanyEntity existingCompanyEntity = companyDAO.findByPK(request.getEntity().getId());

        final LocalDateTime date = LocalDateTime.now();
        final CompanyUpdateRequest company = request.getEntity();

        companyMapper.updateForCompanyUpdate(company, existingCompanyEntity);

        existingCompanyEntity.setModified(date);
        existingCompanyEntity.setModifiedBy(request.getUserId());

        companyDAO.merge(existingCompanyEntity);

        if (request.getEntity().getImageCreateRequest() != null && request.getEntity().getImageCreateRequest().getImageData() != null
                && request.getEntity().getImageCreateRequest().getImageName() != null) {
            CreateMediaRequest mediaRequest = new CreateMediaRequest(ObjectType.COMPANY.getValue(), existingCompanyEntity.getId(),
                    request.getEntity().getImageCreateRequest().getImageData(), request.getEntity().getImageCreateRequest().getImageName(),
                    "IMAGE", "COVER_IMAGE");

            mediaService.saveMedia(new EntityRequest<>(mediaRequest, request));
        }

        final CompanyResponse response = companyMapper.entityToDto(existingCompanyEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);

    }

    @Override
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        companyDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Company deleted!");
    }

    @Override
    public PagedPayloadResponse<LoV> getLoVs(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<LoV> loVs = companyDAO.getLoVs(request.getFilter());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, loVs);
    }

    @Override
    public PayloadResponse<CompanyRegionPlatformResponse> companyRegionPlatformReport(
            final EntityRequest<CompanyRegionPlatformRequest> request) throws ApiException {

        // Get Company name and startDate
        Map<Long, CompanyEntity> companyMap = new HashMap<>();
        for (CompanyEntity company : companyDAO.getCompaniesByIds(request.getEntity().getCompanyIds())) {
            companyMap.put(company.getId(), company);
        }

        // Region
        // Get CompanyRegionPlatform as regions
        // true = company as publisher
        List<CompanyRegionPlatform> regions = regionDAO.getRegionReportByCompanies(request.getEntity().getCompanyIds(), true);
        // false = company as developer
        regions.addAll(regionDAO.getRegionReportByCompanies(request.getEntity().getCompanyIds(), false));
        // Group by Company Id
        Map<Long, List<CompanyRegionPlatform>> regionMapTemp = regions.stream()
                .collect(Collectors.groupingBy(CompanyRegionPlatform::getCompanyId));

        // Get CompanyRegionPlatform as platforms
        List<CompanyRegionPlatform> platforms = platformDAO.getPlatformsReportByCompanies(request.getEntity().getCompanyIds(), true);
        platforms.addAll(platformDAO.getPlatformsReportByCompanies(request.getEntity().getCompanyIds(), false));
        // Group by Company Id
        Map<Long, List<CompanyRegionPlatform>> platformMapTemp = platforms.stream()
                .collect(Collectors.groupingBy(CompanyRegionPlatform::getCompanyId));

        // Response list
        List<CompanyRegionPlatformResponseDetail> cprDetails = new ArrayList<>();
        // true = Creates region map
        Map<Long, CompanyRegionPlatformResponseDetail> regionAllMap = setCompanyRegionPlatformMap(companyMap, regionMapTemp, true);
        // false = Creates platform map
        Map<Long, CompanyRegionPlatformResponseDetail> platformAllMap = setCompanyRegionPlatformMap(companyMap, platformMapTemp, false);

        // If company has releases for regions and platforms we merge those
        // entry sets
        // Otherwise we add them to response list
        for (Long companyId : companyMap.keySet()) {
            if (regionAllMap.containsKey(companyId) && platformAllMap.containsKey(companyId)) {
                regionAllMap.merge(companyId, platformAllMap.get(companyId),
                        (v1, v2) -> new CompanyRegionPlatformResponseDetail(v1.getCompanyId(), v1.getCompanyName(), v1.getStartDate(),
                                v1.getRegionMap(), v2.getPlatformMap()));
                cprDetails.add(regionAllMap.get(companyId));
            } else if (regionAllMap.containsKey(companyId)) {
                cprDetails.add(regionAllMap.get(companyId));
            } else {
                cprDetails.add(platformAllMap.get(companyId));
            }
        }

        CompanyRegionPlatformResponse response = new CompanyRegionPlatformResponse();
        response.setCompaniesReports(cprDetails);

        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    // Creates Map<CompanyId, CompanyRegionPlatformResponseDetail>
    // CompanyRegionPlatformResponseDetail can have only regionMap or
    // platformMap
    // We merge data after
    private Map<Long, CompanyRegionPlatformResponseDetail> setCompanyRegionPlatformMap(Map<Long, CompanyEntity> companyMap,
            Map<Long, List<CompanyRegionPlatform>> companyRegionPlatformMap, boolean region) {
        Map<Long, CompanyRegionPlatformResponseDetail> companyReportDetailsMap = new HashMap<>();

        for (Map.Entry<Long, List<CompanyRegionPlatform>> set : companyRegionPlatformMap.entrySet()) {
            Map<Long, CompanyRegionPlatform> crpMap = new HashMap<>();
            CompanyRegionPlatformResponseDetail companyRegionPlatformReport = new CompanyRegionPlatformResponseDetail();
            companyRegionPlatformReport.setCompanyId(set.getKey());
            companyRegionPlatformReport.setCompanyName(companyMap.get(set.getKey()).getName());
            companyRegionPlatformReport.setStartDate(companyMap.get(set.getKey()).getStartDate());

            // If EntrySet already contains passed Key and if releaseDate is
            // younger than passed one we update data entries
            // If EntrySet already contains passed Key we only add on
            // numOfRelases,
            // and if there is no Key we add it
            for (CompanyRegionPlatform crp : set.getValue()) {
                if (crpMap.containsKey(crp.getObjId()) && crpMap.get(crp.getObjId()).getFirstRelease().isAfter(crp.getFirstRelease())) {
                    crp.setNumOfReleases(crp.getNumOfReleases() + crpMap.get(crp.getObjId()).getNumOfReleases());
                    crpMap.put(crp.getObjId(), crp);
                } else if (crpMap.containsKey(crp.getObjId())) {
                    CompanyRegionPlatform crpTemp = crpMap.get(crp.getObjId());
                    crpTemp.setNumOfReleases(crpTemp.getNumOfReleases() + crp.getNumOfReleases());
                    crpMap.put(crpTemp.getObjId(), crpTemp);
                } else {
                    crpMap.put(crp.getObjId(), crp);
                }
            }

            if (region) {
                companyRegionPlatformReport.setRegionMap(crpMap);
            } else {
                companyRegionPlatformReport.setPlatformMap(crpMap);
            }
            companyReportDetailsMap.put(set.getKey(), companyRegionPlatformReport);
        }

        return companyReportDetailsMap;
    }

    @Override
    public PagedPayloadResponse<CompanySearchResponse> searchCompanies(final EntityRequest<CompanySearchRequest> request)
            throws ApiException {
        requestValidator.validate(request);

        CompanySearchRequest searchRequest = request.getEntity();

        final List<CompanySearchResponse> companyList = companyDAO.searchCompany(searchRequest);

        lookupService.lookupCoverImage(companyList, CompanySearchResponse::getId, ObjectType.COMPANY.getValue(),
                CompanySearchResponse::setImageUrl, CompanySearchResponse::getImageUrl);

        return new PagedPayloadResponse<>(request, ResponseCode.OK, companyList.size(), 1, 1, companyList.size(), companyList);
    }
}
