package com.example.smartlocationalarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class Welcome_Activity extends AppCompatActivity {

    Button create;
    Button exist;
    Button nav;
    Button data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_);

        Button notif;
        notif = findViewById(R.id.test);
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
        exist = findViewById(R.id.test3);
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


    }

    private void existing() {
        Intent intent = new Intent(this, MapsActivity.class);
        this.startActivity(intent);
    }

    private void clickCreate(){
        Intent intent = new Intent(this, PermissionActivity.class);
        this.startActivity(intent);
    }

    private void addNotification() {
        String notif_text = "Vous avez programmé une alarme dans cette région! Rentrez voir de quoi s'agit il.";
        String notif_title = "Nouvelle Notification";

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("text", notif_text);

        PendingIntent pdIntent = PendingIntent.getActivity(Welcome_Activity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle(notif_title)
                .setContentText(notif_text)
                .setContentIntent(pdIntent)
                .setAutoCancel(true);

        NotificationManager ntfManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "5";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Ceci est le titre pour la channel",
                    NotificationManager.IMPORTANCE_HIGH);
            ntfManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        ntfManager.notify(0, builder.build());
    }

    public void AllAlarms(View view) {
        Intent intent = new Intent(this, MainActivityAlarms.class);
        this.startActivity(intent);
    }
}
