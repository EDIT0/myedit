package com.example.owner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class mainloading extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainloading);
        startLoading();
    }

        private void startLoading() {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);

        }
}
