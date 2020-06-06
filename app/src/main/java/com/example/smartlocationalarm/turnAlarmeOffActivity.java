package com.example.smartlocationalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class turnAlarmeOffActivity extends AppCompatActivity {
    Button turn_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_alarme_off);

        final String s = ((MyApplication) this.getApplication()).getId();

        turn_ = findViewById(R.id.button);

        turn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new firebaseDatabaseHelper(turnAlarmeOffActivity.this).deleteAlarm(s, new firebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<alarm> alarms, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(turnAlarmeOffActivity.this, "alarm has been truned off successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(turnAlarmeOffActivity.this, MainActivity.class);
                        startActivity(intent);

                    }
                });
            }
        });


    }
}
