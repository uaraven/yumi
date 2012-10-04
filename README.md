yumi
====

Minimalistic Android view-injection library.

*yumi* is very small library designed to simplify Activity setup process in
Android applications.
Only 15k of source code (or 8k of compiled classes) without any external dependencies.

Inject view into activity field
-------------------------------
```java
@AttachTo(R.id.view_id)
private TextView someView;
@AttachTo(tag = "warning_text")
private TextView warningText;
```

Inject on click listener
------------------------
```java
@HandleClickOn(R.id.button)
void buttonClicked(Button btn) {}

@HandleClickOn(tag = "back_layout")
void layoutClicked(View view) {}
```

Inject content view into Activity
---------------------------------
```java
@Layout(R.layout.main_layout)
class MainActivity extends Activity {}
```

Initialize everything
---------------------
```java
void onCreate(...) {
    super.onCreate(...);
    Injector.injectActivity(this);
}
```
