package com.example.notes.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM `note-table`")
    void deleteAllNotes();

    @Query("SELECT * FROM 'note-table' ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes();
}
//SELECT * FROM backpack_list_table ORDER BY id
//SELECT * FROM 'note-table' ORDER BY priority DESC