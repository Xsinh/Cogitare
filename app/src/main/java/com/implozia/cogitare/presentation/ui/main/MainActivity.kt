package com.implozia.cogitare.presentation.ui.main

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.implozia.cogitare.R
import com.implozia.cogitare.data.repository.NoteRepository
import com.implozia.cogitare.model.Note
import com.implozia.cogitare.presentation.adapter.NoteItem
import com.implozia.cogitare.presentation.ui.note.NoteDetailDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.bottom_sheet_note_details.*
import kotlinx.android.synthetic.main.content_main.*
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

        bindBottomSheet()

        bindNote()

        GlobalScope.launch(Dispatchers.Main) {
            bindUI()
        }
    }

    private suspend fun bindUI(){
        val futureNoteEntries = viewModel.noteEntries()
        
        futureNoteEntries.observe(
            this,
            Observer {noteEntries ->
                initRecyclerView(noteEntries.toFutureNoteItems())
            }
        )

        bindBottomSheet()
    }

    private fun bindNote() {
        note = Note()
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

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
    }

    private fun bindBottomSheet() {
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
    }

    private fun initRecyclerView(items: List<NoteItem>){
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = groupAdapter
        }
        groupAdapter.setOnItemClickListener { item, _ ->
            (item as? NoteItem)?.let {
                NoteDetailDialog(note.text.toString()).show(supportFragmentManager, "popup")
            }
        }
    }
    
    private fun List<Note>.toFutureNoteItems(): List<NoteItem> {
        return this.map { 
            NoteItem(noteRepository,it)
        }
    }
}