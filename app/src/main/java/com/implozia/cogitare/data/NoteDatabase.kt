package com.implozia.cogitare.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.implozia.cogitare.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}