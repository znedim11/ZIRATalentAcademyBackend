package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
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

        // LOGGER.info("Length -> {}", companyNamesMap.size());

        LOGGER.info("TransferPlatform --> Platform CONVERSION BEGINING");
        transferPlatformEntities.stream().forEach(createPlatform(platformEntities, companyEntites, companiesToAdd, companiesToEdit,
                companyNamesMap, platformsToAdd, platformsToEdit, platformCompanyHelper));

        Map<String, List<CompanyEntity>> newCompanyEntites = companiesToAdd.stream().collect(Collectors.groupingBy(CompanyEntity::getName));
        Map<String, List<PlatformEntity>> newPlatformEntities = platformsToAdd.stream()
                .collect(Collectors.groupingBy(PlatformEntity::getFullName));

        LOGGER.info("ADDING NEW COMPANIES");
        for (Map.Entry<String, List<CompanyEntity>> entry : newCompanyEntites.entrySet()) {
            companyDAO.persist(entry.getValue().get(0));
        }

        LOGGER.info("ADDING NEW PLATFORMS");
        for (Map.Entry<String, List<PlatformEntity>> entry : newPlatformEntities.entrySet()) {
            platformDAO.persist(entry.getValue().get(0));
        }

        LOGGER.info("EDITING EXISTING COMPANIES");
        for (CompanyEntity company : companiesToEdit) {
            companyDAO.merge(company);
        }
        LOGGER.info("EDITING EXISTING PLATFORMS");
        for (PlatformEntity platform : platformsToEdit) {
            platformDAO.merge(platform);
        }

        Map<String, List<PlatformCompanyHelper>> platformCompanyHelperMap = platformCompanyHelper.stream()
                .collect(Collectors.groupingBy(PlatformCompanyHelper::getPlatformName));

        LOGGER.info("CREATING RELEASES");
        platformsToAdd.stream().forEach(createRelease(platformEntities, newPlatformEntities, companyEntites, newCompanyEntites,
                companyNamesMap, platformCompanyHelperMap));

        return new PayloadResponse<>(request, ResponseCode.OK, "Platforms converted successfully!");
    }

    private Consumer<? super PlatformEntity> createRelease(Map<String, List<PlatformEntity>> oldPlatformEntities,
            Map<String, List<PlatformEntity>> newPlatformEntities, Map<String, List<CompanyEntity>> oldCompanyEntites,
            Map<String, List<CompanyEntity>> newCompanyEntites, Map<String, String> companyNamesMap,
            Map<String, List<PlatformCompanyHelper>> platformCompanyHelperMap) {
        return platform -> {
            if (platformCompanyHelperMap.get(platform.getFullName()) != null) {
                ReleaseEntity releaseEntity = new ReleaseEntity();
                releaseEntity.setPlatform(platform);
                PlatformCompanyHelper helper = platformCompanyHelperMap.get(platform.getFullName()).get(0);
                if (helper.getReleaseDate() == null) {
                    return;
                } else {
                    releaseEntity.setReleaseDate(LocalDateTime.parse(helper.getReleaseDate().substring(0, 19)));
                }

                if (newCompanyEntites.get(helper.getDeveloperName()) != null) {
                    releaseEntity.setDeveloper(newCompanyEntites.get(helper.getDeveloperName()).get(0));
                } else if (oldCompanyEntites.get(helper.getDeveloperName()) != null) {
                    releaseEntity.setDeveloper(oldCompanyEntites.get(helper.getDeveloperName()).get(0));
                }

                if (newCompanyEntites.get(helper.getPublisherName()) != null) {
                    releaseEntity.setPublisher(newCompanyEntites.get(helper.getPublisherName()).get(0));
                } else if (oldCompanyEntites.get(helper.getPublisherName()) != null) {
                    releaseEntity.setPublisher(oldCompanyEntites.get(helper.getPublisherName()).get(0));
                }

                releaseEntity.setRegion(regionDAO.findByPK(1L));
                releaseEntity.setType(ObjectType.PLATFORM.getValue());
                releaseEntity.setCreated(LocalDateTime.now());
                releaseEntity.setCreatedBy("DTS");
                releaseEntity.setUuid(UUID.randomUUID().toString());

                releaseDAO.merge(releaseEntity);
                // LOGGER.info("Created release --> {}", releaseEntity);
                // LOGGER.info("Created release.");
            }
        };
    }

    private Consumer<? super TransferPlatformEntity> createPlatform(Map<String, List<PlatformEntity>> platformEntities,
            Map<String, List<CompanyEntity>> companyEntites, List<CompanyEntity> companiesToAdd, List<CompanyEntity> companiesToEdit,
            Map<String, String> companyNamesMap, List<PlatformEntity> platformsToAdd, List<PlatformEntity> platformsToEdit,
            List<PlatformCompanyHelper> releaseHelper) {
        return transferPlatform -> {
            PlatformEntity platformEntity = new PlatformEntity();

            if (platformEntities.get(transferPlatform.getName()) == null) {
                platformEntity.setCreated(LocalDateTime.now());
                platformEntity.setCreatedBy("DTS");
                platformEntity.setFullName(transferPlatform.getName());
                platformEntity.setCode(makeAbbrevation(transferPlatform.getName()));
                platformEntity.setAbbriviation(makeAbbrevation(transferPlatform.getName()));
                platformEntity.setAliases(transferPlatform.getPlatformAlternateName());
                platformEntity.setInformation(createPlatformInformation(transferPlatform));
                String developer = checkCompanyName(transferPlatform.getDeveloper(), companyEntites, companiesToAdd, companiesToEdit,
                        companyNamesMap);
                String manufacturer = checkCompanyName(transferPlatform.getManufacturer(), companyEntites, companiesToAdd, companiesToEdit,
                        companyNamesMap);
                platformEntity.setOutlineText(String.format("This platform was made by %s.", manufacturer));

                platformsToAdd.add(platformEntity);
                // LOGGER.info("Created Platform --> {}", platformEntity);

                releaseHelper.add(new PlatformCompanyHelper(platformEntity.getFullName(), developer, manufacturer,
                        transferPlatform.getReleaseDate()));

            } else {
                platformEntity = platformEntities.get(transferPlatform.getName()).get(0);
                platformEntity.setModified(LocalDateTime.now());
                platformEntity.setModifiedBy("DTS");
                platformsToEdit.add(platformEntity);
                // LOGGER.info("Existing Platform --> {}", platformEntity);
            }
        };

    }

    private String createPlatformInformation(TransferPlatformEntity transferPlatform) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("<p>%s<p><br/>", transferPlatform.getNotes()));
        stringBuilder.append("<table>");
        stringBuilder.append("<tr> <th>Element</th> <th>Information</th> </tr>");
        stringBuilder.append(String.format("<tr> <td>CPU</td> <td>%s</td> </tr>", transferPlatform.getCpu()));
        stringBuilder.append(String.format("<tr> <td>Memory</td> <td>%s</td> </tr>", transferPlatform.getMemory()));
        stringBuilder.append(String.format("<tr> <td>Graphics</td> <td>%s</td> </tr>", transferPlatform.getGraphics()));
        stringBuilder.append(String.format("<tr> <td>Sound</td> <td>%s</td> </tr>", transferPlatform.getSound()));
        stringBuilder.append(String.format("<tr> <td>Display</td> <td>%s</td> </tr>", transferPlatform.getDisplay()));
        stringBuilder.append(String.format("<tr> <td>Media</td> <td>%s</td> </tr>", transferPlatform.getMedia()));
        stringBuilder.append(String.format("<tr> <td>Max controllers</td> <td>%s</td> </tr>", transferPlatform.getMaxControles()));
        stringBuilder.append("</table>");

        return stringBuilder.toString();
    }

    private String checkCompanyName(String companyName, Map<String, List<CompanyEntity>> companyEntites, List<CompanyEntity> companiesToAdd,
            List<CompanyEntity> companiesToEdit, Map<String, String> companyNamesMap) {
        String rootCompanyName = null;
        if (companyName != null) {
            rootCompanyName = companyNamesMap.get(companyName);

            if (rootCompanyName == null) {
                companiesToAdd.add(createCompany(companyName));
                return companyName;
            } else if (companyEntites.get(rootCompanyName) == null) {
                companiesToAdd.add(createCompany(rootCompanyName));
            } else {
                CompanyEntity companyEntity = companyEntites.get(rootCompanyName).get(0);
                companyEntity.setModified(LocalDateTime.now());
                companyEntity.setModifiedBy("DTS");
                companiesToEdit.add(companyEntites.get(rootCompanyName).get(0));
                // LOGGER.info("Existing Company --> {}",
                // companyEntites.get(rootCompanyName).get(0));
            }

        }

        return rootCompanyName;
    }

    private CompanyEntity createCompany(String companyName) {
        CompanyEntity company = new CompanyEntity();
        company.setName(companyName);
        company.setCreated(LocalDateTime.now());
        company.setCreatedBy("DTS");
        // LOGGER.info("Created Company --> {}", company);

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

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> gameHUSToGameHUT(EmptyRequest request) throws ApiException {
        // TODO Auto-generated method stub
        return null;
    }

}
