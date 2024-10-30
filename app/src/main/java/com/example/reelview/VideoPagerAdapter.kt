package com.example.reelview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class VideoPagerAdapter(activity: FragmentActivity, private val videoUris: List<String>) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = videoUris.size

    override fun createFragment(position: Int): Fragment {
        return VideoFragment.newInstance(videoUris[position % videoUris.size])
    }
}

