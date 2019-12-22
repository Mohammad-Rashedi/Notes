package com.example.notes.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.notes.Room.Note;
import com.example.notes.Room.NoteDAO;
import com.example.notes.Room.NoteDatabase;

import java.util.List;

public class NoteRepository {

    public NoteDAO noteDAO;
    LiveData<List<Note>> allNotes;
    NoteDatabase noteDatabase;
    public NoteRepository(Application application) {
        noteDatabase = NoteDatabase.getInstance(application.getApplicationContext());
        noteDAO = noteDatabase.noteDAO();
        allNotes = noteDAO.getAllNotes();
    }
    public void insertNote(Note note){
        new asyncTaskInsert(noteDAO).execute(note);
    }
    public void deleteNote(Note note){
        new asyncTaskDelete(noteDAO).execute(note);
    }
    public void updateNote(Note note) {
        new asyncTaskUpdate(noteDAO).execute(note);
    }
    public void deleteAllNotes(){
        new asyncTaskDeleteAllNotes(noteDAO).execute();
    }
    public LiveData<List<Note>> getAllNotes(){
        new asyncTaskGetAllNotes(noteDAO).execute();
        return allNotes;
    }


private class asyncTaskInsert extends AsyncTask<Note,Void,Void>{
    NoteDAO noteDAO;
    public asyncTaskInsert(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        Note note = notes[0];
        noteDAO.insertNote(note);
        return null;
    }
}

    private class asyncTaskUpdate extends AsyncTask<Note,Void,Void>{
        NoteDAO noteDAO;
        public asyncTaskUpdate(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            Note note = notes[0];
            noteDAO.updateNote(note);
            return null;
        }
    }

    private class asyncTaskDelete extends AsyncTask<Note,Void,Void>{
        NoteDAO noteDAO;
        public asyncTaskDelete(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            Note note = notes[0];
            noteDAO.deleteNote(note);
            return null;
        }
    }

    private class asyncTaskDeleteAllNotes extends AsyncTask<Void,Void,Void>{

        NoteDAO noteDAO;
        public asyncTaskDeleteAllNotes(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDAO.deleteAllNotes();
            return null;
        }
    }
    private class asyncTaskGetAllNotes extends AsyncTask<Void,Void,Void>{

        NoteDAO noteDAO;
        public asyncTaskGetAllNotes(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            allNotes = noteDAO.getAllNotes();

            return null;
        }
    }

}




