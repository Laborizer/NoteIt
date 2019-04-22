package laiho.tuni.fi.noteit;

import android.content.Context;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.List;

/**
 * JsonController controls the use of JSON data.
 *
 * JsonController class holds methods with which to control the flow and use of JSON data and files
 * of the app.
 * @author Lauri Laiho
 * @version 1.0
 * @since 2019-04-21
 */
public class JsonController {

    /**
     * Context from which the controller is used from.
     */
    private Context context;

    /**
     * Constructor for the JsonController
     *
     * @param context Context from which the controller is used from.
     */
    public JsonController(Context context) {
        this.context = context;
    }

    /**
     * Method creates a JSONObject from the two parametres given and returns it.
     *
     * @param noteContent The content or text of the Note
     * @param awardPoints The amount of points in the Note in question.
     * @return JSONObject The created JSONObject.
     */
    public JSONObject createJsonObject(String noteContent, int awardPoints) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("NoteContent", noteContent);
            jsonObject.put("AwardPoints", awardPoints);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Method clears all note data by overwriting them with an empty JSONArray for later use.
     */
    public void clearNoteData() {
        JSONArray arr = new JSONArray();
        writeJson("AllNotes.json", arr.toString());
    }

    /**
     * Method checks if Init.json file exists and creates it if it does not.
     */
    public void loadInit() {
        String dir = context.getFilesDir().getAbsolutePath();
        File init = new File(dir,"Init.json");
        if (!init.exists()) {
            try {
                JSONObject initObject = new JSONObject();
                writeJson("Init.json", initObject.toString());
                Log.d("JsonController", "loadInit: Create Init.json");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The writeJson -method writes or overwrites a file of given name, and writes the given
     * data as a String
     *
     * @param fileName The name of the written file.
     * @param data The data to be written into the file as a String
     */
    public void writeJson(String fileName, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.d("Json: WriteToJSON", "writeJson: " + data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method reads data from a file of a given name and builds it into a String
     * using StringBuilder. The data is then returned as a String.
     *
     * @param fileName The name of the file to be read from.
     * @return String The data from the file.
     */
    public String readFromFile(String fileName) {

        String ret = "";
        File file = new File(fileName);

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * Method creates a JSONObject from a String of json data.
     *
     * @param str The String from which the JSONObject is created from.
     * @return JSONObject The resulting JSONObject.
     */
    public JSONObject stringToJson(String str) {
        JSONObject newObject = null;
        try {
            newObject = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newObject;
    }

    /**
     * Method creates a JSONArray from given List containing Notes.
     *
     * @param list The given List of Notes from which the JSONArray is created from.
     * @return JSONArray The resulting JSONArray containing the Notes in JSON format.
     */
    public JSONArray createNoteJsonArray(List<Note> list) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (Note note : list) {
                jsonArray.put(new JSONObject()
                        .put("NoteContent", note.getDescription())
                        .put("AwardPoints", note.getAwardPoints()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * Method creates a List of Notes by reading AllNotes.json file. Iterating through the
     * JSONArray it gets from the file, a List of the Notes is created with each having the
     * corresponding data added to them.
     *
     * @return List All the notes from the file.
     */
    public List<Note> listFromJson() {
        String JSONString = readFromFile("AllNotes.json");
        List<Note> resultList = new ArrayList<>();
        Log.d("JsonController", "listFromJson: " + JSONString);
        JSONObject currentObject = new JSONObject();

        try {
            JSONArray arr = new JSONArray(JSONString);
            for (int i = 0; i < arr.length(); i++) {
                currentObject = arr.getJSONObject(i);
                Log.d("JsonController", "listFromJson: " + currentObject.getString("NoteContent"));
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

}
