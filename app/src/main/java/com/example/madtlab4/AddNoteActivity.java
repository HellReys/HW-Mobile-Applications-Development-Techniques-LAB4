package com.example.madtlab4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextNoteName;
    private EditText editTextNoteContent;
    private Button buttonSaveNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.title_add));
        setContentView(R.layout.activity_add_note);

        editTextNoteName = findViewById(R.id.edittext_note_name);
        editTextNoteContent = findViewById(R.id.edittext_note_content);
        buttonSaveNote = findViewById(R.id.button_save_note);

        buttonSaveNote.setOnClickListener(view -> onSaveClicked());
    }

    private void onSaveClicked() {
        String name = editTextNoteName.getText().toString().trim();
        String content = editTextNoteContent.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, getString(R.string.toast_empty_name), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, getString(R.string.toast_empty_content), Toast.LENGTH_SHORT).show();
            return;
        }

        Storage storage = StorageFactory.getStorage(this);
        if (storage.noteExists(name)) {
            Toast.makeText(this, getString(R.string.toast_note_exists), Toast.LENGTH_SHORT).show();
            return;
        }

        boolean saved = storage.saveNote(new Note(name, content));
        if (saved) {
            Toast.makeText(this, getString(R.string.toast_note_saved), Toast.LENGTH_SHORT).show();
            finish(); // go back to MainActivity
        } else {
            Toast.makeText(this, "Save failed (internal).", Toast.LENGTH_SHORT).show();
        }
    }
}
