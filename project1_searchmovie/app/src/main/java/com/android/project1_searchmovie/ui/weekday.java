package com.android.project1_searchmovie.ui;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.project1_searchmovie.R;
import com.android.project1_searchmovie.adapter.weekday_adapter;
import com.android.project1_searchmovie.data.weekday_list;

import java.util.ArrayList;

public class weekday extends Fragment {

    ViewGroup rootView;

    public static ArrayList<weekday_list> list_array = new ArrayList<>();

    RecyclerView rv;
    public static weekday_adapter d_adapter;

    Button b1;

    String date;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_weekday, container, false);

        weekday_adapter da = new weekday_adapter(getActivity(), list_array);
        //Toast.makeText(getActivity(), ""+da.getItemCount(),Toast.LENGTH_SHORT).show();

        rv = rootView.findViewById(R.id.recycler);
        d_adapter = new weekday_adapter(getActivity(), list_array);

        //리사이클러뷰에 레이아웃 매니저 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);

        //리사이클러뷰에 어댑터 설정
        rv.setAdapter(d_adapter);
        //list_array.clear();

        d_adapter.notifyDataSetChanged(); //리사이클러뷰 갱신



        /*callvolley(date);*/

        return rootView;
    }


}