package com.implozia.cogitare.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.implozia.cogitare.model.NoteModel

@Dao
interface NoteDao {
    @get:Query("SELECT * FROM NoteDatabase")
    val all: List<Any?>?

    @get:Query("SELECT * FROM NoteDatabase")
    val allLiveData: LiveData<List<Any?>?>?

    @Query("SELECT * FROM NoteDatabase WHERE uid IN (:noteIds)")
    fun loadAllByIds(noteIds: IntArray?): List<NoteModel>

    @Query("SELECT * FROM NoteDatabase WHERE uid = :uid LIMIT 1")
    fun findById(uid: Int): NoteModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: NoteModel)

    @Update
    fun update(note: NoteModel)

    @Delete
    fun delete(note: NoteModel)
}