package com.example.smartlocationalarm;

import android.app.Activity;
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

import hotchemi.android.rate.AppRate;

public class WelcomeActivity2 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);

        Button notif;
        Button settings;
        Button rating;
        Button share;
        Button about;

        AppRate.with(this)
                .setInstallDays(0)
                .setLaunchTimes(10)
                .setRemindInterval(5)
                .setShowLaterButton(true)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);

        notif = findViewById(R.id.test);
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotification();
            }
        });
        settings = findViewById(R.id.test2);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        rating = findViewById(R.id.test3);
        final Context c = this;
        final Activity a = this;
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppRate.with(c).showRateDialog(a);
            }
        });
        share = findViewById(R.id.test4);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Smart Location Alarm");
                String message = "\nJe voudrais vous recommander cette application Smart Location Alarm, prochainement nous allons la mettre dans le play store \n\n";

                i.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(i, "Share with others"));
            }
        });
        about = findViewById(R.id.test5);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addNotification() {
        String notif_text = "Vous avez programmé une alarme dans cette région! Rentrez voir de quoi s'agit il.";
        String notif_title = "Nouvelle Notification";

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("text", notif_text);

        PendingIntent pdIntent = PendingIntent.getActivity(WelcomeActivity2.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

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
}
