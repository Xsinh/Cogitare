package com.implozia.cogitare.presentation.adapter

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.SortedList
import com.implozia.cogitare.R
import com.implozia.cogitare.data.repository.NoteRepository
import com.implozia.cogitare.model.Note
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_note_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NoteItem(
    private val noteRepository: NoteRepository,
    private var note: Note
) : Item() {
    private var silentUpdate = false

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            bindList(note)
            updateNoteText()
            completed()
            delete()
        }
    }

    override fun getLayout(): Int = R.layout.item_note_list

    private fun ViewHolder.bindList(noteItem: Note) {
        note = noteItem
        noteText.text = note.text

        silentUpdate = true
        completed.isChecked = note.done
        silentUpdate = false
    }

    private fun ViewHolder.updateNoteText() {
        if (note.done) {
            noteText.paintFlags = noteText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            noteText.paintFlags = noteText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    private fun ViewHolder.delete() {
        delete.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                noteRepository.delete(
                    note
                )
            }
        }
    }

    private fun ViewHolder.completed() {
        delete.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                noteRepository.delete(
                    note
                )
            }
        }
    }
}