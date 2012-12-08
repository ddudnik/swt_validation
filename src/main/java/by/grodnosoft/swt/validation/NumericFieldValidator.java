package by.grodnosoft.swt.validation;

import java.lang.reflect.Method;

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
		try {
			Number num = createNumberFromString(valueToValidate);
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

	private Number createNumberFromString(String valueToValidate) {
		try {
			Class<?> numberUtilsClass = Class.forName("org.apache.commons.lang.math.NumberUtils");
			Method createNumberMethod = numberUtilsClass.getDeclaredMethod("createNumber", String.class);
			return (Number) createNumberMethod.invoke(null, valueToValidate);
		} catch (NumberFormatException e) {
			throw e;
		} catch (Exception e) {
			// no commons-lang, proceed with our simple method for getting Number from a String
		}
		// TODO: add number guessing heuristics
		return null;
	}
}
