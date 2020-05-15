package com.example.smartlocationalarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class alarmDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    private EditText editName, editNotes, editRadius;
    private ImageButton btn_edit, btn_delete, btn_back;
    private String name, note, radius, key, longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_details);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        key = getIntent().getStringExtra("key");
        name = getIntent().getStringExtra("name");
        note = getIntent().getStringExtra("notes");
        radius = getIntent().getStringExtra("radius");
        longitude = getIntent().getStringExtra("longitude");
        latitude = getIntent().getStringExtra("latitude");

        editName = findViewById(R.id.editName);
        editNotes = findViewById(R.id.editNotes);
        editRadius = findViewById(R.id.editradius);

        editName.setText(name);
        editNotes.setText(note);
        editRadius.setText(radius);

        btn_delete = findViewById(R.id.deletebtn);
        btn_edit = findViewById(R.id.editBtn);
        btn_back = findViewById(R.id.backbtn);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm alarm = new alarm();
                alarm.setName(editName.getText().toString());
                alarm.setNotes(editNotes.getText().toString());
                alarm.setRadius(editRadius.getText().toString());
                alarm.setLongitude(longitude);
                alarm.setLatitude(latitude);

                new firebaseDatabaseHelper().updateAlarm(key, alarm, new firebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<com.example.smartlocationalarm.alarm> alarms, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        Toast.makeText(alarmDetailsActivity.this, "alarm has been updated successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(alarmDetailsActivity.this, alarmListActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new firebaseDatabaseHelper().deleteAlarm(key, new firebaseDatabaseHelper.DataStatus() {
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
                        Toast.makeText(alarmDetailsActivity.this, "alarm has been deleted successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(alarmDetailsActivity.this, alarmListActivity.class);
                        startActivity(intent);

                    }
                });
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //add marker in alarm position
        LatLng alarmPosition = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        mMap.addMarker(new MarkerOptions().position(alarmPosition).title(name).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_marker))).showInfoWindow();
        mMap.addCircle(new CircleOptions().center(alarmPosition).radius(Double.parseDouble(radius)).strokeWidth(2.0f).strokeColor(getResources().getColor(R.color.outline)).fillColor(getResources().getColor(R.color.radius)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(alarmPosition));
        moveCamera(alarmPosition, 15f);
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d("Maps activity", "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDarawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDarawable.setBounds(0, 0, vectorDarawable.getIntrinsicWidth(), vectorDarawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDarawable.getIntrinsicWidth(), vectorDarawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDarawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }
}
