package com.example.owner;

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

public class owner_info extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    String owner_name1, owner_address1,store_name1;
    Double owner_lat1, owner_long1;
    private long backBtnTime = 0;
    Button owner_ch_info;
    EditText owner_password1;

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
        setContentView(R.layout.activity_owner_info);

        owner_password1 = (EditText) findViewById(R.id.layout5_et2);

        Intent intent = getIntent();
        owner_name1 = intent.getStringExtra("owner_name");
        owner_address1 = intent.getStringExtra("owner_address");
        owner_lat1 = intent.getDoubleExtra("owner_lat",0.0);
        owner_long1 = intent.getDoubleExtra("owner_long",0.0);
        store_name1 = intent.getStringExtra("store_name");


        getSupportActionBar().setTitle("[개인정보] "+owner_name1+"사장님");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4472C4));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu3);


        owner_ch_info = (Button) findViewById(R.id.layout5_b1);
        owner_ch_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String store_name = store_name1;
                String owner_password = owner_password1.getText().toString();

                String hash_pass = change_hash(owner_password);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                String owner_name  = jsonObject.getString("owner_name");
                                String owner_address = jsonObject.getString("owner_address");
                                Double owner_lat = jsonObject.getDouble("owner_lat");
                                Double owner_long = jsonObject.getDouble("owner_long");
                                String owner_id = jsonObject.getString("owner_id");
                                int owner_number = jsonObject.getInt("owner_number");
                                String owner_password = jsonObject.getString("owner_password");
                                String owner_email = jsonObject.getString("owner_email");
                                int owner_store_number = jsonObject.getInt("owner_store_number");

                                Toast.makeText(getApplicationContext(),"접속 성공",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(owner_info.this, owner_info1.class);
                                intent.putExtra("owner_name",owner_name);
                                intent.putExtra("owner_address",owner_address);
                                intent.putExtra("owner_lat",owner_lat);
                                intent.putExtra("owner_long",owner_long);
                                intent.putExtra("owner_id",owner_id);
                                intent.putExtra("owner_number",owner_number);
                                intent.putExtra("owner_password",owner_password);
                                intent.putExtra("owner_email",owner_email);
                                intent.putExtra("owner_store_number",owner_store_number);
                                intent.putExtra("store_name",store_name);
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
                owner_info_db loginRequest = new owner_info_db(store_name, hash_pass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(owner_info.this);
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

                if(id == R.id.b1){
                    Intent intent = new Intent(getApplicationContext(), owner_main.class);
                    intent.putExtra("owner_name",owner_name1);
                    intent.putExtra("owner_address",owner_address1);
                    intent.putExtra("owner_lat",owner_lat1);
                    intent.putExtra("owner_long",owner_long1);
                    intent.putExtra("store_name",store_name1);
                    startActivity(intent);
                    finish();
                }
                else if(id == R.id.b2){
                    Intent intent1 = new Intent(getApplicationContext(), owner_order_y_n.class);
                    intent1.putExtra("owner_name",owner_name1);
                    intent1.putExtra("owner_address",owner_address1);
                    intent1.putExtra("owner_lat",owner_lat1);
                    intent1.putExtra("owner_long",owner_long1);
                    intent1.putExtra("store_name",store_name1);
                    startActivity(intent1);
                    finish();
                }
                else if(id == R.id.b3){
                    Intent intent2 = new Intent(getApplicationContext(), owner_item_add_del.class);
                    intent2.putExtra("owner_name",owner_name1);
                    intent2.putExtra("owner_address",owner_address1);
                    intent2.putExtra("owner_lat",owner_lat1);
                    intent2.putExtra("owner_long",owner_long1);
                    intent2.putExtra("store_name",store_name1);
                    startActivity(intent2);
                    finish();
                }
                else if(id == R.id.b4){
                    Intent intent3 = new Intent(getApplicationContext(), owner_gongji_management.class);
                    intent3.putExtra("owner_name",owner_name1);
                    intent3.putExtra("owner_address",owner_address1);
                    intent3.putExtra("owner_lat",owner_lat1);
                    intent3.putExtra("owner_long",owner_long1);
                    intent3.putExtra("store_name",store_name1);
                    startActivity(intent3);
                    finish();
                }
                else if(id == R.id.b5){
                    Intent intent4 = new Intent(getApplicationContext(), owner_info.class);
                    intent4.putExtra("owner_name",owner_name1);
                    intent4.putExtra("owner_address",owner_address1);
                    intent4.putExtra("owner_lat",owner_lat1);
                    intent4.putExtra("owner_long",owner_long1);
                    intent4.putExtra("store_name",store_name1);
                    startActivity(intent4);
                    finish();
                }
                else if(id == R.id.b6){
                    Intent intent5 = new Intent(getApplicationContext(), owner_review_management.class);
                    intent5.putExtra("owner_name",owner_name1);
                    intent5.putExtra("owner_address",owner_address1);
                    intent5.putExtra("owner_lat",owner_lat1);
                    intent5.putExtra("owner_long",owner_long1);
                    intent5.putExtra("store_name",store_name1);
                    startActivity(intent5);
                    finish();
                }
                else if(id == R.id.b7){
                    Intent intent6 = new Intent(getApplicationContext(), owner_logout.class);
                    startActivity(intent6);
                    finish();
                }
                else if(id == R.id.b8){
                    Intent intent7 = new Intent(getApplicationContext(), owner_order_record.class);
                    intent7.putExtra("owner_name",owner_name1);
                    intent7.putExtra("owner_address",owner_address1);
                    intent7.putExtra("owner_lat",owner_lat1);
                    intent7.putExtra("owner_long",owner_long1);
                    intent7.putExtra("store_name",store_name1);
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
