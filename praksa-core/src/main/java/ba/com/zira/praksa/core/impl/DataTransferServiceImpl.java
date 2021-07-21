package ba.com.zira.praksa.core.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EmptyRequest;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.DataTransferService;
import ba.com.zira.praksa.api.model.datatransfer.GamePlatformCompanyHelper;
import ba.com.zira.praksa.api.model.datatransfer.PlatformCompanyHelper;
import ba.com.zira.praksa.api.model.datatransfer.TransferGameHelper;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.dao.CompanyDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.PlatformDAO;
import ba.com.zira.praksa.dao.RegionDAO;
import ba.com.zira.praksa.dao.ReleaseDAO;
import ba.com.zira.praksa.dao.TransferCompanyDAO;
import ba.com.zira.praksa.dao.TransferGameDAO;
import ba.com.zira.praksa.dao.TransferPlatformDAO;
import ba.com.zira.praksa.dao.model.CompanyEntity;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.PlatformEntity;
import ba.com.zira.praksa.dao.model.ReleaseEntity;
import ba.com.zira.praksa.dao.model.TransferCompanyEntity;
import ba.com.zira.praksa.dao.model.TransferPlatformEntity;

@Service
public class DataTransferServiceImpl implements DataTransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataTransferServiceImpl.class);
    private static final String NO_INFORMATION = "No information";
    private static final String IRMA = "Irma";

    TransferPlatformDAO transferPlatformDAO;
    TransferCompanyDAO transferCompanyDAO;
    TransferGameDAO transferGameDAO;
    PlatformDAO platformDAO;
    CompanyDAO companyDAO;
    GameDAO gameDAO;
    RegionDAO regionDAO;
    ReleaseDAO releaseDAO;

    public DataTransferServiceImpl(TransferPlatformDAO transferPlatformDAO, TransferCompanyDAO transferCompanyDAO,
            TransferGameDAO transferGameDAO, PlatformDAO platformDAO, CompanyDAO companyDAO, GameDAO gameDAO, RegionDAO regionDAO,
            ReleaseDAO releaseDAO) {
        super();
        this.transferPlatformDAO = transferPlatformDAO;
        this.transferCompanyDAO = transferCompanyDAO;
        this.transferGameDAO = transferGameDAO;
        this.platformDAO = platformDAO;
        this.companyDAO = companyDAO;
        this.gameDAO = gameDAO;
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

        Map<String, String> companyNamesMap = createCompanyNamesMap(transferCompanyEntities);

        LOGGER.debug("Length -> {}", companyNamesMap.size());

        LOGGER.info("TransferPlatform --> Platform CONVERSION BEGINING");
        transferPlatformEntities.stream().forEach(transferPlatform(platformEntities, companyEntites, companiesToAdd, companiesToEdit,
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

    private Consumer<? super TransferPlatformEntity> transferPlatform(Map<String, List<PlatformEntity>> platformEntities,
            Map<String, List<CompanyEntity>> companyEntites, List<CompanyEntity> companiesToAdd, List<CompanyEntity> companiesToEdit,
            Map<String, String> companyNamesMap, List<PlatformEntity> platformsToAdd, List<PlatformEntity> platformsToEdit,
            List<PlatformCompanyHelper> releaseHelper) {
        return transferPlatform -> {
            String developer = checkCompanyName(transferPlatform.getDeveloper(), companyEntites, companiesToAdd, companiesToEdit,
                    companyNamesMap);
            String manufacturer = checkCompanyName(transferPlatform.getManufacturer(), companyEntites, companiesToAdd, companiesToEdit,
                    companyNamesMap);

            String platform = createPlatform(transferPlatform, platformEntities, platformsToAdd, platformsToEdit, manufacturer);

            releaseHelper.add(new PlatformCompanyHelper(platform, developer, manufacturer, transferPlatform.getReleaseDate()));
        };

    }

    private String createPlatform(TransferPlatformEntity transferPlatform, Map<String, List<PlatformEntity>> platformEntities,
            List<PlatformEntity> platformsToAdd, List<PlatformEntity> platformsToEdit, String manufacturer) {
        PlatformEntity platformEntity = new PlatformEntity();

        if (platformEntities.get(transferPlatform.getName()) == null) {
            platformEntity.setCreated(LocalDateTime.now());
            platformEntity.setCreatedBy(IRMA);
            platformEntity.setFullName(transferPlatform.getName());
            platformEntity.setCode(makeAbbreviation(transferPlatform.getName()));
            platformEntity.setAbbriviation(makeAbbreviation(transferPlatform.getName()));
            platformEntity.setAliases(transferPlatform.getPlatformAlternateName());
            platformEntity.setInformation(createPlatformInformation(transferPlatform));

            if (manufacturer != null) {
                platformEntity.setOutlineText(String.format("This platform was made by %s.", manufacturer));
            }

            platformsToAdd.add(platformEntity);
            LOGGER.debug("Created Platform --> {}", platformEntity.getFullName());

            return platformEntity.getFullName();
        } else {
            platformEntity = platformEntities.get(transferPlatform.getName()).get(0);
            platformEntity.setModified(LocalDateTime.now());
            platformEntity.setModifiedBy(IRMA);
            platformsToEdit.add(platformEntity);
            LOGGER.debug("Existing Platform --> {}", platformEntity.getFullName());
            return platformEntity.getFullName();
        }
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
                companyEntity.setModifiedBy(IRMA);
                companiesToEdit.add(companyEntites.get(rootCompanyName).get(0));
                LOGGER.debug("Existing Company --> {}", companyEntites.get(rootCompanyName).get(0).getName());
            } else {
                CompanyEntity companyEntity = companyEntites.get(companyName).get(0);
                companyEntity.setModified(LocalDateTime.now());
                companyEntity.setModifiedBy(IRMA);
                companiesToEdit.add(companyEntites.get(companyName).get(0));
                LOGGER.debug("Existing Company --> {}", companyEntites.get(companyName).get(0).getName());
                return companyName;
            }

        }

        return rootCompanyName;
    }

    private CompanyEntity createCompany(String companyName) {
        CompanyEntity company = new CompanyEntity();
        company.setName(companyName);
        company.setCreated(LocalDateTime.now());
        company.setCreatedBy(IRMA);
        LOGGER.debug("Created Company --> {}", company.getName());

        return company;
    }

    private String makeAbbreviation(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        if (name != null && !name.equals("") && !name.trim().isEmpty()) {
            String[] words = name.split(" ");

            if (words.length > 1) {
                for (String word : words) {
                    if (!word.equals("") && !word.trim().isEmpty() && NumberUtils.isNumber(word)) {
                        stringBuilder.append(word);
                    } else if (!word.equals("") && !word.trim().isEmpty() && !NumberUtils.isNumber(word)) {
                        stringBuilder.append(word.toUpperCase().charAt(0));
                    }
                }

            } else if (name.length() > 3) {
                stringBuilder.append(words[0].toUpperCase().substring(0, 3));
            } else {
                stringBuilder.append(words[0].toUpperCase());
            }
        }

        String abbrevation = stringBuilder.toString();
        if (abbrevation.length() > 20) {
            abbrevation = abbrevation.substring(0, 19);
        }

        return abbrevation;
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

                List<ReleaseEntity> releasesByDevPub = releaseDAO.getReleasesByGamePlatformDevPub(null, platform.getId(), devId, pubId);
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
            releaseEntity.setCreatedBy(IRMA);
            releaseEntity.setUuid(UUID.randomUUID().toString());
            releaseEntity.setDeveloper(developer);
            releaseEntity.setPublisher(publisher);

            releaseDAO.persist(releaseEntity);
        } else if (!releases.isEmpty()) {
            for (ReleaseEntity releaseEntity : releases) {
                releaseEntity.setModified(LocalDateTime.now());
                releaseEntity.setModifiedBy(IRMA);

                releaseDAO.merge(releaseEntity);
            }

        }

    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> gameHUSToGameHUTWrapper(EntityRequest<Long> request) throws ApiException {
        LocalDateTime startTime = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LOGGER.info("Start process --> {}", startTime);

        int numOfRecords = request.getEntity().intValue();
        Long numOfTransferGameEntites = transferGameDAO.getCount();
        Long numOfPages = (long) Math.ceil(numOfTransferGameEntites / (double) numOfRecords);

        List<PlatformEntity> platformEntities = platformDAO.findAll();
        List<CompanyEntity> companyEntites = companyDAO.findAll();
        List<GameEntity> gameEntites = gameDAO.findAll();

        for (int page = 1; page <= numOfPages; page++) {
            gameHUSToGameHUT(numOfRecords, page, platformEntities, companyEntites, gameEntites);
            // gameHUSToGameHUT(numOfRecords, page);
        }

        LocalDateTime endTime = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LOGGER.info("End process --> {}", endTime);
        LOGGER.info("TOTAL TIME --> (PROCESS) --> {} minutes", ChronoUnit.MINUTES.between(startTime, endTime));
        return new PayloadResponse<>(request, ResponseCode.OK, "Games converted successfully!");
    }

    public void gameHUSToGameHUT(int numOfRecords, int page, List<PlatformEntity> platformEntities, List<CompanyEntity> companyEntites,
            List<GameEntity> gameEntites) {
        LocalDateTime startTime = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();

        LOGGER.info("Start (page {}) --> {}", page, startTime);

        List<TransferGameHelper> transferGameEntities = new ArrayList<>();
        transferGameEntities.addAll(transferGameDAO.findAllHelper(numOfRecords, page));

        List<TransferCompanyEntity> transferCompanyEntities = transferCompanyDAO.findAll();

        Map<String, List<PlatformEntity>> platformEntitiesMap = platformEntities.stream()
                .collect(Collectors.groupingBy(PlatformEntity::getFullName));
        Map<String, List<CompanyEntity>> companyEntitesMap = companyEntites.stream().collect(Collectors.groupingBy(CompanyEntity::getName));
        Map<String, List<GameEntity>> gameEntitiesMap = gameEntites.stream().collect(Collectors.groupingBy(GameEntity::getFullName));

        List<GameEntity> gamesToAdd = new ArrayList<>();
        List<GameEntity> gamesToEdit = new ArrayList<>();

        List<CompanyEntity> companiesToAdd = new ArrayList<>();
        List<CompanyEntity> companiesToEdit = new ArrayList<>();

        List<PlatformEntity> platformsToAdd = new ArrayList<>();
        List<PlatformEntity> platformsToEdit = new ArrayList<>();

        List<GamePlatformCompanyHelper> gamePlatformCompanyHelper = new ArrayList<>();

        Map<String, String> companyNamesMap = createCompanyNamesMap(transferCompanyEntities);

        LOGGER.info("TransferGame --> Game CONVERSION BEGINING");
        Map<String, List<TransferGameHelper>> gameHelpersMap = transferGameEntities.stream()
                .collect(Collectors.groupingBy(TransferGameHelper::getName));

        for (Map.Entry<String, List<TransferGameHelper>> entry : gameHelpersMap.entrySet()) {
            entry.getValue().stream().forEach(transferGame(gameEntitiesMap, platformEntitiesMap, companyEntitesMap, companiesToAdd,
                    companiesToEdit, companyNamesMap, platformsToAdd, platformsToEdit, gamesToAdd, gamesToEdit, gamePlatformCompanyHelper));
        }
        Map<String, List<CompanyEntity>> newCompanyEntites = companiesToAdd.stream().collect(Collectors.groupingBy(CompanyEntity::getName));
        Map<String, List<PlatformEntity>> newPlatformEntities = platformsToAdd.stream()
                .collect(Collectors.groupingBy(PlatformEntity::getFullName));
        Map<String, List<GameEntity>> newGameEntities = gamesToAdd.stream().collect(Collectors.groupingBy(GameEntity::getFullName));

        companiesToEdit = companiesToEdit.stream().filter(distinctByKey(CompanyEntity::getName)).collect(Collectors.toList());
        platformsToEdit = platformsToEdit.stream().filter(distinctByKey(PlatformEntity::getFullName)).collect(Collectors.toList());
        gamesToEdit = gamesToEdit.stream().filter(distinctByKey(GameEntity::getFullName)).collect(Collectors.toList());

        LOGGER.info("ADDING NEW COMPANIES --> {}", newCompanyEntites.size());
        for (Map.Entry<String, List<CompanyEntity>> entry : newCompanyEntites.entrySet()) {
            CompanyEntity entity = entry.getValue().get(0);
            companyDAO.persist(entity);
            companyEntites.add(entity);
        }

        LOGGER.info("ADDING NEW PLATFORMS --> {}", newPlatformEntities.size());
        for (Map.Entry<String, List<PlatformEntity>> entry : newPlatformEntities.entrySet()) {
            PlatformEntity entity = entry.getValue().get(0);
            platformDAO.persist(entity);
            platformEntities.add(entity);
        }

        LOGGER.info("ADDING NEW GAMES --> {}", newGameEntities.size());
        for (Map.Entry<String, List<GameEntity>> entry : newGameEntities.entrySet()) {
            GameEntity entity = entry.getValue().get(0);
            gameDAO.persist(entity);
            gameEntites.add(entity);
        }

        LOGGER.info("EDITING EXISTING COMPANIES --> {}", companiesToEdit.size());
        for (CompanyEntity company : companiesToEdit) {
            companyDAO.merge(company);
        }
        LOGGER.info("EDITING EXISTING PLATFORMS --> {}", platformsToEdit.size());
        for (PlatformEntity platform : platformsToEdit) {
            platformDAO.merge(platform);
        }
        LOGGER.info("EDITING EXISTING GAMES --> {}", gamesToEdit.size());
        for (GameEntity game : gamesToEdit) {
            gameDAO.merge(game);
        }

        Map<Long, List<PlatformEntity>> existingPlatformEntitiesMap = platformEntities.stream()
                .collect(Collectors.groupingBy(PlatformEntity::getId));
        Map<Long, List<CompanyEntity>> existingCompanyEntitesMap = companyEntites.stream()
                .collect(Collectors.groupingBy(CompanyEntity::getId));
        Map<Long, List<GameEntity>> existingGameEntitiesMap = gameEntites.stream().collect(Collectors.groupingBy(GameEntity::getId));

        Map<Long, List<CompanyEntity>> companiesToEditMap = companiesToEdit.stream().collect(Collectors.groupingBy(CompanyEntity::getId));
        Map<Long, List<PlatformEntity>> platformsToEditMap = platformsToEdit.stream().collect(Collectors.groupingBy(PlatformEntity::getId));
        Map<Long, List<GameEntity>> gamesToEditMap = gamesToEdit.stream().collect(Collectors.groupingBy(GameEntity::getId));

        existingPlatformEntitiesMap.putAll(platformsToEditMap);
        existingCompanyEntitesMap.putAll(companiesToEditMap);
        existingGameEntitiesMap.putAll(gamesToEditMap);

        companyEntites = existingCompanyEntitesMap.values().stream().flatMap(c -> c.stream()).collect(Collectors.toList());
        platformEntities = existingPlatformEntitiesMap.values().stream().flatMap(p -> p.stream()).collect(Collectors.toList());
        gameEntites = existingGameEntitiesMap.values().stream().flatMap(g -> g.stream()).collect(Collectors.toList());

        Map<String, List<GamePlatformCompanyHelper>> gamePlatformCompanyHelperMap = gamePlatformCompanyHelper.stream()
                .collect(Collectors.groupingBy(GamePlatformCompanyHelper::getGameName));

        List<Long> gameIds = gamesToAdd.stream().map(GameEntity::getId).collect(Collectors.toList());
        gameIds.addAll(gamesToEdit.stream().map(GameEntity::getId).collect(Collectors.toList()));
        List<List<Long>> idLists = Lists.newArrayList(Iterables.partition(gameIds, 1000));
        List<ReleaseEntity> releases = releaseDAO.findReleases(idLists);

        LOGGER.info("CREATING RELEASES FOR NEW GAMES");
        gamesToAdd.stream().filter(g -> g.getId() != null).forEach(createRelease(platformEntitiesMap, newPlatformEntities,
                companyEntitesMap, newCompanyEntites, gamePlatformCompanyHelperMap, releases));
        LOGGER.info("CREATING RELEASES FOR EXISTING GAMES");
        gamesToEdit.stream().forEach(createRelease(platformEntitiesMap, newPlatformEntities, companyEntitesMap, newCompanyEntites,
                gamePlatformCompanyHelperMap, releases));

        LocalDateTime endTime = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LOGGER.info("End --> (page {}) --> {}", page, endTime);
        LOGGER.info("TOTAL TIME --> (page {}) --> {} minutes", page, ChronoUnit.MINUTES.between(startTime, endTime));
    }

    private Consumer<? super GameEntity> createRelease(Map<String, List<PlatformEntity>> oldPlatformEntities,
            Map<String, List<PlatformEntity>> newPlatformEntities, Map<String, List<CompanyEntity>> oldCompanyEntites,
            Map<String, List<CompanyEntity>> newCompanyEntites, Map<String, List<GamePlatformCompanyHelper>> gamePlatformCompanyHelperMap,
            List<ReleaseEntity> releases) {
        return game -> {
            if (gamePlatformCompanyHelperMap.get(game.getFullName()) != null) {
                List<GamePlatformCompanyHelper> helperList = gamePlatformCompanyHelperMap.get(game.getFullName());

                LOGGER.debug("Found {} releases for game {}", helperList.size(), game.getFullName());
                for (GamePlatformCompanyHelper helper : helperList) {
                    CompanyEntity developer = getCompanyEntity(helper.getDeveloperName(), newCompanyEntites, oldCompanyEntites);
                    CompanyEntity publisher = getCompanyEntity(helper.getPublisherName(), newCompanyEntites, oldCompanyEntites);
                    PlatformEntity platform = getPlatformEntity(helper.getPlatformName(), newPlatformEntities, oldPlatformEntities);

                    String developerName = developer == null ? null : developer.getName();
                    String publisherName = publisher == null ? null : publisher.getName();
                    String platformName = platform == null ? null : platform.getFullName();

                    List<ReleaseEntity> releaseList = releases.stream().filter(r -> r.getGame().getId() == game.getId())
                            .collect(Collectors.toList());

                    List<ReleaseEntity> filteredReleases = releaseList.stream().filter(isSame(developerName, publisherName, platformName))
                            .collect(Collectors.toList());

                    populateReleaseEntity(helper, game, platform, developer, publisher, releaseList, filteredReleases);
                }
            }
        };
    }

    private Predicate<? super ReleaseEntity> isSame(String developer, String publisher, String platform) {
        return release -> {
            Boolean isDeveloperNull = (developer == null || developer.equals("") || developer.trim().length() != 0)
                    && (release.getDeveloper() == null || release.getDeveloper().getName() == null
                            || release.getDeveloper().getName().equals("") || release.getDeveloper().getName().trim().length() != 0);
            Boolean isDeveloperSame = developer != null
                    && developer.equalsIgnoreCase(release.getDeveloper() == null ? null : release.getDeveloper().getName());

            Boolean isPublisherNull = (publisher == null || publisher.equals("") || publisher.trim().length() != 0)
                    && (release.getPublisher() == null || release.getPublisher().getName() == null
                            || release.getPublisher().getName().equals("") || release.getPublisher().getName().trim().length() != 0);
            Boolean isPublisherSame = publisher != null
                    && publisher.equalsIgnoreCase(release.getPublisher() == null ? null : release.getPublisher().getName());

            Boolean isPlatformNull = (platform == null || platform.equals("") || platform.trim().length() != 0)
                    && (release.getPlatform() == null || release.getPlatform().getFullName() == null
                            || release.getPlatform().getFullName().equals("") || release.getPlatform().getFullName().trim().length() != 0);
            Boolean isPlatformSame = platform != null
                    && platform.equalsIgnoreCase(release.getPlatform() == null ? null : release.getPlatform().getFullName());

            return (isDeveloperNull || isDeveloperSame) && (isPublisherNull || isPublisherSame) && (isPlatformNull || isPlatformSame);
        };
    }

    private void populateReleaseEntity(GamePlatformCompanyHelper helper, GameEntity game, PlatformEntity platform, CompanyEntity developer,
            CompanyEntity publisher, List<ReleaseEntity> releases, List<ReleaseEntity> releasesByDevPub) {
        if (releasesByDevPub.isEmpty()) {
            ReleaseEntity releaseEntity = new ReleaseEntity();

            releaseEntity.setGame(game);

            if (helper.getReleaseDate() == null) {
                if (helper.getReleaseYear() != null && helper.getReleaseYear().length() == 4) {
                    releaseEntity.setReleaseDate(LocalDateTime.parse(helper.getReleaseYear() + "-01-01T00:00:00"));
                } else {
                    return;
                }
            } else {
                releaseEntity.setReleaseDate(LocalDateTime.parse(helper.getReleaseDate().substring(0, 19)));
            }

            releaseEntity.setPlatform(platform);
            releaseEntity.setRegion(regionDAO.findByPK(1L));
            releaseEntity.setType(ObjectType.GAME.getValue());
            releaseEntity.setCreated(LocalDateTime.now());
            releaseEntity.setCreatedBy(IRMA);
            releaseEntity.setUuid(UUID.randomUUID().toString());
            releaseEntity.setDeveloper(developer);
            releaseEntity.setPublisher(publisher);

            releaseDAO.persist(releaseEntity);
        } else if (!releases.isEmpty()) {
            for (ReleaseEntity releaseEntity : releases) {
                releaseEntity.setModified(LocalDateTime.now());
                releaseEntity.setModifiedBy(IRMA);

                releaseDAO.merge(releaseEntity);
            }

        }

    }

    private PlatformEntity getPlatformEntity(String platformName, Map<String, List<PlatformEntity>> newPlatformEntities,
            Map<String, List<PlatformEntity>> oldPlatformEntities) {
        if (newPlatformEntities.get(platformName) != null) {
            return newPlatformEntities.get(platformName).get(0);
        } else if (oldPlatformEntities.get(platformName) != null) {
            return oldPlatformEntities.get(platformName).get(0);
        } else {
            return null;
        }
    }

    private Consumer<? super TransferGameHelper> transferGame(Map<String, List<GameEntity>> gameEntities,
            Map<String, List<PlatformEntity>> platformEntities, Map<String, List<CompanyEntity>> companyEntites,
            List<CompanyEntity> companiesToAdd, List<CompanyEntity> companiesToEdit, Map<String, String> companyNamesMap,
            List<PlatformEntity> platformsToAdd, List<PlatformEntity> platformsToEdit, List<GameEntity> gamesToAdd,
            List<GameEntity> gamesToEdit, List<GamePlatformCompanyHelper> releaseHelper) {
        return transferGame -> {
            GameEntity gameEntity = new GameEntity();
            String developer = checkCompanyName(transferGame.getDeveloper(), companyEntites, companiesToAdd, companiesToEdit,
                    companyNamesMap);
            String manufacturer = checkCompanyName(transferGame.getPublisher(), companyEntites, companiesToAdd, companiesToEdit,
                    companyNamesMap);
            String platform = createPlatform(new TransferPlatformEntity(transferGame.getPlatform()), platformEntities, platformsToAdd,
                    platformsToEdit, null);

            if (gameEntities.get(transferGame.getName()) == null) {
                gameEntity.setCreated(LocalDateTime.now());
                gameEntity.setCreatedBy(IRMA);
                gameEntity.setFullName(transferGame.getName());
                gameEntity.setGenre(transferGame.getGenres());
                LOGGER.info("Name -> {}", transferGame.getName());
                gameEntity.setAbbriviation(makeAbbreviation(transferGame.getName()));
                LOGGER.info("Abbr -> {}", gameEntity.getAbbriviation());
                gameEntity.setInformation(createGameInformation(transferGame));

                if (manufacturer != null) {
                    gameEntity.setOutlineText(String.format("This game was made by %s.", manufacturer));
                }

                gamesToAdd.add(gameEntity);
                LOGGER.debug("Created Game --> {}", gameEntity.getFullName());
            } else {
                gameEntity = gameEntities.get(transferGame.getName()).get(0);
                gameEntity.setModified(LocalDateTime.now());
                gameEntity.setModifiedBy(IRMA);
                gamesToEdit.add(gameEntity);
                LOGGER.debug("Existing Game --> {}", gameEntity.getFullName());
            }

            releaseHelper.add(new GamePlatformCompanyHelper(gameEntity.getFullName(), platform, developer, manufacturer,
                    transferGame.getReleaseDate(), transferGame.getReleaseYear()));

        };
    }

    private String createGameInformation(TransferGameHelper transferGame) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(String.format("<p>%s<p><br/>", transferGame.getOverview() != null ? transferGame.getOverview() : NO_INFORMATION));
        stringBuilder.append("<table>");
        stringBuilder.append("<tr> <th>Element</th> <th>Information</th> </tr>");
        stringBuilder.append(String.format("<tr> <td>Cooperative</td> <td>%s</td> </tr>",
                transferGame.getCooperative() != null ? (Boolean.parseBoolean(transferGame.getCooperative()) ? "Yes" : "No")
                        : NO_INFORMATION));
        stringBuilder.append(String.format("<tr> <td>Community rating</td> <td>%s</td> </tr>",
                transferGame.getCommunityRating() != null ? transferGame.getCommunityRating() : NO_INFORMATION));
        stringBuilder.append(String.format("<tr> <td>Release type</td> <td>%s</td> </tr>",
                transferGame.getReleaseType() != null ? transferGame.getReleaseType() : NO_INFORMATION));
        stringBuilder.append(String.format("<tr> <td>Max players</td> <td>%s</td> </tr>",
                transferGame.getMaxPlayers() != null ? transferGame.getMaxPlayers() : NO_INFORMATION));
        if (transferGame.getVideoUrl() != null) {
            stringBuilder
                    .append(String.format("<tr> <td>Video</td> <td><a href=\"%s\">Video link</a></td> </tr>", transferGame.getVideoUrl()));
        } else {
            stringBuilder.append(String.format("<tr> <td>Video</td> <td>%s</td> </tr>", "No video link."));
        }

        if (transferGame.getWikipediaUrl() != null) {
            stringBuilder.append(
                    String.format("<tr> <td>Wikipedia</td> <td><a href=\"%s\">Wikipedia link</a></td> </tr>", transferGame.getVideoUrl()));
        } else {
            stringBuilder.append(String.format("<tr> <td>Wikipedia</td> <td>%s</td> </tr>", "No wikipedia link."));
        }
        stringBuilder.append("</table>");

        return stringBuilder.toString();
    }

    private Map<String, String> createCompanyNamesMap(List<TransferCompanyEntity> transferCompanyEntities) {
        Map<String, String> companyNamesMap = new HashMap<>();

        for (TransferCompanyEntity transferCompanyEntity : transferCompanyEntities) {
            String rootName = transferCompanyEntity.getRootName();

            Map<String, String> namesMap = Arrays.stream(transferCompanyEntity.getAllNames().split("#"))
                    .collect(Collectors.toMap(n -> n, n -> rootName));

            companyNamesMap.putAll(namesMap);
        }

        return companyNamesMap;
    }

}
