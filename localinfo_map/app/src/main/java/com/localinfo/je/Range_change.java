package com.localinfo.je;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Range_change extends AppCompatActivity {

    TextView current_range;

    ListView listview;
    List<String> list;

    static int range = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_change);

        current_range = (TextView)findViewById(R.id.current_range);
        current_range.setText("설정 " + range + "m");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_setting_notice_content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listview = (ListView)findViewById(R.id.list);

        list = new ArrayList<>();

        list.add("1km");
        list.add("900m");
        list.add("800m");
        list.add("700m");
        list.add("600m");
        list.add("500m");
        list.add("400m");
        list.add("300m");
        list.add("200m");
        list.add("100m");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String selected_item = (String)adapterView.getItemAtPosition(position);

                switch (selected_item){
                    case "1km":
                        range = 1000;
                        Toast.makeText(getApplicationContext(),"반경 " + adapterView.getItemAtPosition(position) + "로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                        current_range.setText("설정 " + range + "m");
                        break;
                    case "900m":
                        range = 900;
                        Toast.makeText(getApplicationContext(),"반경 " + adapterView.getItemAtPosition(position) + "로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                        current_range.setText("설정 " + range + "m");
                        break;
                    case "800m":
                        range = 800;
                        Toast.makeText(getApplicationContext(),"반경 " + adapterView.getItemAtPosition(position) + "로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                        current_range.setText("설정 " + range + "m");
                        break;
                    case "700m":
                        range = 700;
                        Toast.makeText(getApplicationContext(),"반경 " + adapterView.getItemAtPosition(position) + "로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                        current_range.setText("설정 " + range + "m");
                        break;
                    case "600m":
                        range = 600;
                        Toast.makeText(getApplicationContext(),"반경 " + adapterView.getItemAtPosition(position) + "로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                        current_range.setText("설정 " + range + "m");
                        break;
                    case "500m":
                        range = 500;
                        Toast.makeText(getApplicationContext(),"반경 " + adapterView.getItemAtPosition(position) + "로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                        current_range.setText("설정 " + range + "m");
                        break;
                    case "400m":
                        range = 400;
                        Toast.makeText(getApplicationContext(),"반경 " + adapterView.getItemAtPosition(position) + "로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                        current_range.setText("설정 " + range + "m");
                        break;
                    case "300m":
                        range = 300;
                        Toast.makeText(getApplicationContext(),"반경 " + adapterView.getItemAtPosition(position) + "로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                        current_range.setText("설정 " + range + "m");
                        break;
                    case "200m":
                        range = 200;
                        Toast.makeText(getApplicationContext(),"반경 " + adapterView.getItemAtPosition(position) + "로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                        current_range.setText("설정 " + range + "m");
                        break;
                    case "100m":
                        range = 100;
                        Toast.makeText(getApplicationContext(),"반경 " + adapterView.getItemAtPosition(position) + "로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                        current_range.setText("설정 " + range + "m");
                        break;
                    default:
                        break;
                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}