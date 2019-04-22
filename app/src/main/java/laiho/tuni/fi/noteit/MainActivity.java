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

    public void save(View view) {
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
