package com.localinfo.je;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Marker_detail extends AppCompatActivity {

    String getPlace_name, getAddress_name, getRoad_address_name, getCategory_name, getDistance, getPlace_url, getPhone;

    TextView place_name, category_name, distance, phone, address_origin, address_road, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_detail);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_setting_notice_content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        place_name = findViewById(R.id.place_name);
        category_name = findViewById(R.id.category_name);
        distance = findViewById(R.id.distance);
        phone = findViewById(R.id.phone);
        address_origin = findViewById(R.id.address_origin);
        address_road = findViewById(R.id.address_road);
        url = findViewById(R.id.url);

        Intent getIntent = getIntent();
        getPlace_name = getIntent.getStringExtra("getPlace_name");
        getAddress_name = getIntent.getStringExtra("getAddress_name");
        getRoad_address_name = getIntent.getStringExtra("getRoad_address_name");
        getCategory_name = getIntent.getStringExtra("getCategory_name");
        getDistance = getIntent.getStringExtra("getDistance");
        getPlace_url = getIntent.getStringExtra("getPlace_url");
        getPhone = getIntent.getStringExtra("getPhone");

        if(getPhone.equals("")){
            phone.setVisibility(View.GONE);
        }
        else{
            phone.setText(getPhone);
        }

        if(getPlace_url.equals("")){
            url.setVisibility(View.GONE);
        }
        else{
            url.setText(getPlace_url);
        }

        if(getRoad_address_name.equals("")){
            address_road.setVisibility(View.GONE);
        }
        else{
            address_road.setText(getRoad_address_name);
        }

        if(getAddress_name.equals("")){
            address_origin.setVisibility(View.GONE);
        }
        else{
            address_origin.setText(getAddress_name);
        }

        place_name.setText(getPlace_name);
        category_name.setText(getCategory_name);
        distance.setText("중심으로부터 거리: " + getDistance +"m");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{

                Intent intent = new Intent(Marker_detail.this, map.class);
                setResult(RESULT_OK, intent);

                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}