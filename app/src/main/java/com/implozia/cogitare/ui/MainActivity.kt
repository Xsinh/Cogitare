package com.implozia.cogitare.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.implozia.cogitare.App
import com.implozia.cogitare.R
import com.implozia.cogitare.model.Note
import com.implozia.cogitare.model.adapter.Adapter
import kotlinx.android.synthetic.main.activity_note_details.*

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        note = Note()

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, p1: Int) {

            }
            override fun onSlide(p0: View, p1: Float) {

            }
        })

        addNote.setOnClickListener {
            note.text = text.text.toString()
            note.done = false
            note.timestamp = System.currentTimeMillis()
            App.instance!!.noteDao.update(note)
            App.instance!!.noteDao.insert(note)
        }


        recyclerView = findViewById(R.id.list)
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val adapter = Adapter(fragmentManager = supportFragmentManager)
        recyclerView.adapter = adapter

        val mainViewModel = ViewModelProvider(this).get(
            MainViewModel::class.java
        )
        mainViewModel.noteLiveData.observe(
            this,
            Observer<List<Note?>> { notes: List<Note?>? -> adapter.setItems(notes as List<Note>?) })
    }
}