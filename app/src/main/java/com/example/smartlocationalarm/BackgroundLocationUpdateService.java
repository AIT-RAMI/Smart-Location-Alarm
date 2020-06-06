package com.example.smartlocationalarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BackgroundLocationUpdateService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /* Declare in manifest
    <service android:name=".BackgroundLocationUpdateService"/>
    */

    private final String TAG = "BackgroundLocationUpdtS";
    private final String TAG_LOCATION = "TAG_LOCATION";
    private Context context;
    private boolean stopService = false;

    /* For Google Fused API */
    protected GoogleApiClient mGoogleApiClient;
    protected LocationSettingsRequest mLocationSettingsRequest;
    private String latitude = "0.0", longitude = "0.0";
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    /* For Google Fused API */
    /* For notifications */
    private static final String CHANNEL_ID = "MyNotificationChannelID";
    private Ringtone ringtone;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StartForeground();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (!stopService) {
                        //Perform your task here
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (!stopService) {
                        handler.postDelayed(this, TimeUnit.SECONDS.toMillis(10));
                    }
                }
            }
        };
        handler.postDelayed(runnable, 2000);

        buildGoogleApiClient();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "Service Stopped");
        stopService = true;
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Log.e(TAG_LOCATION, "Location Update Callback Removed");
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void StartForeground() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String CHANNEL_ID = "channel_location";
        String CHANNEL_NAME = "channel_location";

        NotificationCompat.Builder builder = null;
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
            builder.setChannelId(CHANNEL_ID);
            builder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        }

        builder.setContentTitle("Your title");
        builder.setContentText("You are now online");
        Uri notificationSound = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
        builder.setSound(notificationSound);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(101, notification);
    }

    @Override
    public void onLocationChanged(final Location location) {
        Log.e(TAG_LOCATION, "Location Changed Latitude : " + location.getLatitude() + "\tLongitude : " + location.getLongitude());

        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

        if (latitude.equalsIgnoreCase("0.0") && longitude.equalsIgnoreCase("0.0")) {
            requestLocationUpdate();
        } else {
            // Change those values for
            //load data
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            String CHANNEL_ID = "ff";
            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(this, turnAlarmeOffActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("id", 111111);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_alarms)
                    .setContentTitle("Default name")
                    .setContentText("Default notes")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSound(alarmSound)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND);
            ;

            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "My Alarm clock Service", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(notificationChannel);
                notificationChannel.enableVibration(true);
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                notificationChannel.setSound(alarmSound, attributes);

            }
            final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            new firebaseDatabaseHelper(BackgroundLocationUpdateService.this).readAlarms(new firebaseDatabaseHelper.DataStatus() {
                @Override
                public void DataIsLoaded(List<alarm> alarms, List<String> keys) {
                    for (int i = 0; i < alarms.size(); i++) {
                        double latDest = Double.valueOf(alarms.get(i).getLatitude());
                        double logDest = Double.valueOf(alarms.get(i).getLongitude());
                        double radius = Double.valueOf(alarms.get(i).getRadius());
                        String Title = String.valueOf(alarms.get(i).getName());
                        String Note = String.valueOf(alarms.get(i).getNotes());
                        Boolean status = alarms.get(i).getStatus();
                        if (test(latDest, logDest, location.getLatitude(), location.getLongitude())<=radius) {
                            if (status) {
                                Log.d(TAG_LOCATION, "I'm in range! the alarm should notify :)");
                                /* Notification Code*/
                                final int notificationId = 1111;

                                // notificationId is a unique int for each notification that you must define

                                builder.setContentTitle(Title);
                                builder.setContentText(Note);
                                final Notification notification = builder.build();
                                notification.defaults |= Notification.DEFAULT_SOUND;

                                notificationManager.notify(notificationId, notification);
                                ringtone.play();

                                ((MyApplication) getApplication()).setId(String.valueOf(i+1));


                                //notificationManager.cancel(notificationId);

//                            alarm alarm = new alarm();
//                            alarm.setName(alarms.get(i).getName());
//                            alarm.setNotes(alarms.get(i).getNotes());
//                            alarm.setRadius(alarms.get(i).getRadius());
//                            alarm.setLongitude(alarms.get(i).getLongitude());
//                            alarm.setLatitude(alarms.get(i).getLatitude());
//                            alarm.setStatus(false);
//
//                            new firebaseDatabaseHelper(BackgroundLocationUpdateService.this).updateAlarm(String.valueOf(i+1), alarm, new firebaseDatabaseHelper.DataStatus() {
//                                @Override
//                                public void DataIsLoaded(List<com.example.smartlocationalarm.alarm> alarms, List<String> keys) {
//
//                                }
//
//                                @Override
//                                public void DataIsInserted() {
//
//                                }
//
//                                @Override
//                                public void DataIsUpdated() {
//                                }
//
//                                @Override
//                                public void DataIsDeleted() {
//
//                                }
//                            });

                                /* end Notification Code */
                            }
                        }
                    }
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

            Log.e(TAG_LOCATION, "Latitude : " + location.getLatitude() + "\tLongitude : " + location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10 * 10000);
        mLocationRequest.setFastestInterval(5 * 10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.e(TAG_LOCATION, "GPS Success");
                        requestLocationUpdate();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            int REQUEST_CHECK_SETTINGS = 214;
                            ResolvableApiException rae = (ResolvableApiException) e;
                            rae.startResolutionForResult((AppCompatActivity) context, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sie) {
                            Log.e(TAG_LOCATION, "Unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e(TAG_LOCATION, "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.e(TAG_LOCATION, "checkLocationSettings -> onCanceled");
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        connectGoogleClient();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mSettingsClient = LocationServices.getSettingsClient(context);

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        connectGoogleClient();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.e(TAG_LOCATION, "Location Received");
                mCurrentLocation = locationResult.getLastLocation();
                onLocationChanged(mCurrentLocation);
            }
        };
    }

    private void connectGoogleClient() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(context);
        if (resultCode == ConnectionResult.SUCCESS) {
            mGoogleApiClient.connect();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdate() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
    private boolean inRange(double latDest, double logDest, double latCurr, double logCurr, double radius){
        double φ1 = Math.PI * latDest / 180;
        double φ2 = Math.PI * latCurr / 180;
        double Δφ = φ2 - φ1;
        double Δλ = Math.PI * (logDest - logCurr) / 180;
        double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) + Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
        double d = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        if(d < radius) return true;
        else return false;
    }

    public Double test(Double lat1, Double lon1, Double lat2, Double lon2) {
        double earthRadius = 6371;
        double latDiff = Math.toRadians(lat2-lat1);
        double lngDiff = Math.toRadians(lon2-lon1);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat1))*
                        Math.cos(Math.toRadians(lat2))* Math.sin(lngDiff /2) *
                        Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = c;
        return distance;
    }
}