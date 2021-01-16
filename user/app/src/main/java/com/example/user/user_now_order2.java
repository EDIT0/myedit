package com.example.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class user_now_order2 extends Activity {
    String user_name1, user_address1, user_id1, user_address_detail1,s_name1;
    Double user_lat1, user_long1;
    int date1;

    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;

    String memo, items;
    int t_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_now_order2);

        Intent intent = getIntent();
        user_name1 = intent.getStringExtra("user_name");
        user_address1 = intent.getStringExtra("user_address");
        user_lat1 = intent.getDoubleExtra("user_lat",0.0);
        user_long1 = intent.getDoubleExtra("user_long",0.0);
        user_id1 = intent.getStringExtra("user_id");
        user_address_detail1 = intent.getStringExtra("user_address_detail");
        s_name1 = intent.getStringExtra("store_name");
        date1 = intent.getIntExtra("date",0);

        tv1 = (TextView)findViewById(R.id.s_name);
        tv1.setText(s_name1);

        tv2 = (TextView)findViewById(R.id.date);
        tv2.setText("2021"+date1);


        tv3 = (TextView) findViewById(R.id.address);
        tv3.setText(user_address1);

        tv4 = (TextView)findViewById(R.id.memo);
        tv5 = (TextView) findViewById(R.id.t_price);

        tv7 = (TextView)findViewById(R.id.item_list);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        memo = jsonObject.getString("memo");
                        if (memo.length()==0){
                            memo = "공란";
                        }
                        t_price = jsonObject.getInt("t_price");
                        items = jsonObject.getString("items");
                        tv7.setText(items);
                        tv4.setText(memo);
                        tv5.setText(t_price+"원");

                        int yes_no = jsonObject.getInt("yes_no");
                        TextView tv10,tv11,tv12,tv13,tv14,tv15;
                        tv10 = findViewById(R.id.step1);
                        tv11 = findViewById(R.id.s_step1);
                        tv12 = findViewById(R.id.step2);
                        tv13 = findViewById(R.id.s_step2);
                        tv14 = findViewById(R.id.step3);
                        tv15 = findViewById(R.id.s_step3);


                        if(yes_no==0){
                            tv10.setTextColor(Color.parseColor("#111111"));
                            tv11.setTextColor(Color.parseColor("#111111"));
                        }
                        else if(yes_no==1){
                            tv12.setTextColor(Color.parseColor("#111111"));
                            tv13.setTextColor(Color.parseColor("#111111"));
                        }
                        else if(yes_no==2){
                            tv14.setTextColor(Color.parseColor("#111111"));
                            tv15.setTextColor(Color.parseColor("#111111"));
                        }

                    }
                    else{
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        user_now_order2_db registerRequest = new user_now_order2_db(s_name1,user_id1,date1, responseListener);
        RequestQueue queue = Volley.newRequestQueue(user_now_order2.this);
        queue.add(registerRequest);

    }

}
