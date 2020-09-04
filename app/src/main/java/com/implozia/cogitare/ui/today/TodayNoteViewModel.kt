package com.implozia.cogitare.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.implozia.cogitare.App
import com.implozia.cogitare.model.NoteModel

class TodayNoteViewModel : ViewModel() {
    private val noteLiveData: LiveData<List<NoteModel>> =
        App.instance?.getNoteDao()!!.getAllLiveData()

    fun getNoteLiveData(): LiveData<List<NoteModel>> {
        return noteLiveData
    }
}