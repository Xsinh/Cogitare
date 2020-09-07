package com.implozia.cogitare.ui

import androidx.lifecycle.ViewModel
import com.implozia.cogitare.App.Companion.instance

class MainViewModel : ViewModel() {
    val noteLiveData = instance!!.noteDao.getAllLiveData()
}