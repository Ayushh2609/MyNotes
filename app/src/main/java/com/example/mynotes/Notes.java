package com.example.mynotes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Notes {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String content;

    public Notes(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
