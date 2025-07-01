package com.baag.treksahayatri.ui.guide.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baag.treksahayatri.R
import com.baag.treksahayatri.data.model.User

class ChatUserAdapter(private val onClick: (User) -> Unit) :
    ListAdapter<User, ChatUserAdapter.ChatUserViewHolder>(DiffCallback()) {

    inner class ChatUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.username)

        fun bind(user: User) {
            name.text = user.name
            itemView.setOnClickListener { onClick(user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_user, parent, false)
        return ChatUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatUserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.uid == newItem.uid
        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
    }
}
