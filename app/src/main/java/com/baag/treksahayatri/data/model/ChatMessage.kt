package com.baag.treksahayatri.data.model


data class ChatMessage(
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val type: MessageType = MessageType.TEXT,
    val imageUrl: String? = null
)

enum class MessageType {
    TEXT, IMAGE
}

