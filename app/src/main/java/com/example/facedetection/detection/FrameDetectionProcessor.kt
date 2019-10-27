package com.example.facedetection.detection

import android.graphics.Bitmap
import android.graphics.Rect
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import io.fotoapparat.preview.Frame
import io.fotoapparat.util.FrameProcessor

class FrameDetectionProcessor(
    private var faceDetectionListener: FaceDetectionListener,
    private val options: FirebaseVisionFaceDetectorOptions = FirebaseVisionFaceDetectorOptions.Builder()
        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
        .build(),
    private val detector: FirebaseVisionFaceDetector = FirebaseVision.getInstance()
        .getVisionFaceDetector(options),
    private var count: Int = 0,
    private var currentRotation: Int = INVALID_ROTATION
) : FrameProcessor {

    companion object {
        const val FRAME_PROCESS_DELAY = 3
        const val INVALID_ROTATION = -1
    }

    var faceDetected: Boolean = false
        @Synchronized get
        @Synchronized set

    private lateinit var metadata: FirebaseVisionImageMetadata

    private fun processDelayReached(): Boolean {
        count++
        if(count == FRAME_PROCESS_DELAY) {
            count = 0
            return true
        }
        return false
    }

    @Synchronized
    override fun invoke(frame: Frame) {
        if (faceDetected || !processDelayReached()) return

        updateMetadataIfNeeded(frame)
        val image = FirebaseVisionImage.fromByteArray(frame.image, metadata)

        detector.detectInImage(image)
            .addOnSuccessListener {
                @Synchronized
                if (it.size > 0 && !faceDetected) {
                    faceDetectionListener.faceDetected(image.bitmap, it[0].boundingBox)
                    faceDetected = true
                }
            }
    }

    private fun updateMetadataIfNeeded(frame: Frame) {
        if (frame.rotation != currentRotation) {
            metadata = FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setHeight(frame.size.height)
                .setWidth(frame.size.width)
                .setRotation(when (frame.rotation) {
                    0 -> FirebaseVisionImageMetadata.ROTATION_0
                    90 -> FirebaseVisionImageMetadata.ROTATION_270
                    180 -> FirebaseVisionImageMetadata.ROTATION_180
                    else -> FirebaseVisionImageMetadata.ROTATION_90
                })
                .build()
        }
    }

    interface FaceDetectionListener {
        fun faceDetected(bitmap: Bitmap, boundingBox: Rect)
    }
}
