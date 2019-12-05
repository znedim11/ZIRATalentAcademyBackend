package ba.com.zira.praksa.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import java.time.LocalDateTime;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.SpelEvaluationException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.test.configuration.BasicTestConfiguration;
import ba.com.zira.praksa.dao.SampleDAO;
import ba.com.zira.praksa.core.validation.SampleRequestValidation;
import ba.com.zira.praksa.api.model.SampleModel;

public class SampleRequestValidationTest extends BasicTestConfiguration {

    private static final String TEMPLATE_CODE = "TEST_1";

    @Autowired
    private RequestValidator requestValidator;

    private SampleDAO sampleDAO;
    private SampleRequestValidation validation;

    @BeforeMethod
    public void beforeMethod() throws ApiException {
        this.sampleDAO = Mockito.mock(SampleDAO.class);
        this.validation = new SampleRequestValidation(requestValidator, sampleDAO);
    }

    @Test
    public void validateCreateRequestMissingMadatoryFields() {
        ValidationResponse validationResponse = validation.validateUpdateSampleModelRequest(new EntityRequest<>(new SampleModel()),
                "create");

        assertEquals(validationResponse.getCode(), ResponseCode.REQUEST_INVALID);
        Mockito.verifyZeroInteractions(sampleDAO);
        assertEquals(validationResponse.getDescription(),
                "EntityRequest : entity:  name required! entity:  type required! entity:  status required! entity:  validFrom required! ");
    }

    @Test
    public void validateCreateRequestMissingEntity() {
        EntityRequest<SampleModel> request = new EntityRequest<>();
        assertThrows(SpelEvaluationException.class, () -> validation.validateUpdateSampleModelRequest(request, "create"));
        Mockito.verifyZeroInteractions(sampleDAO);
    }

    /**
     * Update
     */

    @Test
    public void validateUpdateRequestSampleNotFound() {
        Mockito.when(sampleDAO.existsByPK(1L)).thenReturn(false);

        ValidationResponse validationResponse = validation.validateUpdateSampleModelRequest(new EntityRequest<>(new SampleModel()),
                "update");

        assertEquals(validationResponse.getCode(), ResponseCode.REQUEST_INVALID);
        assertEquals(validationResponse.getDescription(), "message");
        Mockito.verify(sampleDAO).existsByPK(1L);
    }

}
