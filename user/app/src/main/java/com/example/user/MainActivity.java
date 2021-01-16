package com.example.user;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends Activity {

    String[] permission_list = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private long backBtnTime = 0;
    TextView tv1;

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
        setContentView(R.layout.activity_main);
        checkPermission();

        final Button user_signup_button, user_login_button;

        user_signup_button = (Button) findViewById(R.id.layout1_b3);
        user_signup_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, user_signup1.class);
                startActivity(intent);
            }
        });

        user_login_button = (Button) findViewById(R.id.layout1_b1);
        user_login_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, user_login.class);
                startActivity(intent);
            }
        });

        Intent intent = new Intent(this, mainloading.class);
        startActivity(intent);

        tv1 = findViewById(R.id.web);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://laudaryservice.s3-website.ap-northeast-2.amazonaws.com/"));
                startActivity(intent);
            }
        });

        String line = null;
        File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/account");

        if(!saveFile.exists()){
            saveFile.mkdir();
        }
        try {
            BufferedReader buf = new BufferedReader(new FileReader(saveFile+"/security.txt"));
            while((line=buf.readLine())!=null){
                StringBuilder tv = new StringBuilder();
                String info = tv.append(line).toString();
                String[] arr = info.split(" ");
                String id = arr[0];
                String pw = arr[1];

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
                                String user_address_detail = jsonObject.getString("user_address_detail");

                                Toast.makeText(getApplicationContext(),"접속 성공",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, user_main1.class);
                                intent.putExtra("user_name",user_name);
                                intent.putExtra("user_address",user_address);
                                intent.putExtra("user_lat",user_lat);
                                intent.putExtra("user_long",user_long);
                                intent.putExtra("user_id",user_id);
                                intent.putExtra("user_address_detail",user_address_detail);
                                startActivity(intent);
                            }
                            else{
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                user_login_db registerRequest = new user_login_db(id, pw, responseListener);
                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(registerRequest);
            }
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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