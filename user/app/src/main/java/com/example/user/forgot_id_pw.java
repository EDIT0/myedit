package com.example.user;

import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class forgot_id_pw extends Activity {
    Button b1,b2,b3;
    EditText et1,et2,et3,et4;

    String user_email, snum;
    int num;
    String s_n;
    String user_email1,user_id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_id_pw);

        b1 = (Button)findViewById(R.id.email);
        b2 = (Button) findViewById(R.id.layout2_b2);
        b3 = (Button) findViewById(R.id.layout2_b3);

        et1 = (EditText) findViewById(R.id.layout2_et1);
        et2 = (EditText)findViewById(R.id.layout2_et4);
        et3 = (EditText)findViewById(R.id.layout2_et2);
        et4 = (EditText) findViewById(R.id.layout2_et3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_email = et1.getText().toString();
                user_email=user_email.replace(" ","");
                Random random = new Random();
                num = random.nextInt(999999);
                snum = String.valueOf(num);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getApplicationContext(),"이메일을 확인해주세요.",Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject = new JSONObject(response);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                owner_signup2_email_db registerRequest = new owner_signup2_email_db(user_email,snum, responseListener);
                RequestQueue queue = Volley.newRequestQueue(forgot_id_pw.this);
                queue.add(registerRequest);
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getnum = et2.getText().toString();
                String realkey = String.valueOf(num);
                if (getnum.equals(realkey)){
                    et1.setEnabled(false);
                    s_n="yes";
                    AlertDialog.Builder builder = new AlertDialog.Builder(forgot_id_pw.this);
                    builder.setTitle("알림");
                    builder.setMessage("인증완료, 이메일을 확인하세요.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(forgot_id_pw.this, user_login.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(forgot_id_pw.this);
                    builder.setTitle("알림");
                    builder.setMessage("인증실패, 정확한 인증번호를 입력하세요.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                };
                user_forgot_id_db registerRequest = new user_forgot_id_db(user_email,s_n, responseListener);
                RequestQueue queue = Volley.newRequestQueue(forgot_id_pw.this);
                queue.add(registerRequest);
            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    user_id1 = et3.getText().toString();
                    user_email1 = et4.getText().toString();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "번호를 공백없이 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                if (user_id1.length() == 0 || user_email1.length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(forgot_id_pw.this);
                    builder.setTitle("알림");
                    builder.setMessage("정보를 입력하세요.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Intent intent = new Intent(forgot_id_pw.this, user_forgot_pw_change.class);
                                    intent.putExtra("user_email", user_email1);
                                    intent.putExtra("user_id",user_id1);

                                    startActivity(intent);
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(forgot_id_pw.this);
                                    builder.setTitle("알림");
                                    builder.setMessage("ID와 E-mail을 다시 확인해주세요.");
                                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    user_forgot_pw_db registerRequest = new user_forgot_pw_db(user_email1, user_id1, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(forgot_id_pw.this);
                    queue.add(registerRequest);
                }
            }
        });
    }
}
