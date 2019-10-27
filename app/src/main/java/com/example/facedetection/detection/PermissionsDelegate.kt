package com.example.facedetection.detection

import android.Manifest.permission
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


internal class PermissionsDelegate(private val activity: Activity) {

    fun hasPermissions(): Boolean {
        val writePermission = ContextCompat.checkSelfPermission(
            activity,
            permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraPermission = ContextCompat.checkSelfPermission(
            activity,
            permission.CAMERA
        )
        val internetPermission = ContextCompat.checkSelfPermission(
            activity,
            permission.INTERNET
        )
        return writePermission == PackageManager.PERMISSION_GRANTED &&
                cameraPermission == PackageManager.PERMISSION_GRANTED &&
                internetPermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                permission.WRITE_EXTERNAL_STORAGE,
                permission.CAMERA,
                permission.INTERNET
            ),
            REQUEST_CODE
        )
    }

    companion object {
        private const val REQUEST_CODE = 10
    }
}