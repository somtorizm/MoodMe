package com.vectorinc.moodme

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.CamcorderProfile
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.ar.core.ArCoreApk
import com.google.ar.core.AugmentedFace
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.rendering.Renderable
import com.vectorinc.moodme.model.ViewModel
import com.vectorinc.moodme.ui.FaceArFragment
import com.vectorinc.moodme.utils.CustomFaceNode
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    companion object {
        const val MIN_OPENGL_VERSION = 1.0
    }

    var arFragment: FaceArFragment? = null
    var faceNodeMap = HashMap<AugmentedFace, CustomFaceNode>()
    private var videoRecorder = VideoRecording()
    var isRecording: Boolean? = false
    var recording_txt: TextView? = null
    var sceneView: SceneView? = null
    var scene: Scene? = null
    val model: ViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!checkIsSupportedDeviceOrFinish()) {
            finish()
            return
        }
        if (!checkPermission()) {
            requestPermission()
        }
        arFragment = face_fragment as FaceArFragment
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            sceneView = arFragment?.arSceneView
            (sceneView as ArSceneView?)?.cameraStreamRenderPriority =
                Renderable.RENDER_PRIORITY_FIRST
            scene = (sceneView as ArSceneView?)?.scene
        }
        videoRecorder.setSceneView(arFragment?.arSceneView)
        val orientation = resources.configuration.orientation
        // Set video quality and recording orientation to match that of the device.
        videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_720P, orientation)


        // Specify the AR scene view to be recorded.



        recording_txt = findViewById(R.id.recoridng_txt)
        recording_txt?.isVisible = false

        record_btn.setOnClickListener {
            if (isRecording == false) {
                startRecording()
                it.setBackgroundResource((R.drawable.ic_baseline_fiber_manual_record_24))
            } else {
                stopRecoridng()
                showDialog()
                it.setBackgroundResource(R.drawable.shape_record)
            }
        }


        move_to_media.setOnClickListener {
            startActivity(Intent(this, MediaFiles::class.java))
        }
        mustache.setOnClickListener {
            model.selectButton()
        }

        mustache1.setOnClickListener {
            model.selectButton1()
        }
        scene?.addOnUpdateListener {
            (sceneView as ArSceneView?)?.session
                ?.getAllTrackables(AugmentedFace::class.java)?.let {
                    for (f in it) {
                        if (!faceNodeMap.containsKey(f)) {
                            val faceNode = CustomFaceNode(f, this, model)
                            faceNode.setParent(scene)
                            faceNodeMap[f] = faceNode
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

    private fun requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                startActivityForResult(intent, 2296)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, 2296)
            }
        } else {
            //below android 11
            val PERMISSION_REQUEST_CODE = 2296
            ActivityCompat.requestPermissions(
                this,
                arrayOf(WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
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

    override fun onPause() {
        super.onPause()
        sceneView?.pause()
    }

    override fun onResume() {
        super.onResume()
        sceneView?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        sceneView?.destroy()
    }

    fun showDialog() {
        val builder = AlertDialog.Builder(this, R.style.DialogeTheme)
        val view = layoutInflater.inflate(R.layout.custom_dialog, null)
        builder.setPositiveButton(
            "Save"
        ) { _, _ ->
            videoRecorder.saveImage(view.record_txt_naming.text.trim().toString())
            Toast.makeText(
                this,
                "Video saved to " + videoRecorder.videoPath.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
            .setNegativeButton(
                "Cancel"
            ) { _, _ ->
                videoRecorder.dontSaveVideo()
            }.setMessage("Save Video")

            .setView(view)

        // Create the AlertDialog object and return it
        builder.create().show()
    }

    private fun checkPermission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result =
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
            val result1 =
                ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }


}