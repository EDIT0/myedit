package com.privatememo.j.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.privatememo.j.R
import kotlinx.android.synthetic.main.onlypicfragment.*
import kotlinx.android.synthetic.main.searchfragment.*

class OnlyPicFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.onlypicfragment, onlypicfrag, false)


        return rootView
    }
}