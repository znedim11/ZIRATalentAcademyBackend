package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.dao.ReleaseDAO;

@Component("releaseRequestValidation")
public class ReleaseRequestValidation
{

	private RequestValidator requestValidator;
	private ReleaseDAO releaseDAO;

	public ReleaseRequestValidation(final RequestValidator requestValidator, final ReleaseDAO releaseDAO)
	{
		this.requestValidator = requestValidator;
		this.releaseDAO = releaseDAO;
	}

	public ValidationResponse validateUpdateReleaseRequest(final EntityRequest<ReleaseRequest> request, final String validationRuleMessage)
	{
		ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
		if (validationResponse.getResponseCode() == ResponseCode.OK.getCode())
		{
			StringBuilder errorDescription = new StringBuilder();
			/*
			 * if (!releaseDAO.existsByPK(request.getEntity().getUuid()) {
			 * errorDescription.append("Sample with id: ").append(request.
			 * getEntity().getUuid()).append(" does not exist !"); }
			 */
			validationResponse = requestValidator.createResponse(request, errorDescription);
		}

		return validationResponse;
	}

}