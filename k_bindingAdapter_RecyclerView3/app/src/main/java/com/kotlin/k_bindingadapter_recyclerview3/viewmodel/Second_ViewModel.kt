package com.kotlin.k_bindingadapter_recyclerview3.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class Second_ViewModel : ViewModel(){

    var button_text = ObservableField<String>()
    var rating_text = "평점 "
    var date_text = "개봉일  "

    var vm_title = ObservableField<String>()
    var vm_date = ObservableField<String>()
    var vm_rating = ObservableField<Float>()
    var vm_overview = ObservableField<String>()
    var vm_poster_path = ObservableField<String>()

    init {
        button_text.set("돌아가기")
    }



}