package com.example.reelview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.storage.FirebaseStorage
import android.util.Log

class MainActivity : AppCompatActivity() {
    private val storage = FirebaseStorage.getInstance()
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.view_pager)

        fetchVideoUrisFromFirebase()
    }
    fun fetchVideoUrisFromFirebase() {
        val videosRef = storage.reference.child("videos")

        videosRef.listAll()
            .addOnSuccessListener { listResult ->
                val videoUris = mutableListOf<String>()

                val uriTasks = listResult.items.map { item ->
                    item.downloadUrl.addOnSuccessListener { uri ->
                        videoUris.add(uri.toString())
                        if (videoUris.size == listResult.items.size) {
                            setupViewPager(videoUris)
                        }
                    }.addOnFailureListener { e ->
                        Log.e("MainActivity", "Failed to get download URL", e)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("MainActivity", "Failed to list videos", e)
            }
    }


    private fun setupViewPager(videoUris: List<String>) {
        val loopedUris = mutableListOf<String>()
        repeat(1000) {
            loopedUris.addAll(videoUris)
        }

        val adapter = VideoPagerAdapter(this, loopedUris)
        viewPager.adapter = adapter
    }

}