package com.implozia.cogitare.presentation.ui.main

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.implozia.cogitare.R
import com.implozia.cogitare.data.repository.NoteRepository
import com.implozia.cogitare.model.Note
import com.implozia.cogitare.presentation.adapter.NoteAdapter
import kotlinx.android.synthetic.main.activity_note_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val mainViewModelFactory: MainViewModelFactory by instance()
    private val noteRepository: NoteRepository by instance()

    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var note: Note
    private lateinit var imm: InputMethodManager
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, mainViewModelFactory).get(
            MainViewModel::class.java
        )

        note = Note()
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, p1: Int) {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED)
                    swipeHint.visibility = View.INVISIBLE
                else
                    swipeHint.visibility = View.VISIBLE
            }

            override fun onSlide(p0: View, p1: Float) {

            }
        })

        addNote.setOnClickListener {
            note.text = editTextOfNote.text.toString()
            note.done = false
            note.timestamp = System.currentTimeMillis()
            GlobalScope.launch(Dispatchers.IO) {
                viewModel.updateEntries(note)
                viewModel.insertEntries(note)
            }
            editTextOfNote.text.clear()
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        recyclerView = findViewById(R.id.list)
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val adapter = NoteAdapter(fragmentManager = supportFragmentManager, noteRepository)
        recyclerView.adapter = adapter


        GlobalScope.launch(Dispatchers.Main) {
            viewModel.noteEntries().observe(
                this@MainActivity,
                Observer { notes: List<Note> -> adapter.setItems(notes as List<Note>?) })
        }
    }
}