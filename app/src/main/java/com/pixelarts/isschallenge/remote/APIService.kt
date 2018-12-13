package com.pixelarts.isschallenge.remote

import com.pixelarts.isschallenge.common.RELATIVE_URL
import com.pixelarts.isschallenge.model.APIResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET(RELATIVE_URL)
    fun getISSPasses(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("alt") altitude: Int,
        @Query("n") numberOfPasses: Int
    ): Single<APIResponse>
}