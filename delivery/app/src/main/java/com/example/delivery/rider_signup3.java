package com.example.delivery;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
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


public class rider_signup3 extends FragmentActivity
        implements OnMapReadyCallback {

    String[] permission_list = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };

    Double latitude,longitude;
    List<Address> a=null;

    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private NaverMap naverMap;
    private Button user_signup_back, user_signup_go;
    private EditText user_address_detail1;
    String set_address, user_id1, user_address_detail;
    Double user_lat, user_long;
    String user_address, user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_signup3);
        checkPermission();


        Intent intent = getIntent();
        user_id1 = intent.getStringExtra("user_id");

        user_signup_back = (Button) findViewById(R.id.layout3_b1);
        user_signup_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(rider_signup3.this, rider_signup2.class);
                startActivity(intent);
            }
        });

        user_address_detail1 = (EditText) findViewById(R.id.layout3_et1);
        user_signup_go = (Button) findViewById(R.id.layout3_b2);
        user_signup_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    user_lat = latitude;
                    user_long = longitude;
                    String m = a.get(0).getAddressLine(0).replaceAll("대한민국","");
                    user_address = m;
                    user_id = user_id1;
                    user_address_detail = user_address_detail1.getText().toString();

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
                                Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(rider_signup3.this, login.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                rider_register2_db registerRequest = new rider_register2_db(user_lat, user_long, user_address, user_id, user_address_detail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(rider_signup3.this);
                queue.add(registerRequest);
            }
        });

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
        com.naver.maps.map.MapFragment mapFragment = (com.naver.maps.map.MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = com.naver.maps.map.MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        final InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplication()) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return "주소 지정";
            }
        });


        final Marker marker = new Marker();

        final Geocoder g = new Geocoder(this);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setLocationButtonEnabled(true);
        naverMap.setLocationSource(locationSource);

        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new com.naver.maps.geometry.LatLng(37.5666102, 126.9783881))
                .animate(CameraAnimation.Fly, 3000);
        naverMap.moveCamera(cameraUpdate);


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
}