package com.d121211017.stroyappsubmission.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d121211017.stroyappsubmission.R
import com.d121211017.stroyappsubmission.data.remote.entity.ListStoryItem
import com.d121211017.stroyappsubmission.ui.DetailActivity

class RecyclerViewAdapter(private val storyList: List<ListStoryItem>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val storyThumbnail: ImageView = view.findViewById(R.id.iv_item_photo)
        val storyTitle: TextView = view.findViewById(R.id.tv_item_name)
        val storyDescription: TextView = view.findViewById(R.id.story_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val story = storyList[position]

        Glide
            .with(holder.itemView)
            .load(story.photoUrl)
            .placeholder(R.drawable.baseline_image_24)
            .into(holder.storyThumbnail)

        holder.apply {
            storyTitle.text = story.name
            storyDescription.text = story.description
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_STORY, storyList[position])
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

}