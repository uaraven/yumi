yumi
====

Minimalistic Android view-injection library.

*yumi* is a very small library designed to simplify Activity setup process in
Android applications.
Only 15k of source code (or 8k of compiled classes) without any external dependencies.

How to use
----------

Inject views into object's fields:

```java
@AttachTo(R.id.view_id)
private TextView someView;
@AttachTo(tag = "warning_text")
private TextView warningText;
```

Inject on click listeners:
```java
@HandleClickOn(R.id.button)
void buttonClicked(Button btn) {}

@HandleClickOn(tag = "back_layout")
void layoutClicked(View view) {}
```

Inject content view into Activity:

```java
@Layout(R.layout.main_layout)
class MainActivity extends Activity {}
```

Do it all at once
```java
void onCreate(...) {
    super.onCreate(...);
    Injector.injectActivity(this);
}
```

Or, if you're using Fragment or just need to inject fields and listeners with a View:
```java
void init(View v) {
    Injector.inject(v, this);
}
```
