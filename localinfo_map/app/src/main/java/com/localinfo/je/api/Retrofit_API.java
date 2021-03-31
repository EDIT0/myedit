package com.localinfo.je.api;

import com.localinfo.je.data.Keyword_Result;
import com.localinfo.je.data.address_itmes;
import com.localinfo.je.data.notice_items;
import com.localinfo.je.data.search_address_Result_for_statistic;
import com.localinfo.je.data.statistic_items;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Retrofit_API {

    @GET("v2/local/search/keyword.json")
    Call<Keyword_Result> get_Search_LocationName(
            @Header("Authorization") String token,
            @Query("query") String query,
            @Query("size") int size
    );

    @GET("/v2/local/search/address.json")
    Call<address_itmes> get_Search_LocationName_address(
            @Header("Authorization") String token,
            @Query("query") String query,
            @Query("size") int size,
            @Query("analyze_type") String type
    );

    @GET("v2/local/search/keyword.json")
    Call<Keyword_Result> get_Search_Location_Directly(
            @Header("Authorization") String token,
            @Query("query") String query,
            @Query("x") String x,
            @Query("y") String y,
            @Query("size") int size,
            @Query("radius") int radius,
            @Query("page") int page,
            @Query("sort") String sort
    );

    //카테고리로 검색
    @GET("v2/local/search/category.json")
    Call<Keyword_Result> get_Search_Location_Directly_category(
            @Header("Authorization") String token,
            @Query("category_group_code") String category_group_code,
            @Query("query") String query,
            @Query("x") String x,
            @Query("y") String y,
            @Query("size") int size,
            @Query("radius") int radius,
            @Query("page") int page,
            @Query("sort") String sort
    );

    @GET("v2/local/geo/coord2regioncode.json")
    Call<search_address_Result_for_statistic> get_Search_address(
            @Header("Authorization") String token,
            @Query("x") String x,
            @Query("y") String y,
            @Query("input_coord") String input_coord,
            @Query("output_coord") String output_coord
            //x, y, input_coord, output_coord
    );

    @GET("localinfo_map/notice_list_api.php")
    Call<notice_items> get_notice_list();

    @GET("localinfo_map/json_seoul_local_data.json")
    Call<statistic_items> get_statistic_list();

}
