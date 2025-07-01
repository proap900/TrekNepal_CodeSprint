package com.baag.treksahayatri.ui.traveller.ui.Home.SeeMore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baag.treksahayatri.R
import com.baag.treksahayatri.databinding.FragmentInfoBinding

class InfoFragment : Fragment(R.layout.fragment_info) {

	private var _binding: FragmentInfoBinding? = null
	private val binding get() = _binding!!

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

//		// Set the info content here (for example, info about the trail)
//		binding.textInfo.text = "Information about the trail will go here."
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
