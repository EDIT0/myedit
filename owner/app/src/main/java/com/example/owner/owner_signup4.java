package com.example.owner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class owner_signup4 extends Activity {

    private Button go_to_main, go_to_login;

    String owner_store_name1, owner_name1, owner_id1, owner_number1;
    Double owner_lat1, owner_long1;
    String owner_address1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_signup4);

        Intent intent = getIntent();
        owner_store_name1 = intent.getStringExtra("owner_store_name");
        owner_name1 = intent.getStringExtra("owner_name");
        owner_id1 = intent.getStringExtra("owner_id");
        owner_number1 = intent.getStringExtra("owner_number");
        owner_lat1 = intent.getDoubleExtra("owner_lat",0.0);
        owner_long1 = intent.getDoubleExtra("owner_long",0.0);
        owner_address1 = intent.getStringExtra("owner_address");



        go_to_main = (Button) findViewById(R.id.layout4_b2);
        go_to_main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String owner_store_name = owner_store_name1;
                String owner_name = owner_name1;
                String owner_id = owner_id1;
                int owner_number = Integer.parseInt(owner_number1);
                Double owner_lat = owner_lat1;
                Double owner_long = owner_long1;
                String owner_address = owner_address1;


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                /*Toast.makeText(getApplicationContext(),"회원등록 완료",Toast.LENGTH_SHORT).show();*/
                                Intent intent = new Intent(owner_signup4.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                owner_register3_db registerRequest = new owner_register3_db(owner_store_name, owner_name, owner_id, owner_number, owner_lat, owner_long,
                        owner_address, responseListener);
                RequestQueue queue = Volley.newRequestQueue(owner_signup4.this);
                queue.add(registerRequest);
            }
        });

        go_to_login = (Button) findViewById(R.id.layout4_b1);
        go_to_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String owner_store_name = owner_store_name1;
                String owner_name = owner_name1;
                String owner_id = owner_id1;
                int owner_number = Integer.parseInt(owner_number1);
                Double owner_lat = owner_lat1;
                Double owner_long = owner_long1;
                String owner_address = owner_address1;


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Intent intent = new Intent(owner_signup4.this, owner_login.class);

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
                owner_register3_db registerRequest = new owner_register3_db(owner_store_name, owner_name, owner_id, owner_number, owner_lat, owner_long,
                        owner_address, responseListener);
                RequestQueue queue = Volley.newRequestQueue(owner_signup4.this);
                queue.add(registerRequest);
            }
        });
    }
}
