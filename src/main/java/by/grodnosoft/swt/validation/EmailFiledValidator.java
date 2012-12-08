package by.grodnosoft.swt.validation;

/**
 * Validates if field value is a valid email address.
 * 
 * @author Denis Dudnik <deniska.dudnik@gmail.com>
 *
 */
public class EmailFiledValidator extends RegexFieldValidator {

	public EmailFiledValidator() {
		super("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}

	@Override
	protected String getErrorMessage(String valueToValidate) {
		return String.format("Email address \"%s\" is not valid", valueToValidate);
	}

}
