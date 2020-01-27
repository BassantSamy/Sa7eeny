package com.example.project;

import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AccessData<T> {


    public ArrayList<T> LoadData(Context context, String PrefName, String Key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PrefName, context.MODE_PRIVATE);
        // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json=sharedPreferences.getString(Key, null);
        if(json!=null) {
//            Log.d("Log el string bs", json);
            Type t = new TypeToken<ArrayList<T>>() {
            }.getType();
            ArrayList<T> DI = gson.fromJson(json, t);
            if (DI != null) {
//                Log.d("ana fi LoadData", json);
                return DI;
            }
            if (DI == null) {
                DI = new ArrayList<>();
                return DI;
            }
        }
        return null;

    }
    public ArrayList<alarmDataClass> LoadData_alarmDataClass(Context context, String PrefName, String Key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PrefName, context.MODE_PRIVATE);
        // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json=sharedPreferences.getString(Key, null);
        if(json!=null) {
            Log.d("Log el string bs", json);
            Type t = new TypeToken<ArrayList<alarmDataClass>>() {
            }.getType();
            ArrayList<alarmDataClass> DI = gson.fromJson(json, t);
            if (DI != null) {
                Log.d("ana fi LoadData", json);
                return DI;
            }
            if (DI == null) {
                DI = new ArrayList<alarmDataClass>();
                return DI;
            }
        }
        return null;
    }


    public ArrayList<listEntry> LoadData_listEntry(Context context, String PrefName, String Key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PrefName, context.MODE_PRIVATE);
        // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json=sharedPreferences.getString(Key, null);
        if(json!=null) {
            Log.d("Log el string bs", json);
            Type t = new TypeToken<ArrayList<listEntry>>() {
            }.getType();
            ArrayList<listEntry> DI = gson.fromJson(json, t);
            if (DI != null) {
                Log.d("ana fi LoadData", json);
                return DI;
            }
            if (DI == null) {
                DI = new ArrayList<listEntry>();
                return DI;
            }
        }
        return null;

    }

    public ArrayList<Task> LoadData_Task(Context context, String PrefName, String Key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PrefName, context.MODE_PRIVATE);
        // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json=sharedPreferences.getString(Key, null);
        if(json!=null) {
            Log.d("Log el string bs", json);
            Type t = new TypeToken<ArrayList<Task>>() {
            }.getType();
            ArrayList<Task> DI = gson.fromJson(json, t);
            if (DI != null) {
                Log.d("ana fi LoadData", json);
                return DI;
            }
            if (DI == null) {
                DI = new ArrayList<Task>();
                return DI;
            }
        }
        return null;

    }

    public ArrayList<toEntry> LoadData_toList(Context context, String PrefName, String Key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PrefName, context.MODE_PRIVATE);
        // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json=sharedPreferences.getString(Key, null);
        if(json!=null) {
            Log.d("Log el string bs", json);
            Type t = new TypeToken<ArrayList<toEntry>>() {
            }.getType();
            ArrayList<toEntry> DI = gson.fromJson(json, t);
            if (DI != null) {
                Log.d("ana fi LoadData", json);
                return DI;
            }
            if (DI == null) {
                DI = new ArrayList<toEntry>();
                return DI;
            }
        }
        return null;

    }

    public JSONObject LoadSingleData(Context context, String PrefName, String Key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PrefName, context.MODE_PRIVATE);
        // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        //Gson gson = new Gson();
        String json_str=sharedPreferences.getString(Key, null);

       // JSONObject json = null;
        try {

            JSONObject json = new JSONObject(json_str);
            //Log.d("Log el string bs", json_str);
            return json;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


    public void saveData(List<T> list, Context context,String PrefName, String Key){

        String converted;
        SharedPreferences sharedPreferences=context.getSharedPreferences(PrefName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
//        Type t = new TypeToken<List<T>>() {
//        }.getType();
       // converted = jsonToString(list);
        Gson gson= new Gson();
        converted= gson.toJson(list);
      //  Log.d("fi save", converted);
        editor.putString(Key, converted);
        editor.apply();

    }

    public void saveData_listEntry(List<listEntry> list, Context context,String PrefName, String Key){

        String converted;
        SharedPreferences sharedPreferences=context.getSharedPreferences(PrefName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        Gson gson= new Gson();
        converted= gson.toJson(list);
        Log.d("fi save", converted);
        editor.putString(Key, converted);
        editor.apply();

    }


    public void saveData_obj(T obj, Context context,String PrefName, String Key){

        String converted;
        SharedPreferences sharedPreferences=context.getSharedPreferences(PrefName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        converted = jsonToString(obj);
        Log.d("fi save", converted);
        editor.putString(Key, converted);
        editor.apply();

    }

    public void saveData_str(String str, Context context,String PrefName, String Key){

        SharedPreferences sharedPreferences=context.getSharedPreferences(PrefName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(Key, str);
        editor.apply();

    }

    public void remove_Data(Context context,String PrefName, String Key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove("Alarm"+Key).commit();
        sharedPreferences.edit().remove("Tasks"+Key).commit();
        sharedPreferences.edit().remove("Suggested"+Key).commit();
        removeAlarmList(context, PrefName, Key);

    }

    public void removeAlarmList(Context context,String PrefName, String Key){
        int i;
        List<listEntry> whole_list = LoadData_listEntry(context, PrefName,"AlarmList");
        i = ParseList(whole_list, Key);
        if(i != -1){
            whole_list.remove(i);
        }
        saveData_listEntry(whole_list, context,PrefName,"AlarmList");
    }

    public void EditAlarmList(Context context,String PrefName, String Key, int Hr, int Min){
        int i;
        String Time;
        Time = TwHourFormat(String.valueOf(Hr), String.valueOf(Min));
        List<listEntry> whole_list = LoadData_listEntry(context, PrefName,"AlarmList");
        if(whole_list!=null) {
            i = ParseList(whole_list, Key);
            if (i != -1) {
                whole_list.get(i).setTime(Time);
            }
            saveData_listEntry(whole_list, context, PrefName, "AlarmList");
        }
    }
//    public String jsonToString(List<T> data , Type t){
//        Log.d("fi json to string ", data.get(0).toString());
//        Gson gson= new Gson();
//        String jsonString=gson.toJson(data, t);
//        return jsonString;
//    }

    private String TwHourFormat( String Hours, String minutes) {

        String setTime;
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Hours));
        cal.set(Calendar.MINUTE, Integer.parseInt(minutes));
        cal.set(Calendar.SECOND, 0);
        setTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(cal.getTime());
        return setTime;
    }

    private int ParseList(List<listEntry> whole_list, String ID){

        for (int i = 0; i < whole_list.size(); i++) {
            if(whole_list.get(i).getID().equals(ID)){
                return i;
            }
        }
        return -1;
    }

    public String jsonToString(T data){
        Gson gson= new Gson();
        String jsonString=gson.toJson(data);
        return jsonString;
    }
//    public List<T> StringToList(String data){
//        Gson gson= new Gson();
//        Type t= new TypeToken<List<T>>() {}.getType();
//        List<T> returned_list = gson.fromJson(data, t);
//        return returned_list;
//
//    }

    public ArrayList<MainActivity.Triplet<String, PendingIntent, Calendar, Boolean>> LoadData_triplet(Context context, String PrefName, String Key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PrefName, context.MODE_PRIVATE);
        // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json=sharedPreferences.getString(Key, null);
        if(json!=null) {
            Log.d("Log el string bs", json);
            Type t = new TypeToken<ArrayList<MainActivity.Triplet<String, PendingIntent, Calendar, Boolean>>>() {
            }.getType();
            ArrayList<MainActivity.Triplet<String, PendingIntent, Calendar, Boolean>> DI = gson.fromJson(json, t);
            if (DI != null) {
                Log.d("ana fi LoadData", json);
                return DI;
            }
            if (DI == null) {
                DI = new ArrayList<>();
                return DI;
            }
        }
        return null;

    }

    public String LoadStr(Context context, String PrefName, String Key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PrefName, context.MODE_PRIVATE);
        String str=sharedPreferences.getString(Key, null);
        return str;

    }


}

