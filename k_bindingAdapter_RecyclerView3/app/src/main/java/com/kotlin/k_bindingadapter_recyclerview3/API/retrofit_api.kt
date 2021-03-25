package com.kotlin.k_bindingadapter_recyclerview3.API

import com.kotlin.k_bindingadapter_recyclerview3.data.Movie_info
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface retrofit_api {

    @GET("movie")
    fun get_info(@Query("query") query: String?, @Query("language") language:String?, @Query("page") page: String?, @Query("api_key") api_key: String?): Call<Movie_info>

}