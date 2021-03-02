package com.android.project1_searchmovie;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class my_pocket extends AppCompatActivity {
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;

    String[] movie_name = new String[300];
    String[] open_date = new String[300];

    final ArrayList<String> array_list = new ArrayList<>();
    ListView listView;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pocket);

        listView = findViewById(R.id.listview);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array_list);
        listView.setAdapter(adapter);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Movie");
        actionBar.setDisplayHomeAsUpEnabled(true);

        myHelper = new myDBHelper(getApplicationContext());

        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM my_pocket;", null);

        while (cursor.moveToNext()){
            movie_name[count] = cursor.getString(0) + "\r\n";
            open_date[count] = cursor.getString(1) + "\r\n";
            array_list.add("\n"+movie_name[count]+"\n"+open_date[count]);
        }

        cursor.close();
        sqlDB.close();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String str = array_list.get(position);
                final String[] array = str.split("\n\n");

                AlertDialog.Builder builder = new AlertDialog.Builder(my_pocket.this);
                builder.setTitle("알림");
                builder.setMessage(array[0].trim() + " 을(를) 검색하시겠습니까? (왼쪽하단 삭제가능)");
                builder.setIcon(R.drawable.ic_launcher_foreground);

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*String str = array_list.get(position);
                        String[] array = str.split("\n\n");*/
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query="+array[0]));
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNeutralButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*String str = array_list.get(position);
                        String[] array = str.split("\n\n");*/

                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("DELETE FROM my_pocket WHERE movie_name='" + array[0].trim() + "';");
                        sqlDB.close();

                        array_list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.show();

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}