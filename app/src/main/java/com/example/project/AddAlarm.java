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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.text.DateFormat;

public class AddAlarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    ImageButton mapsButton;
    TextView errorText ;
    EditText timeReady ;
    ImageButton timeButton;
    TextView timePicked;
    static ArrayList<StateVO> listVOs;
    TextView textV ;
    Button setAlarm ;
    Button tasksButton;
    int hours = -1;
    int minutes = -1;
    String setTime = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        mapsButton = (ImageButton) findViewById(R.id.mapsButton);
        timeButton = (ImageButton) findViewById(R.id.timeButton);
        textV = findViewById(R.id.timeText);
        setAlarm= (Button)findViewById(R.id.setButton);
        tasksButton = (Button) findViewById(R.id.tasksButton);
        timeReady = findViewById(R.id.getReadyInput);
        errorText = findViewById(R.id.errorText);



        Resources res = getResources();
        String[] days =res.getStringArray(R.array.days);

        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Maps = new Intent(getApplicationContext(), MapsActivity.class);
                Maps.putExtra("name", "none");
                Maps.putExtra("time", "0");
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
                if (MainActivity.toList.size()>=2 && hours!=-1 || minutes!=-1 ) {
                    Intent Main = new Intent(getApplicationContext(), MainActivity.class);
//                Log.d("hour",String.valueOf(hours));
                    Main.putExtra("hours", String.valueOf(hours));
                    Main.putExtra("minutes", String.valueOf(minutes));
                    toEntry entry = new toEntry(Integer.parseInt(setTime) * 60, null, "readyTime");
                    MainActivity.toList.add(entry);
                    startActivity(Main);
                }
                else
                {
                    errorText.setText("Please Make Sure You Entered All Values");
                }

            }
        });

        tasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.toList.size()>=2 && hours!=-1 && minutes!=-1 ) {
                    Intent tasksPage = new Intent(getApplicationContext(), tasks.class);
                    toEntry entry = new toEntry(Integer.parseInt(setTime) * 60, null , "readyTime");
                    MainActivity.toList.add(entry);
                    startActivity(tasksPage);
                }
                else
                {
                    errorText.setText("Please Make Sure You Entered All Values");
                }
            }
        });

        timeReady.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                setTime = s.toString() ;
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

    }

}
