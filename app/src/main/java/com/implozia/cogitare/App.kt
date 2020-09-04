package com.implozia.cogitare

import android.app.Application
import androidx.room.Room
import com.implozia.cogitare.data.NoteDao
import com.implozia.cogitare.data.NoteDatabase
import okhttp3.internal.Internal.instance

class App : Application() {
    private lateinit var database: NoteDatabase
    private lateinit var noteDao: NoteDao

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java, "app-db-name"
        )
            .allowMainThreadQueries()
            .build()
        noteDao = database.noteDao()
    }

    fun getDatabase(): NoteDatabase {
        return database
    }

    fun setDatabase(database: NoteDatabase) {
        this.database = database
    }
    fun getNoteDao(): NoteDao? {
        return noteDao
    }

    fun setNoteDao(noteDao: NoteDao?) {
        this.noteDao = noteDao!!
    }

    companion object {
        var instance: App? = null
            private set
    }
}
