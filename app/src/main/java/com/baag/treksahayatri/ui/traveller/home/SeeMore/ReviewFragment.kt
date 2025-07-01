package com.baag.treksahayatri.ui.traveller.ui.Home.SeeMore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baag.treksahayatri.R
import com.baag.treksahayatri.databinding.FragmentReviewBinding

class ReviewFragment : Fragment(R.layout.fragment_review) {

	private var _binding: FragmentReviewBinding? = null
	private val binding get() = _binding!!

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
//
//		// Set the reviews content here (for example, reviews for the trail)
//		binding.textReviews.text = "User reviews for the trail will be shown here."
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}