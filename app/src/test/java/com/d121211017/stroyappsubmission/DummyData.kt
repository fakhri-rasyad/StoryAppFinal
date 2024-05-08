package com.d121211017.stroyappsubmission

import com.d121211017.stroyappsubmission.data.remote.entity.ListStoryItem

object DummyData {
    fun getDummyData() : List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for(i in 1..100){
            items.add(
                ListStoryItem(
                    "https://story-api.dicoding.dev/images/stories/photos-1715136124547_5281e3bfe2d43abb75e4.jpg",
                    "2024-05-08T02:42:04.549Z",
                    "hhh",
                    "test bro",
                    "story-tPRi4DqTTdLaUu54",
                    -7.3649664,
                    112.69205
                )
            )
        }

        return items
    }
}