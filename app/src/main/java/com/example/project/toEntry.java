package com.example.project;

import com.google.android.gms.maps.model.LatLng;

public class toEntry {
    int timeSeconds;
    LatLng latLng;

        public toEntry(int timeSeconds , LatLng latLng)
        {
            this.latLng= latLng;
            this.timeSeconds = timeSeconds;
        }


}
