package com.implozia.cogitare

import android.app.Application
import androidx.room.Room
import com.implozia.cogitare.data.NoteDao
import com.implozia.cogitare.data.NoteDatabase

class App : Application() {
    private lateinit var database: NoteDatabase
    lateinit var noteDao: NoteDao
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

    companion object {
        @JvmStatic
        var instance: App? = null
            private set
    }
}