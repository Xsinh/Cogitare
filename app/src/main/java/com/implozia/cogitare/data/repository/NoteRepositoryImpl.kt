package com.implozia.cogitare.data.repository

import androidx.lifecycle.LiveData
import com.implozia.cogitare.data.db.NoteDao
import com.implozia.cogitare.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {
    override suspend fun getAllNote(): LiveData<List<Note>> = withContext(Dispatchers.IO) {
        noteDao.getAllNote()
    }

    override suspend fun update(note: Note) = withContext(Dispatchers.IO) {
        noteDao.update(note)
    }

    override suspend fun delete(note: Note) = withContext(Dispatchers.IO) {
        noteDao.delete(note)
    }


    override suspend fun insert(note: Note) = withContext(Dispatchers.IO) {
        noteDao.insert(note)
    }
}