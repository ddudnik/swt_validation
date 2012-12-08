package by.grodnosoft.swt.validation;

import java.util.regex.Pattern;

import by.grodnosoft.swt.validation.ValidationToolkit.IFieldValidator;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult.ValidationStatus;

public class RegexFieldValidator implements IFieldValidator{
	
	private Pattern pattern;
	
	public RegexFieldValidator(String regex) {
		this.pattern = Pattern.compile(regex);
	}

	@Override
	public ValidationResult validate(String valueToValidate) {
		if (valueToValidate != null && !valueToValidate.isEmpty()
				&& !pattern.matcher(valueToValidate).matches()) {
			return new ValidationResult(
					ValidationStatus.ERROR, getErrorMessage(valueToValidate), this);
		}
		return new ValidationResult(ValidationStatus.OK);
	}
	
	protected String getErrorMessage(String valueToValidate) {
		return String.format("Value \"%s\" does not match regular expression \"%s\"", valueToValidate, pattern.pattern());
	}
	
}
