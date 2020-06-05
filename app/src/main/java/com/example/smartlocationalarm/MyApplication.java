package com.example.smartlocationalarm;

import android.app.Application;

public class MyApplication extends Application {

    private String id = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
