yumi
====

Minimalistic Android view-injection library.


Inject view into activity field
-------------------------------
@AttachTo(R.id.view_id)
private TextView someView;
@AttachTo(tag = "warning_text")
private TextView warningText;

Inject on click listener
------------------------
@HandleClickOn(R.id.button)
void buttonClicked(Button btn) {}

@HandleClickOn(tag = "back_layout")
void layoutClicked(View view) {}

Inject content view into Activity
---------------------------------
@Layout(R.layout.main_layout)
class MainActivity extends Activity {}


Initialize everything
---------------------

void onCreate(...) {
    super.onCreate(...);
    Injector.injectActivity(this);
}

