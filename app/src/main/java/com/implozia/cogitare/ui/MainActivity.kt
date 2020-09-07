package com.implozia.cogitare.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.implozia.cogitare.R
import com.implozia.cogitare.model.Note
import com.implozia.cogitare.model.adapter.Adapter

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.list)
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val adapter = Adapter()
        recyclerView.adapter = adapter
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { NoteDetailsActivity.start(this@MainActivity, null) }
        val mainViewModel = ViewModelProvider(this).get(
            MainViewModel::class.java
        )
        mainViewModel.noteLiveData.observe(
            this,
            Observer<List<Note?>> { notes: List<Note?>? -> adapter.setItems(notes as List<Note>?) })
    }
}