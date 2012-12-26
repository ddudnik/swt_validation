package by.grodnosoft.swt.validation;

import org.junit.Test;

public class RegexValidatorTest {

    @Test
    public void validateNullValue() throws Exception {
        RegexValidator validator = new RegexValidator("\\s");
        ValidatorsTestSuite.assertValidationResultOK(
                validator, validator.validate(null));
    }

    @Test
    public void validateEmptyValue() throws Exception {
        RegexValidator validator = new RegexValidator("\\s");
        ValidatorsTestSuite.assertValidationResultOK(
                validator, validator.validate(""));
    }

    @Test
    public void validateCorrectRegex() throws Exception {
        RegexValidator validator = new RegexValidator("[0-9]+");
        ValidatorsTestSuite.assertValidationResultOK(
                validator, validator.validate("12345"));

        validator = new RegexValidator("[A-Za-z0-9]?.*");
        ValidatorsTestSuite.assertValidationResultOK(
                validator, validator.validate("a456"));

        validator = new RegexValidator("^aaa.*bbb$");
        ValidatorsTestSuite.assertValidationResultOK(
                validator, validator.validate("aaa123123bbb"));

    }

    @Test
    public void validateIncorrectRegex() throws Exception {
        RegexValidator validator = new RegexValidator("[0-9]+");
        ValidatorsTestSuite.assertValidationResultERROR(
                validator, validator.validate("asd"));

        validator = new RegexValidator("[A-Za-z0-9]{1}.*");
        ValidatorsTestSuite.assertValidationResultERROR(
                validator, validator.validate("--asd"));

        validator = new RegexValidator("^aaa.*bbb$");
        ValidatorsTestSuite.assertValidationResultERROR(
                validator, validator.validate("aaabb"));

    }
}
