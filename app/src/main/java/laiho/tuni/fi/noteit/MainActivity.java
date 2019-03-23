package laiho.tuni.fi.noteit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private LinearLayout layoutContent;
    private int noteAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.saveButton);
        this.noteAmount = 0;
        layoutContent = (LinearLayout) this.findViewById(R.id.mainContent);
        Log.d(TAG, layoutContent.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean save(View view) {
        EditText editText = findViewById(R.id.MainEditText);
        String noteName = "Note" + noteAmount + 1;
        TextView note = (TextView) new TextView(this);
        boolean wasSaved = false;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) LinearLayout.LayoutParams.MATCH_PARENT,
                (int) LinearLayout.LayoutParams.WRAP_CONTENT
        );

        note.setText(editText.getText());
        Log.d(TAG, note.getText().toString());
        note.setPadding(10,10,10,10);
        if (!note.getText().toString().equals("") || note.getText().toString() != null) {
            layoutContent.addView(note, params);
            editText.setText("");
            Log.d(TAG, "save: Saved a note!");
            wasSaved= true;
        }

        return wasSaved;
    }
}
