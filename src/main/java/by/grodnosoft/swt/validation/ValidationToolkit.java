package by.grodnosoft.swt.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import by.grodnosoft.swt.validation.ValidationToolkit.ValidationResult.ValidationStatus;

/**
 * Provides utility methods for adding different validators to UI fields
 * and gathering validation feedback via {@link ValidationCallback}s.
 * 
 * @author Denis Dudnik <deniska.dudnik@gmail.com>
 *
 */
public class ValidationToolkit {
	
	public static final IFieldValidator NON_EMPTY = new NonEmptyFieldValidator();
	public static final IFieldValidator NUMERIC = new NumericFieldValidator();
	public static final IFieldValidator EMAIL = new EmailFiledValidator();
	public static final IFieldValidator PHONE_NUMBER = new PhoneNumberFieldValidator();

	/**
	 * Common interface for all validators
	 */
	public interface IFieldValidator {
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
		private String message;
		private IFieldValidator validator;
		private Control field;
		private Collection<ValidationResult> childValidationResults;
		
		public ValidationResult(ValidationStatus status) {
			this(status, null, null);
		}
		
		public ValidationResult(ValidationStatus status, String message, IFieldValidator validator) {
			this.status = status;
			this.message = message;
			this.validator = validator;
			this.childValidationResults = new ArrayList<ValidationResult>();
		}
		
		public IFieldValidator getValidator() {
			return validator;
		}
		
		public ValidationStatus getStatus() {
			return status;
		}
		
		private void setField(Control field) {
			this.field = field;
		}
		
		public Control getField() {
			return field;
		}
		
		public String getMessage() {
			return message;
		}
		
		public void addChildResult(ValidationResult result) {
			if (getStatus().lessThan(result.getStatus())) {
				this.status = result.getStatus();
			}
			this.childValidationResults.add(result);
		}
		
		public Collection<ValidationResult> getChildren() {
			return childValidationResults;
		}
		
		public boolean isCompound() {
			return !childValidationResults.isEmpty();
		}
	}

	/**
	 * Use this class to setup validation when your UI fields represent a 'form'
	 * (bunch of fields logically connected) which should be validated as a whole. 
	 * Otherwise use methods from {@link ValidationToolkit} for single fields. 
	 */
	public static class ValidationContext {
		
		private ValidationCallback callback;
		private Map<Control, IFieldValidator[]> fieldConfig;

		public ValidationContext(ValidationCallback callback) {
			if (callback == null) {
				throw new IllegalArgumentException("Callback can't be null!");
			}
			this.callback = callback;
			this.fieldConfig = new HashMap<Control, IFieldValidator[]>();
		}

		public void setupField(Control field, IFieldValidator validator) {
			setupField(field, new IFieldValidator[] {validator});
		}
		
		public void setupField(Control field, IFieldValidator[] validators) {
			if (field == null) {
				throw new IllegalArgumentException("Field can't be null!");
			}
			fieldConfig.put(field, validators);
		}

		public void setupFields(Control[] fields, IFieldValidator validator) {
			setupFields(fields, new IFieldValidator[] {validator});
		}

		public void setupFields(Control[] fields, IFieldValidator[] validators) {
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
						IFieldValidator[] validators = validationContext.fieldConfig.get(field);
						for (IFieldValidator validator : validators) {
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
			Control field, final IFieldValidator validator, final ValidationCallback callback) {
		setupValidation(field, new IFieldValidator[]{validator}, callback);
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
			final Control field, final IFieldValidator[] validators, final ValidationCallback callback) {
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
					for (IFieldValidator validator : validators) {
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
		if (field instanceof Text) {
			return ((Text) field).getText();
		} else if (field instanceof Combo) {
			return ((Combo) field).getText();
		} else if (field instanceof CCombo) {
			return ((CCombo) field).getText();
		}
		return null;
	}
}
