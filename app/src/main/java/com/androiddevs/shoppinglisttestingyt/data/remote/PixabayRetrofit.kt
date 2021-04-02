package com.androiddevs.shoppinglisttestingyt.data.remote

import com.androiddevs.shoppinglisttestingyt.BuildConfig
import com.androiddevs.shoppinglisttestingyt.other.Constants
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

class PixabayRetrofit {

    private lateinit var pixabayAPI: PixabayAPI

    init {
        pixabayAPI = createAPI(createRetrofit())
    }

    private fun createRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private fun createAPI(retrofit: Retrofit): PixabayAPI =
        retrofit.create(PixabayAPI::class.java)
}