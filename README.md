SWT Validation Toolkit
===============================

This is a simple and flexible toolkit for adding validation to your SWT UI controls, primarily text fields. Since SWT itself does not provide us with a high-level validation capabilities I decided such a toolkit could be helpful. Especially if you *do not* like fussing with numeruos SWT listeners in every piece of UI code repeating the same over and over until your brain hurts.

I tried to keep it high-level so you could basically add a validation capabilities to your UI control with a single line of code. However I also tried to make it flexible in terms of dealing with validation feedback so sometimes you'll find adding a bit more code quite helpful. But enough words, let's get started!

Getting started
----------------------

Obtaining binary file is straightforward: just **clone the repo** and **run** `mvn install`. Note that it references SWT dependency which is of course absolutely vital for building *BUT* then I assume you have own SWT jars somewhere in your project's classpath. I suppose there's no need to explain why otherwise you clearly would like to know more about [SWT](http://www.eclipse.org/swt/) itself :)

Now let's suppose you want to validate if user entered valid email address:

```java
ValidationToolkit.setupValidation(textField, ValidationToolkit.EMAIL, null);
```

That's it! Simple as that. However in current very early version of SWT Validation Toolkit this will add almost nothing valuable to your UI code. To make it interesting you need to add a **validation callback**:

```java
ValidationToolkit.setupValidation(textField, ValidationToolkit.EMAIL, new ValidationCallback() {
  @Override
  public void validationDone(ValidationResult result) {
    if (ValidationStatus.ERROR.equals(result.getStatus())) {
  	  // ... do something, i.e. mark this text field as invalid
		}
	}
});
```

A bit harder but gives you possibility to implement any UI feedback which you find the most suitable for your application and users. But hey, I know there are lots of lazy software engineers like me who wants everything out of the box. Don't worry, some default feedbacks will come as this toolkit will develop. Take a look at development plan and don't hesitate to contribute to this project!