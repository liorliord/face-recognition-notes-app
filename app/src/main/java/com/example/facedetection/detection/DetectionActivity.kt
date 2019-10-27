package com.example.facedetection.detection

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.facedetection.R
import com.example.facedetection.add.AddNewFaceActivity
import com.example.facedetection.notes.NotesActivity
import io.fotoapparat.Fotoapparat
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_detection.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class DetectionActivity : AppCompatActivity(), DetectionView {

    companion object {
        const val TAG = "DetectionActivity"
    }
    private lateinit var fotoapparat: Fotoapparat
    private val presenter = DetectionPresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detection)
        refresh.setOnClickListener {
            presenter.refreshClicked()
        }
        presenter.init()
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun onPause() {
        super.onPause()
        Glide.with(this)
            .onStop()
        presenter.pause()
    }

    override fun startCamera() {
        cameraView.visibility = View.VISIBLE
        fotoapparat.start()
    }

    override fun stopCamera() {
        fotoapparat.stop()
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun initCamera(frameDetectionProcessor: FrameDetectionProcessor) {
        fotoapparat = Fotoapparat.with(this)
            .into(cameraView)
//            .lensPosition(front())
            .frameProcessor(frameDetectionProcessor)
            .build()
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun takePicture(path: String): Single<Boolean> {
        return Single.create { emitter ->
            Toast.makeText(this, "taking image...",Toast.LENGTH_SHORT).show()
            Handler().postDelayed(
                {
                    val imageFile = File(path)
                    imageFile.parentFile?.mkdirs()
                    imageFile.createNewFile()
                    FileOutputStream(imageFile, false).use {
                        fotoapparat.takePicture().saveToFile(imageFile)
                        Toast.makeText(this, "picture taken",Toast.LENGTH_SHORT).show()
                        emitter.onSuccess(true)
                    }
                },
                1000
            )
        }
    }

    override fun toast(text: String) {
        Toast.makeText(this, text,Toast.LENGTH_SHORT).show()
    }

    override fun showRefresh() {
        refresh.visibility = View.VISIBLE
        refresh.isClickable = true
    }

    override fun hideRefresh() {
        refresh.visibility = View.GONE
        refresh.isClickable = false
    }

    override fun startVerifySuccessfulAnimation() {
        recognizingAnimationImageView.visibility = View.VISIBLE
        Glide.with(this)
            .load(R.drawable.verify_successful)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e(TAG, "Failed to load gif")
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    (resource as GifDrawable).setLoopCount(1)
                    resource.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable) {
                            runOnUiThread {
                                presenter.verifyAnimationEnded()
                            }
                        }
                    })
                    return false
                }
            })
            .into(recognizingAnimationImageView)
    }

    override fun startRecognizingAnimation() {
        recognizingAnimationImageView.visibility = View.VISIBLE
        Glide.with(this)
            .load(R.drawable.recognizing_only)
            .into(recognizingAnimationImageView)
    }

    override fun hideCamera() {
        fotoapparat.stop()
        cameraView.visibility = View.INVISIBLE
    }

    override fun stopDetectingAnimation() {
        recognizingAnimationImageView.visibility = View.GONE
    }

    override fun switchToNotes() {
        val intent = Intent(applicationContext, NotesActivity::class.java)
        val faceDetails = presenter.getFaceDetails()
        intent.putExtra("name", faceDetails?.name)
        intent.putExtra("id", faceDetails?.id)
        startActivity(intent)
        this.finish()
    }

    override fun switchToAddNewFace() {
        startActivity(Intent(applicationContext, AddNewFaceActivity::class.java))
        this.finish()
    }
}
