package com.example.owner;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.naver.maps.geometry.LatLng;
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

public class owner_signup3 extends FragmentActivity
        implements OnMapReadyCallback {

    String[] permission_list = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };

    Double latitude,longitude;
    List<Address> a=null;

    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private NaverMap naverMap;


    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private Button user_signup_back, user_signup_go;
    private GoogleMap mMap;

    String set_address,cd1,owner_nin1,owner_store_name1,owner_name1,owner_id1,owner_number1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_signup3);
        checkPermission();

        Intent intent = getIntent();
        owner_nin1 = intent.getStringExtra("owner_nin");
        cd1 = intent.getStringExtra("cd");
        owner_store_name1 = intent.getStringExtra("owner_store_name");
        owner_name1 = intent.getStringExtra("owner_name");
        owner_id1 = intent.getStringExtra("owner_id");
        owner_number1 = intent.getStringExtra("owner_number");


        user_signup_back = (Button) findViewById(R.id.layout3_b1);
        user_signup_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(owner_signup3.this, owner_signup2.class);
                startActivity(intent);
            }
        });

        user_signup_go = (Button) findViewById(R.id.layout3_b2);
        user_signup_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Double owner_lat = latitude;
                Double owner_long = longitude;
                String m = a.get(0).getAddressLine(0).replaceAll("대한민국","");
                final String owner_address = m;
                String owner_nin = owner_nin1;

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                String owner_store_name = owner_store_name1;
                                String owner_name = owner_name1;
                                String owner_id = owner_id1;
                                String owner_number = owner_number1;
                                Double owner_lat = latitude;
                                Double owner_long = longitude;
                                String owner_address = a.get(0).getAddressLine(0);

                                Toast.makeText(getApplicationContext(),"회원가입 완료",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(owner_signup3.this, owner_signup4.class);

                                intent.putExtra("owner_store_name",owner_store_name);
                                intent.putExtra("owner_name",owner_name);
                                intent.putExtra("owner_id",owner_id);
                                intent.putExtra("owner_number",owner_number);
                                intent.putExtra("owner_lat",owner_lat);
                                intent.putExtra("owner_long",owner_long);
                                intent.putExtra("owner_address",owner_address);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"회원가입 실패",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                owner_register2_db registerRequest = new owner_register2_db(owner_lat, owner_long, owner_address, owner_nin, responseListener);
                RequestQueue queue = Volley.newRequestQueue(owner_signup3.this);
                queue.add(registerRequest);
            }
            /*public void onClick(View v) {
                Intent intent = new Intent(owner_signup3.this, owner_signup4.class);
                startActivity(intent);
            }*/
            // set_address 값을 db에 저장 후 불러오기
                /*Toast.makeText(getApplicationContext(), ""+set_address, Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(user_signup3.this,user_main.class);
                intent2.putExtra("set_address",set_address);
                startActivity(intent2);*/
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
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

