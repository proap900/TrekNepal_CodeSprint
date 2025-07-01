package com.baag.treksahayatri.ui.traveller.ui.Home.SeeMore

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SeeMorePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
	override fun getItemCount(): Int = 3

	override fun createFragment(position: Int): Fragment {
		return when (position) {
			0 -> InfoFragment()
			1 -> GroupsFragment()
			2 -> ReviewFragment()
			else -> throw IllegalArgumentException("Invalid position")
		}
	}
}
