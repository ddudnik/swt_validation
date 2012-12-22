package by.grodnosoft.swt.validation;


/**
 * Validates if field value is a valid at most 10-digit (country code not included) phone number,
 * which means it conforms to some of this phone number formats:
 * 
 * <ul>
 * 	<li>1234567890</li>
 * 	<li>123-456-7890</li>
 * 	<li>123.456.7890</li>
 * 	<li>123 456 7890</li>
 * 	<li>(123) 456 7890</li>
 * 	<li>+1 (123) 456-7890</li>
 * 	<li>1-123-456-7890</li>
 * </ul>
 * 
 * @author Denis Dudnik <deniska.dudnik@gmail.com>
 *
 */
public class PhoneNumberValidator extends RegexValidator {
	
	/**
	 * This regular expression was found here: http://blog.stevenlevithan.com/archives/validate-phone-number
	 * There are more regular expressions discussed in this article, this one is the most general one.
	 */
	public PhoneNumberValidator() {
		super("^(?:\\+?[0-9]{1,3}[-. ]?)?\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$");
	}

	@Override
	protected String getErrorMessage(String valueToValidate) {
		return String.format("Phone number \"%s\" is not valid", valueToValidate);
	}

}
