package com.kotlin.k_bindingadapter_recyclerview3.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin.k_bindingadapter_recyclerview3.API.retrofit_api
import com.kotlin.k_bindingadapter_recyclerview3.data.Movie_info
import com.kotlin.k_bindingadapter_recyclerview3.data.Movie_info2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Main_ViewModel : ViewModel() {

    var items = ObservableArrayList<Movie_info2>()

    var button_name = "검색"

    var keyword = ObservableField<String>()

    var controler = MutableLiveData<Boolean>()

    init {
        controler.value = false
    }

    fun switching(){
        if(items.size == 0){
            controler.value = false
        }
        else{
            controler.value = true
        }
    }

    fun search(){
        items.clear()
        call_retrofit()
    }

    fun call_retrofit(){
        var baseurl = "https://api.themoviedb.org/3/search/"
        var private_key = "6db59bbd7b6f0437a11a5d733f5889fb"

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseurl)
            .build()
        val client: retrofit_api = retrofit.create(retrofit_api::class.java)
        val call: Call<Movie_info> = client.get_info(keyword.get(),"ko-KR","1", private_key)

        call.enqueue(object : Callback<Movie_info> {
            override fun onResponse(call: Call<Movie_info>, response: Response<Movie_info>) {
                val result: Movie_info? = response.body()

                try {
                    var info: ArrayList<Movie_info2> = result!!.results

                    for (i in 0 until result.results.size) {
                        items.add(result.results.get(i))
                    }
                }catch (e:Exception){

                }

                switching()
            }

            override fun onFailure(call: Call<Movie_info>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }

}