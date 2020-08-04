package com.implozia.cogitare.ui.today.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.implozia.cogitare.R
import com.implozia.cogitare.ui.base.ScopedFragment

class TodayNoteDetailFragment : ScopedFragment() {

    companion object {
        fun newInstance() = TodayNoteDetailFragment()
    }

    private lateinit var viewModel: TodayNoteDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.today_note_detail_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TodayNoteDetailViewModel::class.java)
    }
}