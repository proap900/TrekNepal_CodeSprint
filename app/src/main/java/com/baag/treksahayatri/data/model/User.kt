package com.baag.treksahayatri.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "Traveller",
    val loginMethod: String = "email"
)