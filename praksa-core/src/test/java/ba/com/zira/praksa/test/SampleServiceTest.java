package ba.com.zira.praksa.test;

import static org.testng.Assert.assertEquals;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.Request;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.model.Filter;
import ba.com.zira.commons.model.User;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.SampleService;
import ba.com.zira.praksa.api.model.SampleModel;
import ba.com.zira.praksa.api.response.SampleResponse;
import ba.com.zira.praksa.dao.SampleDAO;
import ba.com.zira.praksa.test.configuration.BasicTestConfiguration;
import ba.com.zira.praksa.test.configuration.ApplicationTestConfiguration;
import ba.com.zira.praksa.core.validation.SampleRequestValidation;
import ba.com.zira.praksa.core.impl.SampleServiceImpl;
import ba.com.zira.praksa.mapper.SampleModelMapper;

@ContextConfiguration(classes = ApplicationTestConfiguration.class)
public class SampleServiceTest extends BasicTestConfiguration {

    @Autowired
    private SampleModelMapper sampleMapper;

    private SampleDAO sampleDAO;
    private RequestValidator requestValidator;
    private SampleRequestValidation sampleRequestValidation;
    private SampleService sampleService;

    @BeforeMethod
    public void beforeMethod() throws ApiException {
        this.requestValidator = Mockito.mock(RequestValidator.class);
        this.sampleDAO = Mockito.mock(SampleDAO.class);
        this.sampleRequestValidation = Mockito.mock(SampleRequestValidation.class);
        this.sampleService = new SampleServiceImpl(requestValidator, sampleDAO, sampleMapper);
    }
    
    @Test
    public void createTemplateMandatoryFields() {
        try {
            SampleResponse sampleModel = new SampleResponse();
            Request request = new Request();
            PagedPayloadResponse<SampleResponse> response = sampleService.find(request);

            Mockito.verify(sampleDAO).persist(Mockito.any());
            //assert
        } catch (ApiException e) {
            Assert.fail();
        }
    }

}
