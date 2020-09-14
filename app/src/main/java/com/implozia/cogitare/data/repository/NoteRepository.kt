package com.implozia.cogitare.data.repository

import androidx.lifecycle.LiveData
import com.implozia.cogitare.model.Note

interface NoteRepository {
    suspend fun getAllNote(): LiveData<List<Note>>
    suspend fun update(note: Note)
    suspend fun delete(note: Note)
    suspend fun insert(note: Note)
}