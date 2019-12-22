package com.example.notes.Room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Note.class, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public static NoteDatabase instance = null;
    public abstract NoteDAO noteDAO();
    public static synchronized NoteDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note-database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    //for creating 3 notes at the first time
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{
        private NoteDAO noteDAO;

        public PopulateDbAsyncTask(NoteDatabase db) {
            noteDAO = db.noteDAO();
                    }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDAO.insertNote(new Note("Title1","Description1",1));
            noteDAO.insertNote(new Note("Title2","Description2",2));
            noteDAO.insertNote(new Note("Title3","Description3",3));
            return null;
        }
    }
}
