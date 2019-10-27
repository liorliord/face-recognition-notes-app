package com.example.facedetection.detection

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import com.example.facedetection.face.FaceDetails
import com.example.facedetection.notes.NotesActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.io.IOException

class DetectionPresenter(
    private val view: DetectionView,
    activity: Activity
) : FrameDetectionProcessor.FaceDetectionListener {

    companion object {
        var faceBitmap: Bitmap? = null
    }

    private var faceDetails: FaceDetails? = null
    private var permissionsGranted: Boolean = false
    private val permissionsDelegate = PermissionsDelegate(activity)
    private var disposable: Disposable? = null

    private val frameDetectionProcessor =
        FrameDetectionProcessor(this)

    private lateinit var model: DetectionModel

    fun init() {
        view.hideProgress()
        model = DetectionModel()
        view.initCamera(frameDetectionProcessor)
    }

    fun resume() {
        permissionsGranted = permissionsDelegate.hasPermissions()

        if (permissionsGranted) {
            view.startCamera()
        } else {
            permissionsDelegate.requestPermissions()
        }
    }

    fun pause() {
        view.stopCamera()
        model.stop()
        disposable?.dispose()
    }

    private fun startRecognition() {
        view.stopDetectingAnimation()
        view.hideRefresh()
        view.hideProgress()
        view.startCamera()
        frameDetectionProcessor.faceDetected = false
    }

    //called by the frame detection processor when a face is detected in camera preview
    @RequiresApi(Build.VERSION_CODES.O)
    override fun faceDetected(bitmap: Bitmap, boundingBox: Rect) {
        faceBitmap = bitmap
        view.hideRefresh()
        view.hideProgress()
        view.hideCamera()
        view.startRecognizingAnimation()
        disposable = model.getName(bitmap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { faceDetails ->
                if (faceDetails.name == null) {
                    view.switchToAddNewFace()
                } else {
                    this.faceDetails = faceDetails
                    view.startVerifySuccessfulAnimation()
                }
            }
    }

    fun refreshClicked() {
        startRecognition()
    }

    fun verifyAnimationEnded() {
        view.switchToNotes()
    }

    fun getFaceDetails() = faceDetails

}