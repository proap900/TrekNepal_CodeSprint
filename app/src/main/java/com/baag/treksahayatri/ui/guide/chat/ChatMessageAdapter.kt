package com.baag.treksahayatri.ui.guide.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baag.treksahayatri.R
import com.baag.treksahayatri.data.model.ChatMessage
import com.baag.treksahayatri.data.model.MessageType
import com.bumptech.glide.Glide

class ChatMessageAdapter(private val currentUserId: String) :
    ListAdapter<ChatMessage, RecyclerView.ViewHolder>(DiffCallback()) {

    private val TYPE_TEXT_SENT = 1
    private val TYPE_TEXT_RECEIVED = 2
    private val TYPE_IMAGE_SENT = 3
    private val TYPE_IMAGE_RECEIVED = 4

    override fun getItemViewType(position: Int): Int {
        val msg = getItem(position)
        val isSender = msg.senderId == currentUserId
        return when (msg.type) {
            MessageType.TEXT -> if (isSender) TYPE_TEXT_SENT else TYPE_TEXT_RECEIVED
            MessageType.IMAGE -> if (isSender) TYPE_IMAGE_SENT else TYPE_IMAGE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_TEXT_SENT -> TextViewHolder(inflater.inflate(R.layout.item_text_sent, parent, false))
            TYPE_TEXT_RECEIVED -> TextViewHolder(inflater.inflate(R.layout.item_text_received, parent, false))
            TYPE_IMAGE_SENT -> ImageViewHolder(inflater.inflate(R.layout.item_image_sent, parent, false))
            else -> ImageViewHolder(inflater.inflate(R.layout.item_image_received, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = getItem(position)
        if (holder is TextViewHolder) {
            holder.textView.text = msg.message
        } else if (holder is ImageViewHolder) {
            Glide.with(holder.itemView).load(msg.imageUrl).into(holder.imageView)
        }
    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.messageText)
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.messageImage)
    }

    class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage) = oldItem.timestamp == newItem.timestamp
        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage) = oldItem == newItem
    }
}
