package com.d121211017.stroyappsubmission.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.d121211017.stroyappsubmission.data.remote.entity.ListStoryItem
import com.d121211017.stroyappsubmission.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding

    companion object {
        var EXTRA_STORY = "extra_story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story =
            if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra(EXTRA_STORY, ListStoryItem::class.java)}
            else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(EXTRA_STORY)
            }

        binding.apply {
            if (story != null) {
                Glide.with(this@DetailActivity).load(story.photoUrl).into(this.ivDetailPhoto)
                tvDetailName.text = story.name
                tvDetailDescription.text = story.description
            }

        }

    }
}