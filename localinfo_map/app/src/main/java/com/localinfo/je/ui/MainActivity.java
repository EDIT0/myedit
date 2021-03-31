package com.localinfo.je.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.localinfo.je.R;
import com.localinfo.je.api.Retrofit_API;
import com.localinfo.je.data.notice_items;
import com.localinfo.je.data.search_address_Result_for_statistic;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    Retrofit retrofit;
    Retrofit_API retrofit_api;
    Call<notice_items> call;
    Call<search_address_Result_for_statistic> call2;
    String base_url = "http://edit0.dothome.co.kr/";

    statistic v_statistic;
    map v_map;
    notice v_notice;
    setting v_setting;
    FragmentManager fragmentManager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume()");

        int status = Network_status.getConnectivityStatus(getApplicationContext());
        if(status == Network_status.TYPE_MOBILE){
        }else if (status == Network_status.TYPE_WIFI){
        }else {
            Toast.makeText(getApplicationContext(),"네트워크 연결 상태를 확인해주세요.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,"onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"onCreate()");

        request_retrofit_get_notice_list(); //공지사항 가져오기 요청

        Intent intent = new Intent(this, Loading_logo.class);
        startActivity(intent);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_center);

        v_statistic = new statistic();
        v_map = new map();
        v_notice = new notice();
        v_setting = new setting();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.framelayout, v_map).commit();

        bottomNavigationView = findViewById(R.id.bottomnavigationview);
        bottomNavigationView.setSelectedItemId(R.id.map_tab);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.statistic_tab:
                        request_retrofit_Search_address();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("알림");
                        builder.setMessage("이 지역의 통계자료를 보시겠습니까?");
                        builder.setIcon(R.mipmap.localinfo_map);

                        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fragmentManager.beginTransaction().replace(R.id.framelayout, v_statistic).commit();
                            }
                        });

                        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.show();

                        return true;

                    case R.id.map_tab:
                        fragmentManager.beginTransaction().replace(R.id.framelayout, v_map).commit();
                        return true;

                    case R.id.notice:
                        fragmentManager.beginTransaction().replace(R.id.framelayout, v_notice).commit();
                        return true;

                    case R.id.setting:
                        fragmentManager.beginTransaction().replace(R.id.framelayout, v_setting).commit();
                        return true;
                }

                return false;
            }
        });
    }

    private long backBtnTime = 0;
    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && 1000 >= gapTime) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }
    }

    //========================================액션바 메뉴======================================

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.setting_menu, menu);

        //아이콘을 보여주기 위한 문단
        try{
            Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            method.setAccessible(true);
            method.invoke(menu, true);
        } catch (Exception e){
            Log.i(TAG,"onCreateOptionMenu() Error");
        }
        //setOptionalIconsVisible() 함수를 이용하여 값을 true로 설정하면 아이콘이 표시된다.

        return super.onCreateOptionsMenu(menu);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //등록한 메뉴 항목들이 눌렸을 경우
        switch (item.getItemId()){
            case R.id.setting:
                Log.i(TAG,"click setting");
                break;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void request_retrofit_get_notice_list(){
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit_api = retrofit.create(Retrofit_API.class);

        call = retrofit_api.get_notice_list();
        call.enqueue(new Callback<notice_items>() {
            @Override
            public void onResponse(Call<notice_items> call, Response<notice_items> response) {
                //통신 성공 시
                if (response.isSuccessful()) {
                    notice_items result = response.body();

                    ArrayList<notice_items.items> items = result.getItems();

                    for (int i = 0; i < items.size(); i++) {
                        notice_items.items temp = items.get(i);
                        notice.notice_list_array.add(temp);
                        //Log.i(TAG,temp.title + " / " + temp.content + " / " + temp.date);
                    }


                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_notice_list");
                }
            }

            @Override
            public void onFailure(Call<notice_items> call, Throwable t) {
                //통신 실패 시
                Log.i(TAG, "onFailure() - request_retrofit_get_notice_list");
            }
        });

    }

    //=================================통신 서울시 주소 얻기(동)===================================

    public void request_retrofit_Search_address() {
        retrofit = new Retrofit.Builder()
                .baseUrl(map.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit_api = retrofit.create(Retrofit_API.class);


        call2 = retrofit_api.get_Search_address(map.rest_api_key, String.valueOf(map.current_location_long), String.valueOf(map.current_location_lat), "WGS84", "WGS84");
        call2.enqueue(new Callback<search_address_Result_for_statistic>() {
            @Override
            public void onResponse(Call<search_address_Result_for_statistic> call2, Response<search_address_Result_for_statistic> response) {
                if (response.isSuccessful()) {
                    search_address_Result_for_statistic result = response.body();

                    List<search_address_Result_for_statistic.Document> address = result.getDocuments();
                    int total_count = result.getMeta().getTotal_count();

                    for(int i=0;i<total_count;i++){
                        if(address.get(i).getRegion_type().equals("H")){
                            map.region_type_depth_dong = address.get(i).getRegion_3depth_name();
                            map.region_type_depth_gu = address.get(i).getRegion_2depth_name();
                            map.region_type_depth_si = address.get(i).getRegion_1depth_name();
                            Log.i(TAG,"주소 가져오기: "+ map.region_type_depth_si + " / "+ map.region_type_depth_gu +" / "+map.region_type_depth_dong);
                        }
                    }


                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_Location_Directly");
                }
            }

            @Override
            public void onFailure(Call<search_address_Result_for_statistic> call2, Throwable t) {
                Log.i(TAG, "onFailure() - request_retrofit_get_Search_Location_Directly");
            }
        });
    }
}