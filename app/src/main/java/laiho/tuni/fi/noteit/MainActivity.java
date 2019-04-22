package laiho.tuni.fi.noteit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.ItemClickListener {

    private static final String TAG = "MainActivity";
    private LinearLayout layoutContent;
    private EditText mainEditText;
    private TextView pointView;
    private MainRecyclerViewAdapter adapter;
    private int totalPoints;
    private FileController fileController;
    private JsonController jsonController;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.saveButton);
        layoutContent = (LinearLayout) this.findViewById(R.id.mainContent);

        this.mainEditText = findViewById(R.id.MainEditText);
        this.fileController = new FileController(this);
        this.jsonController = new JsonController(this);

        jsonController.loadInit();
        this.noteList = this.jsonController.listFromJson();
        try {
            JSONObject obj = new JSONObject(jsonController.readFromFile("Init.json"));
            if (!obj.isNull("TotalPoints")) {
                this.totalPoints = obj.getInt("TotalPoints");
            } else {
                this.totalPoints = 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.pointView = (TextView) findViewById(R.id.totalPointsTextView);
        pointView.setText("Total Points: " + this.totalPoints);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);

        adapter = new MainRecyclerViewAdapter(this, this.noteList, jsonController);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        this.listFromJson();

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

    /*
    public void initFiles() {
        if (!fileFound("init.txt")) {
            File file = new File("init.txt");
            try {
                file.mkdirs();
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        loadInit();
        loadAllNotes();
    }

    private void loadInit() {
        StringBuilder builder = new StringBuilder();
        int points;

        if (fileFound("init.txt")) {
            try {
                InputStream inputStream = openFileInput("init.txt");
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    BufferedReader buffered = new BufferedReader(reader);
                    String tmp;

                    while((tmp = buffered.readLine()) != null) {
                        builder.append(tmp);
                    }
                    inputStream.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!builder.toString().equals("")) {
            this.totalPoints = Integer.parseInt(builder.toString());
        } else {
            this.totalPoints = 0;
        }
        TextView textView = findViewById(R.id.totalPointsTextView);
        textView.setText("Total Points: " + this.totalPoints);

    }
*/
    public void save(View view) {
        /*
        String fName = "Note" + fileController.noteAmount() + ".txt";
        Log.d(TAG, "save: " + fName);
        noteList.add(fileController.saveNote(
                fName,
                mainEditText.getText().toString(),
                fileController.noteAmount()));
        adapter.notifyDataSetChanged(); */
        Note newNote = new Note(mainEditText.getText().toString());


        noteList.add(newNote);
        adapter.notifyDataSetChanged();
        JSONArray arr = jsonController.createNoteJsonArray(this.noteList);
        jsonController.writeJson("AllNotes.json", arr.toString());
    }

    public List<Note> listFromJson() {
        String JSONString = this.jsonController.readFromFile("AllNotes.json");
        List<Note> resultList = new ArrayList<>();
        Log.d(TAG, "listFromJson: " + JSONString);
        JSONObject currentObject = new JSONObject();

        try {
            JSONArray arr = new JSONArray(JSONString);
            for (int i = 0; i < arr.length(); i++) {
                currentObject = arr.getJSONObject(i);
                Log.d(TAG, "listFromJson: " + currentObject.getString("NoteContent"));
                resultList.add(new Note(
                        currentObject.getString("NoteContent"),
                        currentObject.getInt("AwardPoints"),
                        false));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        resultList.toString();
        return resultList;
    }


/*
    public void saveNote(String fileName) {
        File file = new File(fileName);
        try {
            file.mkdirs();
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Note savedNote = new Note(mainEditText.getText().toString());
        try {
            PrintWriter out = new PrintWriter(openFileOutput(fileName, 0));
            out.println(savedNote.getDescription());
            out.println(",");
            out.println(savedNote.getAwardPoints());
            out.close();
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
            noteList.add(savedNote);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        this.totalPoints += savedNote.getAwardPoints();
    }

    public boolean fileFound(String fileName) {
        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }

    public void loadNote(String fileName) {
        String noteContent = "";
        int notePoints = 0;
        StringBuilder builder = new StringBuilder();

        if (fileFound(fileName)) {
            try {
                InputStream inputStream = openFileInput(fileName);
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    BufferedReader buffered = new BufferedReader(reader);
                    String tmp;

                    while((tmp = buffered.readLine()) != null) {
                        builder.append(tmp);
                    }
                    inputStream.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] lines = builder.toString().split(",");
        noteContent = lines[0];
        Log.d(TAG, "loadNote: lines length: " + lines.length);
        Log.d(TAG, "loadNote: lines: " + lines[0]);
        notePoints = Integer.parseInt(lines[1]);

        this.noteList.add(new Note(noteContent, notePoints, false));
    }

    public void loadAllNotes() {
        File directory;
        directory = getFilesDir();
        File[] files = directory.listFiles();
        Log.d(TAG, "loadAllNotes: " + (files.length - 1));
        this.noteAmount = files.length;
        for (int i = 0; i < files.length; i++) {
            String fName = "Note" + i + ".txt";
            Log.d(TAG, "loadAllNotes: " + files[i].getName());
            loadNote(fName);
        }
    } */

    @Override
    public void onItemClick(View view, int position, int points) {
        if (points != 0) {

            try {
                JSONObject obj = new JSONObject(
                        jsonController.readFromFile("Init.json"));
                JSONObject newObj = new JSONObject();
                if (obj.isNull("TotalPoints")) {
                    this.totalPoints = points;
                } else {
                    this.totalPoints = obj.getInt("TotalPoints") + points;
                }

                newObj.put("TotalPoints", this.totalPoints);
                jsonController.writeJson("Init.json", newObj.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            this.pointView.setText("Total Points: " + this.totalPoints);
            Toast.makeText(this, "Task cleared! Points Awarded: " + points, Toast.LENGTH_SHORT).show();
        }

    }
}
