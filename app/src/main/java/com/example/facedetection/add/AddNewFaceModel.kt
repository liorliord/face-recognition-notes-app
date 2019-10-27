package com.example.facedetection.add

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.amazonaws.services.rekognition.model.Image
import com.amazonaws.services.rekognition.model.IndexFacesRequest
import com.example.facedetection.aws.AwsManager
import com.example.facedetection.aws.AwsManager.Companion.COMPRESSION_QUALITY
import com.example.facedetection.detection.DetectionModel
import com.example.facedetection.face.FaceDetails
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class AddNewFaceModel {
    var disposable: Disposable? = null

    private fun bitmapToJpegByteBuffer(bitmap: Bitmap): ByteBuffer {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,
            COMPRESSION_QUALITY, outputStream)
        return ByteBuffer.wrap(outputStream.toByteArray())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addFace(name: String, bitmap: Bitmap, listener: Listener) {
        disposable = Single.just(Pair(name, bitmap))
            .subscribeOn(Schedulers.io())
            .map {(name, bitmap) ->
                val image = Image()
                    .withBytes(bitmapToJpegByteBuffer(bitmap))

                val request = IndexFacesRequest()
                    .withImage(image)
                    .withCollectionId(AwsManager.COLLECTION_ID)
                    .withExternalImageId(name)
                    .withDetectionAttributes("DEFAULT")

                AwsManager.rekognitionClient.indexFaces(request)

            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                listener.faceAdded(
                    FaceDetails(result.faceRecords.first().face.externalImageId,
                        result.faceRecords.first().face.faceId)
                )
            }
    }

    interface Listener {
        fun faceAdded(faceDetails: FaceDetails)
    }
}