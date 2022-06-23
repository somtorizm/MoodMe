package com.vectorinc.moodme.ui

import android.os.Bundle
import android.os.Environment
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.vectorinc.moodme.R
import com.vectorinc.moodme.VideoAdapterprivate
import com.vectorinc.moodme.model.VideoModel
import kotlinx.android.synthetic.main.fragment_video_items.view.*
import java.io.File


class VideoItems : Fragment() {
    var pathList : MutableList<VideoModel> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getListofFiles()




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view =  inflater.inflate(R.layout.fragment_video_items, container, false)
        val courseAdapter = VideoAdapterprivate(pathList,view.context)

        view.recycler_view?.layoutManager = GridLayoutManager(activity?.applicationContext,4,LinearLayoutManager.VERTICAL,false)
        view.recycler_view.adapter =  courseAdapter

        view.recycler_view.setHasFixedSize(true)
         return  view
    }



    override fun onResume() {
        super.onResume()

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