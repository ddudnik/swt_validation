package by.grodnosoft.swt.validation;

import java.text.DecimalFormatSymbols;

import org.junit.Before;
import org.junit.Test;

public class NumericValidatorTest {
	
	private NumericValidator validator;
	
	@Before
	public void setUp() throws Exception {
		validator = new NumericValidator();
	}
	
	@Test
	public void validateNullValue() throws Exception {
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(null));
	}

	@Test
	public void validateEmptyValue() throws Exception {
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(""));
	}

	@Test(expected=IllegalArgumentException.class)
	public void setIllegalRadix() throws Exception {
		validator.setRadix(1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void setIllegalNumberType() throws Exception {
		validator.setNumberType(null);
	}
	
	@Test
	public void validateCorrectIntegers() throws Exception {
		validator.setNumberType(Integer.class);
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("123456"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("000123"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("   123   "));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("123 456"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("-145"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("+456"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(String.valueOf(Integer.MAX_VALUE)));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(String.valueOf(Integer.MIN_VALUE)));
	}
	
	@Test
	public void validateCorrectDoubles() throws Exception {
		char separator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(
						String.format("123%c456", separator)));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(
						String.format("000%c123", separator)));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(
						String.format("   1%c23   ", separator)));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(
						String.format("1 123%c456", separator)));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(
						String.format("-1%c45", separator)));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(
						String.format("+45%c6", separator)));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(String.valueOf(Double.MAX_VALUE)));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(String.valueOf(Double.MIN_VALUE)));
	}

	@Test
	public void validateCorrectLongs() throws Exception {
		validator.setNumberType(Long.class);
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("123456789 123456789"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("-10000000000000"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("999999999999999999"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(String.valueOf(Long.MAX_VALUE)));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(String.valueOf(Long.MIN_VALUE)));
	}

	@Test
	public void validateCorrectBytes() throws Exception {
		validator.setNumberType(Byte.class);
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("12"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("-45"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("0"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(String.valueOf(Byte.MAX_VALUE)));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate(String.valueOf(Byte.MIN_VALUE)));
	}

}
