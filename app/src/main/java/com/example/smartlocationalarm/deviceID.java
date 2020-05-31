package com.example.smartlocationalarm;

import java.util.UUID;

public class deviceID {
    public String uniqueID;

    public static String getId() {
        String unique = UUID.randomUUID().toString();
        return unique;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

}
