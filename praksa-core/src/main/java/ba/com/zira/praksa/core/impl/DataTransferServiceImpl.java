package ba.com.zira.praksa.core.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EmptyRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.DataTransferService;
import ba.com.zira.praksa.api.model.datatransfer.PlatformCompanyHelper;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.dao.CompanyDAO;
import ba.com.zira.praksa.dao.PlatformDAO;
import ba.com.zira.praksa.dao.RegionDAO;
import ba.com.zira.praksa.dao.ReleaseDAO;
import ba.com.zira.praksa.dao.TransferCompanyDAO;
import ba.com.zira.praksa.dao.TransferPlatformDAO;
import ba.com.zira.praksa.dao.model.CompanyEntity;
import ba.com.zira.praksa.dao.model.PlatformEntity;
import ba.com.zira.praksa.dao.model.ReleaseEntity;
import ba.com.zira.praksa.dao.model.TransferCompanyEntity;
import ba.com.zira.praksa.dao.model.TransferPlatformEntity;

@Service
public class DataTransferServiceImpl implements DataTransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataTransferServiceImpl.class);
    private static final String NO_INFORMATION = "No information";

    TransferPlatformDAO transferPlatformDAO;
    TransferCompanyDAO transferCompanyDAO;
    PlatformDAO platformDAO;
    CompanyDAO companyDAO;
    RegionDAO regionDAO;
    ReleaseDAO releaseDAO;

    public DataTransferServiceImpl(TransferPlatformDAO transferPlatformDAO, TransferCompanyDAO transferCompanyDAO, PlatformDAO platformDAO,
            CompanyDAO companyDAO, RegionDAO regionDAO, ReleaseDAO releaseDAO) {
        super();
        this.transferPlatformDAO = transferPlatformDAO;
        this.transferCompanyDAO = transferCompanyDAO;
        this.platformDAO = platformDAO;
        this.companyDAO = companyDAO;
        this.regionDAO = regionDAO;
        this.releaseDAO = releaseDAO;
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> platformHUSToPlatformHUT(EmptyRequest request) throws ApiException {
        LOGGER.info("Start --> {}", Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime());

        List<TransferPlatformEntity> transferPlatformEntities = transferPlatformDAO.findAll();
        List<TransferCompanyEntity> transferCompanyEntities = transferCompanyDAO.findAll();

        Map<String, List<PlatformEntity>> platformEntities = platformDAO.findAll().stream()
                .collect(Collectors.groupingBy(PlatformEntity::getFullName));
        Map<String, List<CompanyEntity>> companyEntites = companyDAO.findAll().stream()
                .collect(Collectors.groupingBy(CompanyEntity::getName));

        List<CompanyEntity> companiesToAdd = new ArrayList<>();
        List<CompanyEntity> companiesToEdit = new ArrayList<>();

        List<PlatformEntity> platformsToAdd = new ArrayList<>();
        List<PlatformEntity> platformsToEdit = new ArrayList<>();

        List<PlatformCompanyHelper> platformCompanyHelper = new ArrayList<>();

        Map<String, String> companyNamesMap = new HashMap<>();

        for (TransferCompanyEntity transferCompanyEntity : transferCompanyEntities) {
            String rootName = transferCompanyEntity.getRootName();

            Map<String, String> namesMap = Arrays.stream(transferCompanyEntity.getAllNames().split("#"))
                    .collect(Collectors.toMap(n -> n, n -> rootName));

            companyNamesMap.putAll(namesMap);
        }

        LOGGER.debug("Length -> {}", companyNamesMap.size());

        LOGGER.info("TransferPlatform --> Platform CONVERSION BEGINING");
        transferPlatformEntities.stream().forEach(createPlatform(platformEntities, companyEntites, companiesToAdd, companiesToEdit,
                companyNamesMap, platformsToAdd, platformsToEdit, platformCompanyHelper));

        Map<String, List<CompanyEntity>> newCompanyEntites = companiesToAdd.stream().collect(Collectors.groupingBy(CompanyEntity::getName));
        Map<String, List<PlatformEntity>> newPlatformEntities = platformsToAdd.stream()
                .collect(Collectors.groupingBy(PlatformEntity::getFullName));

        companiesToEdit = companiesToEdit.stream().filter(distinctByKey(CompanyEntity::getName)).collect(Collectors.toList());
        platformsToEdit = platformsToEdit.stream().filter(distinctByKey(PlatformEntity::getFullName)).collect(Collectors.toList());

        LOGGER.info("ADDING NEW COMPANIES --> {}", newCompanyEntites.size());
        for (Map.Entry<String, List<CompanyEntity>> entry : newCompanyEntites.entrySet()) {
            companyDAO.persist(entry.getValue().get(0));
        }

        LOGGER.info("ADDING NEW PLATFORMS --> {}", newPlatformEntities.size());
        for (Map.Entry<String, List<PlatformEntity>> entry : newPlatformEntities.entrySet()) {
            platformDAO.persist(entry.getValue().get(0));
        }

        LOGGER.info("EDITING EXISTING COMPANIES --> {}", companiesToEdit.size());
        for (CompanyEntity company : companiesToEdit) {
            companyDAO.merge(company);
        }
        LOGGER.info("EDITING EXISTING PLATFORMS --> {}", platformsToEdit.size());
        for (PlatformEntity platform : platformsToEdit) {
            platformDAO.merge(platform);
        }

        Map<String, List<PlatformCompanyHelper>> platformCompanyHelperMap = platformCompanyHelper.stream()
                .collect(Collectors.groupingBy(PlatformCompanyHelper::getPlatformName));

        LOGGER.info("CREATING RELEASES FOR NEW PLATFORMS");
        platformsToAdd.stream().filter(p -> p.getId() != null)
                .forEach(createRelease(companyEntites, newCompanyEntites, platformCompanyHelperMap));
        LOGGER.info("CREATING RELEASES FOR EXISTING PLATFORMS");
        platformsToEdit.stream().forEach(createRelease(companyEntites, newCompanyEntites, platformCompanyHelperMap));

        LOGGER.info("End --> {}", Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        return new PayloadResponse<>(request, ResponseCode.OK, "Platforms converted successfully!");
    }

    private Consumer<? super TransferPlatformEntity> createPlatform(Map<String, List<PlatformEntity>> platformEntities,
            Map<String, List<CompanyEntity>> companyEntites, List<CompanyEntity> companiesToAdd, List<CompanyEntity> companiesToEdit,
            Map<String, String> companyNamesMap, List<PlatformEntity> platformsToAdd, List<PlatformEntity> platformsToEdit,
            List<PlatformCompanyHelper> releaseHelper) {
        return transferPlatform -> {
            PlatformEntity platformEntity = new PlatformEntity();
            String developer = checkCompanyName(transferPlatform.getDeveloper(), companyEntites, companiesToAdd, companiesToEdit,
                    companyNamesMap);
            String manufacturer = checkCompanyName(transferPlatform.getManufacturer(), companyEntites, companiesToAdd, companiesToEdit,
                    companyNamesMap);

            if (platformEntities.get(transferPlatform.getName()) == null) {
                platformEntity.setCreated(LocalDateTime.now());
                platformEntity.setCreatedBy("DTS");
                platformEntity.setFullName(transferPlatform.getName());
                platformEntity.setCode(makeAbbrevation(transferPlatform.getName()));
                platformEntity.setAbbriviation(makeAbbrevation(transferPlatform.getName()));
                platformEntity.setAliases(transferPlatform.getPlatformAlternateName());
                platformEntity.setInformation(createPlatformInformation(transferPlatform));

                platformEntity.setOutlineText(String.format("This platform was made by %s.", manufacturer));

                platformsToAdd.add(platformEntity);
                LOGGER.debug("Created Platform --> {}", platformEntity);
            } else {
                platformEntity = platformEntities.get(transferPlatform.getName()).get(0);
                platformEntity.setModified(LocalDateTime.now());
                platformEntity.setModifiedBy("DTS");
                platformsToEdit.add(platformEntity);
                LOGGER.debug("Existing Platform --> {}", platformEntity);
            }

            releaseHelper.add(
                    new PlatformCompanyHelper(platformEntity.getFullName(), developer, manufacturer, transferPlatform.getReleaseDate()));
        };

    }

    private String checkCompanyName(String companyName, Map<String, List<CompanyEntity>> companyEntites, List<CompanyEntity> companiesToAdd,
            List<CompanyEntity> companiesToEdit, Map<String, String> companyNamesMap) {
        String rootCompanyName = null;
        if (companyName != null) {
            rootCompanyName = companyNamesMap.get(companyName);

            if (rootCompanyName == null && companyEntites.get(companyName) == null) {
                companiesToAdd.add(createCompany(companyName));
                return companyName;
            } else if (rootCompanyName != null && companyEntites.get(rootCompanyName) == null) {
                companiesToAdd.add(createCompany(rootCompanyName));
            } else if (rootCompanyName != null && companyEntites.get(rootCompanyName) != null) {
                CompanyEntity companyEntity = companyEntites.get(rootCompanyName).get(0);
                companyEntity.setModified(LocalDateTime.now());
                companyEntity.setModifiedBy("DTS");
                companiesToEdit.add(companyEntites.get(rootCompanyName).get(0));
                LOGGER.debug("Existing Company --> {}", companyEntites.get(rootCompanyName).get(0));
            } else {
                CompanyEntity companyEntity = companyEntites.get(companyName).get(0);
                companyEntity.setModified(LocalDateTime.now());
                companyEntity.setModifiedBy("DTS");
                companiesToEdit.add(companyEntites.get(companyName).get(0));
                LOGGER.debug("Existing Company --> {}", companyEntites.get(companyName).get(0));
                return companyName;
            }

        }

        return rootCompanyName;
    }

    private CompanyEntity createCompany(String companyName) {
        CompanyEntity company = new CompanyEntity();
        company.setName(companyName);
        company.setCreated(LocalDateTime.now());
        company.setCreatedBy("DTS");
        LOGGER.debug("Created Company --> {}", company);

        return company;
    }

    private String makeAbbrevation(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        if (name != null && !name.equals("") && !name.trim().isEmpty()) {
            String[] words = name.split(" ");

            if (words.length > 1) {
                for (String word : words) {
                    if (NumberUtils.isNumber(word)) {
                        stringBuilder.append(word);
                    } else {
                        stringBuilder.append(word.toUpperCase().charAt(0));
                    }
                }
            } else {
                stringBuilder.append(words[0].toUpperCase().substring(0, 3));
            }
        }
        return stringBuilder.toString();
    }

    private String createPlatformInformation(TransferPlatformEntity transferPlatform) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("<p>%s<p><br/>", transferPlatform.getNotes()));
        stringBuilder.append("<table>");
        stringBuilder.append("<tr> <th>Element</th> <th>Information</th> </tr>");
        stringBuilder.append(String.format("<tr> <td>CPU</td> <td>%s</td> </tr>",
                transferPlatform.getCpu() != null ? transferPlatform.getCpu() : NO_INFORMATION));
        stringBuilder.append(String.format("<tr> <td>Memory</td> <td>%s</td> </tr>",
                transferPlatform.getMemory() != null ? transferPlatform.getMemory() : NO_INFORMATION));
        stringBuilder.append(String.format("<tr> <td>Graphics</td> <td>%s</td> </tr>",
                transferPlatform.getGraphics() != null ? transferPlatform.getGraphics() : NO_INFORMATION));
        stringBuilder.append(String.format("<tr> <td>Sound</td> <td>%s</td> </tr>",
                transferPlatform.getSound() != null ? transferPlatform.getSound() : NO_INFORMATION));
        stringBuilder.append(String.format("<tr> <td>Display</td> <td>%s</td> </tr>",
                transferPlatform.getDisplay() != null ? transferPlatform.getDisplay() : NO_INFORMATION));
        stringBuilder.append(String.format("<tr> <td>Media</td> <td>%s</td> </tr>",
                transferPlatform.getMedia() != null ? transferPlatform.getMedia() : NO_INFORMATION));
        stringBuilder.append(String.format("<tr> <td>Max controllers</td> <td>%s</td> </tr>",
                transferPlatform.getMaxControles() != null ? transferPlatform.getMaxControles() : NO_INFORMATION));
        stringBuilder.append("</table>");

        return stringBuilder.toString();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private Consumer<? super PlatformEntity> createRelease(Map<String, List<CompanyEntity>> oldCompanyEntites,
            Map<String, List<CompanyEntity>> newCompanyEntites, Map<String, List<PlatformCompanyHelper>> platformCompanyHelperMap) {
        return platform -> {
            if (platformCompanyHelperMap.get(platform.getFullName()) != null) {
                PlatformCompanyHelper helper = platformCompanyHelperMap.get(platform.getFullName()).get(0);

                CompanyEntity developer = getCompanyEntity(helper.getDeveloperName(), newCompanyEntites, oldCompanyEntites);
                CompanyEntity publisher = getCompanyEntity(helper.getPublisherName(), newCompanyEntites, oldCompanyEntites);

                Long devId = developer != null ? developer.getId() : null;
                Long pubId = publisher != null ? publisher.getId() : null;

                List<ReleaseEntity> releasesByDevPub = releaseDAO.getReleasesByPlatformDevPub(platform.getId(), devId, pubId);
                List<ReleaseEntity> releases = releaseDAO.getReleasesByPlatform(platform.getId());

                populateReleaseEntity(helper, platform, developer, publisher, releases, releasesByDevPub);

            }

        };
    }

    private CompanyEntity getCompanyEntity(String companyName, Map<String, List<CompanyEntity>> newCompanyEntites,
            Map<String, List<CompanyEntity>> oldCompanyEntites) {
        if (newCompanyEntites.get(companyName) != null) {
            return newCompanyEntites.get(companyName).get(0);
        } else if (oldCompanyEntites.get(companyName) != null) {
            return oldCompanyEntites.get(companyName).get(0);
        } else {
            return null;
        }
    }

    private void populateReleaseEntity(PlatformCompanyHelper helper, PlatformEntity platform, CompanyEntity developer,
            CompanyEntity publisher, List<ReleaseEntity> releases, List<ReleaseEntity> releasesByDevPub) {
        if (releasesByDevPub.isEmpty()) {
            ReleaseEntity releaseEntity = new ReleaseEntity();

            releaseEntity.setPlatform(platform);

            if (helper.getReleaseDate() == null) {
                return;
            } else {
                releaseEntity.setReleaseDate(LocalDateTime.parse(helper.getReleaseDate().substring(0, 19)));
            }

            releaseEntity.setRegion(regionDAO.findByPK(1L));
            releaseEntity.setType(ObjectType.PLATFORM.getValue());
            releaseEntity.setCreated(LocalDateTime.now());
            releaseEntity.setCreatedBy("DTS");
            releaseEntity.setUuid(UUID.randomUUID().toString());
            releaseEntity.setDeveloper(developer);
            releaseEntity.setPublisher(publisher);

            releaseDAO.persist(releaseEntity);
        } else if (!releases.isEmpty()) {
            for (ReleaseEntity releaseEntity : releases) {
                releaseEntity.setModified(LocalDateTime.now());
                releaseEntity.setModifiedBy("DTS");

                releaseDAO.merge(releaseEntity);
            }

        }

    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> gameHUSToGameHUT(EmptyRequest request) throws ApiException {
        // TODO Auto-generated method stub
        return null;
    }

}
