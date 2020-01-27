package com.example.project;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static java.lang.Thread.sleep;

public class checkDuration extends AsyncTask<String,Integer,Void> {

    int totalDuration ;
    ArrayList <toEntry>  toList;
    String timeArrive ;
    String alarmId ;


    @Override
    protected Void doInBackground(String... strings) {

        if (MainActivity.toList != null &&MainActivity.timeArrive!= null ) {
            toList = MainActivity.toList;
            timeArrive = MainActivity.timeArrive;
            alarmId = strings[0];
            boolean flag = true ;


            while (flag) {



                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm");
                Date dateUser = null;
                Date dateCurrent = null;

                try {
                    dateUser = df.parse(timeArrive);
                    dateCurrent = df.parse(currentTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                long diff = dateUser.getTime() - dateCurrent.getTime();


                String url = getDirectionsUrl();
                String duration = getIncrement(url);

                totalDuration = appendUserTime(Integer.parseInt(duration));

                Log.d("diff",String.valueOf(diff / 1000));
                Log.d("totalDuration",String.valueOf(totalDuration));

                if (diff / 1000 <= totalDuration) {
                    MainActivity.f.id = alarmId;
                    MainActivity.f.setSomeVariable(totalDuration);
                    cancel(true);
                }


                try {
                    sleep(1000); //one hour increments
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(isCancelled()) {
                    flag = false;
                    cancel(true);
                }

            }
        }
        return null;
    }

    private String getDirectionsUrl()
    {
        LatLng tasks;


        int versionNumber = 1;
        //https://api.tomtom.com/routing/1/calculateRoute/52.50931%2C13.42936%3A52.50274%2C13.43872/json?avoid=unpavedRoads&key=QYbB9Q5GbCsw9Wlk1vXRWtXTBGgsbwMn
        StringBuilder googleDirectionsUrl = new StringBuilder("https://api.tomtom.com/routing/");
        googleDirectionsUrl.append(versionNumber+"/calculateRoute/");

        if (toList.get(0).latLng != null) {
            LatLng current = toList.get(0).latLng;
            googleDirectionsUrl.append(current.latitude + "," + current.longitude);
        }


        for (int i=1 ;i<toList.size() ;i++) {
            if (toList.get(i).latLng != null) {
                tasks = toList.get(i).latLng;
                googleDirectionsUrl.append(":" + tasks.latitude + "," + tasks.longitude);
            }

        }
        googleDirectionsUrl.append("/json?");
        googleDirectionsUrl.append("avoid=unpavedRoads&key="+"QYbB9Q5GbCsw9Wlk1vXRWtXTBGgsbwMn");

        Log.d("MapsActivity", "Directionsurl = "+googleDirectionsUrl.toString());


        return googleDirectionsUrl.toString();
    }


    String getIncrement (String url)
    {
        String googleDirectionsData = null;
        String duration;


        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String,String> directionsList = null;
        DataParser parser = new DataParser();
        directionsList = parser.parseDirections(googleDirectionsData);
        duration = directionsList.get("duration");

        return duration;

    }

    int appendUserTime(int duration)
    {

        for (int i=0 ;i<toList.size();i++)
        {
            duration+= toList.get(i).timeSeconds;
        }
        return duration;

    }


}
