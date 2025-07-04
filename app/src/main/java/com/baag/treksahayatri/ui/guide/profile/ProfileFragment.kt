package com.baag.treksahayatri.ui.guide.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.baag.treksahayatri.data.model.GuideMedia
import com.baag.treksahayatri.databinding.FragmentProfileBinding
import com.baag.treksahayatri.ui.auth.LoginActivity
import com.baag.treksahayatri.ui.traveller.ui.Profile.EditProfileBottomSheet
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var adapter: GuideGalleryAdapter
    private var currentFilter = "image"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        setupObservers()
        setupUI()

        return binding.root
    }

    private fun setupUI() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        adapter = GuideGalleryAdapter(onItemClick = { item ->
            showFullScreenMedia(item)
        }, onItemDelete = { item ->
            viewModel.deleteMedia(uid, item)
        })
        binding.rvGallery.layoutManager = GridLayoutManager(context, 3)
        binding.rvGallery.adapter = adapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentFilter = if (tab?.position == 0) "image" else "video"
                viewModel.loadMedia(uid, currentFilter)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.rowEditProfile.setOnClickListener {
            EditProfileBottomSheet().show(parentFragmentManager, "EditProfileBottomSheet")
        }

        binding.rowChangePassword.setOnClickListener {
          //  findNavController().navigate(R.id.action_profile_to_changePasswordFragment)
        }

        binding.rowLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        binding.switchAvailability.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateAvailability(uid, isChecked)
        }
    }

    private fun setupObservers() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModel.loadProfile(uid)
        viewModel.loadMedia(uid, currentFilter)

        viewModel.profile.observe(viewLifecycleOwner) { user ->
            binding.tvProfileName.text = user.name
            binding.tvProfileEmail.text = user.email
            binding.tvProfileDescription.text = user.description
            binding.tvProfileGuidingPlaces.text = "Guiding Places: ${user.guidingPlaces}"
            binding.switchAvailability.isChecked = user.isAvailable
            Glide.with(this).load(user.photoUrl).into(binding.ivProfileImage)
        }

        viewModel.mediaItems.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmptyGallery.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showFullScreenMedia(media: GuideMedia) {
        val intent = Intent(requireContext(), FullScreenViewerActivity::class.java)
        intent.putExtra("media_url", media.url)
        intent.putExtra("media_type", media.type)
        startActivity(intent)
    }
}
