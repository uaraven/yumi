package net.ninjacat.yumi.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import net.ninjacat.yumi.AttachTo;
import net.ninjacat.yumi.HandleClickOn;
import net.ninjacat.yumi.Injector;
import net.ninjacat.yumi.Layout;

@Layout(R.layout.main)
public class YumiTestActivity extends Activity {

    @AttachTo(tag = "text1")
    private TextView textView;
    @AttachTo(R.id.editor)
    private EditText editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.injectActivity(this);

        textView.setText("New text");
        editor.setText("Edit this, please");
    }

    @HandleClickOn(R.id.button)
    public void buttonClick2(View v) {
        Toast.makeText(this, "Button clicked via id", Toast.LENGTH_SHORT).show();
    }

    @HandleClickOn(tag = "btn")
    public void buttonClick(View v) {
        Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
    }
}
