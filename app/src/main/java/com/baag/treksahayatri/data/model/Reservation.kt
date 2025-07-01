package com.baag.treksahayatri.data.model


data class Reservation(
    val id: String = "",
    val travellerId: String = "",
    val travellerName: String = "",
    val date: String = "",
    val time: String = "",
    val status: String = "pending",
    val roomCategory: String = ""  // ➕ new field
)

