package com.example.smartlocationalarm;

public class DataSetFire {
    String Nom;

    String Date;

    public DataSetFire() {
    }

    public DataSetFire(String nom, String date) {
        Nom = nom;
        Date = date;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
