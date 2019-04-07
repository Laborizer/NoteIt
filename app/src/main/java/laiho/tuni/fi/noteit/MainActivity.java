package laiho.tuni.fi.noteit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.ItemClickListener {

    private static final String TAG = "MainActivity";
    private LinearLayout layoutContent;
    private ArrayList<String> noteList;
    private int noteAmount;
    private EditText mainEditText;
    private MainRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.saveButton);
        layoutContent = (LinearLayout) this.findViewById(R.id.mainContent);
        this.noteList = new ArrayList<>();
        this.noteAmount = 0;

        this.mainEditText = findViewById(R.id.MainEditText);

        loadAllNotes();
        makeNoteViews();


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainRecyclerViewAdapter(this, noteList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
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

    public void save(View view) {
        String fName = "Note" + this.noteList.size() + ".txt";
        saveNote(fName);
    }

    public void saveNote(String fileName) {
        try {
            OutputStreamWriter out =
                    new OutputStreamWriter(openFileOutput(fileName, 0));
            out.write(mainEditText.getText().toString());
            out.close();
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
            noteList.add(mainEditText.getText().toString());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        TextView noteView = new TextView(this);
        noteView.setText(mainEditText.getText().toString());
        noteView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        layoutContent.addView(noteView);
    }

    public boolean fileFound(String fileName) {
        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }

    public void loadNote(String fileName) {
        String noteContent = "";
        if (fileFound(fileName)) {
            try {
                InputStream inputStream = openFileInput(fileName);
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    BufferedReader buffered = new BufferedReader(reader);
                    String tmp;
                    StringBuilder builder = new StringBuilder();

                    while((tmp = buffered.readLine()) != null) {
                        builder.append(tmp);
                    }
                    inputStream.close();
                    noteContent = builder.toString();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.noteList.add(noteContent);
    }

    public void loadAllNotes() {
        File directory;
        directory = getFilesDir();
        File[] files = directory.listFiles();
        Log.d(TAG, "loadAllNotes: " + files.length);
        this.noteAmount = files.length;
        for (int i = 0; i < files.length; i++) {
            String fName = "Note" + i + ".txt";
            Log.d(TAG, "loadAllNotes: " + files[i].getName());
            loadNote(fName);
        }
    }

    public void makeNoteViews() {
        for (int i=0; i < noteList.size(); i++) {
            TextView noteView = new TextView(this);
            noteView.setText(noteList.get(i));
            noteView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            layoutContent.addView(noteView);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
