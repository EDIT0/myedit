package com.privatememo.j.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.privatememo.j.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    var fm: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //fm.beginTransaction().replace(R.id.framelayout, fragment2()).commit()
        with(fm.beginTransaction()){
            replace(
                R.id.framelayout,
                MemoFragment()
            )
            commit()
        }

        bottom_tab.setOnNavigationItemSelectedListener(this)


    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId){
            R.id.toolbar_item_memo ->{
                fm.beginTransaction().replace(
                    R.id.framelayout,
                    MemoFragment()
                ).commit()

                return true
            }
            R.id.toolbar_item_search -> {
                fm.beginTransaction().replace(
                    R.id.framelayout,
                    SearchFragment()
                ).commit()
                return true
            }
            R.id.toolbar_item_calendar -> {
                fm.beginTransaction().replace(
                    R.id.framelayout,
                    CalendarFragment()
                ).commit()
                return true
            }
            R.id.toolbar_item_setting -> {
                fm.beginTransaction().replace(
                    R.id.framelayout,
                    SettingFragment()
                ).commit()
                return true
            }
        }
        return false
    }
}