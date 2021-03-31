package com.localinfo.je.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.localinfo.je.R;

public class notice_content extends AppCompatActivity {

    String title, content, date;

    TextView tv_title, tv_content, tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_content);

        tv_title = (TextView) findViewById(R.id.title);
        tv_content = (TextView) findViewById(R.id.content);
        tv_date = (TextView) findViewById(R.id.date);

        Intent getIntent = getIntent();
        title = getIntent.getStringExtra("title");
        content = getIntent.getStringExtra("content");
        date = getIntent.getStringExtra("date");


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_setting_notice_content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_title.setText(title);
        tv_content.setText(content);
        tv_date.setText(date);

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