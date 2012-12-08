package by.grodnosoft.swt.validation;

import by.grodnosoft.swt.validation.ValidationToolkit.IFieldValidator;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult.ValidationStatus;

/**
 * Validates if field value is NOT empty
 * 
 * @author Denis Dudnik <deniska.dudnik@gmail.com>
 *
 */
public class NonEmptyFieldValidator implements IFieldValidator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidationResult validate(String valueToValidate) {
		if (valueToValidate == null || valueToValidate.isEmpty()) {
			return new ValidationResult(
					ValidationStatus.ERROR, "Value should be non-empty!", this);
		}
		return new ValidationResult(ValidationStatus.OK);
	}

}
