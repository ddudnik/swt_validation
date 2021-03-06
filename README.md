SWT Validation Toolkit
===============================

This is a lightweight and flexible toolkit for adding validation to your SWT UI controls, primarily text fields. Since SWT itself does not provide us with a high-level validation capabilities I decided such a toolkit could be helpful. Especially if you *do not* like fussing with myriads of SWT listeners in every piece of UI code repeating the same over and over until your brain hurts.

I tried to keep it high-level so you could basically add a validation capabilities to your UI controls with a single line of code. However I also tried to make it flexible in terms of dealing with validation feedback so sometimes you'll find adding a bit more code quite helpful. But enough words, let's get started!

Current version is **0.1**, so it's in very beginning as you could assume.

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

A bit harder but gives you possibility to implement any UI feedback which you find the most suitable for your application and users. But hey, I know there are lots of lazy software engineers like me who wants everything out of the box. Don't worry, some default feedback callbacks will come as this toolkit will develop. Take a look at development plan below and don't hesitate to contribute to this project!

For the beginning there are several validators ready to use:
* `ValidationToolkit.NON_EMPTY` - checks if field value is not empty
* `ValidationToolkit.NUMERIC` - checks if field content is a number (not yet completed however)
* `ValidationToolkit.EMAIL` - checks if field content is valid email address (does not check actual _existence_ of this address)
* `ValidationToolkit.PHONE_NUMBER` - checks if field content is a valid phone number (see `PhoneNumberValidator.java` for details on acceptable phone number patters)
* `RegexValidator` which does not have a named constant and checks if field content matches against given pattern

Not many, but new validators will definitely come with new toolkit versions. Now you could add whatever is missing by implementing `IValidator` interface (and maybe contributing some to the toolkit).

You might ask what exactly is `textField` in previous examples? Is it SWT Text or Combo? Could I use validation for my own UI element derived from `org.eclipse.swt.Control` class?

**Yes, you can.** The only requirements for the object representing UI field are having `public String getText()` method and being able to notify listeners of `SWT.Modify` event. Think of it in Ruby way - your object does not need to be a certain type but it should respond to certain method calls (I'm a bit cheating here since it should be a subclass of `Control`).

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

You could also notice in previous snippet that **multiple validators could be combined** to be called on a single field. Just specify them as an array which by the way will also define order in which they will be called. **Important note**: email and phone number validators DOES NOT demand field value to be non-empty. If you want your value to be a valid email AND to be non-empty you should combine validators as it is shown in snippet.

There's also possibility to **combine fields** which should have similar validation, i.e. be non-empty. So you don't need to setup non-empty validator for every field:

```java
ValidationToolkit.setupValidation(
  new Control[] {field1, field2, field3}, ValidationToolkit.NON_EMPTY, /* your callback*/);
```


Toolkit Development Notes
=========================================================

Want to contribute? That's fantastic! There are couple of very simple steps before you get going:

1. Discuss what you would like to work on. Just to make sure that we can avoid duplicating effort. Also you may get some feedback, which can help you being more effective. To get in touch just send me a message to <deniska.dudnik@gmail.com>.
2. Fork the repo and start coding. Once you finish the work send a pull request. As simple as that.
3. Read through the list of some development notes and make sure your code is not breaking them before you commit.

Development notes
------------------------------------------
* I would really like to keep this toolkit as simple **to use** as possible but not simpler, so make sure your features will not demand lots of coding from the client developer perspective. Forget complex software patterns or at least hide them deeply in your own code :) Would be perfect if everything could be achieved with a single line of code.
* Try to develop this toolkit horizontally adding more useful validators and feedback callbacks.
* Make sure your code does not interfere with user data and UI controls. Since you have access to UI control you could do lots of things with it but every such intent should be considered and appear to be reasonable.
* Don't forget to add test cases for a new feature or a bug fix.
* If new feature has some visual representation please add it to examples so everyone could easily discover it.

Plan for final 1.0 release:
------------------------
* add default callbacks which could somehow decorate invalid field (modifying background, adding small icon etc.) and/or show small tip with error message
* add more validators: improve numeric validator to handle ranges, date, internet address etc.
* add some unit testing and (maybe) UI testing with SWTBot or something similar
* add some examples which could be easily built and run
* think of a feature that lets specify input masks for text field
* further improvement of documentation
* .....
