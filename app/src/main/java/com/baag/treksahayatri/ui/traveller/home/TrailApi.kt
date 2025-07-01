package com.baag.treksahayatri.ui.traveller.ui.Home

import com.baag.treksahayatri.data.model.Trail
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
	@GET("api/trails/")
	fun getTrails(): Call<List<Trail>>
}

