package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.dao.SampleDAO;
import ba.com.zira.praksa.api.SampleService;
import ba.com.zira.praksa.api.model.SampleModel; 

/**
 * SampleRequestValidation is used for validation of
 * {@link SampleService} requests.<br>
 * e.g. database validation needed
 * 
 * @author zira
 *
 */
@Component("sampleRequestValidation")
public class SampleRequestValidation {

    private RequestValidator requestValidator;
    private SampleDAO sampleDAO;

    public SampleRequestValidation(final RequestValidator requestValidator, SampleDAO sampleDAO) {
        this.requestValidator = requestValidator;
        this.sampleDAO = sampleDAO;
    }
    
    /**
     * Validates update SampleModel plan from {@link SampleService}.
     *
     * @param request
     *            the {@link EntityRequest} to validate.
     * @param validationRuleMessage
     *            name of the validation rule that is going to be used for
     *            validating {@link SampleModel}
     * 
     * @return {@link ValidationResponse}
     */
    public ValidationResponse validateUpdateSampleModelRequest(final EntityRequest<SampleModel> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!sampleDAO.existsByPK(request.getEntity().getId())) {
                errorDescription.append("Sample with id: ").append(request.getEntity().getId()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

}
