package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

        for (TransferPlatformEntity transferPlatform : transferPlatformEntities) {
            PlatformEntity platformEntity = new PlatformEntity();
            platformEntity.setFullName(transferPlatform.getName());
            platformEntity.setAbbriviation(makeAbbrevation(transferPlatform.getName()));
            checkCompanyName(transferPlatform.getDeveloper(), transferCompanyEntities, companyEntites);

        }
        return null;
    }

    private CompanyEntity checkCompanyName(String developer, List<TransferCompanyEntity> transferCompanyEntities,
            Map<String, List<CompanyEntity>> companyEntites) {
        String companyName = null;
        if (developer != null) {
            for (TransferCompanyEntity transferCompany : transferCompanyEntities) {
                String[] names = transferCompany.getAllNames().split("#");
                for (String name : names) {
                    if (name.equalsIgnoreCase(developer)) {
                        companyName = transferCompany.getRootName();
                        break;
                    }
                }
            }

            if (companyEntites.get(companyName) == null) {
                return createCompany(companyName);

            } else {
                LOGGER.info("Existing Company --> {}", companyEntites.get(companyName).get(0));
                return companyEntites.get(companyName).get(0);
            }
        } else {
            return null;
        }

    }

    private CompanyEntity createCompany(String companyName) {
        CompanyEntity company = new CompanyEntity();
        company.setName(companyName);
        company.setCreated(LocalDateTime.now());
        company.setCreatedBy("DTS");
        companyDAO.persist(company);
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
