package com.example.facedetection.detection

import io.reactivex.Single

interface DetectionView {
    fun startCamera()
    fun stopCamera()
    fun hideProgress()
    fun initCamera(frameDetectionProcessor: FrameDetectionProcessor)
    fun showProgress()
    fun takePicture(path: String): Single<Boolean>
    fun toast(text: String)
    fun showRefresh()
    fun hideRefresh()
    fun startRecognizingAnimation()
    fun hideCamera()
    fun stopDetectingAnimation()
    fun switchToNotes()
    fun startVerifySuccessfulAnimation()
    fun switchToAddNewFace()
}