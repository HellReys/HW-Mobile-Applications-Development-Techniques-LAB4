package com.example.madtlab4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteNoteActivity extends AppCompatActivity {

    private ListView listViewDelete;
    private ArrayAdapter<String> adapter;
    private List<String> noteNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.title_delete));
        setContentView(R.layout.activity_delete_note);

        listViewDelete = findViewById(R.id.listview_delete_notes);
        noteNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.text_item, noteNames);
        listViewDelete.setAdapter(adapter);

        listViewDelete.setOnItemClickListener((parent, view, position, id) -> {
            String nameToDelete = noteNames.get(position);
            confirmAndDelete(nameToDelete);
        });

        refreshList();
    }

    private void refreshList() {
        Storage storage = StorageFactory.getStorage(this);
        List<Note> notes = storage.loadAllNotes();
        noteNames.clear();
        for (Note n : notes) noteNames.add(n.getName());
        Collections.sort(noteNames, String.CASE_INSENSITIVE_ORDER);
        adapter.notifyDataSetChanged();
    }

    private void confirmAndDelete(String name) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete_title))
                .setMessage(String.format(getString(R.string.confirm_delete_message), name))
                .setPositiveButton(getString(R.string.confirm_delete_yes), (dialog, which) -> {
                    Storage storage = StorageFactory.getStorage(DeleteNoteActivity.this);
                    boolean removed = storage.deleteNoteByName(name);
                    if (removed) {
                        Toast.makeText(DeleteNoteActivity.this, getString(R.string.toast_note_deleted), Toast.LENGTH_SHORT).show();
                        refreshList();
                    } else {
                        Toast.makeText(DeleteNoteActivity.this, "Delete failed.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getString(R.string.confirm_delete_no), null)
                .show();
    }
}
