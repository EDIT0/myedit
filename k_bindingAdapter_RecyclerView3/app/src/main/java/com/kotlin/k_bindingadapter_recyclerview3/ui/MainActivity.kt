package com.kotlin.k_bindingadapter_recyclerview3.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.k_bindingadapter_recyclerview3.R
import com.kotlin.k_bindingadapter_recyclerview3.adapter.main_rcv_adapter
import com.kotlin.k_bindingadapter_recyclerview3.databinding.ActivityMainBinding
import com.kotlin.k_bindingadapter_recyclerview3.viewmodel.Main_ViewModel

class MainActivity : AppCompatActivity() {

    var viewModel = Main_ViewModel()
    lateinit var binding: ActivityMainBinding
    var adapter = main_rcv_adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)

        viewModel = ViewModelProvider(this).get(Main_ViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.setLifecycleOwner(this)
        binding.mainviewmodel = viewModel

        var layoutmanager =LinearLayoutManager(binding.rcv.context)
        binding.rcv.layoutManager = layoutmanager
        binding.rcv.adapter = adapter

        if(viewModel.items.size == 0){
            binding.layout.visibility = View.VISIBLE
        }
        else{
            binding.layout.visibility = View.INVISIBLE
        }

    }
}