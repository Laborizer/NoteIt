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

public class JsonController {
    private Context context;
    public JsonController(Context context) {
        this.context = context;
    }

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

    public void loadInit() {
        String dir = context.getFilesDir().getAbsolutePath();
        File init = new File(dir,"Init.json");
        File allNotes = new File(dir,"AllNotes.json");
        Log.d("JsonController", "loadInit: " + init.exists());
        Log.d("JsonController", "loadInit: " + allNotes.exists());
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

    public JSONObject stringToJson(String str) {
        JSONObject newObject = null;
        try {
            newObject = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newObject;
    }

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
