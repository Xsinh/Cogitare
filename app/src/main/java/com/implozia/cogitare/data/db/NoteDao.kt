package com.implozia.cogitare.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.implozia.cogitare.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note")
    fun getAllNote(): LiveData<List<Note>>

    @Query("SELECT * FROM Note WHERE uid IN (:noteIds)")
    fun loadAllByIds(noteIds: IntArray): List<Note>

    @Query("SELECT * FROM Note WHERE uid = :uid LIMIT 1")
    fun findById(uid: Int): Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)
}