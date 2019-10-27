package com.example.facedetection.welcome

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.facedetection.R
import com.example.facedetection.detection.DetectionActivity
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.concurrent.TimeUnit

class WelcomeActivity : AppCompatActivity() {

    var disposable: Disposable? = null

    companion object {
        const val TAG = "WelcomeActivity"
        const val WELCOME_ACTIVITY_DELAY_MILLI= 4000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        disposable = Single.timer(WELCOME_ACTIVITY_DELAY_MILLI, TimeUnit.MILLISECONDS)
            .doOnSubscribe {
                Glide.with(this)
                    .load(R.drawable.welcome)
                    .into(welcomeAnimationImageView)
            }
            .subscribe({
                startActivity(Intent(this, DetectionActivity::class.java))
                this.finish()
            }, {
                Log.e(TAG, "Error playing animation")
            })
    }

    override fun onPause() {
        disposable?.dispose()
        super.onPause()
    }
}
