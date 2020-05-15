package com.example.smartlocationalarm;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    public ImageView mAdd, mSaved;
    Boolean Marker = false;
    Dialog myDialog;
    Button mEdit;
    TextView Ename;
    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    ////////////////////////////////////////////////////////////////////////////////
    //read data
    final ArrayList<alarm> alarms = new ArrayList<alarm>();
    private ArrayList<String> longitudes = new ArrayList<String>();
    private ArrayList<String> latitudes = new ArrayList<String>();
    private ArrayList<String> names = new ArrayList<String>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public void readAlarms() {
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("alarm");
        Toast.makeText(this, "databaseLoaded", Toast.LENGTH_SHORT).show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                alarms.clear();
                longitudes.clear();
                latitudes.clear();
                names.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    alarm mAlarm = keyNode.getValue(alarm.class);
                    alarms.add(mAlarm);
                }
                int a = alarms.size();
                Log.d("aaaaaaaa", String.valueOf(a));
                int h = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mAdd = findViewById(R.id.ic_add_alarm);
        mSaved = findViewById(R.id.ic_alarms);


//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                alarms alarm=new alarms();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MapsActivity.this, CreateAlarmActivity.class);
                startActivity(intent1);
            }
        });
        mSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MapsActivity.this, alarmListActivity.class);
                startActivity(intent2);
            }
        });
        getLocationPermission();
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        String Raduiss,Notess;
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            final Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);
                            if (Marker == false) {
                                LatLng MyLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(MyLocation).title("My Location"));
                                Marker = true;
                                firebaseDatabase = firebaseDatabase.getInstance();
                                databaseReference = firebaseDatabase.getReference().child("alarm");
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        alarms.clear();
                                        longitudes.clear();
                                        latitudes.clear();
                                        names.clear();
                                        List<String> keys = new ArrayList<>();
                                        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                                            keys.add(keyNode.getKey());
                                            alarm mAlarm = keyNode.getValue(alarm.class);
                                            alarms.add(mAlarm);
                                        }
                                        for (int i = 0; i < alarms.size(); i++) {
                                            double latitude = Double.valueOf(alarms.get(i).getLatitude());
                                            double longitude = Double.valueOf(alarms.get(i).getLongitude());
                                            double radius = Double.valueOf(alarms.get(i).getRadius());
                                            String notes = alarms.get(i).getName();
                                            String name = alarms.get(i).getName()+ " : "+alarms.get(i).getNotes();
                                            LatLng position = new LatLng(latitude, longitude);
                                            mMap.addMarker(new MarkerOptions().position(position).title(name).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_marker))).showInfoWindow();
                                            mMap.addCircle(new CircleOptions().center(position).radius(radius).strokeWidth(2.0f).strokeColor(getResources().getColor(R.color.outline)).fillColor(getResources().getColor(R.color.radius)));
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
//                                try {
//                                    // Customise the styling of the base map using a JSON object defined
//                                    // in a raw resource file.
//                                    boolean success = mMap.setMapStyle(
//                                            MapStyleOptions.loadRawResourceStyle(
//                                                    MapsActivity.this, R.raw.mapsretro));
//
//                                    if (!success) {
//                                        Log.e("MapsActivityRaw", "Style parsing failed.");
//                                    }
//                                } catch (Resources.NotFoundException e) {
//                                    Log.e("MapsActivityRaw", "Can't find style.", e);
//                                }
//                                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                                    @Override
//                                    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
//                                        String markerTitle = marker.getTitle();
//
//                                        myAlarmDialog(markerTitle);
//                                        return false;
//                                    }
//                                });
//                                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
                            }
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDarawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDarawable.setBounds(0, 0, vectorDarawable.getIntrinsicWidth(), vectorDarawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDarawable.getIntrinsicWidth(), vectorDarawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDarawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }

//    public void myAlarmDialog(final String title) {
//        myDialog = new Dialog(MapsActivity.this);
//        myDialog.setContentView(R.layout.activity_popup);
//        myDialog.setTitle("About");
//        Ename=myDialog.findViewById(R.id.Ename);
//        Ename.setText(title);
//        mEdit = myDialog.findViewById(R.id.Edit);
//        mEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MapsActivity.this, Edit.class);
//                intent.putExtra("name",title);
//                startActivity(intent);
//            }
//        });
//        myDialog.show();
//    }
}