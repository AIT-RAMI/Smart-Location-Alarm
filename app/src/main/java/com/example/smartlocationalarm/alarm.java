package com.example.smartlocationalarm;

public class alarm {
    public String longitude, latitude, name,notes,radius;
    public Boolean status;

    public alarm(String longitude, String latitude, String name, String notes, String radius, Boolean status) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.notes = notes;
        this.radius = radius;
        this.status = status;
    }

    public alarm() {

    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
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
