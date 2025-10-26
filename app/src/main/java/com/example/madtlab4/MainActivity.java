package com.example.madtlab4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView notesListView;
    private ArrayAdapter<String> listAdapter;
    private TextView textCurrentStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.title_main));
        setContentView(R.layout.activity_main);

        notesListView = findViewById(R.id.listview_notes);
        textCurrentStorage = findViewById(R.id.text_current_storage);

        listAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.text_item, new ArrayList<>());
        notesListView.setAdapter(listAdapter);

        notesListView.setOnItemClickListener((adapterView, view, position, id) -> {
            String noteName = listAdapter.getItem(position);
            Storage storage = StorageFactory.getStorage(MainActivity.this);
            List<Note> notes = storage.loadAllNotes();
            for (Note n : notes) {
                if (n.getName().equals(noteName)) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(n.getName());
                    builder.setMessage(n.getContent());
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    break;
                }
            }
        });

        refreshUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUi();
    }

    private void refreshUi() {
        Storage storage = StorageFactory.getStorage(this);
        List<Note> notes = storage.loadAllNotes();
        List<String> names = new ArrayList<>();
        for (Note n : notes) names.add(n.getName());
        Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
        listAdapter.clear();
        listAdapter.addAll(names);
        listAdapter.notifyDataSetChanged();

        String current = StorageFactory.getCurrentStorageName(this);
        String display = current.equals(StorageFactory.STORAGE_FILES) ? getString(R.string.storage_files) : getString(R.string.storage_prefs);
        textCurrentStorage.setText("Storage: " + display);

        findViewById(R.id.text_empty).setVisibility(names.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        updateMenuTitle(menu.findItem(R.id.action_switch_storage));
        return true;
    }

    private void updateMenuTitle(MenuItem item) {
        String current = StorageFactory.getCurrentStorageName(this);
        if (StorageFactory.STORAGE_FILES.equals(current)) {
            item.setTitle(getString(R.string.storage_files));
        } else {
            item.setTitle(getString(R.string.storage_prefs));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_note) {
            startActivity(new Intent(this, AddNoteActivity.class));
            return true;
        } else if (id == R.id.action_delete_note) {
            startActivity(new Intent(this, DeleteNoteActivity.class));
            return true;
        } else if (id == R.id.action_switch_storage) {
            StorageFactory.switchStorage(this);
            invalidateOptionsMenu();
            refreshUi();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
