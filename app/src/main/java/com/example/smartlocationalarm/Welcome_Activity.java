package com.example.smartlocationalarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Welcome_Activity extends AppCompatActivity {

    Button create;
    Button exist;
    Button nav;
    Button notif;
    Button allalarms;
    Button data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_);

        notif = findViewById(R.id.settings);
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome_Activity.this, WelcomeActivity2.class);
                startActivity(intent);
            }
        });

        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                clickCreate();
            }
        });
        exist = findViewById(R.id.exist);
        nav = findViewById(R.id.nav);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome_Activity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
        exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existing();
            }
        });
        allalarms = findViewById(R.id.allalarms);
        allalarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome_Activity.this, MainActivityAlarms.class);
                startActivity(intent);
            }
        });

    }

    private void existing() {
        Intent intent = new Intent(this, MapsActivity.class);
        this.startActivity(intent);
    }

    private void clickCreate(){
        Intent intent = new Intent(this, PermissionActivity.class);
        this.startActivity(intent);
    }

    public void AllAlarms(View view) {
        Intent intent = new Intent(this, MainActivityAlarms.class);
        this.startActivity(intent);
    }
}
