package com.example.facedetection.notes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facedetection.R
import kotlinx.android.synthetic.main.activity_notes.*

class NotesActivity : AppCompatActivity(), NotesView {

    private val notesAdapter = NotesAdapter()
    private lateinit var presenter: NotesPresenter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        titleTextView.text = "Welcome back ${intent.getStringExtra("name")}!"
        presenter = NotesPresenter(this, intent.getStringExtra("name")!!, intent.getStringExtra("id")!!)


        notesRecyclerView.adapter = notesAdapter
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        presenter.init()

        sendButton.setOnClickListener {
            presenter.addNote(noteEditText.text.toString())
        }

        noteEditText.addTextChangedListener {
            presenter.textChanged()
        }
    }

    override fun getNoteLength() = noteEditText.text.length

    override fun enableButton() {
        sendButton.isEnabled = true
    }

    override fun disableButton() {
        sendButton.isEnabled = false
    }

    override fun setTitle(title: String) {
        titleTextView.text = title
    }

    override fun setNotes(list: List<String>) {
        notesAdapter.items = list
        notesAdapter.notifyDataSetChanged()
        if (list.isNotEmpty()) {
            notesRecyclerView.smoothScrollToPosition(list.size - 1)
        }
    }

    override fun clearNote() {
        noteEditText.setText("")
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}
