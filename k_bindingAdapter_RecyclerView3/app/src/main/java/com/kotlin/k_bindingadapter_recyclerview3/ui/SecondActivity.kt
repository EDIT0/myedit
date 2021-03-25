package com.kotlin.k_bindingadapter_recyclerview3.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kotlin.k_bindingadapter_recyclerview3.R
import com.kotlin.k_bindingadapter_recyclerview3.databinding.SecondActivityBinding
import com.kotlin.k_bindingadapter_recyclerview3.viewmodel.Second_ViewModel
import kotlinx.android.synthetic.main.second_activity.*

class SecondActivity : AppCompatActivity() {

    var viewModel = Second_ViewModel()
    lateinit var binding: SecondActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(Second_ViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.second_activity)
        binding.setLifecycleOwner(this)
        binding.secondviewmodel = viewModel

        val actionBar = supportActionBar
        actionBar!!.title = "영화정보"
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        var getintent = getIntent()

        var act_title = getintent.getStringExtra("title")
        var act_date = getintent.getStringExtra("date")
        var act_rating = getintent.getFloatExtra("rating",0.toFloat())
        var act_overview = getintent.getStringExtra("overview")
        var act_poster_path = getintent.getStringExtra("poster")



        with(viewModel){
            vm_title.set(act_title)
            vm_date.set(act_date)
            vm_rating.set(act_rating/2)
            vm_overview.set(act_overview)
            vm_poster_path.set(act_poster_path)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}