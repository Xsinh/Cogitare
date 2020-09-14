package com.implozia.cogitare.presentation.ui.main

import androidx.lifecycle.ViewModel
import com.implozia.cogitare.data.repository.NoteRepository
import com.implozia.cogitare.model.Note

class MainViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    suspend fun noteEntries() = noteRepository.getAllNote()
    suspend fun updateEntries(note: Note) = noteRepository.update(note)
    suspend fun insertEntries(note: Note) = noteRepository.insert(note)
}