package com.example.project;

import com.google.android.gms.maps.model.LatLng;

public class toEntry {
    int timeSeconds;
    LatLng latLng;
    String id ;

        public toEntry(int timeSeconds , LatLng latLng, String id)
        {
            this.latLng= latLng;
            this.timeSeconds = timeSeconds;
            this.id = id ;
        }



}
