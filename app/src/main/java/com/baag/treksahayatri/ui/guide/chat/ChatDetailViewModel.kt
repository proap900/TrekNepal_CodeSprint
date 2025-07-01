package com.baag.treksahayatri.ui.guide.chat

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baag.treksahayatri.data.model.ChatMessage
import com.baag.treksahayatri.data.model.MessageType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ChatDetailViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> = _messages

    fun getChatId(user1: String, user2: String): String {
        return if (user1 < user2) "${user1}_$user2" else "${user2}_$user1"
    }

    fun loadMessages(chatId: String) {
        firestore.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(ChatMessage::class.java) }
                    _messages.value = list
                }
            }
    }

    fun sendText(chatId: String, senderId: String, receiverId: String, message: String) {
        val msg = ChatMessage(senderId, receiverId, message, System.currentTimeMillis(), MessageType.TEXT)
        firestore.collection("chats").document(chatId).collection("messages").add(msg)
    }

    fun sendImage(chatId: String, senderId: String, receiverId: String, imageUri: Uri, onUploaded: () -> Unit) {
        val fileRef = storage.reference.child("chat_images/${UUID.randomUUID()}.jpg")
        fileRef.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) throw task.exception ?: Exception("Upload failed")
                fileRef.downloadUrl
            }
            .addOnSuccessListener { uri ->
                val msg = ChatMessage(senderId, receiverId, "", System.currentTimeMillis(), MessageType.IMAGE, uri.toString())
                firestore.collection("chats").document(chatId).collection("messages").add(msg)
                onUploaded()
            }
    }
}
