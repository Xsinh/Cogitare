package com.implozia.cogitare.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.implozia.cogitare.model.NoteModel

@Dao
interface NoteDao {
    @Query("SELECT * FROM NoteModel")
    fun getAll(): List<NoteModel>

    @Query("SELECT * FROM NoteModel")
    fun getAllLiveData(): LiveData<List<NoteModel>>

    @Query("SELECT * FROM NoteModel WHERE uid IN (:noteIds)")
    fun loadAllByIds(noteIds: IntArray): List<NoteModel>

    @Query("SELECT * FROM NoteModel WHERE uid = :uid LIMIT 1")
    fun findById(uid: Int): NoteModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: NoteModel)

    @Update
    fun update(note: NoteModel)

    @Delete
    fun delete(note: NoteModel)

}