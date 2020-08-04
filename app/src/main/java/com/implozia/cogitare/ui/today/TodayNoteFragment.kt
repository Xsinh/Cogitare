package com.implozia.cogitare.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.implozia.cogitare.R
import com.implozia.cogitare.ui.base.ScopedFragment

class TodayNoteFragment : ScopedFragment() {

    companion object {
        fun newInstance() = TodayNoteFragment()
    }

    private lateinit var noteViewModel: TodayNoteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.today_note_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        noteViewModel = ViewModelProvider(this).get(TodayNoteViewModel::class.java)
    }
}