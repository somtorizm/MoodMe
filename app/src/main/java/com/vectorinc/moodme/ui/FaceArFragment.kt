package com.vectorinc.moodme.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment
import java.io.File
import java.util.*

class FaceArFragment : ArFragment() {
    var sessions : Session? = null
    override fun getSessionConfiguration(session: Session?): Config {
        val config = Config(session)
        sessions = session
        config.augmentedFaceMode = Config.AugmentedFaceMode.MESH3D
        return config

    }


    override fun getSessionFeatures(): MutableSet<Session.Feature> {
        return EnumSet.of(Session.Feature.FRONT_CAMERA)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val frameLayout = super.onCreateView(inflater, container, savedInstanceState) as? FrameLayout
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)

        return  frameLayout
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}