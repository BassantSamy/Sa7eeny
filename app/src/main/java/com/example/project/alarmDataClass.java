package com.example.project;

public class alarmDataClass {
    int alarmHr;
    int alarmMin;
    String Repeat;

    public alarmDataClass(){
        alarmHr=0;
        alarmMin=0;
        Repeat="";
    }

    public alarmDataClass(String alarmid, int alarmHr, int alarmMin, String destination, String repeat) {
        this.alarmHr = alarmHr;
        this.alarmMin = alarmMin;

        Repeat = repeat;
    }



    public void SetTime(int alarmHr, int alaramMin)
    {
        this.alarmHr=alarmHr;
        this.alarmMin=alaramMin;
    }


    public void setRepeat(String repeat) {
        Repeat = repeat;
    }

    public void setAlarmHr(int alarmHr) {
        this.alarmHr = alarmHr;
    }

    public void setAlarmMin(int alarmMin) {
        this.alarmMin = alarmMin;
    }


    public int getAlarmHr() {
        return alarmHr;
    }

    public int getAlarmMin() {
        return alarmMin;
    }


    public String getRepeat() {
        return Repeat;
    }

//    @Override
//    public String toString() {
//
//        return "{" +
//                "Repeat=" + Repeat +
//                ", alarmHr=" + alarmHr +
//                ", alarmMin=" + alarmMin +
//                "}";
//    }


}
