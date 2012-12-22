package by.grodnosoft.swt.validation;

import org.junit.Before;
import org.junit.Test;

public class EmailValidatorTest {
	
	private EmailValidator validator;
	
	@Before
	public void setUp() throws Exception {
		validator = new EmailValidator();
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
	
	@Test
	public void validateCorrectEmails() throws Exception {
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("test@test.com"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("email.with.dots@test.host.with.dots"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("CAPITAL_LETTERS@CAPITAL-HOST.NAME"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("Mixed.Case.Letters@host.COM"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("Letters123WithNum456789@host014.by"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("456789465@55555.org"));
		ValidatorsTestSuite.assertValidationResultOK(
				validator, validator.validate("email-with-hyphen@host-with-hyphen.net"));
	}

	@Test
	public void validateIncorrectEmails() throws Exception {
		ValidatorsTestSuite.assertValidationResultERROR(
				validator, validator.validate("nohost@test"));
		ValidatorsTestSuite.assertValidationResultERROR(
				validator, validator.validate("no_at_sign.com"));
		ValidatorsTestSuite.assertValidationResultERROR(
				validator, validator.validate("wrong_symbols(@)host.com"));
		ValidatorsTestSuite.assertValidationResultERROR(
				validator, validator.validate("wrong+symbols@*host*.com"));
		ValidatorsTestSuite.assertValidationResultERROR(
				validator, validator.validate("illegal spaces@host. com"));
	}

}
