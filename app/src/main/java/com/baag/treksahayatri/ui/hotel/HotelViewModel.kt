package com.baag.treksahayatri.ui.hotel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baag.treksahayatri.data.model.Hotel
import com.baag.treksahayatri.data.model.RoomCategory
import kotlinx.coroutines.launch



class HotelViewModel : ViewModel() {
    private val repository = HotelRepository()

    val hotel = MutableLiveData<Hotel>()
    val isLoading = MutableLiveData(false)

    fun fetchHotel() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getOrCreateHotel()
            result?.let { hotel.value = it }
            isLoading.value = false
        }
    }


    fun saveHotel(updated: Hotel) {
        viewModelScope.launch {
            val summary = calculateSummary(updated.roomCategories)
            val fullHotel = updated.copy(
                totalRooms = summary.first,
                availableRooms = summary.second,
                bookedRooms = summary.third
            )
            repository.updateHotel(fullHotel)
            hotel.value = fullHotel
        }
    }
    fun observeHotel() {
        repository.observeHotelRealtime(
            onChange = { hotel.value = it },
            onError = { e -> e.printStackTrace() }
        )
    }

    private fun calculateSummary(categories: List<RoomCategory>): Triple<Int, Int, Int> {
        val total = categories.sumOf { it.total }
        val available = categories.sumOf { it.available }
        val booked = categories.sumOf { it.booked }
        return Triple(total, available, booked)
    }
}

