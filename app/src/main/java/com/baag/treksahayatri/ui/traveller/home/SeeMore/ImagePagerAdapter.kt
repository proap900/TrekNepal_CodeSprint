package com.baag.treksahayatri.ui.traveller.ui.Home.SeeMore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baag.treksahayatri.R
import com.baag.treksahayatri.databinding.ItemImageBinding
import com.bumptech.glide.Glide

//class ImagePagerAdapter(private val images: List<String>) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {
//
//	// ViewHolder for binding the item view (image view)
//	inner class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)
//
//	// Inflate the view for each item in the ViewPager
//	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
//		val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//		return ImageViewHolder(binding)
//	}
//
//	// Bind the data to the image view using Glide
//	override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//		val imageUrl = images[position]
//
//		// Load image using Glide into the ImageView
//		Glide.with(holder.itemView.context)
//			.load(imageUrl)
//			.placeholder(R.drawable.cityscape)  // Placeholder image until the actual image loads
//			.into(holder.binding.imageView)     // Set image to ImageView
//	}
//
//	// Return the number of images in the list
//	override fun getItemCount(): Int = images.size
//}
class ImagePagerAdapter(private val imageUrls: List<String>) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
		val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ImageViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
		val imageUrl = imageUrls[position]
		Glide.with(holder.itemView.context)
			.load(imageUrl)
			.placeholder(R.drawable.profile_trekker) // Add a placeholder image
			.into(holder.binding.imageView)
	}

	override fun getItemCount(): Int = imageUrls.size

	inner class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)
}
