package com.privatememo.j.utility

import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.privatememo.j.adapter.MemoViewPagerAdapter
import com.privatememo.j.ui.MemoFragment
import kotlinx.android.synthetic.main.memofragment.*

object MemoUtility {

    /*@BindingAdapter("ViewPagerSetting")
    @JvmStatic
    fun a (viewPager2: ViewPager2){
        *//*(rcv.adapter as main_rcv_adapter).items = items
        rcv.adapter?.notifyDataSetChanged()*//*

        var adapter = MemoViewPagerAdapter(FragmentActivity())

        viewPager2.adapter = adapter
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager2.offscreenPageLimit = 3

        //화면 돌아갈 시 콜백
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                println("$position")
                //indicator.animatePageSelected(position)
            }
        })
    }*/

    /*@BindingAdapter("TabLayoutSetting")
    @JvmStatic
    fun b (tabLayout: TabLayout){
        //viewpager와 tablayout 연결
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->

            when(position) {
                0 -> {
                    tab.text = "첫 번째"
                }
                1 -> {
                    tab.text = "두 번쩨"
                }
                2 -> {
                    tab.text = "세 번째"
                }
            }
        }.attach()
    }*/
}