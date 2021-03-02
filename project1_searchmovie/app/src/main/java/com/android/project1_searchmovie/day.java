package com.android.project1_searchmovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class day extends Fragment {

    ViewGroup rootView;

    public static ArrayList<data_list> list_array = new ArrayList<>();

    RecyclerView rv;
    public static day_adapter d_adapter;

    Button b1;

    String date;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_day, container, false);

        day_adapter da = new day_adapter(getActivity(), list_array);
        //Toast.makeText(getActivity(), ""+da.getItemCount(),Toast.LENGTH_SHORT).show();

        rv = rootView.findViewById(R.id.recycler);
        d_adapter = new day_adapter(getActivity(), list_array);

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