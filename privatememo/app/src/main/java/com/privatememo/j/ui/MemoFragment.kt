package com.privatememo.j.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.privatememo.j.R
import com.privatememo.j.adapter.MemoViewPagerAdapter
import com.privatememo.j.databinding.MemofragmentBinding
import com.privatememo.j.viewmodel.MemoViewModel
import kotlinx.android.synthetic.main.memofragment.*

class MemoFragment : Fragment() {

    lateinit var MemoBinding: MemofragmentBinding
    var memoViewModel = MemoViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        MemoBinding = DataBindingUtil.inflate(inflater, R.layout.memofragment, memofrag,false)
        memoViewModel = ViewModelProvider(this).get(MemoViewModel::class.java)
        MemoBinding.setLifecycleOwner(this)
        MemoBinding.memoViewModel = memoViewModel

        return MemoBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var adapter = MemoViewPagerAdapter(requireActivity())
        viewpager_setting(adapter)
        tablayout_setting()

    }

    fun viewpager_setting(adapter : MemoViewPagerAdapter){
        viewpager.adapter = adapter
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewpager.offscreenPageLimit = 2

        //화면 돌아갈 시 콜백
        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                println("$position")
                //indicator.animatePageSelected(position)
            }
        })
    }

    fun tablayout_setting(){

        //viewpager와 tablayout 연결
        TabLayoutMediator(tabs, viewpager) { tab, position ->

            when(position) {
                0 -> {
                    tab.text = "첫 번째"
                }
                1 -> {
                    tab.text = "두 번쩨"
                }
            }
        }.attach()

    }
}