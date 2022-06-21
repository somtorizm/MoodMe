package com.vectorinc.moodme

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.navigation.findNavController
import com.vectorinc.moodme.adapter.Adapter
import com.vectorinc.moodme.adapter.VideoModel
import kotlinx.android.synthetic.main.activity_media_files.*
import kotlinx.android.synthetic.main.fragment_video_items.*
import kotlinx.android.synthetic.main.fragment_video_items.view.*
import java.io.File


class VideoItems : Fragment() {
    var pathList : MutableList<VideoModel> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment

        for (items in pathList){
            Log.d("Items name",items.videoName.toString())
            Log.d("Items path",items.videoPath.toString())

        }
        val courseAdapter = Adapter(pathList, activity?.applicationContext!!)
        val view =  inflater.inflate(R.layout.fragment_video_items, container, false)
        view.grid_layout.adapter = courseAdapter
        view.grid_layout.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val action = VideoItemsDirections.actionVideoItemsToPlaybackFragment(name = pathList.get(position).videoPath.toString())
            view.findNavController().navigate(action)


        }
         return  view
    }

    override fun onResume() {
        super.onResume()
        pathList.clear()
        getListofFiles()

    }

    fun getListofFiles(){
        var dirx = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/MoodMe"
        )
        val lister: File = dirx.absoluteFile
        lister.list()?.forEachIndexed{index, s ->
            var  videoModel = VideoModel(s.toString(), "$dirx/$s")
            pathList.add(index,videoModel)
        }
    }


}