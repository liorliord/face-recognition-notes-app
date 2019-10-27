package com.example.facedetection.notes

import android.annotation.SuppressLint


class NotesPresenter(private val view: NotesView, private val name: String, private val id: String) :
    NotesModel.Listener {

    private val notesModel = NotesModel(this)

    @SuppressLint("DefaultLocale")
    fun init() {
        view.showProgress()
        notesModel.init(id)
        view.setTitle("${name.capitalize()}'s notes")
    }

    fun addNote(note: String?) {
        view.clearNote()
        notesModel.addString(id, note)
    }

    fun textChanged() {
        if (view.getNoteLength() > 0) {
            view.enableButton()
        } else {
            view.disableButton()
        }
    }

    override fun updateList(list: List<String>) {
        view.hideProgress()
        view.setNotes(list)
    }
}