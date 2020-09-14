package com.implozia.cogitare.presentation.ui.main

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SortedList
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

    private lateinit var sortedList: SortedList<Note>

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

    private suspend fun bindUI() {
        val futureNoteEntries = viewModel.noteEntries()
        sortedList = SortedList(Note::class.java, object : SortedList.Callback<Note>() {
            override fun compare(o1: Note, o2: Note): Int {
                if (!o2.done && o1.done) {
                    return 1
                }
                return if (o2.done && !o1.done) {
                    -1
                } else (o2.timestamp - o1.timestamp).toInt()
            }

            override fun onChanged(position: Int, count: Int) {
                recyclerView.adapter?.notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(item1: Note, item2: Note): Boolean {
                return item1.uid == item2.uid
            }

            override fun onInserted(position: Int, count: Int) {
                recyclerView.adapter?.notifyItemRangeInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                recyclerView.adapter?.notifyItemRangeRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
            }
        })
        futureNoteEntries.observe(
            this,
            Observer { noteEntries ->
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
            override fun onStateChanged(p0: View, p1: Int) = when {
                bottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED -> swipeHint.visibility =
                    View.INVISIBLE
                else -> swipeHint.visibility = View.VISIBLE
            }


            override fun onSlide(p0: View, p1: Float) = Unit
        })
    }

    private fun initRecyclerView(items: List<NoteItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
            recyclerView.adapter
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

    private fun List<Note>.toFutureNoteItems(): List<NoteItem> = this.map {
        NoteItem(noteRepository, it, supportFragmentManager)
    }
}