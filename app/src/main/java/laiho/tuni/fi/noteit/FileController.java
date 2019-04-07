package laiho.tuni.fi.noteit;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileController {
    private Context context;

    public FileController(Context context) {
        this.context = context;
    }
    public boolean fileFound(String fileName) {
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }

    public Note loadNote(String fileName) {
        String noteContent = "";
        int notePoints = 0;
        StringBuilder builder = new StringBuilder();

        if (fileFound(fileName)) {
            try {
                InputStream inputStream = context.openFileInput(fileName);
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
        notePoints = Integer.parseInt(lines[1]);

        return new Note(noteContent, notePoints, false);
    }

    public List<Note> loadAllNotes() {
        File directory;
        List<Note> notes = new ArrayList<>();
        directory = context.getFilesDir();
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            String fName = "Note" + i + ".txt";
            notes.add(loadNote(fName));
        }

        return notes;
    }

    public Note saveNote(String fileName, String savedString, int size) {
        File file = new File(fileName);
        try {
            file.mkdirs();
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Note savedNote = new Note(savedString);
        try {
            PrintWriter out = new PrintWriter(context.openFileOutput(fileName, 0));
            out.println(savedNote.getDescription());
            out.println(",");
            out.println(savedNote.getAwardPoints());
            out.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return savedNote;
    }

    public int loadInit() {
        StringBuilder builder = new StringBuilder();
        int points;

        if (fileFound("init.txt")) {
            try {
                InputStream inputStream = context.openFileInput("init.txt");
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
            points = Integer.parseInt(builder.toString());
        } else {
            points = 0;
        }
        return points;
    }

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
    }
}
