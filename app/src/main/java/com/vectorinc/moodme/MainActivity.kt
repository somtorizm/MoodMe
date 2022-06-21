package com.vectorinc.moodme

import android.app.ActivityManager
import android.content.Context
import android.media.CamcorderProfile
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.ar.core.ArCoreApk
import com.google.ar.core.AugmentedFace
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.rendering.Renderable
import com.vectorinc.moodme.model.ViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val MIN_OPENGL_VERSION = 3.0
    }

    lateinit var arFragment: FaceArFragment
    var faceNodeMap = HashMap<AugmentedFace, CustomFaceNode>()
    private var videoRecorder = VideoRecording()
    var isRecording: Boolean? = false
    var recording_txt: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!checkIsSupportedDeviceOrFinish()) {
            return
        }

        setContentView(R.layout.activity_main)
        val model: ViewModel by viewModels()
        arFragment = face_fragment as FaceArFragment
        recording_txt = findViewById(R.id.recoridng_txt)
        recording_txt?.isVisible = false



        val sceneView = arFragment.arSceneView
        sceneView.cameraStreamRenderPriority = Renderable.RENDER_PRIORITY_FIRST
        val scene = sceneView.scene
        videoRecorder.setSceneView(arFragment.getArSceneView());
        val orientationx = resources.configuration.orientation
        videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_2160P, orientationx)


        // Specify the AR scene view to be recorded.

        // Specify the AR scene view to be recorded.
        videoRecorder.setSceneView(arFragment.arSceneView)

        // Set video quality and recording orientation to match that of the device.

        // Set video quality and recording orientation to match that of the device.
        val orientation = resources.configuration.orientation
        videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_2160P, orientation)


        record_btn.setOnClickListener {
            if (isRecording == false) {
                startRecording()
            } else {
                stopRecoridng()
            }


        }
        mustache.setOnClickListener {
            model.selectButton()
        }

        mustache1.setOnClickListener {
            model.selectButton1()
        }


        scene.addOnUpdateListener {
            sceneView.session
                ?.getAllTrackables(AugmentedFace::class.java)?.let {
                    for (f in it) {
                        if (!faceNodeMap.containsKey(f)) {
                            val faceNode = CustomFaceNode(f, this, model)
                            faceNode.setParent(scene)
                            faceNodeMap.put(f, faceNode)
                        }
                    }
                    // Remove any AugmentedFaceNodes associated with an AugmentedFace that stopped tracking.
                    val iter = faceNodeMap.entries.iterator()
                    while (iter.hasNext()) {
                        val entry = iter.next()
                        val face = entry.key
                        if (face.trackingState == TrackingState.STOPPED) {
                            val faceNode = entry.value
                            faceNode.setParent(null)
                            iter.remove()
                        }
                    }
                }


        }


    }

    private fun checkIsSupportedDeviceOrFinish(): Boolean {
        if (ArCoreApk.getInstance()
                .checkAvailability(this) == ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE
        ) {
            Toast.makeText(this, "Augmented Faces requires ARCore", Toast.LENGTH_LONG).show()
            finish()
            return false
        }
        val openGlVersionString = (getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)
            ?.deviceConfigurationInfo
            ?.glEsVersion

        openGlVersionString?.let { s ->
            if (java.lang.Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
                Toast.makeText(this, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show()
                finish()
                return false
            }
        }
        return true
    }

    fun startRecording() {
        if (isRecording == false) {
            var record = videoRecorder.onToggleRecord()
            record = true
            isRecording = true
            Log.d("Record status", "Recording started")
            recording_txt?.isVisible = true
        }


    }

    fun stopRecoridng() {
        if (isRecording == true) {
            var record = videoRecorder.onToggleRecord()
            record = false
            isRecording = false
            Log.d("Record status", "Recording stopped")
            recording_txt?.isVisible = false


        }


    }

    override fun onDestroy() {
        super.onDestroy()
    }
}