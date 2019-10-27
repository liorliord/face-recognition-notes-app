package com.example.facedetection.notes

interface NotesView {
    fun getNoteLength(): Int
    fun enableButton()
    fun disableButton()
    fun setTitle(title: String)
    fun setNotes(list: List<String>)
    fun clearNote()
    fun showProgress()
    fun hideProgress()
}