package laiho.tuni.fi.noteit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.Calendar;
import java.util.List;

import static android.support.v7.widget.RecyclerView.VERTICAL;

/**
 * NoteIt is a simple program that allows users to create little notes for their day and be awarded
 * for their productivity with points to help motivate them to note things. Notes are deleted at
 * the end of each day.
 *
 * MainActivity is the main class of the application that controls the flow of the application,
 * the layout and its components.
 *
 * @author Lauri Laiho
 * @version 1.0
 * @since 2019-03-17
 */
public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.ItemClickListener {

    /**
     * Tag for logging purposes, states the name of the class.
     */
    private static final String TAG = "MainActivity";

    /**
     * LinearLayout which holds the main components of the application.
     */
    private LinearLayout layoutContent;

    /**
     * EditText component which references the main text field of the app.
     */
    private EditText mainEditText;

    /**
     * TextView component which shows and references the amount of points user has collected.
     */
    private TextView pointView;

    /**
     * Class which holds and controls the RecyclerView holding and showing the notes.
     */
    private MainRecyclerViewAdapter adapter;

    /**
     * Amount of points the user has.
     */
    private int totalPoints;

    /**
     * JsonController which controls the flow of JSON data into and from files.
     */
    private JsonController jsonController;

    /**
     * List of Notes which are currently in the app.
     */
    private List<Note> noteList;

    /**
     * Method that is called when launching the app. It sets the pointers to different layout
     * components. Also sets the amount of points the user has after it as read it from the files.
     *
     * @param savedInstanceState Bundle from which a saved state of the activity can be retrieved
     *                           from.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add -Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.saveButton);

        //Layout
        layoutContent = (LinearLayout) this.findViewById(R.id.mainContent);

        //EditText
        this.mainEditText = findViewById(R.id.MainEditText);

        //JsonController
        this.jsonController = new JsonController(this);
        jsonController.loadInit();

        //Loading data from Init.json and AllNotes.json
        this.noteList = this.jsonController.listFromJson();
        this.totalPoints = this.jsonController.pointsFromJson();

        //TextView
        this.pointView = (TextView) findViewById(R.id.totalPointsTextView);
        pointView.setText("Total Points: " + this.totalPoints);

        //RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, VERTICAL);
        recyclerView.addItemDecoration(itemDecor);

        adapter = new MainRecyclerViewAdapter(this, this.noteList, jsonController);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //Setting Alarm
        setAlarm();

    }

    /**
     * Method sets an alarm to clear the JSON data at approximately 12am and to repeat it on
     * a daily interval. Uses an intent to notify a BroadcastReceiver to perform the task even
     * when the app is not running.
     */
    public void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        //00:00:00
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, DeletionReceiver.class);
        PendingIntent pintent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
    }

    /**
     * Method creates a Note when Save -button is pressed. Note gets the text from the EditText
     * and is then saved into the Note List. RecyclerView is notified of the data change and the
     * new List of Notes is written into AllNotes.json file.
     *
     * @param view View from which the onClick was invoked.
     */
    public void save(View view) {
        Note newNote = new Note(mainEditText.getText().toString());


        noteList.add(newNote);
        adapter.notifyDataSetChanged();
        JSONArray arr = jsonController.createNoteJsonArray(this.noteList);
        jsonController.writeJson("AllNotes.json", arr.toString());
    }

    /**
     * Method assigns a new Total Point value based on amount of points received from a cleared
     * Note and the amount user had previously. The amount is also updated on the TextView.
     *
     * @param view RecyclerViewItem's radiobutton.
     * @param position Position of the Item on RecyclerView.
     * @param points Amount of points the Note had assigned to it.
     */
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
