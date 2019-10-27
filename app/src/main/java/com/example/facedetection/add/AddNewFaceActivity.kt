package com.example.facedetection.add

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.facedetection.R
import com.example.facedetection.notes.NotesActivity
import kotlinx.android.synthetic.main.activity_add_new_face.*

class AddNewFaceActivity : AppCompatActivity(), AddNewFaceView {

    private val presenter = AddNewFacePresenter(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_face)

        addButton.setOnClickListener { presenter.addNewFace() }

        nameEditText.addTextChangedListener { presenter.textChanged() }

    }

    override fun getNameLength() = nameEditText.text.length

    override fun enableButton() {
        addButton.isEnabled = true
    }

    override fun disableButton() {
        addButton.isEnabled = false
    }

    override fun getName() = nameEditText.text.toString()

    override fun switchToNotes() {
        val intent = Intent(applicationContext, NotesActivity::class.java)
        val faceDetails = presenter.getFaceDetails()
        intent.putExtra("name", faceDetails?.name)
        intent.putExtra("id", faceDetails?.id)
        startActivity(intent)
        this.finish()
    }

}
