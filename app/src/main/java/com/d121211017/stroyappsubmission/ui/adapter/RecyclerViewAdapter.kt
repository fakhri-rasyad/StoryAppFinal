package com.d121211017.stroyappsubmission.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d121211017.stroyappsubmission.R
import com.d121211017.stroyappsubmission.data.remote.entity.ListStoryItem
import com.d121211017.stroyappsubmission.ui.DetailActivity

class RecyclerViewAdapter : PagingDataAdapter<ListStoryItem, RecyclerViewAdapter.ViewHolder>(
    DIFF_CALLBACK) {
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

        val story = getItem(position)

        if(story != null){
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
                intent.putExtra(DetailActivity.EXTRA_STORY, story)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}