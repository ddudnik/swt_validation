package by.grodnosoft.swt.validation;

import by.grodnosoft.swt.validation.ValidationToolkit.IFieldValidator;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult.ValidationStatus;

public class RegexFieldValidator implements IFieldValidator{
	
	private String regex;
	
	public RegexFieldValidator(String regex) {
		this.regex = regex;
	}

	@Override
	public ValidationResult validate(String valueToValidate) {
		if (valueToValidate != null && !valueToValidate.isEmpty()
				&& !valueToValidate.matches(regex)) {
			return new ValidationResult(
					ValidationStatus.ERROR, getErrorMessage(valueToValidate), this);
		}
		return new ValidationResult(ValidationStatus.OK);
	}
	
	protected String getErrorMessage(String valueToValidate) {
		return String.format("Value \"%s\" does not match regular expression \"%s\"", valueToValidate, regex);
	}
	
}
