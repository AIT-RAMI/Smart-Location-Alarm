package com.example.smartlocationalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat s1, s2;
    boolean state1, state2;
    SharedPreferences pref;
    SeekBar volControl;

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

        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volControl = findViewById(R.id.seekbar);
        volControl.setMax(maxVolume);
        volControl.setProgress(curVolume);
        volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
            }
        });
    }
}
