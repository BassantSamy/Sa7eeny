package com.example.project;

public class listEntry {

    private String time;
    private String ampm;

    public listEntry(String time, String ampm){
        this.time = time;
        this.ampm = ampm;
    }

    public String getTime(){
        return time;
    }

    public String getAmpm(){
        return ampm;
    }
}