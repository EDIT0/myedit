package com.example.owner;

import androidx.annotation.RequiresApi;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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

public class owner_main extends AppCompatActivity implements BackgroundResultReceiver.Receiver{
    private DrawerLayout mDrawerLayout;
    TextView tv1;
    ImageView iv1;

    int count=0;
    public String var_name, var_address, var_store_name;
    public Double var_lat, var_long;
    private long backBtnTime = 0;

    public static String store_name1;
    public static String owner_name1, owner_address1;
    public static Double owner_lat1, owner_long1;

    private static String TAG = "TAG";

    private TextView mTextViewResult;
    private ArrayList<owner_main1_list> mArrayList;
    private owner_main_Adpter mAdapter;
    private RecyclerView mRecyclerView;
    private String mJsonString;

    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    public BackgroundResultReceiver mReceiver;
    static int alarmcount=0;

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
        setContentView(R.layout.activity_owner_main);

        if(alarmcount==6 || alarmcount==0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(owner_main.this);
            builder.setTitle("꼭꼭꼭 읽어주세요!!!");
            builder.setMessage("'세탁이'를 완전히 종료시키시면 알림을 받을 수 없습니다.\n" +
                    "영업시에는 꼭 작업창에 '세탁이' 어플리케이션이 작동되고 있도록 유지해주시기 바랍니다.\n" +
                    "감사합니다.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            builder.setNegativeButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            alarmcount=1;
        }
        alarmcount++;


        Intent intent = getIntent();
        owner_name1 = intent.getStringExtra("owner_name");
        owner_address1 = intent.getStringExtra("owner_address");
        owner_lat1 = intent.getDoubleExtra("owner_lat", 0.0);
        owner_long1 = intent.getDoubleExtra("owner_long", 0.0);
        store_name1 = intent.getStringExtra("store_name");

        var_name = owner_name1;
        var_address = owner_address1;
        var_lat = owner_lat1;
        var_long = owner_long1;
        var_store_name = store_name1;

        mTextViewResult = (TextView) findViewById(R.id.textView_main_result);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        mArrayList = new ArrayList<>();

        mAdapter = new owner_main_Adpter(this, mArrayList, var_name, var_address, var_lat, var_long,var_store_name);
        mRecyclerView.setAdapter(mAdapter);

        mArrayList.clear();
        mAdapter.notifyDataSetChanged();

        RecyclerDecoration spaceDecoration = new RecyclerDecoration(20);
        mRecyclerView.addItemDecoration(spaceDecoration);

        owner_main.GetData task = new owner_main.GetData();
        task.execute("http://edit0.dothome.co.kr/owner_main1_db.php", store_name1);

        final SwipeRefreshLayout srfl = findViewById(R.id.swipe_refresh_layout);

        srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.setAdapter(mAdapter);
                mArrayList.clear();
                mAdapter.notifyDataSetChanged();

                GetData task1 = new GetData();
                task1.execute("http://edit0.dothome.co.kr/owner_main1_db.php", store_name1);
                srfl.setRefreshing(false);
            }
        });


        getSupportActionBar().setTitle(owner_name1 + " 사장님");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4472C4));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu3);


        tv1 = findViewById(R.id.backtext);
        iv1 = findViewById(R.id.backimage);
        if(count==0) {
            iv1.setImageResource(R.drawable.reject);
            iv1.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.VISIBLE);
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
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(owner_main.this,
                    "Please Wait", null, true, true);
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
            String postParameters = "store_name=" + params[1];

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

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){

        String TAG_JSON="result";
        String TAG_u_address = "u_address";
        String TAG_u_id = "u_id";
        String TAG_u_pw ="u_pw";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String u_address1 = item.getString(TAG_u_address);
                String u_id1 = item.getString(TAG_u_id);
                String u_pw1 = item.getString(TAG_u_pw);

                owner_main1_list personalData = new owner_main1_list();

                personalData.setMember_u_address(u_address1);
                personalData.setMember_u_id(u_id1);
                personalData.setMember_u_pw(u_pw1);

                count++;

                if(count>0){
                    iv1.setVisibility(View.INVISIBLE);
                    tv1.setVisibility(View.INVISIBLE);
                }

                mArrayList.add(personalData);
                mAdapter.notifyDataSetChanged();
            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        final Intent intent = new Intent(Intent.ACTION_SYNC, null, this, BackgroundService.class);
        stopService(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
            case BackgroundService.STATUS_RUNNING:
                break;

            case BackgroundService.STATUS_FINISHED:
                break;


            case BackgroundService.STATUS_ERROR:
                Toast.makeText(this, "STATUS_ERROR", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mReceiver = new BackgroundResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        final Intent intent = new Intent(Intent.ACTION_SYNC, null, this, BackgroundService.class);

        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "increase count");
        startService(intent);


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
