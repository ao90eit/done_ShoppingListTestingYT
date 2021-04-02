package com.androiddevs.shoppinglisttestingyt.data.remote

import com.androiddevs.shoppinglisttestingyt.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchString: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): Response<PixabayResponse>
}