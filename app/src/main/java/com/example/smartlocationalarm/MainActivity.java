package com.example.smartlocationalarm;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("UUID", UUID.randomUUID().toString());
            editor.putBoolean("firstStart", false);
            editor.apply();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                Intent homeIntent = new Intent(MainActivity.this, Welcome_Activity.class);
                startActivity(homeIntent);
            }
        },SPLASH_TIME_OUT);
    }
}
