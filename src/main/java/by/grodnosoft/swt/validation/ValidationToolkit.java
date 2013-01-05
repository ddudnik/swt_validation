package by.grodnosoft.swt.validation;

import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult.ValidationStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides utility methods for adding different validators to UI fields
 * and gathering validation feedback via {@link ValidationCallback}s.
 * 
 * @author Denis Dudnik <deniska.dudnik@gmail.com>
 *
 */
public class ValidationToolkit {

    private static final String GET_TEXT_METHOD_NAME = "getText";

	public static final IValidator NON_EMPTY = new NonEmptyValidator();
	public static final IValidator NUMERIC = new NumericValidator();
	public static final IValidator EMAIL = new EmailValidator();
	public static final IValidator PHONE_NUMBER = new PhoneNumberValidator();

    /**
	 * Common interface for all validators
	 */
	public interface IValidator {
		ValidationResult validate(String valueToValidate);
	}

	/**
	 * Interface for validation callbacks
	 */
	public interface ValidationCallback {
		void validationDone(ValidationResult result);
	}

	/**
	 * Just to distinguish between modify listeners added for validation purposes 
	 * and other ones which might be added to UI fields somewhere else.  
	 */
	private interface ValidationModifyListener extends Listener{ }

	/**
	 * Represents result of a validation, could be compound when validating fields
	 * configured via the same {@link ValidationContext}
	 */
	public static class ValidationResult {

		public enum ValidationStatus {
			OK, WARNING, ERROR;	// order should stay the same
			private boolean lessThan(ValidationStatus status) {
				return ordinal() < status.ordinal();
			}
		}
		
		private ValidationStatus status;
		private final String message;
		private final IValidator validator;
		private Control field;
		private Collection<ValidationResult> childValidationResults;
		
		public ValidationResult(ValidationStatus status) {
			this(status, null, null);
		}

		public ValidationResult(ValidationStatus status, IValidator validator) {
			this(status, null, validator);
		}

		public ValidationResult(ValidationStatus status, String message, IValidator validator) {
			this.status = status;
			this.message = message;
			this.validator = validator;
			this.childValidationResults = new ArrayList<ValidationResult>();
		}
		
		public IValidator getValidator() {
			return validator;
		}
		
		public ValidationStatus getStatus() {
			return status;
		}
		
		public Control getField() {
			return field;
		}
		
		public String getMessage() {
			return message;
		}
		
		public Collection<ValidationResult> getChildren() {
			return childValidationResults;
		}
		
		public boolean isCompound() {
			return !childValidationResults.isEmpty();
		}
		
		private void addChildResult(ValidationResult result) {
			if (getStatus().lessThan(result.getStatus())) {
				this.status = result.getStatus();
			}
			this.childValidationResults.add(result);
		}
		
		private void setField(Control field) {
			this.field = field;
		}
	}

	/**
	 * Use this class to setup validation when your UI fields represent a 'form'
	 * (bunch of fields logically connected) which should be validated as a whole. 
	 * Otherwise use methods from {@link ValidationToolkit} for single fields. 
	 */
	public static class ValidationContext {
		
		private ValidationCallback callback;
		private Map<Control, IValidator[]> fieldConfig;

		public ValidationContext(ValidationCallback callback) {
			if (callback == null) {
				throw new IllegalArgumentException("Callback can't be null!");
			}
			this.callback = callback;
			this.fieldConfig = new HashMap<Control, IValidator[]>();
		}

		public void setupField(Control field, IValidator validator) {
			setupField(field, new IValidator[] {validator});
		}
		
		public void setupField(Control field, IValidator[] validators) {
			if (field == null) {
				throw new IllegalArgumentException("Field can't be null!");
			}
			fieldConfig.put(field, validators);
		}

		public void setupFields(Control[] fields, IValidator validator) {
			setupFields(fields, new IValidator[] {validator});
		}

		public void setupFields(Control[] fields, IValidator[] validators) {
			for (Control field : fields) {
				fieldConfig.put(field, validators);
			}
		}
		
	}
	
	
	
	/**
	 * Setup validation for a bunch 'connected' UI fields represented by given {@link ValidationContext}
	 * 
	 * @param validationContext can't be <code>null</code>
	 * @throws IllegalArgumentException if validationContext is <code>null</code>
	 */
	public static void setupValidation(final ValidationContext validationContext) {
		if (validationContext == null) {
			throw new IllegalArgumentException("ValidationContext can't be null!");
		}
		for (final Control field : validationContext.fieldConfig.keySet()) {
			field.addListener(SWT.Modify, new ValidationModifyListener() {
				@Override
				public void handleEvent(Event event) {
					ValidationResult result = new ValidationResult(ValidationStatus.OK);
					for (Control field : validationContext.fieldConfig.keySet()) {
						IValidator[] validators = validationContext.fieldConfig.get(field);
						for (IValidator validator : validators) {
							ValidationResult childResult = validator.validate(getTextFromField(field));
							childResult.setField(field);
							result.addChildResult(childResult);
						}
					}
					validationContext.callback.validationDone(result);
				}
			});
		}
	}

	/**
	 * Setup validation for given text field.
	 * 
	 * @param field control which input will be validated, not <code>null</code>
	 * @param validator performs actual validation of input value, not <code>null</code>
	 * @param callback called with {@link ValidationResult} when validation is done, could be <code>null</code>
	 * @throws IllegalArgumentException if either field or validator are <code>null</code>
	 */
	public static void setupValidation(
			Control field, final IValidator validator, final ValidationCallback callback) {
		setupValidation(field, new IValidator[]{validator}, callback);
	}
	
	/**
	 * Setup validation for given text field
	 * 
	 * @param field control which input will be validated, not <code>null</code>
	 * @param validators bunch of validators to performs actual validation of input value, one by one, not <code>null</code> nor empty
	 * @param callback called with {@link ValidationResult} when validation is done, could be <code>null</code>
	 * @throws IllegalArgumentException if either field is <code>null</code> or validators is <code>null</code> or empty
	 */
	public static void setupValidation(
			final Control field, final IValidator[] validators, final ValidationCallback callback) {
		if (field == null) {
			throw new IllegalArgumentException("Text field can't be null!");
		}
		if (validators == null || validators.length == 0) {
			throw new IllegalArgumentException("At least one field validator should be specified!");
		}
		field.addListener(SWT.Modify, new ValidationModifyListener() {
			@Override
			public void handleEvent(Event event) {
				ValidationResult result;
				String text = getTextFromField(field);
				if (validators.length == 1) {
					result = validators[0].validate(text);
					result.setField(field);
				} else {
					result = new ValidationResult(ValidationStatus.OK);
					for (IValidator validator : validators) {
						ValidationResult childResult = validator.validate(text);
						childResult.setField(field);
						result.addChildResult(childResult);
					}
				}
				if (callback != null) {
					callback.validationDone(result);
				}
			}
		});
	}
	
	/**
	 * Remove validation capabilities from a given UI field
	 * 
	 * @param field to remove validation from, can't be <code>null</code>
	 * @throws IllegalArgumentException if given field is <code>null</code>
	 */
	public static void removeValidation(Control field) {
		if (field == null) {
			throw new IllegalArgumentException("Field can't be null!");
		}
		Listener[] listeners = field.getListeners(SWT.Modify);
		for (Listener listener : listeners) {
			if (listener instanceof ValidationModifyListener) {
				field.removeListener(SWT.Modify, listener);
			}
		}
	}

	private static String getTextFromField(Control field) {
        try {

            return String.valueOf(
                    field.getClass().getDeclaredMethod(GET_TEXT_METHOD_NAME).invoke(field));

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(
                    String.format("Control of type %s does not have '%s()' method!",
                            field.getClass().getName(), GET_TEXT_METHOD_NAME), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(
                    String.format("'%s()' method threw an exception: %s", GET_TEXT_METHOD_NAME, e.getMessage()), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    String.format("'%s()' method enforced Java access control or is inaccessible.", GET_TEXT_METHOD_NAME), e);
        }
    }
}
