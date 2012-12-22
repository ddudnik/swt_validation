package by.grodnosoft.swt.validation;

import org.junit.Before;
import org.junit.Test;

public class NonEmptyValidatorTest {
	
	private NonEmptyValidator validator;

	@Before
	public void setUp() throws Exception {
		validator = new NonEmptyValidator();
	}
	
	@Test
	public void validateNullValue() throws Exception {
		ValidatorsTestSuite.assertValidationResultERROR(
				validator, validator.validate(null));
	}

	@Test
	public void validateEmptyValue() throws Exception {
		ValidatorsTestSuite.assertValidationResultERROR(
				validator, validator.validate(""));
	}

	@Test
	public void validateNonEmptyValue() throws Exception {
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("NonEmptyValue"));
	}
	
}
