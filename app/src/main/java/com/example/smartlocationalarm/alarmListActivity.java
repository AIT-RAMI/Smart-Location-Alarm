package com.example.smartlocationalarm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class alarmListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_alarms);
        new firebaseDatabaseHelper().readAlarms(new firebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<alarm> alarms, List<String> keys) {
                new RecyclerView_Config().setConfig(recyclerView, alarmListActivity.this, alarms, keys);

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });


    }
}
