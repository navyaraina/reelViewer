package com.example.reelview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

class VideoFragment : Fragment() {
    private lateinit var likeButton: ImageView
    private lateinit var shareButton: ImageView
    private lateinit var downloadButton: ImageView
    private lateinit var uploadButton: ImageView
    private val storage = FirebaseStorage.getInstance()

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView

    private var videoUri: Uri? = null

    companion object {
        private const val ARG_VIDEO_URI = "video_uri"
        private val REQUEST_VIDEO_CAPTURE = 1

        fun newInstance(videoUri: String): VideoFragment {
            val fragment = VideoFragment()
            val args = Bundle()
            args.putString(ARG_VIDEO_URI, videoUri)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.videoview, container, false)

        videoUri = Uri.parse(arguments?.getString(ARG_VIDEO_URI))

        playerView = view.findViewById(R.id.video_view)
        likeButton = view.findViewById(R.id.like)
        shareButton = view.findViewById(R.id.share)
        downloadButton = view.findViewById(R.id.dowload)
        uploadButton = view.findViewById(R.id.upload)

        setupVideo()
        setupButtons()

        return view
    }

    private fun setupVideo() {
        player = ExoPlayer.Builder(requireContext()).build()
        playerView.player = player
        videoUri?.let {
            val mediaItem = MediaItem.fromUri(it)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()
        player.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        player.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    private fun setupButtons() {
        var isLiked = false
        likeButton.setOnClickListener {
            if (!isLiked) {
                likeButton.setImageResource(R.drawable.ic_heart_filled)
                isLiked = true
            } else {
                likeButton.setImageResource(R.drawable.ic_heart_outlined)
            }
        }

        shareButton.setOnClickListener {
            videoUri?.let { uri -> shareVideo(uri.toString()) }
        }

        downloadButton.setOnClickListener {
            downloadVideo()
        }

        uploadButton.setOnClickListener {
            openCamera()
        }
    }
    private var recordedVideoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            recordedVideoUri = data?.data

            uploadVideo(recordedVideoUri!!)
        }
    }

    private fun openCamera() {
        val videoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(videoIntent, REQUEST_VIDEO_CAPTURE)
    }


    private fun shareVideo(uri: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Check out this video: $uri")
        }
        startActivity(Intent.createChooser(shareIntent, "Share Video"))
    }

    private fun downloadVideo() {
        videoUri?.let { uri ->
            val videoRef = storage.reference.child(uri.toString())
            val localFile = File(context?.cacheDir, uri.toString().substringAfterLast("/")) // Convert Uri to String

            videoRef.getFile(localFile)
                .addOnSuccessListener {
                    Log.d("VideoFragment", "Video downloaded successfully: ${localFile.absolutePath}")
                }
                .addOnFailureListener { e ->
                    Log.e("VideoFragment", "Failed to download video", e)
                }
        } ?: run {
            Log.e("VideoFragment", "Video URI is null")
        }
    }

    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun uploadVideo(videoUri: Uri) {
        val videoRef = storage.reference.child("videos/${getRandomString(10)}")
        videoRef.putFile(videoUri)
            .addOnSuccessListener {
                Log.d("VideoFragment", "Video uploaded successfully")
                Toast.makeText(context, "Video Uploaded Successfully", Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).fetchVideoUrisFromFirebase()
                Toast.makeText(context, "Refreshing feed", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e ->
                Log.e("VideoFragment", "Failed to upload video", e)
                Toast.makeText(context, "Video Upload Failed", Toast.LENGTH_SHORT).show()

            }
    }
}