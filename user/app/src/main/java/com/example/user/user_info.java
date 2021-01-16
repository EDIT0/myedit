package com.example.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class user_info extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    private Button user_ch_info;
    private EditText user_password1;
    private long backBtnTime = 0;

    String user_name1, user_address1, user_id1, user_address_detail1;
    Double user_lat1, user_long1;

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && 2000 >= gapTime) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        user_password1 = (EditText) findViewById(R.id.layout5_et2);

        Intent intent = getIntent();
        user_name1 = intent.getStringExtra("user_name");
        user_address1 = intent.getStringExtra("user_address");
        user_lat1 = intent.getDoubleExtra("user_lat",0.0);
        user_long1 = intent.getDoubleExtra("user_long",0.0);
        user_id1 = intent.getStringExtra("user_id");
        user_address_detail1 = intent.getStringExtra("user_address_detail");

        getSupportActionBar().setTitle("[개인정보]  "+user_name1+"님");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4472C4));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu3);

        user_ch_info = (Button) findViewById(R.id.layout5_b1);
        user_ch_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = user_id1;
                String user_password = user_password1.getText().toString();

                user_password = change_hash(user_password);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                String user_name  = jsonObject.getString("user_name");
                                String user_address = jsonObject.getString("user_address");
                                Double user_lat = jsonObject.getDouble("user_lat");
                                Double user_long = jsonObject.getDouble("user_long");
                                String user_id = jsonObject.getString("user_id");
                                int user_number = jsonObject.getInt("user_number");
                                String user_password = jsonObject.getString("user_password");
                                String user_email = jsonObject.getString("user_email");

                                Intent intent = new Intent(user_info.this, user_info1.class);
                                intent.putExtra("user_name",user_name);
                                intent.putExtra("user_address",user_address);
                                intent.putExtra("user_lat",user_lat);
                                intent.putExtra("user_long",user_long);
                                intent.putExtra("user_id",user_id);
                                intent.putExtra("user_number",user_number);
                                intent.putExtra("user_password",user_password);
                                intent.putExtra("user_email",user_email);
                                intent.putExtra("user_address_detail",user_address_detail1);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"정확한 아이디/패스워드를 입력해주세요.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                user_login_db loginRequest = new user_login_db(user_id, user_password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(user_info.this);
                queue.add(loginRequest);
            }
        });

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

    public String change_hash(String text){
        MessageDigest mdSHA256 = null;
        try {
            mdSHA256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            mdSHA256.update(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] sha256Hash = mdSHA256.digest();

        StringBuilder hexSHA256hash = new StringBuilder();
        for(byte b : sha256Hash) {
            String hexString = String.format("%02x", b);
            hexSHA256hash.append(hexString);
        }

        return hexSHA256hash.toString();
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

