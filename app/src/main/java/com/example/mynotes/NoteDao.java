package com.example.mynotes;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void addNote(Notes note);

    @Query("SELECT * FROM Notes")
    List<Notes> getNotes();
}
