package com.baag.treksahayatri.data.model

data class GuideMedia(
    val url: String = "",
    val type: String = "image", // or "video"
    val timestamp: Long = System.currentTimeMillis()
)

