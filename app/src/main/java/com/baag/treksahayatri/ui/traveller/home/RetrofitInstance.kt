package com.baag.treksahayatri.ui.traveller.ui.Home

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

	private val okHttpClient = OkHttpClient.Builder()
		.connectTimeout(30, TimeUnit.SECONDS)
		.readTimeout(30, TimeUnit.SECONDS)
		.writeTimeout(30, TimeUnit.SECONDS)
		.build()

	private val retrofit by lazy {
		Retrofit.Builder()
			.baseUrl("https://trekking-api.onrender.com/")
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	val api: ApiService by lazy {
		retrofit.create(ApiService::class.java)
	}
}
