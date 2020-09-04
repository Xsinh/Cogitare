package com.implozia.cogitare.model.adapter

import android.app.Activity
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.implozia.cogitare.App
import com.implozia.cogitare.R
import com.implozia.cogitare.model.NoteModel
import okhttp3.internal.Internal.instance

class NoteAdapter() : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var sortedList: SortedList<NoteModel>


    init {
        sortedList =
            SortedList<NoteModel>(NoteModel::class.java, object : SortedList.Callback<NoteModel>() {
                override fun compare(o1: NoteModel, o2: NoteModel): Int = when {
                    !o2.done && o1.done -> {
                        1
                    }
                    else -> if (o2.done && !o1.done) {
                        -1
                    } else ((o2.timestamp - o1.timestamp))
                }

                override fun onChanged(position: Int, count: Int) =
                    notifyItemRangeChanged(position, count)

                override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean =
                    oldItem == newItem

                override fun areItemsTheSame(item1: NoteModel, item2: NoteModel): Boolean =
                    item1.uid == item2.uid


                override fun onInserted(position: Int, count: Int) =
                    notifyItemRangeInserted(position, count)

                override fun onRemoved(position: Int, count: Int) =
                    notifyItemRangeRemoved(position, count)

                override fun onMoved(fromPosition: Int, toPosition: Int) =
                    notifyItemMoved(fromPosition, toPosition)
            })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(sortedList[position])
    }

    override fun getItemCount(): Int {
        return sortedList.size()
    }

    fun setItems(notes: List<NoteModel>) {
        sortedList.replaceAll(notes)
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var noteText: TextView
        private var completed: CheckBox
        private var delete: View
        private lateinit var note: NoteModel
        private var silentUpdate = false

        fun bind(note: NoteModel) {
            this.note = note
            noteText.text = note.text
            updateStrokeOut()
            silentUpdate = true
            completed.isChecked = note.done
            silentUpdate = false
        }

        private fun updateStrokeOut() {
            when {
                note.done ->
                    noteText.paintFlags =
                        noteText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                else -> noteText.paintFlags =
                    noteText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        init {
            noteText = itemView.findViewById(R.id.note_text)
            completed = itemView.findViewById(R.id.completed)
            delete = itemView.findViewById(R.id.delete)
//            itemView.setOnClickListener {
////                NoteDetailsActivity.start(
////                    itemView.context as Activity,
////                    note
////                )
////            }
            delete.setOnClickListener { App.instance?.getNoteDao()?.delete(note) }
            completed.setOnCheckedChangeListener { compoundButton, checked ->
                if (!silentUpdate) {
                    note.done = checked
                    App.instance?.getNoteDao()?.update(note)
                }
                updateStrokeOut()
            }
        }
    }
}