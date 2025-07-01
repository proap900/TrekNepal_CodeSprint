//package com.baag.treksahayatri.ui.traveller.ui.Home
//
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.baag.treksahayatri.R
//import com.baag.treksahayatri.data.model.Trail
//import com.baag.treksahayatri.databinding.ItemTrailBinding
//import com.baag.treksahayatri.ui.traveller.ui.Home.SeeMore.SeeMoreActivity
//import com.bumptech.glide.Glide
//
//class TrailAdapter(private var trailList: MutableList<Trail>) : RecyclerView.Adapter<TrailAdapter.TrailViewHolder>() {
//
//	inner class TrailViewHolder(val binding: ItemTrailBinding) : RecyclerView.ViewHolder(binding.root)
//
//	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {
//		val binding = ItemTrailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//		return TrailViewHolder(binding)
//	}
//
//	override fun onBindViewHolder(holder: TrailViewHolder, position: Int) {
//		val trail = trailList[position]
//
//		holder.binding.apply {
//			trailName.text = trail.name
//			trailLocation.text = "Region: ${trail.region}"
//
//			// Load image using Glide
//			if (trail.images.isNotEmpty()) {
//				Glide.with(holder.itemView.context)
//					.load(trail.images[0].image)
//					.placeholder(R.drawable.cityscape)
//					.into(trailImage)
//			}
//
//			seeMoreButton.setOnClickListener {
//				// Create an Intent to open SeeMoreActivity
//				val intent = Intent(holder.itemView.context, SeeMoreActivity::class.java)
//
//				// Pass the Trail object to the SeeMoreActivity
//				intent.putExtra("trailData", trail)
//
//				// Start SeeMoreActivity
//				holder.itemView.context.startActivity(intent)
//			}
//
//		}
//	}
//
//	override fun getItemCount(): Int = trailList.size
//
//	// Method to update the data in the adapter
//	fun updateTrails(newTrails: List<Trail>) {
//		trailList.clear()  // Clear the old data
//		trailList.addAll(newTrails)  // Add the new data
//		notifyDataSetChanged()  // Notify the adapter to refresh the view
//	}
//}









package com.baag.treksahayatri.ui.traveller.ui.Home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baag.treksahayatri.R
import com.baag.treksahayatri.data.model.Trail
import com.baag.treksahayatri.databinding.ItemTrailBinding
import com.baag.treksahayatri.ui.traveller.ui.Home.SeeMore.SeeMoreActivity
import com.bumptech.glide.Glide

class TrailAdapter(private var trailList: MutableList<Trail>) : RecyclerView.Adapter<TrailAdapter.TrailViewHolder>() {

	inner class TrailViewHolder(val binding: ItemTrailBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {
		val binding = ItemTrailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return TrailViewHolder(binding)
	}

	override fun onBindViewHolder(holder: TrailViewHolder, position: Int) {
		val trail = trailList[position]

		holder.binding.apply {
			trailName.text = trail.name
			trailLocation.text = "Region: ${trail.region}"

			// Load image using Glide
			if (trail.images.isNotEmpty()) {
				Glide.with(holder.itemView.context)
					.load(trail.images[0].image)
					.placeholder(R.drawable.cityscape)
					.into(trailImage)
			}

			seeMoreButton.setOnClickListener {
				// Create an Intent to open SeeMoreActivity
				val intent = Intent(holder.itemView.context, SeeMoreActivity::class.java)

				// Pass the Trail object to the SeeMoreActivity
				intent.putExtra("trailData", trail)

				// Start SeeMoreActivity
				holder.itemView.context.startActivity(intent)
			}

		}
	}

	override fun getItemCount(): Int = trailList.size

	// Method to update the data in the adapter
	fun updateTrails(newTrails: List<Trail>) {
		trailList.clear()  // Clear the old data
		trailList.addAll(newTrails)  // Add the new data
		notifyDataSetChanged()  // Notify the adapter to refresh the view
	}
}
