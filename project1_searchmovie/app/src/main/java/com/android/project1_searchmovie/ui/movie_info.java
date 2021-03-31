package com.android.project1_searchmovie.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.project1_searchmovie.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class movie_info extends AppCompatActivity {

    TextView tv1, tv2, tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11, gotodetal;

    StringRequest request;
    RequestQueue requestQueue;

    String[] get_nationNm = new String[3];
    String[] get_genreNm = new String[3];
    String[] get_peopleNm_director = new String[3];
    String[] get_peopleNm_actor = new String[3];
    String[] get_cast = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Movie Information");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent getintent = getIntent();
        String movie_info_url = getintent.getStringExtra("movie_info_url");

        if(requestQueue == null) {
            //getApplicationContext() 메소드를 통해 Context 객체를 전달
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        detail_info(movie_info_url);

        tv1 = findViewById(R.id.textview1);
        tv2 = findViewById(R.id.textview2);
        tv3 = findViewById(R.id.textview3);
        tv4 = findViewById(R.id.textview4);
        tv5 = findViewById(R.id.textview5);
        tv6= findViewById(R.id.textview6);
        tv7 = findViewById(R.id.textview7);
        tv8 = findViewById(R.id.textview8);
        tv9 = findViewById(R.id.textview9);
        tv10 = findViewById(R.id.textview10);
        tv11 = findViewById(R.id.textview11);
        gotodetal = findViewById(R.id.gotodetail);

    }

    void detail_info(String url){
        request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("movieInfoResult");
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("movieInfo");

                    final String moive_name = jsonObject3.getString("movieNm");
                    tv1.setText(moive_name);
                    tv2.setText(jsonObject3.getString("movieNmEn"));
                    tv3.setText(jsonObject3.getString("showTm")+"분");
                    String temp_openDt = jsonObject3.getString("openDt");
                    String year = temp_openDt.substring(0,4);
                    String month = temp_openDt.substring(4,6);
                    String days = temp_openDt.substring(6,8);
                    tv4.setText(year+"년 "+month+"월 "+days+"일");
                    tv5.setText(jsonObject3.getString("typeNm"));

                    JSONArray jsonArray1 = jsonObject3.getJSONArray("nations");
                    for(int i=0;i<3;i++){
                        try {
                            JSONObject item1 = jsonArray1.getJSONObject(i);
                            get_nationNm[i] = item1.getString("nationNm"); //국가
                        }catch (Exception e){}
                            //tv6.setText(tv6.getText().toString() + " " + get_nationNm[i]);
                        if(!(get_nationNm[i] == null)){
                            tv6.append(get_nationNm[i] + " ");
                        }

                    }

                    JSONArray jsonArray2 = jsonObject3.getJSONArray("genres");
                    for(int i=0;i<3;i++){
                        try {
                            JSONObject item2 = jsonArray2.getJSONObject(i);
                            get_genreNm[i] = item2.getString("genreNm"); //장르
                            Log.i("태그tag",get_genreNm[i]);
                        } catch (Exception e){ }

                        if(!(get_genreNm[i] == null)){
                            tv7.append(get_genreNm[i] + " ");
                        }
                    }

                    JSONArray jsonArray3 = jsonObject3.getJSONArray("directors");
                    for(int i=0;i<3;i++){
                        try {
                            JSONObject item3 = jsonArray3.getJSONObject(i);
                            get_peopleNm_director[i] = item3.getString("peopleNm"); //감독
                        } catch (Exception e){ }
                        if(!(get_peopleNm_director[i] == null)){
                            tv8.append(get_peopleNm_director[i] + " ");
                        }
                    }

                    JSONArray jsonArray4 = jsonObject3.getJSONArray("actors");
                    for(int i=0;i<3;i++){
                        try {
                            JSONObject item4 = jsonArray4.getJSONObject(i);
                            get_peopleNm_actor[i] = item4.getString("peopleNm"); //배우
                            get_cast[i] = item4.getString("cast"); //역
                        }catch (Exception e){}
                        if(!(get_peopleNm_director[i] == null)){
                            tv9.append(get_peopleNm_actor[i] + "\n(" + get_cast[i] + ")\n");
                        }

                    }

                    JSONArray jsonArray5 = jsonObject3.getJSONArray("audits");
                    JSONObject item5 = jsonArray5.getJSONObject(0);
                    String get_watchGradeNm = item5.getString("watchGradeNm"); //등급
                    tv10.setText(get_watchGradeNm);

                    JSONArray jsonArray6 = jsonObject3.getJSONArray("companys");
                    JSONObject item6 = jsonArray6.getJSONObject(0);
                    JSONObject item7 = jsonArray6.getJSONObject(1);
                    String get_company = item6.getString("companyNm"); //등급
                    String get_company2 = item7.getString("companyNm");
                    tv11.setText(get_company+"\n"+get_company2);

                    gotodetal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query="+moive_name));
                            startActivity(intent);
                        }
                    });


                } catch (Exception e) {
                    Log.i("태그", String.valueOf(e.getStackTrace()));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {};
        request.setShouldCache(false); //캐시 데이터 false, 이전 데이터가 있으면 다시 보여줄 수 있으므로 항상 새로 받은 데이터 보여주기 위해 false
        requestQueue.add(request); //RequestQueue에 추가*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}