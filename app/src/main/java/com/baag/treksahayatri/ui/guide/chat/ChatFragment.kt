package com.baag.treksahayatri.ui.guide.chat

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.baag.treksahayatri.R
import com.baag.treksahayatri.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel = ChatListViewModel()
    private lateinit var adapter: ChatUserAdapter
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ChatUserAdapter { user ->
            val intent = Intent(requireContext(), ChatDetailActivity::class.java)
            intent.putExtra("receiverId", user.uid)
            startActivity(intent)
        }

        binding.chatUserRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.chatUserRecycler.adapter = adapter

        viewModel.chatUsers.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.fetchChatUsers(currentUserId)
    }
}
