package com.example.delivery;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class rider_changelocation extends AppCompatActivity
        implements OnMapReadyCallback {

    public static String rider_name1, rider_address1,rider_id1;
    public static Double rider_lat1, rider_long1;

    Double latitude,longitude;
    List<Address> a=null;

    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private long backBtnTime = 0;

    EditText et1;

    private Button user_ch_lo;
    String set_address, user_id1;
    Double rider_lat, rider_long;
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
        setContentView(R.layout.activity_rider_changelocation);

        Intent intent = getIntent();
        rider_name1 = intent.getStringExtra("rider_name");
        rider_address1 = intent.getStringExtra("rider_address");
        rider_lat1 = intent.getDoubleExtra("rider_lat",0.0);
        rider_long1 = intent.getDoubleExtra("rider_long",0.0);
        rider_id1 = intent.getStringExtra("rider_id");

        getSupportActionBar().setTitle("[위치변경]  " + rider_name1 + "님");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4472C4));


        et1 = (EditText) findViewById(R.id.layout3_et1);

        user_ch_lo = (Button) findViewById(R.id.layout3_b2);
        user_ch_lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rider_lat = latitude;
                    rider_long = longitude;
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
                                Intent intent = new Intent(rider_changelocation.this, rider_changelocation.class);
                                intent.putExtra("rider_name",rider_name1);
                                intent.putExtra("rider_address",user_address);
                                intent.putExtra("rider_lat",rider_lat);
                                intent.putExtra("rider_long",rider_long);
                                intent.putExtra("rider_id",rider_id1);
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
                rider_register2_db registerRequest = new rider_register2_db(rider_lat, rider_long, user_address, rider_id1, user_address_detail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(rider_changelocation.this);
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
        marker.setPosition(new com.naver.maps.geometry.LatLng( rider_lat1,rider_long1));
        marker.setMap(naverMap);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new com.naver.maps.geometry.LatLng(rider_lat1,rider_long1))
                .animate(CameraAnimation.Fly, 3000);
        naverMap.moveCamera(cameraUpdate);

        final Geocoder g = new Geocoder(this);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setLocationButtonEnabled(true);
        naverMap.setLocationSource(locationSource);

        infoWindow.open(marker);

        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener(){
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull com.naver.maps.geometry.LatLng latLng) {
                marker.setPosition(new LatLng( latLng.latitude,latLng.longitude));
                marker.setMap(naverMap);
                latitude=latLng.latitude;
                longitude=latLng.longitude;

                /*naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);*/

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






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(item.getItemId()){
            case R.id.b1:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("rider_name",rider_name1);
                intent.putExtra("rider_address",rider_address1);
                intent.putExtra("rider_lat",rider_lat1);
                intent.putExtra("rider_long",rider_long1);
                intent.putExtra("rider_id",rider_id1);
                startActivity(intent);
                break;
            case R.id.b2:
                Intent intent1 = new Intent(this, rider_gongji.class);
                intent1.putExtra("rider_name",rider_name1);
                intent1.putExtra("rider_address",rider_address1);
                intent1.putExtra("rider_lat",rider_lat1);
                intent1.putExtra("rider_long",rider_long1);
                intent1.putExtra("rider_id",rider_id1);
                startActivity(intent1);
                break;
            case R.id.b3:
                Intent intent2 = new Intent(this, rider_changelocation.class);
                intent2.putExtra("rider_name",rider_name1);
                intent2.putExtra("rider_address",rider_address1);
                intent2.putExtra("rider_lat",rider_lat1);
                intent2.putExtra("rider_long",rider_long1);
                intent2.putExtra("rider_id",rider_id1);
                startActivity(intent2);
                break;
            case R.id.b4:
                Intent intent3 = new Intent(this, rider_info.class);
                intent3.putExtra("rider_name",rider_name1);
                intent3.putExtra("rider_address",rider_address1);
                intent3.putExtra("rider_lat",rider_lat1);
                intent3.putExtra("rider_long",rider_long1);
                intent3.putExtra("rider_id",rider_id1);
                startActivity(intent3);
                break;

            case R.id.b8:
                Intent intent7 = new Intent(this, rider_logout.class);
                startActivity(intent7);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}