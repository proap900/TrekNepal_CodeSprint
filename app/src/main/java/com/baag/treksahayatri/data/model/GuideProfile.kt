package com.baag.treksahayatri.data.model

data class GuideProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val address: String = "",
    val photoUrl: String = "",
    val phoneNumber: String = "",
    val description: String = "",
    val guidingPlaces: String = "",
    val isAvailable: Boolean = false
)

