package com.kotlin.k_bindingadapter_recyclerview3.data

data class Movie_info(
    var page: Int,
    var results: ArrayList<Movie_info2>,
    var total_pages: Int,
    var total_results: Int
){


}