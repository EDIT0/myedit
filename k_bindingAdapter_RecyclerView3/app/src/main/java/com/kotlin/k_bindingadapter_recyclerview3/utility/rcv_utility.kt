package com.kotlin.k_bindingadapter_recyclerview3.utility

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlin.k_bindingadapter_recyclerview3.adapter.main_rcv_adapter
import com.kotlin.k_bindingadapter_recyclerview3.data.Movie_info2

object rcv_utility {

    @BindingAdapter("main_rcv")
    @JvmStatic
    fun a (rcv: RecyclerView, items : ArrayList<Movie_info2>){
        (rcv.adapter as main_rcv_adapter).items = items
        rcv.adapter?.notifyDataSetChanged()
    }
    
    @BindingAdapter("main_rcv_image", "error")
    @JvmStatic
    fun b (iv : ImageView, url : String?, error : Drawable){
        var image_baseurl = "http://image.tmdb.org/t/p/original/"
        Glide.with(iv.context).load(image_baseurl + url).override(500,500).error(error).into(iv)
    }


}