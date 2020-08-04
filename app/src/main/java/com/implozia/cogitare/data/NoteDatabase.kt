package com.implozia.cogitare.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class NoteDatabase {
    @PrimaryKey(autoGenerate = true)
    val uid = 0

    @ColumnInfo(name = "text")
    val text: String? = null

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = 0

    @ColumnInfo(name = "done")
    val done = false
}