package by.grodnosoft.swt.validation;

import java.util.regex.Pattern;

import by.grodnosoft.swt.validation.ValidationToolkit.IValidator;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult.ValidationStatus;

/**
 * Validates input value against specified regular expression.
 *
 * @author Denis Dudnik <deniska.dudnik@gmail.com>
 */
public class RegexValidator implements IValidator{
	
	private final Pattern pattern;
	
	public RegexValidator(String regex) {
		this.pattern = Pattern.compile(regex);
	}

	@Override
	public ValidationResult validate(String valueToValidate) {
		if (valueToValidate != null && !valueToValidate.isEmpty()
				&& !pattern.matcher(valueToValidate).matches()) {
			return new ValidationResult(
					ValidationStatus.ERROR, getErrorMessage(valueToValidate), this);
		}
		return new ValidationResult(ValidationStatus.OK, this);
	}
	
	protected String getErrorMessage(String valueToValidate) {
		return String.format("Value \"%s\" does not match regular expression \"%s\"", valueToValidate, pattern.pattern());
	}
	
}
