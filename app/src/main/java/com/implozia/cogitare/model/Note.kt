package com.implozia.cogitare.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note (
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,


    @ColumnInfo(name = "text")
    var text: String? = null,


    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0,

    @ColumnInfo(name = "done")
    var done: Boolean = false
)