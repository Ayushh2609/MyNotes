package com.example.mynotes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void addNote(Notes note);

    @Query("SELECT * FROM my_notes")
    List<Notes> getNotes();


    @Delete
    void deleteNote(Notes note);
}
