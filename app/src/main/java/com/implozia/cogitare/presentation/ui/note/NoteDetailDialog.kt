package com.implozia.cogitare.presentation.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.implozia.cogitare.R
import kotlinx.android.synthetic.main.detail_note_dialog_fragment.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class NoteDetailDialog(private val noteText: String) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_note_dialog_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        noteDetail.text = noteText
        closeNote.setOnClickListener {
            this.dialog?.cancel()
        }
    }

    override fun onResume() {
        super.onResume()
        val width  = activity?.resources?.getDimensionPixelSize(R.dimen.popup_width)
        val height = activity?.resources?.getDimensionPixelSize(R.dimen.popup_height)

        if (width != null && height != null) {
            dialog?.window?.setLayout(width, height)
        }
    }
}