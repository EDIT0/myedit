package com.example.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class user_login extends Activity {
    private Button findyourid, user_login;
    private EditText user_id1, user_password1;

    private long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        user_id1 = (EditText) findViewById(R.id.layout5_et1);
        user_password1 = (EditText) findViewById(R.id.layout5_et2);

        findyourid = (Button) findViewById(R.id.layout5_b1);
        findyourid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(user_login.this, forgot_id_pw.class);
                startActivity(intent);
            }
        });

        user_login = (Button) findViewById(R.id.layout5_b2);
        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_id = user_id1.getText().toString();
                final String user_password = user_password1.getText().toString();

                final String u_p = change_hash(user_password);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/account");

                                if(!saveFile.exists()){
                                    saveFile.mkdir();
                                }

                                try {
                                    long now = System.currentTimeMillis();
                                    Date date = new Date(now);
                                    SimpleDateFormat sdf = new SimpleDateFormat();
                                    String nowTime = sdf.format(date);

                                    BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile+"/security.txt", true));

                                    buf.append(user_id+" "+u_p);
                                    buf.newLine();
                                    buf.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String user_name  = jsonObject.getString("user_name");
                                String user_address = jsonObject.getString("user_address");
                                Double user_lat = jsonObject.getDouble("user_lat");
                                Double user_long = jsonObject.getDouble("user_long");
                                String user_id = jsonObject.getString("user_id");
                                String user_address_detail = jsonObject.getString("user_address_detail");

                                Toast.makeText(getApplicationContext(),"접속 성공",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(user_login.this, user_main1.class);
                                intent.putExtra("user_name",user_name);
                                intent.putExtra("user_address",user_address);
                                intent.putExtra("user_lat",user_lat);
                                intent.putExtra("user_long",user_long);
                                intent.putExtra("user_id",user_id);
                                intent.putExtra("user_address_detail",user_address_detail);
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
                user_login_db loginRequest = new user_login_db(user_id, u_p, responseListener);
                RequestQueue queue = Volley.newRequestQueue(user_login.this);
                queue.add(loginRequest);
            }
        });
    }
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
