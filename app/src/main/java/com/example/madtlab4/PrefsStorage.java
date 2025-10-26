package com.example.madtlab4;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrefsStorage implements Storage {
    private static final String PREFS_NAME = "madt_notes_prefs";
    private static final String KEY_NOTES_JSON = "notes_json";
    private final SharedPreferences sharedPreferences;

    public PrefsStorage(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public List<Note> loadAllNotes() {
        String jsonString = sharedPreferences.getString(KEY_NOTES_JSON, "{}");
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
            String jsonString = sharedPreferences.getString(KEY_NOTES_JSON, "{}");
            JSONObject root = new JSONObject(jsonString);
            root.put(note.getName(), note.getContent());
            sharedPreferences.edit().putString(KEY_NOTES_JSON, root.toString()).apply();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteNoteByName(String name) {
        try {
            String jsonString = sharedPreferences.getString(KEY_NOTES_JSON, "{}");
            JSONObject root = new JSONObject(jsonString);
            if (root.has(name)) {
                root.remove(name);
                sharedPreferences.edit().putString(KEY_NOTES_JSON, root.toString()).apply();
                return true;
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
        String jsonString = sharedPreferences.getString(KEY_NOTES_JSON, "{}");
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
