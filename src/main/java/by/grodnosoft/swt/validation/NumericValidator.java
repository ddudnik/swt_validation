package by.grodnosoft.swt.validation;

import java.lang.reflect.Method;

import by.grodnosoft.swt.validation.ValidationToolkit.IValidator;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult.ValidationStatus;


/**
 * Validates if field value is a {@link Number}
 * TODO: this validator could be improved by checking if numeric value is between max and min values.
 * 
 * @author Denis Dudnik <deniska.dudnik@gmail.com>
 *
 */
public class NumericValidator implements IValidator {
	
	private Class<? extends Number> numberType = Double.class;
	private int radix = 10;
	
	/**
	 * Set type of expected number, one of {@link Number} subclasses.
	 * Default number type is {@link Double}.
	 * 
	 * @param numberType class object which is subclass to {@link Number}, can't be <code>null</code>
	 * @throws IllegalArgumentException if numberType is <code>null</code> 
	 */
	public void setNumberType(Class<? extends Number> numberType) {
		if (numberType == null) {
			throw new IllegalArgumentException("NumberType could not be null!");
		}
		this.numberType = numberType;
	}
	
	/**
	 * Set radix of expected number, default is 10.
	 * 
	 * @param radix integer specifying the radix
	 * @throws IllegalArgumentException if radix is less than {@link Character#MIN_RADIX} or bigger than {@link Character#MAX_RADIX}
	 */
	public void setRadix(int radix) {
		if (radix < Character.MIN_RADIX
				|| radix > Character.MAX_RADIX) {
			throw new IllegalArgumentException(
					String.format("Radix should be between %d and %d!", Character.MIN_RADIX, Character.MAX_RADIX));
		}
		this.radix = radix;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidationResult validate(String valueToValidate) {
		if (valueToValidate != null && !valueToValidate.isEmpty()) {
			try {
				String modifiedValue = valueToValidate.trim().replaceAll("\\s", "").replaceFirst("\\+", "");
				Number num = createNumberFromString(modifiedValue);
				if (!numberType.equals(num.getClass())) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				return new ValidationResult(
						ValidationStatus.ERROR, 
						String.format("Value %s should be a %s number!", valueToValidate, getNumberTypeName()), this);
			}
		}
		return new ValidationResult(ValidationStatus.OK, this);
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
			// do nothing,
			// probably no commons-lang was found in classpath, 
			// proceed with our simple number guessing heuristics
		}
		
		return parseNumber(valueToValidate);
	}
	
	private Number parseNumber(String valueToValidate) {
		if (Integer.class.equals(numberType)) {
			return Integer.parseInt(valueToValidate, radix);
		} else if (Float.class.equals(numberType)) {
			return Float.parseFloat(valueToValidate);
		} else if (Double.class.equals(numberType)) {
			return Double.parseDouble(valueToValidate);
		} else if (Long.class.equals(numberType)) {
			return Long.parseLong(valueToValidate, radix);
		} else if (Short.class.equals(numberType)) {
			return Short.parseShort(valueToValidate, radix);
		} else if (Byte.class.equals(numberType)) {
			return Byte.parseByte(valueToValidate, radix);
		}
		throw new IllegalArgumentException(
				String.format("Unknown numberType %s!", getNumberTypeName()));
	}
}
