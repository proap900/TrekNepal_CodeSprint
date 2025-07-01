package com.baag.treksahayatri.ui.traveller.ui.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baag.treksahayatri.data.model.Trail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

	private val _trails = MutableLiveData<List<Trail>>()
	val trails: LiveData<List<Trail>> get() = _trails

	private var originalTrails: List<Trail> = emptyList()

	// Fetch the trails data from the API
	fun fetchTrails() {
		RetrofitInstance.api.getTrails().enqueue(object : Callback<List<Trail>> {
			override fun onResponse(call: Call<List<Trail>>, response: Response<List<Trail>>) {
				if (response.isSuccessful) {
					originalTrails = response.body() ?: emptyList()
					_trails.postValue(originalTrails)
				}
			}

			override fun onFailure(call: Call<List<Trail>>, t: Throwable) {
				// Handle failure
			}
		})
	}

	// Filter trails based on search query (name or region)
	fun filterTrails(query: String) {
		val filteredList = originalTrails.filter {
			it.name.contains(query, ignoreCase = true) || it.region.contains(query, ignoreCase = true)
		}
		_trails.postValue(filteredList)
	}

	// Filter trails based on difficulty
	fun filterByDifficulty(difficulty: String) {
		val filteredList = originalTrails.filter { it.difficulty.equals(difficulty, ignoreCase = true) }
		_trails.postValue(filteredList)
	}
}






//class HomeViewModel : ViewModel() {
//
//	private val _trails = MutableLiveData<List<Trail>>()
//	val trails: LiveData<List<Trail>> get() = _trails
//
//	// Fetch the trails data from the API
//	fun fetchTrails() {
//		RetrofitInstance.api.getTrails().enqueue(object : Callback<List<Trail>> {
//			override fun onResponse(call: Call<List<Trail>>, response: Response<List<Trail>>) {
//				if (response.isSuccessful) {
//					_trails.postValue(response.body()) // Update the live data
//				}
//			}
//
//			override fun onFailure(call: Call<List<Trail>>, t: Throwable) {
//				// Handle failure (you can add error handling logic here)
//			}
//		})
//	}
//}
