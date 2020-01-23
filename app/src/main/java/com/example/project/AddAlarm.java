package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.text.DateFormat;

public class AddAlarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    ImageButton mapsButton;
    ImageButton timeButton;
    TextView timePicked;
    static ArrayList<StateVO> listVOs;
    TextView textV ;
    Button setAlarm ;
    Button tasksButton;
    int hours;
    int minutes;
    String setTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        mapsButton = (ImageButton) findViewById(R.id.mapsButton);
        timeButton = (ImageButton) findViewById(R.id.timeButton);
        textV = findViewById(R.id.timeText);
        setAlarm= (Button)findViewById(R.id.setButton);
        tasksButton = (Button) findViewById(R.id.tasksButton);


        Resources res = getResources();
        String[] days =res.getStringArray(R.array.days);

        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Maps = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(Maps);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Main = new Intent(getApplicationContext(), MainActivity.class);
//                Log.d("hour",String.valueOf(hours));
                Main.putExtra("hours", String.valueOf(hours));
                Main.putExtra("minutes", String.valueOf(minutes));
                startActivity(Main);
            }
        });

        tasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tasksPage = new Intent(getApplicationContext(), tasks.class);
                startActivity(tasksPage);
            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < days.length; i++) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(days[i]);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        MyAdapter myAdapter = new MyAdapter(AddAlarm.this, 0,
                listVOs);
        spinner.setAdapter(myAdapter);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timePicked = (TextView) findViewById(R.id.timePicked);
        timePicked.setText(hourOfDay + " :" + minute);
        hours = hourOfDay;
        minutes = minute;
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
//        cal.set(Calendar.MINUTE, minute);
//        cal.set(Calendar.SECOND, 0);
//        Log.d("onTimeSetH",String.valueOf(hours));
//        Log.d("onTimeSetM",String.valueOf(minutes));
//        UpdateTextView(cal);
//        setAlarm(cal);
    }

//    private void UpdateTextView(Calendar c) {
//
//        setTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
//    }
//
//    private void setAlarm(Calendar c) {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
//        if (c.before(Calendar.getInstance())) {
//            c.add(Calendar.DATE, 1);
//        }
//        Log.d("setAlarm",String.valueOf(c.getTimeInMillis()));
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//    }
//
//    private void cancelAlarm() {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
//        alarmManager.cancel(pendingIntent);
//        //mTextView.setText("Alarm canceled");
//    }
}
