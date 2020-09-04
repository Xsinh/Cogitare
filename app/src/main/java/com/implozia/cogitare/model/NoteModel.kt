package com.implozia.cogitare.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class NoteModel {
    @PrimaryKey(autoGenerate = true)
    var uid = 0

    @ColumnInfo(name = "text")
    var text: String? = null

    @ColumnInfo(name = "timestamp")
    var timestamp: Int = 0

    @ColumnInfo(name = "done")
    var done = false
}