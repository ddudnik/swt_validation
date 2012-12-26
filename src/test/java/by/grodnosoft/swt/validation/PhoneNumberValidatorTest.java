package by.grodnosoft.swt.validation;

import org.junit.Before;
import org.junit.Test;

public class PhoneNumberValidatorTest {

    private PhoneNumberValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new PhoneNumberValidator();
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
    public void validateCorrectPhoneNumber() throws Exception {
        ValidatorsTestSuite.assertValidationResultOK(
                validator, validator.validate("123-456-7890"));
        ValidatorsTestSuite.assertValidationResultOK(
                validator, validator.validate("123.456.7890"));
        ValidatorsTestSuite.assertValidationResultOK(
                validator, validator.validate("123 456 7890"));
        ValidatorsTestSuite.assertValidationResultOK(
                validator, validator.validate("(123) 456 7890"));
        ValidatorsTestSuite.assertValidationResultOK(
                validator, validator.validate("+1 (123) 456-7890"));
        ValidatorsTestSuite.assertValidationResultOK(
                validator, validator.validate("1-123-456-7890"));
        ValidatorsTestSuite.assertValidationResultOK(
                validator, validator.validate("11234567890"));
    }

    @Test
    public void validateIncorrectPhoneNumber() throws Exception {
        ValidatorsTestSuite.assertValidationResultERROR(
                validator, validator.validate("123-456"));
        ValidatorsTestSuite.assertValidationResultERROR(
                validator, validator.validate("123..456..7890"));
        ValidatorsTestSuite.assertValidationResultERROR(
                validator, validator.validate("1234567890123456"));
        ValidatorsTestSuite.assertValidationResultERROR(
                validator, validator.validate("((123)) 456 7890"));
        ValidatorsTestSuite.assertValidationResultERROR(
                validator, validator.validate("-1 (123) 456-7890"));
        ValidatorsTestSuite.assertValidationResultERROR(
                validator, validator.validate("00000"));
    }

}
