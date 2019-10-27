package com.example.facedetection.detection

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.amazonaws.services.rekognition.model.Image
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest
import com.example.facedetection.aws.AwsManager.Companion.COLLECTION_ID
import com.example.facedetection.aws.AwsManager.Companion.COMPRESSION_QUALITY
import com.example.facedetection.aws.AwsManager.Companion.MINIMUM_MATCH_PERCENTAGE
import com.example.facedetection.aws.AwsManager.Companion.rekognitionClient
import com.example.facedetection.face.FaceDetails
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class DetectionModel {

    private var disposable: Disposable? = null

    private fun bitmapToJpegByteBuffer(bitmap: Bitmap): ByteBuffer {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,
            COMPRESSION_QUALITY, outputStream)
        return ByteBuffer.wrap(outputStream.toByteArray())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getName(bitmap: Bitmap): Single<FaceDetails> {
        return Single.just(bitmap)
            .subscribeOn(Schedulers.io())
            .map {
                val request = SearchFacesByImageRequest()
                    .withCollectionId(COLLECTION_ID)
                    .withImage(Image().withBytes(bitmapToJpegByteBuffer(it)))
                    .withMaxFaces(1)
                    .withFaceMatchThreshold(MINIMUM_MATCH_PERCENTAGE)
                val result = rekognitionClient.searchFacesByImage(request)

                if (result.faceMatches != null && result.faceMatches.size > 0) {
                    FaceDetails(result.faceMatches.first().face.externalImageId, result.faceMatches.first().face.faceId)
                } else {
                    FaceDetails()
                }
            }
    }

    fun stop() {
        disposable?.dispose()
    }

}