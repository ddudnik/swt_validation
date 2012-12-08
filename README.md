SWT Validation Toolkit
===============================

This is a simple and flexible toolkit for adding validation to your SWT UI controls, primarily text fields. Since SWT itself does not provide us with a high-level validation capabilities I decided such a toolkit could be helpful. Especially if you *do not* like fussing with numeruos SWT listeners in every piece of UI code repeating the same over and over until your brain hurts.

I tried to keep it high-level so you could basically add a validation capabilities to your UI controls with a single line of code. However I also tried to make it flexible in terms of dealing with validation feedback so sometimes you'll find adding a bit more code quite helpful. But enough words, let's get started!

Getting started
----------------------

Obtaining binary file is straightforward: just **clone the repo** and **run** `mvn install`. Note that it references SWT dependency which is of course absolutely vital for building *BUT* then I assume you have own SWT jars somewhere in your project's classpath. I suppose there's no need to explain why otherwise you clearly would like to know more about [SWT](http://www.eclipse.org/swt/) itself :) And yes, no other 3rd party dependencies needed.

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

For the beginning there are several validators ready to use:
* `ValidationToolkit.NON_EMPTY` - checks if field value is not empty
* `ValidationToolkit.NUMERIC` - checks if field content is a number
* `ValidationToolkit.EMAIL` - checks if field content is valid email address (does not check actual _existance_ of this address)
* `ValidationToolkit.PHONE_NUMBER` - checks if field content is a valid phone number (see `PhoneNumberFieldValidator.java` for details on acceptable phone number patters)
* `RegexFieldValidator` which does not have a named constant and checks if field content matches against given pattern

Not many, but new validators will definitely come with new toolkit versions. Now you could add whatever is missing by implementing `IFieldValidator` interface (and maybe contributing some to the toolkit).

Want more?
-------------------------
Sure you do. So far you know how to add validation feature to a single UI element. What if you want a bunch of UI controls be validated together as Form? As we know there's no such thing as Form in _pure_ SWT which could combine several fields into a single logical unit.

SWT Validation Toolkit gives you a possibility to validate a bunch of related fields together though it looks a bit more complicated:

```java
ValidationContext ctx = new ValidationContext(/* your callback goes here */);
ctx.setupField(nameField, ValidationToolkit.NON_EMPTY);
ctx.setupField(emailField, new IFieldValidator[] { ValidationToolkit.NON_EMPTY, ValidationToolkit.EMAIL });
ctx.setupField(passwordField, ValidationToolkit.NON_EMPTY);
		
ValidationToolkit.setupValidation(ctx);
```

Now all three fields are validated as a whole and you'll receive a compound `ValidationResult` in your callback containing details on every field's validity.