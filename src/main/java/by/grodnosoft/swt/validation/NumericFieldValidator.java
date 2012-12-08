package by.grodnosoft.swt.validation;

import by.grodnosoft.swt.validation.ValidationToolkit.IFieldValidator;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult.ValidationStatus;


/**
 * Validates if field value is a {@link Number}
 * TODO: this validator could be improved by checking if numeric value is between max and min values.
 * 
 * @author Denis Dudnik <deniska.dudnik@gmail.com>
 *
 */
public class NumericFieldValidator implements IFieldValidator {
	
	private Class<? extends Number> numberType = Integer.class;
	
	public void setNumberType(Class<? extends Number> numberType) {
		this.numberType = numberType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidationResult validate(String valueToValidate) {
		if (valueToValidate == null || valueToValidate.isEmpty()) {
			return new ValidationResult(
					ValidationStatus.ERROR, 
					String.format("Value should be a %s number!", getNumberTypeName()), this);
		}
		Number num = null;
		try {
			// TODO
//			num = NumberUtils.createNumber(valueToValidate);
			if (!numberType.isInstance(num)) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			return new ValidationResult(
					ValidationStatus.ERROR, 
					String.format("Value %s should be a %s number!", valueToValidate, getNumberTypeName()), this);
		}
		return new ValidationResult(ValidationStatus.OK);
	}
	
	
	private String getNumberTypeName() {
		String className = numberType.getName();
		return className.substring(className.lastIndexOf("."));
	}

}
