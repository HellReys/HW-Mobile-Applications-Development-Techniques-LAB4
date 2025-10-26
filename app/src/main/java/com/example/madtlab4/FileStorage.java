package com.example.madtlab4;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileStorage implements Storage {
    private static final String FILENAME = "notes_data.json";
    private final Context context;

    public FileStorage(Context context) {
        this.context = context.getApplicationContext();
    }

    private String readFileString() {
        try (FileInputStream fis = context.openFileInput(FILENAME);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            String content = sb.toString();
            return TextUtils.isEmpty(content) ? "{}" : content;
        } catch (FileNotFoundException e) {
            return "{}";
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    private boolean writeFileString(String data) {
        try (FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
             OutputStreamWriter osw = new OutputStreamWriter(fos);
             BufferedWriter writer = new BufferedWriter(osw)) {
            writer.write(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Note> loadAllNotes() {
        String jsonString = readFileString();
        List<Note> notes = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonString);
            Iterator<String> keys = root.keys();
            while (keys.hasNext()) {
                String name = keys.next();
                String content = root.optString(name, "");
                notes.add(new Note(name, content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notes;
    }

    @Override
    public boolean saveNote(Note note) {
        try {
            String jsonString = readFileString();
            JSONObject root = new JSONObject(jsonString);
            root.put(note.getName(), note.getContent());
            return writeFileString(root.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteNoteByName(String name) {
        try {
            String jsonString = readFileString();
            JSONObject root = new JSONObject(jsonString);
            if (root.has(name)) {
                root.remove(name);
                return writeFileString(root.toString());
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean noteExists(String name) {
        String jsonString = readFileString();
        if (TextUtils.isEmpty(jsonString)) return false;
        try {
            JSONObject root = new JSONObject(jsonString);
            return root.has(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
