package com.example.project;

public class Task {

    String name;
    int Duration_hr;
    int Duration_min;


    public Task(String name){
        this.name=name;
        Duration_hr=0;
        Duration_min=0;
    }


    public Task( String name, int Duration_hr, int Durtaion_min) {
        this.name = name;
        this.Duration_hr=Duration_hr;
        this.Duration_min=Durtaion_min;

    }


    public String getName() {
        return name;
    }
    public void SetName(String n)
    {
        name=n;
    }

    public int getDuration_hr ()
    {
        return Duration_hr;
    }
    public int getDuration_min ()
    {
        return Duration_min;
    }
    public void setDuration_hr(int duration_hr)
    {
        this.Duration_hr=duration_hr;
    }

    public void setDuration_min(int duration_min)
    {
        this.Duration_min=duration_min;
    }


    @Override
    public String toString() {

        return "Task{" +
                " TaskName='" + name + '\'' +
                ", HourDuration='" + Duration_hr + '\'' +
                ", MinDuration='" + Duration_min + '\'' +
                '}';
    }

}