package com.example.enactushack;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Moment {
    public String message;
    public String date;
    public String user;
    public LatLng pos;
    public boolean found;
    public Marker marker;

    public Moment(String message, String date, String user, LatLng pos, Marker marker) {
        this.message = message;
        this.date = date;
        this.user = user;
        this.pos = pos;
        this.marker = marker;
    }
}
