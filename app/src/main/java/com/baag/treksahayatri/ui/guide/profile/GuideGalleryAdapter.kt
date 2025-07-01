package com.baag.treksahayatri.ui.guide.profile

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baag.treksahayatri.R
import com.baag.treksahayatri.data.model.GuideMedia
import com.bumptech.glide.Glide

class GuideGalleryAdapter(
    private val onItemClick: (GuideMedia) -> Unit,
    private val onItemDelete: (GuideMedia) -> Unit
) : ListAdapter<GuideMedia, GuideGalleryAdapter.GalleryViewHolder>(DiffCallback()) {

    inner class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.ivGalleryItem)

        fun bind(media: GuideMedia) {
            Glide.with(itemView.context)
                .load(media.url)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(imageView)

            imageView.setOnClickListener {
                onItemClick(media)
            }

            imageView.setOnLongClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle("Delete ${media.type.capitalize()}?")
                    .setMessage("Are you sure you want to delete this ${media.type}?")
                    .setPositiveButton("Delete") { _, _ -> onItemDelete(media) }
                    .setNegativeButton("Cancel", null)
                    .show()
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery_photo_square, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<GuideMedia>() {
        override fun areItemsTheSame(oldItem: GuideMedia, newItem: GuideMedia): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: GuideMedia, newItem: GuideMedia): Boolean {
            return oldItem == newItem
        }
    }
}
