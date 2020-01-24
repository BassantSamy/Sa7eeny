package com.example.project;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) throws JSONException {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity= "-NA-"; //address
        String latitude= "";
        String longitude="";
        String reference="";

        placeName= googlePlaceJson.getJSONObject("poi").getString("name");
        latitude= googlePlaceJson.getJSONObject("position").getString("lat");
        longitude=googlePlaceJson.getJSONObject("position").getString("lon");



        Log.d("DataParser","jsonobject ="+googlePlaceJson.toString());


            googlePlaceMap.put("place_name", placeName);
            //googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            //googlePlaceMap.put("reference", reference);

        return googlePlaceMap;

    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String, String>> placelist = new ArrayList<>();
        HashMap<String, String> placeMap = null;

        for(int i = 0; i<count;i++)
        {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placelist.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placelist;
    }

    public List<HashMap<String, String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        Log.d("json data", jsonData);

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }


    private HashMap<String,String> getDuration(JSONObject googleDirectionsJson)
    {
        HashMap<String,String> googleDirectionsMap = new HashMap<>();
        String duration = "";
        String distance ="";


        try {

            distance = googleDirectionsJson.getString("lengthInMeters");
            duration = googleDirectionsJson.getString("travelTimeInSeconds");

            googleDirectionsMap.put("duration" , duration);
            googleDirectionsMap.put("distance", distance);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return googleDirectionsMap;
    }

    public HashMap<String,String> parseDirections(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject = null;
        JSONObject tomtom = null;


        try {
            jsonObject = new JSONObject(jsonData);
           // jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs"); //routes array
            tomtom = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONObject("summary"); //routes array

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getDuration(tomtom);

    }



}
