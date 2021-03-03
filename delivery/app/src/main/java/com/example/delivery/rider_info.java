package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class rider_info extends AppCompatActivity{

    private Button user_ch_info;
    private EditText rider_password1;
    private long backBtnTime = 0;
    String rider_name1, rider_address1, rider_id1;
    Double rider_lat1, rider_long1;

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
        setContentView(R.layout.activity_rider_info);

        rider_password1 = (EditText) findViewById(R.id.layout5_et2);

        Intent intent = getIntent();
        rider_name1 = intent.getStringExtra("rider_name");
        rider_address1 = intent.getStringExtra("rider_address");
        rider_lat1 = intent.getDoubleExtra("rider_lat",0.0);
        rider_long1 = intent.getDoubleExtra("rider_long",0.0);
        rider_id1 = intent.getStringExtra("rider_id");

        getSupportActionBar().setTitle("[개인정보]  "+rider_name1+"님");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4472C4));

        user_ch_info = (Button) findViewById(R.id.layout5_b1);
        user_ch_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rider_id = rider_id1;
                String rider_password = rider_password1.getText().toString();

                rider_password = change_hash(rider_password);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                String rider_name  = jsonObject.getString("rider_name");
                                String rider_id = jsonObject.getString("rider_id");
                                String rider_number = jsonObject.getString("rider_number");
                                String rider_password = jsonObject.getString("rider_password");
                                String rider_email = jsonObject.getString("rider_email");

                                Toast.makeText(getApplicationContext(),"접속 성공",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplication(), rider_info1.class);
                                intent.putExtra("rider_name",rider_name1);
                                intent.putExtra("rider_address",rider_address1);
                                intent.putExtra("rider_lat",rider_lat1);
                                intent.putExtra("rider_long",rider_long1);
                                intent.putExtra("rider_id",rider_id1);
                                intent.putExtra("rider_number",rider_number);
                                intent.putExtra("rider_password",rider_password);
                                intent.putExtra("rider_email",rider_email);
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
                rider_login_db loginRequest = new rider_login_db(rider_id, rider_password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(rider_info.this);
                queue.add(loginRequest);
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
}