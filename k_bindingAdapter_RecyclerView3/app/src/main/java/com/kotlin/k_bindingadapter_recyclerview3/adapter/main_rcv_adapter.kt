package com.kotlin.k_bindingadapter_recyclerview3.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.k_bindingadapter_recyclerview3.data.Movie_info2
import com.kotlin.k_bindingadapter_recyclerview3.databinding.MainRcvItemBinding
import com.kotlin.k_bindingadapter_recyclerview3.ui.SecondActivity

class main_rcv_adapter : RecyclerView.Adapter<main_rcv_adapter.ViewHolder>() {

    var items = ArrayList<Movie_info2>()

    class ViewHolder(binding : MainRcvItemBinding): RecyclerView.ViewHolder(binding.root){
        var binding = binding



        fun bind(movies : Movie_info2){
            binding.movieinfomodel = movies

            binding.root.setOnClickListener {
                var intent = Intent(binding.root.context, SecondActivity::class.java)
                intent.putExtra("title",movies.title)
                intent.putExtra("date",movies.release_date)
                intent.putExtra("rating",movies.vote_average)
                intent.putExtra("overview",movies.overview)
                intent.putExtra("poster",movies.poster_path)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): main_rcv_adapter.ViewHolder {
        var binding = MainRcvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: main_rcv_adapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

}