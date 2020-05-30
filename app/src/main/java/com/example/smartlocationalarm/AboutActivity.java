package com.example.smartlocationalarm;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.logo)
                .setDescription(getString(R.string.desc))
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Contact")
                .addEmail("adnaneaabbar@gmail.com")
                .addWebsite("https://github.com/adnaneaabbar")
                .addFacebook("adnaneaabbar")
                //.addPlayStore("en attendant un lien vers playstore")
                .addInstagram("_seaholic")
                .addGitHub("AIT-RAMI/Smart-Location-Alarm")
                .create();

        setContentView(aboutPage);
    }
}
