package com.example.owner;

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

public class owner_login extends Activity {
    private Button findyourid, owner_login;
    private EditText owner_id1, owner_password1;
    private long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);

        owner_id1 = (EditText) findViewById(R.id.layout5_et1);
        owner_password1 = (EditText) findViewById(R.id.layout5_et2);

        findyourid = (Button) findViewById(R.id.layout5_b1);
        findyourid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(owner_login.this, owner_forgot_id_pw.class);
                startActivity(intent);
            }
        });

        owner_login = (Button) findViewById(R.id.layout5_b2);
        owner_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String owner_id = owner_id1.getText().toString();
                final String owner_password = owner_password1.getText().toString();

                final String hash_pass = change_hash(owner_password);

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
                                    long now = System.currentTimeMillis(); // 현재시간 받아오기
                                    Date date = new Date(now);
                                    SimpleDateFormat sdf = new SimpleDateFormat();
                                    String nowTime = sdf.format(date);

                                    BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile+"/security1.txt", true));
                                    buf.append(owner_id+" "+hash_pass); // 파일 쓰기
                                    buf.newLine(); // 개행
                                    buf.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String owner_name  = jsonObject.getString("owner_name");
                                String owner_address = jsonObject.getString("owner_address");
                                Double owner_lat = jsonObject.getDouble("owner_lat");
                                Double owner_long = jsonObject.getDouble("owner_long");
                                String store_name = jsonObject.getString("owner_store_name");

                                Toast.makeText(getApplicationContext(),"접속 성공",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(owner_login.this, owner_main.class);
                                intent.putExtra("owner_name",owner_name);
                                intent.putExtra("owner_address",owner_address);
                                intent.putExtra("owner_lat",owner_lat);
                                intent.putExtra("owner_long",owner_long);
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
                owner_login_db loginRequest = new owner_login_db(owner_id, hash_pass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(owner_login.this);
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