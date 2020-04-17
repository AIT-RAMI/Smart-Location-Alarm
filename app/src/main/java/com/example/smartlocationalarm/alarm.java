package com.example.smartlocationalarm;

public class alarm {
    public String longitude, latitude, name;

    public alarm(String longitude, String altitude, String name) {
        this.longitude = longitude;
        this.latitude = altitude;
        this.name = name;
    }

    public alarm() {

    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String altitude) {
        this.latitude = altitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
