package com.example.facedetection.aws

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import com.amazonaws.services.rekognition.model.CreateCollectionRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class AwsManager {
    companion object {
        private const val accessKey = "AKIAJZ2YMRQPRY7OAJSQ"
        private const val secretKey = "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"
        const val COLLECTION_ID = "liorAndNoasCollection"
        const val MINIMUM_MATCH_PERCENTAGE = 90F
        const val COMPRESSION_QUALITY = 80

        var rekognitionClient : AmazonRekognitionClient = AmazonRekognitionClient(
            BasicAWSCredentials(
                accessKey,
                secretKey
            )
        )

        init {
            val disposable = Single.just(COLLECTION_ID)
                .subscribeOn(Schedulers.io())
                .doOnSuccess {
                    rekognitionClient.createCollection(CreateCollectionRequest().withCollectionId(it))
                }
                .doOnError {
                    it.printStackTrace()
                }
                .subscribe(
                    {}, {
                        it.printStackTrace()
                    }
                )
        }
    }


}