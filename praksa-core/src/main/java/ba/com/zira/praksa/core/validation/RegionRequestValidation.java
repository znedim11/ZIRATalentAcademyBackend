/**
 *
 */
package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.dao.RegionDAO;

/**
 * @author zira
 *
 */

@Component("regionRequestValidation")
public class RegionRequestValidation {
    RequestValidator requestValidator;
    RegionDAO regionDAO;

    public RegionRequestValidation(RequestValidator requestValidator, RegionDAO regionDAO) {
        super();
        this.requestValidator = requestValidator;
        this.regionDAO = regionDAO;
    }

    public ValidationResponse validateRegionExists(final EntityRequest<Long> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!regionDAO.existsByPK(request.getEntity())) {
                errorDescription.append("Region with id ").append(request.getEntity()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }
}
