package com.example.facedetection.add

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.facedetection.detection.DetectionPresenter
import com.example.facedetection.face.FaceDetails

class AddNewFacePresenter(val view: AddNewFaceView): AddNewFaceModel.Listener {
    private var faceDetails: FaceDetails? = null

    val model = AddNewFaceModel()

    override fun faceAdded(faceDetails: FaceDetails) {
        this.faceDetails = faceDetails
        view.switchToNotes()
    }

    fun textChanged() {
        if (view.getNameLength() > 0) {
            view.enableButton()
        } else {
            view.disableButton()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addNewFace() {
        model.addFace(view.getName(), DetectionPresenter.faceBitmap!!, this)
    }

    fun getFaceDetails() = faceDetails

}