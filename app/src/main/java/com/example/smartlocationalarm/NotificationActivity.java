package com.example.smartlocationalarm;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        TextView txtV;
        txtV = findViewById(R.id.text);

        String text = getIntent().getStringExtra("text");
        txtV.setText(text);
    }
}
