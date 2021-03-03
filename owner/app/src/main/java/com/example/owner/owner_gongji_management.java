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

public class owner_gongji_management extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    String owner_name1, owner_address1,store_name1,gongji1;
    Double owner_lat1, owner_long1;

    private long backBtnTime = 0;
    Button b1;
    EditText et1;

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
        setContentView(R.layout.activity_owner_gongji_management);
        et1 = (EditText) findViewById(R.id.contentgongji);

        Intent intent = getIntent();
        owner_name1 = intent.getStringExtra("owner_name");
        owner_address1 = intent.getStringExtra("owner_address");
        owner_lat1 = intent.getDoubleExtra("owner_lat",0.0);
        owner_long1 = intent.getDoubleExtra("owner_long",0.0);
        store_name1 = intent.getStringExtra("store_name");
        gongji1 = intent.getStringExtra("gongji");


        getSupportActionBar().setTitle(owner_name1+"님");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4472C4));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu3);


        et1.setText(gongji1);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        String gongji1 = jsonObject.getString("content");
                        if (gongji1.length()==0){
                            et1.setText("공지사항을 적어주세요!");
                        }
                        else {
                            et1.setText(gongji1);
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
        owner_gongji_management_re_db registerRequest = new owner_gongji_management_re_db(store_name1, responseListener);
        RequestQueue queue = Volley.newRequestQueue(owner_gongji_management.this);
        queue.add(registerRequest);

        b1 = (Button) findViewById(R.id.savebutton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et1.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                String gongji = jsonObject.getString("content");
                                Toast.makeText(getApplicationContext(),"저장되었습니다.",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(owner_gongji_management.this, owner_gongji_management.class);
                                intent.putExtra("owner_name",owner_name1);
                                intent.putExtra("owner_address",owner_address1);
                                intent.putExtra("owner_lat",owner_lat1);
                                intent.putExtra("owner_long",owner_long1);
                                intent.putExtra("store_name",store_name1);
                                intent.putExtra("gongji",gongji);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
                owner_gongji_management_db registerRequest = new owner_gongji_management_db(content, store_name1, responseListener);
                RequestQueue queue = Volley.newRequestQueue(owner_gongji_management.this);
                queue.add(registerRequest);
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