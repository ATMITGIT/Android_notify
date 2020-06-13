package com.example.myapplication.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note ORDER BY CASE WHEN ischecked   THEN 0 ELSE 1 END, ischecked,date;")
    List<Note> getAll();
    @Insert
    void insert(Note employee);
    @Update
    void update(Note employee);

    @Delete
    void delete(Note employee);
}
