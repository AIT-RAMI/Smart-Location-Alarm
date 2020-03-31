package com.example.smartlocationalarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat s1, s2;
    boolean state1, state2;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        pref = getSharedPreferences("PREFS", 0);
        state1 = pref.getBoolean("s1", false);
        state2 = pref.getBoolean("s2", false);

        s1 = findViewById(R.id.switch1);
        s2 = findViewById(R.id.switch2);

        s1.setChecked(state1);
        s2.setChecked(state2);

        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state1 = !state1;
                s1.setChecked(state1);
                SharedPreferences.Editor ed = pref.edit();
                ed.putBoolean("s1", state1);
                ed.apply();
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state2 = !state2;
                s2.setChecked(state2);
                SharedPreferences.Editor ed = pref.edit();
                ed.putBoolean("s2", state2);
                ed.apply();
            }
        });
    }
}
