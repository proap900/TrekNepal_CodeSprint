package com.baag.treksahayatri.data.model


data class Hotel(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val imageUrl: String = "",
    val totalRooms: Int = 0,
    val availableRooms: Int = 0,
    val bookedRooms: Int = 0,
    val roomCategories: List<RoomCategory> = listOf()
)

data class RoomCategory(
    val type: String = "",  // "Single", "Double", "Suite"
    val total: Int = 0,
    val available: Int = 0,
    val booked: Int = 0
)
