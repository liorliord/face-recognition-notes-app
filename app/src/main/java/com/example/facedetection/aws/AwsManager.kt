package com.example.facedetection.aws

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import com.amazonaws.services.rekognition.model.CreateCollectionRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class AwsManager {
    companion object {
        private const val accessKey = "AKIAJTS75V6HV4LIPJ5A"
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

//        init {
//            val disposable = Single.just(COLLECTION_ID)
//                .subscribeOn(Schedulers.io())
//                .doOnSuccess {
//                    rekognitionClient.createCollection(CreateCollectionRequest().withCollectionId(it))
//                }
//                .doOnError {
//                    it.printStackTrace()
//                }
//                .subscribe(
//                    {}, {
//                        it.printStackTrace()
//                    }
//                )
//        }
    }


}