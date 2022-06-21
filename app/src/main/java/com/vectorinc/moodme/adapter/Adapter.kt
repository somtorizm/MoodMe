package com.vectorinc.moodme.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.vectorinc.moodme.R


class Adapter (private val videoList: List<VideoModel>,
               private val context: Context) : BaseAdapter() {
        private var layoutInflater: LayoutInflater? = null
        private lateinit var videoTitle: TextView
        private lateinit var videoImage: ImageView

        override fun getCount(): Int {
           return videoList.size
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        override fun getItemId(p0: Int): Long {
            return 0
         }

        override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
            var convertView = p1
            // on blow line we are checking if layout inflater
            // is null, if it is null we are initializing it.
            if (layoutInflater == null) {
                layoutInflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            }
            // on the below line we are checking if convert view is null.
            // If it is null we are initializing it.
            if (convertView == null) {
                // on below line we are passing the layout file
                // which we have to inflate for each item of grid view.
                convertView = layoutInflater!!.inflate(R.layout.grid_layout, null)
            }
            // on below line we are initializing our course image view
            // and course text view with their ids.
            videoImage = convertView!!.findViewById(R.id.idIVCourse)
            videoTitle = convertView!!.findViewById(R.id.idTVCourse)
            // on below line we are setting image for our course image view.

            videoImage.setImageBitmap(ThumbnailUtils.createVideoThumbnail(videoList.get(position).videoPath.toString(),
                MediaStore.Video.Thumbnails.MINI_KIND));
            // on below line we are setting text in our course text view.
            videoTitle.setText(videoList.get(position).videoName)
            // at last we are returning our convert view.
            return convertView
        }

    }
