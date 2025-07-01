package com.baag.treksahayatri.ui.guide.chat

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baag.treksahayatri.R
import com.google.firebase.auth.FirebaseAuth

class ChatDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: ChatDetailViewModel
    private lateinit var adapter: ChatMessageAdapter
    private lateinit var currentUserId: String
    private lateinit var receiverId: String
    private lateinit var chatId: String

    private lateinit var recycler: RecyclerView
    private lateinit var input: EditText
    private lateinit var sendBtn: ImageButton
    private lateinit var imagePicker: ImageView

    private val imagePickLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            viewModel.sendImage(chatId, currentUserId, receiverId, it) {
                Toast.makeText(this, "Image Sent", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)

        receiverId = intent.getStringExtra("receiverId") ?: return
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModel = ChatDetailViewModel()
        chatId = viewModel.getChatId(currentUserId, receiverId)

        recycler = findViewById(R.id.messageRecycler)
        input = findViewById(R.id.messageInput)
        sendBtn = findViewById(R.id.sendButton)
        imagePicker = findViewById(R.id.imagePicker)

        adapter = ChatMessageAdapter(currentUserId)
        recycler.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        recycler.adapter = adapter

        viewModel.messages.observe(this) {
            adapter.submitList(it)
            recycler.scrollToPosition(it.size - 1)
        }

        viewModel.loadMessages(chatId)

        sendBtn.setOnClickListener {
            val msg = input.text.toString().trim()
            if (msg.isNotEmpty()) {
                viewModel.sendText(chatId, currentUserId, receiverId, msg)
                input.setText("")
            }
        }

        imagePicker.setOnClickListener {
            imagePickLauncher.launch("image/*")
        }
    }
}
