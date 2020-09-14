package com.implozia.cogitare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.implozia.cogitare.data.repository.NoteRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val noteRepository: NoteRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MainViewModel(
        noteRepository
    ) as T
}