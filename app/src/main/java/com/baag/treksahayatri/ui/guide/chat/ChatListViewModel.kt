package com.baag.treksahayatri.ui.guide.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baag.treksahayatri.data.model.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ChatListViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _chatUsers = MutableLiveData<List<User>>()
    val chatUsers: LiveData<List<User>> = _chatUsers

    fun fetchChatUsers(currentUserId: String) {
        firestore.collection("chats")
            .get()
            .addOnSuccessListener { snapshot ->
                val users = mutableSetOf<String>()
                for (doc in snapshot) {
                    val chatId = doc.id
                    if (chatId.contains(currentUserId)) {
                        val otherId = chatId.replace(currentUserId, "")
                            .replace("_", "")
                        users.add(otherId)
                    }
                }

                // Now fetch user details from "users" collection
                val userList = mutableListOf<User>()
                val tasks = users.map { uid ->
                    firestore.collection("users").document(uid).get()
                }

                Tasks.whenAllSuccess<DocumentSnapshot>(tasks)
                    .addOnSuccessListener { documents ->
                        documents.forEach { snap ->
                            snap.toObject(User::class.java)?.let { userList.add(it) }
                        }
                        _chatUsers.value = userList
                    }
            }
    }
}
