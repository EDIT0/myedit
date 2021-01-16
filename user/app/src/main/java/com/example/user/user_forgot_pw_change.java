package com.example.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class user_forgot_pw_change extends Activity {
    String user_email1,user_id1,u_pw;
    EditText et1;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forgot_pw_change);

        Intent intent = getIntent();
        user_email1 = intent.getStringExtra("user_email");
        user_id1 = intent.getStringExtra("user_id");

        et1 = findViewById(R.id.et1);
        b1 = findViewById(R.id.b1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    u_pw = et1.getText().toString();



                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "번호를 공백없이 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                if (et1.getText().toString().length()<=7 || et1.getText().toString().length()>=17) {
                    Toast.makeText(getApplicationContext(), "비밀번호(8~16자)를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    u_pw = change_hash(u_pw);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(user_forgot_pw_change.this);
                                    builder.setTitle("알림");
                                    builder.setMessage("비밀번호가 변경되었습니다.");
                                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(user_forgot_pw_change.this, user_login.class);

                                            startActivity(intent);
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    return;
                                }
                                else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    user_forgot_pw_change_db registerRequest = new user_forgot_pw_change_db(user_email1, user_id1,u_pw, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(user_forgot_pw_change.this);
                    queue.add(registerRequest);
                }
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
}
