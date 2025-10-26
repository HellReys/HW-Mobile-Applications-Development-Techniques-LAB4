package com.example.madtlab4;

import java.util.List;

public interface Storage {
    List<Note> loadAllNotes();
    boolean saveNote(Note note);
    boolean deleteNoteByName(String name);
    boolean noteExists(String name);
}
