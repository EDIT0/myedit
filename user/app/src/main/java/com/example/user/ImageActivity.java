package com.example.user;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {
    String user_name1,user_address1,user_id1,user_address_detail1;
    double user_lat1,user_long1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();
        user_name1 = intent.getStringExtra("user_name");
        user_address1 = intent.getStringExtra("user_address");
        user_lat1 = intent.getDoubleExtra("user_lat",0.0);
        user_long1 = intent.getDoubleExtra("user_long",0.0);
        user_id1 = intent.getStringExtra("user_id");
        user_address_detail1 = intent.getStringExtra("user_address_detail");

        final String re_set_title = intent.getStringExtra("title");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(re_set_title);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF4472C4));
        actionBar.setDisplayHomeAsUpEnabled(true);

        ViewPager pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);

        FragmentStatePagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        user_frag_first fragment2 = new user_frag_first();
        ((PagerAdapter) adapter).addItem(fragment2);

        user_storereview fragment3 = new user_storereview();
        ((PagerAdapter) adapter).addItem(fragment3);

        pager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                Intent intent = new Intent(getApplicationContext(), user_main1.class);
                intent.putExtra("user_name",user_name1);
                intent.putExtra("user_address",user_address1);
                intent.putExtra("user_lat",user_lat1);
                intent.putExtra("user_long",user_long1);
                intent.putExtra("user_id",user_id1);
                intent.putExtra("user_address_detail",user_address_detail1);
                setResult(RESULT_OK,intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item){
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { }
    }
}
