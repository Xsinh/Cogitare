package com.implozia.cogitare.presentation.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.implozia.cogitare.R
import com.implozia.cogitare.data.repository.NoteRepository
import com.implozia.cogitare.model.Note
import com.implozia.cogitare.presentation.adapter.NoteAdapter.NoteViewHolder
import com.implozia.cogitare.presentation.ui.note.NoteDetailDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NoteAdapter(val fragmentManager: FragmentManager, private val noteRepository: NoteRepository) : RecyclerView.Adapter<NoteViewHolder>() {
    private val sortedList: SortedList<Note>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note_list, parent, false),
            fragmentManager = fragmentManager,
            noteRepository
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(sortedList[position])
    }

    override fun getItemCount(): Int {
        return sortedList.size()
    }

    fun setItems(notes: List<Note>?) {
        sortedList.replaceAll(notes!!)
    }

    class NoteViewHolder(
        itemView: View,
        val fragmentManager: FragmentManager,
        private val noteRepository: NoteRepository
    ) : RecyclerView.ViewHolder(itemView) {
        var noteText: TextView
        var completed: CheckBox
        var delete: View
        var note: Note? = null
        var silentUpdate = false
        fun bind(note: Note) {
            this.note = note
            noteText.text = note.text
            updateStrokeOut()
            silentUpdate = true
            completed.isChecked = note.done
            silentUpdate = false
        }

        private fun updateStrokeOut() {
            if (note!!.done) {
                noteText.paintFlags = noteText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                noteText.paintFlags = noteText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        init {
            noteText = itemView.findViewById(R.id.noteText)
            completed = itemView.findViewById(R.id.completed)
            delete = itemView.findViewById(R.id.delete)
            itemView.setOnClickListener { view: View? ->
                NoteDetailDialog(note?.text.toString()).show(fragmentManager, "popup")
            }
            delete.setOnClickListener { view: View? ->
                GlobalScope.launch(Dispatchers.IO) {
                    noteRepository.delete(
                        note!!
                    )
                }
            }
            completed.setOnCheckedChangeListener { compoundButton: CompoundButton?, checked: Boolean ->
                if (!silentUpdate) {
                    note!!.done = checked
                    GlobalScope.launch(Dispatchers.IO) {
                        noteRepository.update(note!!)
                    }
                }
                updateStrokeOut()
            }
        }
    }

    init {
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
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(item1: Note, item2: Note): Boolean {
                return item1.uid == item2.uid
            }

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }
        })
    }
}