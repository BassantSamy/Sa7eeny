package com.example.project;

public class listEntry {

    private String time;
    private String ID;

    public listEntry(String time, String ID){
        this.time = time;
        this.ID = ID;
    }

    public String getTime(){
        return time;
    }

    public  String getID(){return ID;}

    public void setTime(String Time){this.time=Time;}
}