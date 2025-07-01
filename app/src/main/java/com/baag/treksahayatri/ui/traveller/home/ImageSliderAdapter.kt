package com.baag.treksahayatri.ui.traveller.ui.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baag.treksahayatri.databinding.ItemImageSlideBinding
import com.bumptech.glide.Glide

class ImageSliderAdapter(private val images: List<String>) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

	inner class ImageViewHolder(val binding: ItemImageSlideBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
		val binding = ItemImageSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ImageViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
		val imageUrl = images[position]
		Glide.with(holder.binding.imageView.context)
			.load(imageUrl)
			.into(holder.binding.imageView)
	}

	override fun getItemCount(): Int = images.size
}
