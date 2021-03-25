package com.kotlin.k_bindingadapter_recyclerview3.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.kotlin.k_bindingadapter_recyclerview3.R

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val actionBar = supportActionBar
        actionBar?.hide()

        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed({ finish() }, 2000)
    }
}