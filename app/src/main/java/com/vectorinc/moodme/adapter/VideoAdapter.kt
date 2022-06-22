package com.vectorinc.moodme

import android.content.Context
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vectorinc.moodme.model.VideoModel
import com.vectorinc.moodme.ui.VideoItemsDirections
import kotlinx.android.synthetic.main.grid_layout.view.*

class VideoAdapterprivate(val items: List<VideoModel>, val context: Context) :
    RecyclerView.Adapter<VideoAdapterprivate.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(items: VideoModel) {
            itemView.image_txt.text = items.videoName
            var image_path = items.videoPath.toString()
            var imageView = itemView.image_thumbnail
            imageView.setOnClickListener{
                val action = VideoItemsDirections.actionVideoItemsToPlaybackFragment(image_path,items.videoName.toString())
                itemView.findNavController().navigate(action)



            }
            loadImage(image_path,imageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.grid_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items?.get(position)!!)
    }

    override fun getItemCount(): Int {
        return items?.size!!
    }
   fun loadImage(string: String, imageView: ImageView){
       Glide
           .with(context)
           .load(string)
           .centerCrop()
           .into(imageView.image_thumbnail);
   }
}