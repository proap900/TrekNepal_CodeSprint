package com.baag.treksahayatri.ui.guide.profile

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.baag.treksahayatri.R
import com.bumptech.glide.Glide

class FullScreenViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageView = ImageView(this)
        setContentView(imageView)

        val url = intent.getStringExtra("media_url") ?: ""
        val type = intent.getStringExtra("media_type") ?: "image"

        if (type == "image") {
            Glide.with(this).load(url).into(imageView)
        } else {
            val videoView = VideoView(this)
            setContentView(videoView)
            videoView.setVideoURI(Uri.parse(url))
            videoView.start()
        }
    }
}
