package com.baag.treksahayatri.ui.traveller.ui.Home.SeeMore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baag.treksahayatri.R
import com.baag.treksahayatri.databinding.FragmentGroupsBinding

class GroupsFragment : Fragment(R.layout.fragment_groups) {

	private var _binding: FragmentGroupsBinding? = null
	private val binding get() = _binding!!

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

//		// Set the groups content here (for example, available groups for the trail)
//		binding.textGroups.text = "Here are the available groups for this trail."
//	}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}