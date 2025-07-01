package com.baag.treksahayatri.ui.traveller.ui.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.baag.treksahayatri.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

	private val viewModel: HomeViewModel by viewModels()
	private lateinit var adapter: TrailAdapter
	private var _binding: FragmentHomeBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentHomeBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// Set current date and time
		setCurrentDateTime()

		// Initialize the RecyclerView and Adapter
		adapter = TrailAdapter(mutableListOf()) // Initialize with an empty mutable list
		binding.destinationsRecyclerView.layoutManager = LinearLayoutManager(context)
		binding.destinationsRecyclerView.adapter = adapter

		// Observe the trails data from the ViewModel
		viewModel.trails.observe(viewLifecycleOwner) { trails ->
			trails?.let {
				// Convert the immutable list to a mutable list before updating the adapter
				adapter.updateTrails(it.toMutableList())
			}
		}

		// Fetch the trails data
		viewModel.fetchTrails()

		// Set up difficulty category card click listeners
		binding.difficultCategoryCard.setOnClickListener {
			viewModel.filterByDifficulty("Difficult")
		}

		binding.moderateCategoryCard.setOnClickListener {
			viewModel.filterByDifficulty("Moderate")
		}

		binding.easyCategoryCard.setOnClickListener {
			viewModel.filterByDifficulty("Easy")
		}

		// Set up search functionality
		binding.searchEditText.addTextChangedListener {
			val query = it.toString()
			viewModel.filterTrails(query)  // Filter trails based on the search query
		}

		binding.btnSos.setOnClickListener {
			activity?.let { context ->
				val intent = Intent(context, SOSActivity::class.java)
				startActivity(intent)
			}
		}

	}

	private fun setCurrentDateTime() {
		val calendar = Calendar.getInstance()

		// Format for the date
		val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
		val currentDate = dateFormat.format(calendar.time)

		// Format for the time
		val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
		val currentTime = timeFormat.format(calendar.time)

		// Set the date and time on the TextViews
		binding.dayText.text = currentDate
		binding.dateText.text = currentTime
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
