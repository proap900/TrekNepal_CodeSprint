package com.baag.treksahayatri.ui.traveller.ui.Home.SeeMore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.baag.treksahayatri.R
import com.baag.treksahayatri.data.model.Image
import com.baag.treksahayatri.data.model.Trail
import com.baag.treksahayatri.databinding.ActivitySeeMoreBinding
import com.bumptech.glide.Glide

class SeeMoreActivity : AppCompatActivity() {

	private lateinit var binding: ActivitySeeMoreBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Set up view binding
		binding = ActivitySeeMoreBinding.inflate(layoutInflater)
		setContentView(binding.root)

		// Get the passed Trail object
		val trail: Trail = intent.getParcelableExtra("trailData")!!

		// Set up UI elements
		setupViewPager(trail.images)
		binding.trailName.text = trail.name
//		binding.trailLocation.text = "Region: ${trail.region}"
//		binding.trailDescription.text = trail.description

		// Optionally load the first image as a header image or placeholder
//		if (trail.images.isNotEmpty()) {
//			Glide.with(this)
//				.load(trail.images[0].image)
//				.placeholder(R.drawable.img)
//				.into(binding.headerImage)
//		}
	}

	private fun setupViewPager(images: List<Image>) {
		val imageUrls = images.map { it.image }  // Assuming Image has a 'image' URL field
		val adapter = ImagePagerAdapter(imageUrls)
		binding.viewPager.adapter = adapter
	}

}
