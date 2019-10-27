package com.example.facedetection.add

import android.graphics.Bitmap

interface AddNewFaceView {
    fun getNameLength(): Int
    fun enableButton()
    fun disableButton()
    fun getName(): String
    fun switchToNotes()

}