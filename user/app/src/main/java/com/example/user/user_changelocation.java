package com.example.user;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class user_changelocation extends AppCompatActivity implements OnMapReadyCallback {

    String[] permission_list = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private DrawerLayout mDrawerLayout;

    Double latitude,longitude;
    List<Address> a=null;

    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private long backBtnTime = 0;
    EditText et1;


    String user_name1, user_address1, user_address_detail1;
    Double user_lat1, user_long1;
    private Button user_ch_lo;
    String set_address, user_id1;

    Double user_lat, user_long;
    String user_address, user_id, user_address_detail;

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_changelocation);
        checkPermission();

        Intent intent = getIntent();
        user_name1 = intent.getStringExtra("user_name");
        user_address1 = intent.getStringExtra("user_address");
        user_lat1 = intent.getDoubleExtra("user_lat", 0.0);
        user_long1 = intent.getDoubleExtra("user_long", 0.0);
        user_id1 = intent.getStringExtra("user_id");
        user_address_detail1 = intent.getStringExtra("user_address_detail");

        getSupportActionBar().setTitle("[위치변경] "+user_name1+"님");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4472C4));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu3);

        et1 = (EditText) findViewById(R.id.layout3_et1);

        user_ch_lo = (Button) findViewById(R.id.layout3_b2);
        user_ch_lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    user_lat = latitude;
                    user_long = longitude;
                    String m = a.get(0).getAddressLine(0).replaceAll("대한민국","");
                    user_address = m;
                    user_id = user_id1;
                    user_address_detail = et1.getText().toString();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "지도에 위치를 지정하세요.", Toast.LENGTH_SHORT).show();
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "주소가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(user_changelocation.this, user_changelocation.class);
                                intent.putExtra("user_name", user_name1);
                                intent.putExtra("user_address", user_address + " " + user_address_detail);
                                intent.putExtra("user_lat", user_lat);
                                intent.putExtra("user_long", user_long);
                                intent.putExtra("user_id", user_id1);
                                intent.putExtra("user_address_detail", user_address_detail);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "주소 변경 실패\n상품을 주문해놓은 상태라면, 모든 주문을 마친 후 변경할 수 있습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                user_register2_db registerRequest = new user_register2_db(user_lat, user_long, user_address, user_id, user_address_detail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(user_changelocation.this);
                queue.add(registerRequest);
            }
        });

        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
        com.naver.maps.map.MapFragment mapFragment = (com.naver.maps.map.MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = com.naver.maps.map.MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.b1){
                    Intent intent = new Intent(getApplicationContext(), user_main1.class);
                    intent.putExtra("user_name",user_name1);
                    intent.putExtra("user_address",user_address1);
                    intent.putExtra("user_lat",user_lat1);
                    intent.putExtra("user_long",user_long1);
                    intent.putExtra("user_id",user_id1);
                    intent.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent);
                    finish();
                }
                else if(id == R.id.b2){
                    Intent intent1 = new Intent(getApplicationContext(), user_gongji.class);
                    intent1.putExtra("user_name",user_name1);
                    intent1.putExtra("user_address",user_address1);
                    intent1.putExtra("user_lat",user_lat1);
                    intent1.putExtra("user_long",user_long1);
                    intent1.putExtra("user_id",user_id1);
                    intent1.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent1);
                    finish();
                }
                else if(id == R.id.b3){
                    Intent intent2 = new Intent(getApplicationContext(), user_changelocation.class);
                    intent2.putExtra("user_name",user_name1);
                    intent2.putExtra("user_address",user_address1);
                    intent2.putExtra("user_lat",user_lat1);
                    intent2.putExtra("user_long",user_long1);
                    intent2.putExtra("user_id",user_id1);
                    intent2.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent2);
                    finish();
                }
                else if(id == R.id.b4){
                    Intent intent3 = new Intent(getApplicationContext(), user_info.class);
                    intent3.putExtra("user_name",user_name1);
                    intent3.putExtra("user_address",user_address1);
                    intent3.putExtra("user_lat",user_lat1);
                    intent3.putExtra("user_long",user_long1);
                    intent3.putExtra("user_id",user_id1);
                    intent3.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent3);
                    finish();
                }
                else if(id == R.id.b5){
                    Intent intent4 = new Intent(getApplicationContext(), user_review.class);
                    intent4.putExtra("user_name",user_name1);
                    intent4.putExtra("user_address",user_address1);
                    intent4.putExtra("user_lat",user_lat1);
                    intent4.putExtra("user_long",user_long1);
                    intent4.putExtra("user_id",user_id1);
                    intent4.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent4);
                    finish();
                }
                else if(id == R.id.b6){
                    Intent intent5 = new Intent(getApplicationContext(), user_now_order.class);
                    intent5.putExtra("user_name",user_name1);
                    intent5.putExtra("user_address",user_address1);
                    intent5.putExtra("user_lat",user_lat1);
                    intent5.putExtra("user_long",user_long1);
                    intent5.putExtra("user_id",user_id1);
                    intent5.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent5);
                    finish();
                }
                else if(id == R.id.b7){
                    Intent intent6 = new Intent(getApplicationContext(), user_order_record.class);
                    intent6.putExtra("user_name",user_name1);
                    intent6.putExtra("user_address",user_address1);
                    intent6.putExtra("user_lat",user_lat1);
                    intent6.putExtra("user_long",user_long1);
                    intent6.putExtra("user_id",user_id1);
                    intent6.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent6);
                    finish();
                }
                else if(id == R.id.b8){
                    Intent intent7 = new Intent(getApplicationContext(), user_logout.class);
                    startActivity(intent7);
                    finish();
                }

                return true;
            }
        });
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        final InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplication()) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return "배달 위치";
            }
        });

        final Marker marker = new Marker();
        marker.setPosition(new LatLng( user_lat1,user_long1));
        marker.setMap(naverMap);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(user_lat1,user_long1))
                .animate(CameraAnimation.Fly, 3000);
        naverMap.moveCamera(cameraUpdate);

        final Geocoder g = new Geocoder(this);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setLocationButtonEnabled(true);
        naverMap.setLocationSource(locationSource);

        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener(){
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull com.naver.maps.geometry.LatLng latLng) {
                marker.setPosition(new LatLng( latLng.latitude,latLng.longitude));
                marker.setMap(naverMap);
                latitude=latLng.latitude;
                longitude=latLng.longitude;

                infoWindow.open(marker);

                try {
                    a = g.getFromLocation(latitude,longitude,1);
                    String m = a.get(0).getAddressLine(0).replaceAll("대한민국","");
                    Toast.makeText(getApplicationContext(), ""+m, Toast.LENGTH_SHORT).show();
                    set_address = a.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void checkPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            int chk = checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                requestPermissions(permission_list,0);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
