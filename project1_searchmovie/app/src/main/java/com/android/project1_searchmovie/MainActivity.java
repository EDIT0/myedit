package com.android.project1_searchmovie;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;
    day fragment_day;
    first first;
    weekday fragment_weekday;
    korea_movie fragment_korea_movie;
    foreign_movie fragment_foreign_movie;
    weekend fragment_weekend;
    independent fragment_independent;
    commercial fragment_commercial;

    static Button b1,b2,b3,b4,b5,b6,b7, mybutton;

    static RequestQueue requestQueue1, requestQueue2, requestQueue3, requestQueue4, requestQueue5, requestQueue6, requestQueue7;

    StringRequest request1, request2, request3, request4, request5, request6, request7;
    String day_date, weekday_date, weekend_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, first_loading.class);
        startActivity(intent);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("박스오피스 순위");

        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        b5 = findViewById(R.id.button5);
        b6 = findViewById(R.id.button6);
        b7 = findViewById(R.id.button7);
        mybutton = findViewById(R.id.mybutton);

        fm = getSupportFragmentManager();
        fragment_day = new day();
        first = new first();
        fragment_weekday = new weekday();
        fragment_korea_movie = new korea_movie();
        fragment_foreign_movie = new foreign_movie();
        fragment_weekend = new weekend();
        fragment_independent = new independent();
        fragment_commercial = new commercial();

        fm.beginTransaction().replace(R.id.fragment, first).commit();

        mybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), my_pocket.class);
                startActivity(intent);
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.fragment, fragment_day).commit();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.fragment, fragment_weekday).commit();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.fragment, fragment_weekend).commit();
            }
        });


        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.fragment, fragment_korea_movie).commit();
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.fragment, fragment_foreign_movie).commit();
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.fragment, fragment_independent).commit();
            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.fragment, fragment_commercial).commit();
            }
        });






    }

    @Override
    protected void onResume() {
        super.onResume();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);  // 오늘 날짜에서 하루를 뺌.
        day_date = sdf.format(calendar.getTime());

        if(requestQueue1 == null) {
            //getApplicationContext() 메소드를 통해 Context 객체를 전달
            requestQueue1 = Volley.newRequestQueue(getApplicationContext());

            //b1
            weekly(day_date);
        }

        if(requestQueue2 == null) {
            //getApplicationContext() 메소드를 통해 Context 객체를 전달
            requestQueue2 = Volley.newRequestQueue(getApplicationContext());

            calendar.add(Calendar.DATE, -7);
            weekday_date = sdf.format(calendar.getTime());
            //b2
            weekday(weekday_date);

        }

        if(requestQueue3 == null) {
            //getApplicationContext() 메소드를 통해 Context 객체를 전달
            requestQueue3 = Volley.newRequestQueue(getApplicationContext());

            //b3
            calendar.add(Calendar.DATE, -7);
            weekend_date = sdf.format(calendar.getTime());
            weekend(weekend_date);
        }

        if(requestQueue4 == null) {
            //getApplicationContext() 메소드를 통해 Context 객체를 전달
            requestQueue4 = Volley.newRequestQueue(getApplicationContext());

            //b4
            korea_movie(day_date);
        }

        if(requestQueue5 == null) {
            //getApplicationContext() 메소드를 통해 Context 객체를 전달
            requestQueue5 = Volley.newRequestQueue(getApplicationContext());

            //b5
            foreign_movie(day_date);
        }

        if(requestQueue6 == null) {
            //getApplicationContext() 메소드를 통해 Context 객체를 전달
            requestQueue6 = Volley.newRequestQueue(getApplicationContext());

            //b6
            independent(day_date);
        }

        if(requestQueue7 == null) {
            //getApplicationContext() 메소드를 통해 Context 객체를 전달
            requestQueue7 = Volley.newRequestQueue(getApplicationContext());

            //b7
            commercial(day_date);
        }




    }

    public void weekly(String date){
        String url = "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=c4e62bb6b5adb5b5194d59ab8c9ca518&targetDt=" + date;

        //요청을 위한 Request 객체 생성
        //파라미터 (통신 방식 get, 요청할 url, String으로 응답 받겠음)
        request1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("태그1",response);
                //정상 응답일 경우 실행
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("boxOfficeResult");

                    JSONArray jsonArray = jsonObject2.getJSONArray("dailyBoxOfficeList");

                    //배열 길이만큼 반복
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject item = jsonArray.getJSONObject(i); //getJSONObject 메소드는 json배열을 JSONObject 객체로 만듬

                        //key 값을 가져와서 각각의 String 변수에 넣어줌
                        String get_movieNm = item.getString("movieNm");
                        String get_openDt = item.getString("openDt");
                        String get_audiAcc = item.getString("audiAcc");
                        String get_movieCd = item.getString("movieCd");
                        //String get_prdtYear = jsonObject2.getString("prdtYear");
                        //String get_movieNm = item.getString("movieNm");
                        //String get_openDt = item.getString("openDt");

                        //String second_url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=c4e62bb6b5adb5b5194d59ab8c9ca518&movieCd=" + item.getString("movieCd");

                        /*request(second_url);
                        Log.i("태그333", "아아"+ output);*/

                        /*request1_1 = new StringRequest(Request.Method.GET, second_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(response);
                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("movieInfoResult");
                                    JSONObject jsonObject3 = jsonObject2.getJSONObject("movieInfo");

                                    JSONArray jsonArray1 = jsonObject3.getJSONArray("nations");
                                    JSONObject item1 = jsonArray1.getJSONObject(0);
                                    String get_nationNm = item1.getString("nationNm"); //국가

                                    JSONArray jsonArray2 = jsonObject3.getJSONArray("genres");
                                    JSONObject item2 = jsonArray2.getJSONObject(0);
                                    String get_genreNm = item2.getString("genreNm"); //장르

                                    JSONArray jsonArray3 = jsonObject3.getJSONArray("directors");
                                    JSONObject item3 = jsonArray3.getJSONObject(0);
                                    String get_peopleNm_director = item3.getString("peopleNm"); //감독

                                    JSONArray jsonArray4 = jsonObject3.getJSONArray("actors");
                                    JSONObject item4 = jsonArray4.getJSONObject(0);
                                    String get_peopleNm_actor = item4.getString("peopleNm"); //배우

                                    JSONArray jsonArray5 = jsonObject3.getJSONArray("audits");
                                    JSONObject item5 = jsonArray5.getJSONObject(0);
                                    String get_watchGradeNm = item5.getString("watchGradeNm"); //등급

                                    Log.i("태그", get_nationNm +" / " + get_genreNm +" / " +get_peopleNm_director
                                    +" / " + get_peopleNm_actor + " / " + get_watchGradeNm);


                                } catch (Exception e) {
                                    Log.i("태그", e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {};
                        request1_1.setShouldCache(false); //캐시 데이터 false, 이전 데이터가 있으면 다시 보여줄 수 있으므로 항상 새로 받은 데이터 보여주기 위해 false
                        Log.i("태그111","큐 바로 전1-1");
                        requestQueue1.add(request1_1); //RequestQueue에 추가*/



                        //MainActivity_list 클래스의 객체 생성 후 set 후
                        //MainActivity_lsit의 ArrayList에 add 해줌
                        //각 객체 변수 하나당 한사이클의 데이터 값들이 들어있다는 것
                        data_list d_list = new data_list();

                        d_list.set_movieNm(get_movieNm);
                        d_list.set_openDt(get_openDt);
                        d_list.set_audiAcc(get_audiAcc);
                        d_list.set_movieCd((get_movieCd));



                        day.list_array.add(d_list);

                    }
                    android.util.Log.i("태그","객체화 완료1");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //에러 발생 시 실행
                //tv1.append("에러 -> " + error.getMessage());
            }
        }) { //post 방식으로 파라미터를 넣고 싶을 경우, Request 객체 안에 메소드를 재정의
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                //데이터 보낼 경우 put 사용
                /*String value = "Value";
                params.put("key",value);*/

                return params;
            }
        };

        request1.setShouldCache(false); //캐시 데이터 false, 이전 데이터가 있으면 다시 보여줄 수 있으므로 항상 새로 받은 데이터 보여주기 위해 false
        requestQueue1.add(request1); //RequestQueue에 추가

    }

    public void weekday(String date){
        String url = "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchWeeklyBoxOfficeList.json?key=c4e62bb6b5adb5b5194d59ab8c9ca518&targetDt="+ date +"&weekGb=2";

        //요청을 위한 Request 객체 생성
        //파라미터 (통신 방식 get, 요청할 url, String으로 응답 받겠음)
        request2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //정상 응답일 경우 실행
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("boxOfficeResult");

                    JSONArray jsonArray = jsonObject2.getJSONArray("weeklyBoxOfficeList");

                    //배열 길이만큼 반복
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject item = jsonArray.getJSONObject(i); //getJSONObject 메소드는 json배열을 JSONObject 객체로 만듬

                        //key 값을 가져와서 각각의 String 변수에 넣어줌
                        String get_movieNm = item.getString("movieNm");
                        String get_openDt = item.getString("openDt");
                        String get_audiAcc = item.getString("audiAcc");
                        String get_movieCd = item.getString("movieCd");
                        //String get_prdtYear = jsonObject2.getString("prdtYear");
                        //String get_movieNm = item.getString("movieNm");
                        //String get_openDt = item.getString("openDt");


                        //MainActivity_list 클래스의 객체 생성 후 set 후
                        //MainActivity_lsit의 ArrayList에 add 해줌
                        //각 객체 변수 하나당 한사이클의 데이터 값들이 들어있다는 것
                        weekday_list d_list = new weekday_list();

                        d_list.set_movieNm(get_movieNm);
                        d_list.set_openDt(get_openDt);
                        d_list.set_audiAcc(get_audiAcc);
                        d_list.set_movieCd((get_movieCd));


                        weekday.list_array.add(d_list);

                    }
                    android.util.Log.i("태그","객체화 완료2");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //에러 발생 시 실행
                //tv1.append("에러 -> " + error.getMessage());
            }
        }) { //post 방식으로 파라미터를 넣고 싶을 경우, Request 객체 안에 메소드를 재정의
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                //데이터 보낼 경우 put 사용
                /*String value = "Value";
                params.put("key",value);*/

                return params;
            }
        };

        request2.setShouldCache(false); //캐시 데이터 false, 이전 데이터가 있으면 다시 보여줄 수 있으므로 항상 새로 받은 데이터 보여주기 위해 false
        requestQueue2.add(request2); //RequestQueue에 추가
    }

    public void weekend(String date){
        String url = "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchWeeklyBoxOfficeList.json?key=c4e62bb6b5adb5b5194d59ab8c9ca518&targetDt="+ date;

        //요청을 위한 Request 객체 생성
        //파라미터 (통신 방식 get, 요청할 url, String으로 응답 받겠음)
        request3 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //정상 응답일 경우 실행
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("boxOfficeResult");

                    JSONArray jsonArray = jsonObject2.getJSONArray("weeklyBoxOfficeList");

                    //배열 길이만큼 반복
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject item = jsonArray.getJSONObject(i); //getJSONObject 메소드는 json배열을 JSONObject 객체로 만듬

                        //key 값을 가져와서 각각의 String 변수에 넣어줌
                        String get_movieNm = item.getString("movieNm");
                        String get_openDt = item.getString("openDt");
                        String get_audiAcc = item.getString("audiAcc");
                        String get_movieCd = item.getString("movieCd");
                        //String get_prdtYear = jsonObject2.getString("prdtYear");
                        //String get_movieNm = item.getString("movieNm");
                        //String get_openDt = item.getString("openDt");


                        //MainActivity_list 클래스의 객체 생성 후 set 후
                        //MainActivity_lsit의 ArrayList에 add 해줌
                        //각 객체 변수 하나당 한사이클의 데이터 값들이 들어있다는 것
                        weekend_list d_list = new weekend_list();

                        d_list.set_movieNm(get_movieNm);
                        d_list.set_openDt(get_openDt);
                        d_list.set_audiAcc(get_audiAcc);
                        d_list.set_movieCd((get_movieCd));


                        weekend.list_array.add(d_list);

                    }
                    android.util.Log.i("태그","객체화 완료3");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //에러 발생 시 실행
                //tv1.append("에러 -> " + error.getMessage());
            }
        }) { //post 방식으로 파라미터를 넣고 싶을 경우, Request 객체 안에 메소드를 재정의
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                //데이터 보낼 경우 put 사용
                /*String value = "Value";
                params.put("key",value);*/

                return params;
            }
        };

        request3.setShouldCache(false); //캐시 데이터 false, 이전 데이터가 있으면 다시 보여줄 수 있으므로 항상 새로 받은 데이터 보여주기 위해 false
        requestQueue3.add(request3); //RequestQueue에 추가
    }

    public void korea_movie(String date){
        String url = "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=c4e62bb6b5adb5b5194d59ab8c9ca518&targetDt="+ date +"&repNationCd=K";

        //요청을 위한 Request 객체 생성
        //파라미터 (통신 방식 get, 요청할 url, String으로 응답 받겠음)
        request4 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //정상 응답일 경우 실행
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("boxOfficeResult");

                    JSONArray jsonArray = jsonObject2.getJSONArray("dailyBoxOfficeList");

                    //배열 길이만큼 반복
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject item = jsonArray.getJSONObject(i); //getJSONObject 메소드는 json배열을 JSONObject 객체로 만듬

                        //key 값을 가져와서 각각의 String 변수에 넣어줌
                        String get_movieNm = item.getString("movieNm");
                        String get_openDt = item.getString("openDt");
                        String get_audiAcc = item.getString("audiAcc");
                        //String get_prdtYear = jsonObject2.getString("prdtYear");
                        //String get_movieNm = item.getString("movieNm");
                        //String get_openDt = item.getString("openDt");


                        //MainActivity_list 클래스의 객체 생성 후 set 후
                        //MainActivity_lsit의 ArrayList에 add 해줌
                        //각 객체 변수 하나당 한사이클의 데이터 값들이 들어있다는 것
                        korea_movie_list d_list = new korea_movie_list();

                        d_list.set_movieNm(get_movieNm);
                        d_list.set_openDt(get_openDt);
                        d_list.set_audiAcc(get_audiAcc);


                        korea_movie.list_array.add(d_list);

                    }
                    android.util.Log.i("태그","객체화 완료4");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //에러 발생 시 실행
                //tv1.append("에러 -> " + error.getMessage());
            }
        }) { //post 방식으로 파라미터를 넣고 싶을 경우, Request 객체 안에 메소드를 재정의
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                //데이터 보낼 경우 put 사용
                /*String value = "Value";
                params.put("key",value);*/

                return params;
            }
        };

        request4.setShouldCache(false); //캐시 데이터 false, 이전 데이터가 있으면 다시 보여줄 수 있으므로 항상 새로 받은 데이터 보여주기 위해 false
        requestQueue4.add(request4); //RequestQueue에 추가
    }

    public void foreign_movie(String date){
        String url = "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=c4e62bb6b5adb5b5194d59ab8c9ca518&targetDt="+ date +"&repNationCd=F";

        //요청을 위한 Request 객체 생성
        //파라미터 (통신 방식 get, 요청할 url, String으로 응답 받겠음)
        request5 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //정상 응답일 경우 실행
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("boxOfficeResult");

                    JSONArray jsonArray = jsonObject2.getJSONArray("dailyBoxOfficeList");

                    //배열 길이만큼 반복
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject item = jsonArray.getJSONObject(i); //getJSONObject 메소드는 json배열을 JSONObject 객체로 만듬

                        //key 값을 가져와서 각각의 String 변수에 넣어줌
                        String get_movieNm = item.getString("movieNm");
                        String get_openDt = item.getString("openDt");
                        String get_audiAcc = item.getString("audiAcc");
                        //String get_prdtYear = jsonObject2.getString("prdtYear");
                        //String get_movieNm = item.getString("movieNm");
                        //String get_openDt = item.getString("openDt");


                        //MainActivity_list 클래스의 객체 생성 후 set 후
                        //MainActivity_lsit의 ArrayList에 add 해줌
                        //각 객체 변수 하나당 한사이클의 데이터 값들이 들어있다는 것
                        foreign_movie_list d_list = new foreign_movie_list();

                        d_list.set_movieNm(get_movieNm);
                        d_list.set_openDt(get_openDt);
                        d_list.set_audiAcc(get_audiAcc);


                        foreign_movie.list_array.add(d_list);

                    }
                    android.util.Log.i("태그","객체화 완료5");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //에러 발생 시 실행
                //tv1.append("에러 -> " + error.getMessage());
            }
        }) { //post 방식으로 파라미터를 넣고 싶을 경우, Request 객체 안에 메소드를 재정의
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                //데이터 보낼 경우 put 사용
                /*String value = "Value";
                params.put("key",value);*/

                return params;
            }
        };

        request5.setShouldCache(false); //캐시 데이터 false, 이전 데이터가 있으면 다시 보여줄 수 있으므로 항상 새로 받은 데이터 보여주기 위해 false
        requestQueue5.add(request5); //RequestQueue에 추가
    }

    public void independent(String date){
        String url = "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=c4e62bb6b5adb5b5194d59ab8c9ca518&targetDt="+ date +"&multiMovieYn=Y";

        //요청을 위한 Request 객체 생성
        //파라미터 (통신 방식 get, 요청할 url, String으로 응답 받겠음)
        request6 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //정상 응답일 경우 실행
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("boxOfficeResult");

                    JSONArray jsonArray = jsonObject2.getJSONArray("dailyBoxOfficeList");

                    //배열 길이만큼 반복
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject item = jsonArray.getJSONObject(i); //getJSONObject 메소드는 json배열을 JSONObject 객체로 만듬

                        //key 값을 가져와서 각각의 String 변수에 넣어줌
                        String get_movieNm = item.getString("movieNm");
                        String get_openDt = item.getString("openDt");
                        String get_audiAcc = item.getString("audiAcc");
                        String get_movieCd = item.getString("movieCd");
                        //String get_prdtYear = jsonObject2.getString("prdtYear");
                        //String get_movieNm = item.getString("movieNm");
                        //String get_openDt = item.getString("openDt");


                        //MainActivity_list 클래스의 객체 생성 후 set 후
                        //MainActivity_lsit의 ArrayList에 add 해줌
                        //각 객체 변수 하나당 한사이클의 데이터 값들이 들어있다는 것
                        independent_list d_list = new independent_list();

                        d_list.set_movieNm(get_movieNm);
                        d_list.set_openDt(get_openDt);
                        d_list.set_audiAcc(get_audiAcc);
                        d_list.set_movieCd(get_movieCd);


                        independent.list_array.add(d_list);

                    }
                    android.util.Log.i("태그","객체화 완료6");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //에러 발생 시 실행
                //tv1.append("에러 -> " + error.getMessage());
            }
        }) { //post 방식으로 파라미터를 넣고 싶을 경우, Request 객체 안에 메소드를 재정의
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                //데이터 보낼 경우 put 사용
                /*String value = "Value";
                params.put("key",value);*/

                return params;
            }
        };

        request6.setShouldCache(false); //캐시 데이터 false, 이전 데이터가 있으면 다시 보여줄 수 있으므로 항상 새로 받은 데이터 보여주기 위해 false
        requestQueue6.add(request6); //RequestQueue에 추가
    }

    public void commercial(String date){
        String url = "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=c4e62bb6b5adb5b5194d59ab8c9ca518&targetDt=" + date + "&multiMovieYn=N";

        //요청을 위한 Request 객체 생성
        //파라미터 (통신 방식 get, 요청할 url, String으로 응답 받겠음)
        request7 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //정상 응답일 경우 실행
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("boxOfficeResult");

                    JSONArray jsonArray = jsonObject2.getJSONArray("dailyBoxOfficeList");

                    //배열 길이만큼 반복
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject item = jsonArray.getJSONObject(i); //getJSONObject 메소드는 json배열을 JSONObject 객체로 만듬

                        //key 값을 가져와서 각각의 String 변수에 넣어줌
                        String get_movieNm = item.getString("movieNm");
                        String get_openDt = item.getString("openDt");
                        String get_audiAcc = item.getString("audiAcc");
                        String get_movieCd = item.getString("movieCd");
                        //String get_prdtYear = jsonObject2.getString("prdtYear");
                        //String get_movieNm = item.getString("movieNm");
                        //String get_openDt = item.getString("openDt");

                        //MainActivity_list 클래스의 객체 생성 후 set 후
                        //MainActivity_lsit의 ArrayList에 add 해줌
                        //각 객체 변수 하나당 한사이클의 데이터 값들이 들어있다는 것
                        commercial_list d_list = new commercial_list();

                        d_list.set_movieNm(get_movieNm);
                        d_list.set_openDt(get_openDt);
                        d_list.set_audiAcc(get_audiAcc);
                        d_list.set_movieCd((get_movieCd));



                        commercial.list_array.add(d_list);

                    }
                    android.util.Log.i("태그","객체화 완료7");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //에러 발생 시 실행
                //tv1.append("에러 -> " + error.getMessage());
            }
        }) { //post 방식으로 파라미터를 넣고 싶을 경우, Request 객체 안에 메소드를 재정의
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                //데이터 보낼 경우 put 사용
                /*String value = "Value";
                params.put("key",value);*/

                return params;
            }
        };

        request7.setShouldCache(false); //캐시 데이터 false, 이전 데이터가 있으면 다시 보여줄 수 있으므로 항상 새로 받은 데이터 보여주기 위해 false
        requestQueue7.add(request7); //RequestQueue에 추가

    }
}