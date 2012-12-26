package by.grodnosoft.swt.validation;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import by.grodnosoft.swt.validation.ValidationToolkit.IValidator;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult;
import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult.ValidationStatus;

@RunWith(Suite.class)
@SuiteClasses({
        NonEmptyValidatorTest.class, EmailValidatorTest.class,
        NumericValidatorTest.class, PhoneNumberValidatorTest.class,
        RegexValidatorTest.class
})
public class ValidatorsTestSuite {

	protected static void assertValidationResultOK(
			IValidator validator, ValidationResult result) throws Exception {
		ValidatorsTestSuite.basicValidationResultAssert(validator, result, ValidationStatus.OK);
	}

	protected static void assertValidationResultERROR(
			IValidator validator, ValidationResult result) throws Exception {
		ValidatorsTestSuite.basicValidationResultAssert(validator, result, ValidationStatus.ERROR);
	}

	private static void basicValidationResultAssert(
			IValidator validator, ValidationResult result, ValidationStatus desiredStatus) throws Exception {
		Assert.assertNotNull(result);
		Assert.assertFalse(result.isCompound());
		Assert.assertEquals(validator, result.getValidator());
		Assert.assertEquals(desiredStatus, result.getStatus());
		if (ValidationStatus.ERROR.equals(result.getStatus())) {
			Assert.assertNotNull(result.getMessage());
			Assert.assertFalse(result.getMessage().isEmpty());
		}
	}
	
}
