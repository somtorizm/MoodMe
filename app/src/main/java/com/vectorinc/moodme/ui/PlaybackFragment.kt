package com.vectorinc.moodme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vectorinc.moodme.R
import kotlinx.android.synthetic.main.fragment_playback.view.*


class PlaybackFragment : Fragment() {

    private lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            path = it.getString(LETTER).toString()
        }

    }

    companion object {
        var LETTER = "name"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_playback, container, false)
        view.video_player.setVideoPath(path)
        view.video_player.start()

        return view

    }

}