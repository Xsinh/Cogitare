package com.implozia.cogitare;

import android.app.Application;

import androidx.room.Room;

import com.implozia.cogitare.data.NoteDao;
import com.implozia.cogitare.data.NoteDatabase;

public class App extends Application {

    private NoteDatabase database;
    private NoteDao noteDao;

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        database = Room.databaseBuilder(getApplicationContext(),
                NoteDatabase.class, "app-db-name")
                .allowMainThreadQueries()
                .build();

        noteDao = database.noteDao();
    }

    public NoteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(NoteDatabase database) {
        this.database = database;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public void setNoteDao(NoteDao noteDao) {
        this.noteDao = noteDao;
    }
}
