package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.PersonService;
import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.dao.PersonDAO;
import lombok.AllArgsConstructor;

/**
 * PersonRequestValidation is used for validation of {@link PersonService}
 * requests.<br>
 * e.g. database validation needed
 *
 * @author zira
 *
 */

@Component
@AllArgsConstructor
public class PersonRequestValidation {

	private RequestValidator requestValidator;
	private PersonDAO personDAO;

	/**
	 * Validates find, findById, update, delete Person plan from
	 * {@link PersonService}.
	 *
	 * @param request               the {@link EntityRequest} to validate.
	 * @param validationRuleMessage name of the validation rule that is going to be
	 *                              used for validating {@link Person}
	 *
	 * @return {@link ValidationResponse}
	 */

	public ValidationResponse validatePersonRequest(final EntityRequest<Long> request,
			final String validationRuleMessage) {
		ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
		if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
			StringBuilder errorDescription = new StringBuilder();
			if (!personDAO.existsByPK(request.getEntity())) {
				errorDescription.append("Sample with id: ").append(request.getEntity()).append(" does not exist !");
			}
			validationResponse = requestValidator.createResponse(request, errorDescription);
		}

		return validationResponse;
	}

}
