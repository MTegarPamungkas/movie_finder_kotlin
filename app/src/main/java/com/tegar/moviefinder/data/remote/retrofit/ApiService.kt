package com.tegar.moviefinder.data.remote.retrofit

import com.tegar.moviefinder.data.model.DetailResponse
import com.tegar.moviefinder.data.model.SearchResponse
import com.tegar.moviefinder.data.model.TrendingResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("trending/movie/day?language=en-US")
    suspend fun getRecomendationMovie(
        @Query("page") page: Int = 1,
    ): TrendingResponse

    @GET("search/movie")
    suspend fun searchMovie(@Query("query") title: String): SearchResponse

    @GET("movie/{id}")
    suspend fun detailMovie(@Path("id") id: Int): DetailResponse
}