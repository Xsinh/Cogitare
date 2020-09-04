package com.implozia.cogitare.ui

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.implozia.cogitare.R
import com.implozia.cogitare.model.NoteModel
import com.implozia.cogitare.model.adapter.NoteAdapter
import com.implozia.cogitare.ui.today.TodayNoteViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val w: Window = window
        translucentStatusBar(w)

        recyclerView = findViewById(R.id.noteList)
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val adapter = NoteAdapter()
        recyclerView.adapter = adapter

//        fab.setOnClickListener(View.OnClickListener {
//            NoteDetailsActivity.start(
//                this@MainActivity,
//                null
//            )
//        })

        val todayNoteViewModel: TodayNoteViewModel =
            ViewModelProvider(this).get<TodayNoteViewModel>(
                TodayNoteViewModel::class.java
            )
        todayNoteViewModel.getNoteLiveData().observe(this,
            Observer<List<NoteModel>> { notes -> adapter.setItems(notes) })
    }

    private fun translucentStatusBar(window: Window) = window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
}