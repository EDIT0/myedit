package com.example.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class user_main1 extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    TextView tv11;
    ImageView iv11;

    int count=0;

    public String var_name, var_address,  var_id;
    public Double var_lat, var_long;

    private long backBtnTime = 0;
    TextView user_address;

    int status=0;

    String user_name1, user_address1,user_id1, user_address_detail1;
    Double user_lat1, user_long1;

    private static String TAG = "phptest";

    private TextView mTextViewResult;
    private ArrayList<main1_list> mArrayList;
    private main1_Adpter mAdapter;
    private RecyclerView mRecyclerView;
    private String mJsonString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main1);

        Intent intent = getIntent();
        user_name1 = intent.getStringExtra("user_name");
        user_address1 = intent.getStringExtra("user_address");
        user_lat1 = intent.getDoubleExtra("user_lat",0.0);
        user_long1 = intent.getDoubleExtra("user_long",0.0);
        user_id1 = intent.getStringExtra("user_id");
        user_address_detail1 = intent.getStringExtra("user_address_detail");


        var_name = user_name1;
        var_address = user_address1;
        var_lat = user_lat1;
        var_long = user_long1;
        var_id = user_id1;
        String var_address_detail = user_address_detail1;

        TextView text = findViewById(R.id.text);
        SpannableString content = new SpannableString("가게혼잡도 낮은 순으로 정렬");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        text.setText(content);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(user_main1.this);
                builder.setTitle("정보");
                builder.setMessage("원활한 가게 운영과 높은 서비스 제공을 위해 가게의 주문 건수를 바탕으로 계산하여 제공되는 서비스입니다.\n" +
                        "상단에 표시될수록 가게의 현재 주문량이 여유롭다는 것을 알려드립니다.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                builder.setNegativeButton("", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) { }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        mArrayList = new ArrayList<>();

        mAdapter = new main1_Adpter(this, mArrayList, var_name,var_address,var_lat,var_long,var_id, var_address_detail);
        mRecyclerView.setAdapter(mAdapter);

        mArrayList.clear();
        mAdapter.notifyDataSetChanged();

        RecyclerDecoration spaceDecoration = new RecyclerDecoration(10);
        mRecyclerView.addItemDecoration(spaceDecoration);

        GetData task = new GetData();
        task.execute("http://edit0.dothome.co.kr/main1_db.php",String.valueOf(user_lat1),String.valueOf(user_long1));

        SwitchButton switchButton = (SwitchButton) findViewById(R.id.sb_use_listener);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mRecyclerView.setAdapter(mAdapter);
                    mArrayList.clear();
                    mAdapter.notifyDataSetChanged();
                    status=1;

                    GetData task2 = new GetData();
                    task2.execute("http://edit0.dothome.co.kr/main2_db.php",String.valueOf(user_lat1),String.valueOf(user_long1));

                }else{
                    mRecyclerView.setAdapter(mAdapter);
                    mArrayList.clear();
                    mAdapter.notifyDataSetChanged();
                    status=0;

                    GetData task3 = new GetData();
                    task3.execute("http://edit0.dothome.co.kr/main1_db.php",String.valueOf(user_lat1),String.valueOf(user_long1));
                }
            }
        });

        final SwipeRefreshLayout srfl = findViewById(R.id.swipe_refresh_layout);

        srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(status==1){
                    mRecyclerView.setAdapter(mAdapter);
                    mArrayList.clear();
                    mAdapter.notifyDataSetChanged();

                    GetData task4 = new GetData();
                    task4.execute("http://edit0.dothome.co.kr/main2_db.php",String.valueOf(user_lat1),String.valueOf(user_long1));
                    srfl.setRefreshing(false);
                }
                else{
                    mRecyclerView.setAdapter(mAdapter);
                    mArrayList.clear();
                    mAdapter.notifyDataSetChanged();

                    GetData task5 = new GetData();
                    task5.execute("http://edit0.dothome.co.kr/main1_db.php",String.valueOf(user_lat1),String.valueOf(user_long1));

                    srfl.setRefreshing(false);
                }

            }
        });

        getSupportActionBar().setTitle("[홈] "+user_name1+"님");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4472C4));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu3);

        user_address = findViewById(R.id.tv1);
        user_address.setText(user_address1);

        tv11 = findViewById(R.id.backtext);
        iv11 = findViewById(R.id.backimage);
        if(count==0) {
            iv11.setImageResource(R.drawable.reject);
            iv11.setVisibility(View.VISIBLE);
            tv11.setVisibility(View.VISIBLE);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.b1){
                    Intent intent = new Intent(getApplicationContext(), user_main1.class);
                    intent.putExtra("user_name",user_name1);
                    intent.putExtra("user_address",user_address1);
                    intent.putExtra("user_lat",user_lat1);
                    intent.putExtra("user_long",user_long1);
                    intent.putExtra("user_id",user_id1);
                    intent.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent);
                    finish();
                }
                else if(id == R.id.b2){
                    Intent intent1 = new Intent(getApplicationContext(), user_gongji.class);
                    intent1.putExtra("user_name",user_name1);
                    intent1.putExtra("user_address",user_address1);
                    intent1.putExtra("user_lat",user_lat1);
                    intent1.putExtra("user_long",user_long1);
                    intent1.putExtra("user_id",user_id1);
                    intent1.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent1);
                    finish();
                }
                else if(id == R.id.b3){
                    Intent intent2 = new Intent(getApplicationContext(), user_changelocation.class);
                    intent2.putExtra("user_name",user_name1);
                    intent2.putExtra("user_address",user_address1);
                    intent2.putExtra("user_lat",user_lat1);
                    intent2.putExtra("user_long",user_long1);
                    intent2.putExtra("user_id",user_id1);
                    intent2.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent2);
                    finish();
                }
                else if(id == R.id.b4){
                    Intent intent3 = new Intent(getApplicationContext(), user_info.class);
                    intent3.putExtra("user_name",user_name1);
                    intent3.putExtra("user_address",user_address1);
                    intent3.putExtra("user_lat",user_lat1);
                    intent3.putExtra("user_long",user_long1);
                    intent3.putExtra("user_id",user_id1);
                    intent3.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent3);
                    finish();
                }
                else if(id == R.id.b5){
                    Intent intent4 = new Intent(getApplicationContext(), user_review.class);
                    intent4.putExtra("user_name",user_name1);
                    intent4.putExtra("user_address",user_address1);
                    intent4.putExtra("user_lat",user_lat1);
                    intent4.putExtra("user_long",user_long1);
                    intent4.putExtra("user_id",user_id1);
                    intent4.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent4);
                    finish();
                }
                else if(id == R.id.b6){
                    Intent intent5 = new Intent(getApplicationContext(), user_now_order.class);
                    intent5.putExtra("user_name",user_name1);
                    intent5.putExtra("user_address",user_address1);
                    intent5.putExtra("user_lat",user_lat1);
                    intent5.putExtra("user_long",user_long1);
                    intent5.putExtra("user_id",user_id1);
                    intent5.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent5);
                    finish();
                }
                else if(id == R.id.b7){
                    Intent intent6 = new Intent(getApplicationContext(), user_order_record.class);
                    intent6.putExtra("user_name",user_name1);
                    intent6.putExtra("user_address",user_address1);
                    intent6.putExtra("user_lat",user_lat1);
                    intent6.putExtra("user_long",user_long1);
                    intent6.putExtra("user_id",user_id1);
                    intent6.putExtra("user_address_detail",user_address_detail1);
                    startActivity(intent6);
                    finish();
                }
                else if(id == R.id.b8){
                    Intent intent7 = new Intent(getApplicationContext(), user_logout.class);
                    startActivity(intent7);
                    finish();
                }

                return true;
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

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(user_main1.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){
                mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "user_lat=" + params[1] + "&user_long=" + params[2];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "GetData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showResult(){
        String TAG_JSON="result";
        String TAG_s_name = "s_name";
        String TAG_o_id = "o_id";
        String TAG_o_pw ="o_pw";
        String TAG_cd = "cd";
        String TAG_data2 = "data2";
        String TAG_data3 = "data3";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String s_name1 = item.getString(TAG_s_name);
                String o_id1 = item.getString(TAG_o_id);
                String o_pw1 = item.getString(TAG_o_pw);
                String cd1 = item.getString(TAG_cd);
                String data21 = item.getString(TAG_data2);
                String data31 = item.getString(TAG_data3);

                main1_list personalData = new main1_list();

                personalData.setMember_s_name(s_name1);
                personalData.setMember_o_id(o_id1);
                personalData.setMember_o_pw(o_pw1);
                personalData.setMember_cd(cd1);
                personalData.setMember_data2(data21);
                personalData.setMember_data3(data31);

                count++;

                if(count>0){
                    iv11.setVisibility(View.INVISIBLE);
                    tv11.setVisibility(View.INVISIBLE);
                }

                mArrayList.add(personalData);
                mAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
