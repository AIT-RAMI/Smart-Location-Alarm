package com.example.smartlocationalarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adding_alarm_db extends AppCompatActivity {

    public EditText mlongitude, mlatitude, mname;
    public long mid;
    public Button mAjouter;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_alarm_db);
        mlongitude = findViewById(R.id.longitude);
        mlatitude = findViewById(R.id.latitude);
        mname = findViewById(R.id.Name);
        mAjouter = findViewById(R.id.add);
        final alarm alarm = new alarm();

        reff = FirebaseDatabase.getInstance().getReference().child("alarm");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mid = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String longitude = mlongitude.getText().toString().trim();
                String latitude = mlatitude.getText().toString().trim();
                String name = mname.getText().toString().trim();
                alarm.setLongitude(longitude);
                alarm.setLatitude(latitude);
                alarm.setName(name);
                reff.child(String.valueOf(mid + 1)).setValue(alarm);
                Toast.makeText(adding_alarm_db.this, "saved successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(adding_alarm_db.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}