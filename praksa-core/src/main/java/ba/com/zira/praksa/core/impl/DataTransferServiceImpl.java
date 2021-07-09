package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import ba.com.zira.praksa.api.DataTransferService;
import ba.com.zira.praksa.dao.CompanyDAO;
import ba.com.zira.praksa.dao.PlatformDAO;
import ba.com.zira.praksa.dao.TransferCompanyDAO;
import ba.com.zira.praksa.dao.TransferPlatformDAO;
import ba.com.zira.praksa.dao.model.CompanyEntity;
import ba.com.zira.praksa.dao.model.PlatformEntity;
import ba.com.zira.praksa.dao.model.TransferCompanyEntity;
import ba.com.zira.praksa.dao.model.TransferPlatformEntity;

@Service
public class DataTransferServiceImpl implements DataTransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataTransferServiceImpl.class);
    TransferPlatformDAO transferPlatformDAO;
    TransferCompanyDAO transferCompanyDAO;
    PlatformDAO platformDAO;
    CompanyDAO companyDAO;

    public DataTransferServiceImpl(TransferPlatformDAO transferPlatformDAO, TransferCompanyDAO transferCompanyDAO, PlatformDAO platformDAO,
            CompanyDAO companyDAO) {
        super();
        this.transferPlatformDAO = transferPlatformDAO;
        this.transferCompanyDAO = transferCompanyDAO;
        this.platformDAO = platformDAO;
        this.companyDAO = companyDAO;
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

        Map<String, String> companyNamesMap = new HashMap<>();

        for (TransferCompanyEntity transferCompanyEntity : transferCompanyEntities) {
            String rootName = transferCompanyEntity.getRootName();
            List<String> names = Arrays.asList(transferCompanyEntity.getAllNames().split("#"));

            for (String name : names) {
                companyNamesMap.put(name, rootName);
            }
        }

        transferPlatformEntities.stream().forEach(createPlatform(platformEntities, companyEntites, companiesToAdd, companiesToEdit,
                companyNamesMap, platformsToAdd, platformsToEdit));

        return null;
    }

    private Consumer<? super TransferPlatformEntity> createPlatform(Map<String, List<PlatformEntity>> platformEntities,
            Map<String, List<CompanyEntity>> companyEntites, List<CompanyEntity> companiesToAdd, List<CompanyEntity> companiesToEdit,
            Map<String, String> companyNamesMap, List<PlatformEntity> platformsToAdd, List<PlatformEntity> platformsToEdit) {
        return transferPlatform -> {
            PlatformEntity platformEntity = new PlatformEntity();

            if (platformEntities.get(transferPlatform.getName()) == null) {
                platformEntity.setCreated(LocalDateTime.now());
                platformEntity.setCreatedBy("DTS");
                platformEntity.setFullName(transferPlatform.getName());
                platformEntity.setAbbriviation(makeAbbrevation(transferPlatform.getName()));
                platformEntity.setAliases(transferPlatform.getPlatformAlternateName());
                platformEntity.setInformation(createPlatformInformation(transferPlatform));
                checkCompanyName(transferPlatform.getDeveloper(), companyEntites, companiesToAdd, companiesToEdit, companyNamesMap);

                platformsToAdd.add(platformEntity);
            } else {
                platformEntity = platformEntities.get(transferPlatform.getName()).get(0);
                platformEntity.setModified(LocalDateTime.now());
                platformEntity.setModifiedBy("DTS");
                platformsToEdit.add(platformEntity);
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

    private void checkCompanyName(String companyName, Map<String, List<CompanyEntity>> companyEntites, List<CompanyEntity> companiesToAdd,
            List<CompanyEntity> companiesToEdit, Map<String, String> companyNamesMap) {
        String rootCompanyName = null;
        if (companyName != null) {
            rootCompanyName = companyNamesMap.get(companyName);

            if (rootCompanyName == null) {
                companiesToAdd.add(createCompany(companyName));
            } else if (companyEntites.get(rootCompanyName) == null) {
                companiesToAdd.add(createCompany(rootCompanyName));
            } else {
                LOGGER.info("Existing Company --> {}", companyEntites.get(rootCompanyName).get(0));
                companiesToEdit.add(companyEntites.get(rootCompanyName).get(0));
            }
        }
    }

    private CompanyEntity createCompany(String companyName) {
        CompanyEntity company = new CompanyEntity();
        company.setName(companyName);
        company.setCreated(LocalDateTime.now());
        company.setCreatedBy("DTS");
        // companyDAO.persist(company);
        LOGGER.info("Created Company --> {}", company.getName());

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
    public PayloadResponse<String> gameHUSToGameHUT(EmptyRequest request) throws ApiException {
        // TODO Auto-generated method stub
        return null;
    }

}
