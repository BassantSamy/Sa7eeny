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

import org.json.JSONException;
import org.json.JSONObject;

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
    //Button setAlarm ;
    Button tasksButton;
    AccessData<alarmDataClass> AD_2;
    AccessData<toEntry> AD_toEntry;

    alarmDataClass ADC;

    String AlarmId;
    int hours = -1;
    int minutes = -1;
    String setTime ="";
    String[] days;
    boolean Edit;
    String Serialized="";
    MyAdapter myAdapter;
    boolean timeSet = false;
    int Hour, min;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        AD_2  = new AccessData<>();
        ADC = new alarmDataClass();
        AD_toEntry= new AccessData<>();

        final String PrefName = getString(R.string.PrefName);
        mapsButton = (ImageButton) findViewById(R.id.mapsButton);
        timeButton = (ImageButton) findViewById(R.id.timeButton);
        textV = findViewById(R.id.timeText);
        //setAlarm= (Button)findViewById(R.id.setButton);
        tasksButton = (Button) findViewById(R.id.tasksButton);
        timePicked = (TextView) findViewById(R.id.timePicked);
        timeReady = findViewById(R.id.getReadyInput);
        errorText = findViewById(R.id.errorText);


        String Edit_str=getIntent().getStringExtra("EditFlag");
        if(Edit_str.equals("1")) {
            AlarmId=getIntent().getStringExtra("AlarmId");
            Edit = true;
            tasksButton.setText("Edit Tasks");
            try {
                Serialized=AD_2.LoadSingleData(getApplicationContext(), PrefName, "Alarm" + AlarmId).getString("Repeat");
                Serialized="0"+Serialized;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else Edit = false;

        if(Edit==true){
            Initialize(timePicked);
        }



        Resources res = getResources();
        days =res.getStringArray(R.array.days);


        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < days.length; i++) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(days[i]);
            if(Edit==true){
                stateVO.setSelected(Initizalize_repeat(Serialized, i));
            }
            else{
                stateVO.setSelected(false);
            }

            listVOs.add(stateVO);
        }
        myAdapter = new MyAdapter(AddAlarm.this, 0,
                listVOs);
        spinner.setAdapter(myAdapter);


        int ind= repeated("readyTime") ;
        if (ind != -1)
        {
            setTime= MainActivity.toList.get(ind).timeSeconds+"";
        }



        if (MainActivity.timeArrive != null)
        {
            hours =  Integer.parseInt(MainActivity.timeArrive.split("\\:")[0]);
            minutes =  Integer.parseInt(MainActivity.timeArrive.split("\\:")[1]);


            timePicked.setText(MainActivity.timeArrive);
        }

        if (MainActivity.toList.size()>= 3)
        {
            timeReady.setText(MainActivity.toList.get(repeated("readyTime")).timeSeconds/60+"");
        }

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



        tasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmId=getIntent().getStringExtra("AlarmId");
                Intent tasksPage = new Intent(getApplicationContext(), tasks.class);
                ADC.setRepeat(myAdapter.GetSelected());
                tasksPage.putExtra("alarmDetails", AD_2.jsonToString(ADC));
                tasksPage.putExtra("AlarmId",AlarmId);
                if(Edit==true){
                    tasksPage.putExtra("EditFlag", "1");
                    tasksPage.putExtra("hours", String.valueOf(hours));
                    tasksPage.putExtra("minutes", String.valueOf(minutes));
                    tasksPage.putExtra("repeatDays",myAdapter.GetSelected());
                }
                else {
                    tasksPage.putExtra("EditFlag", "0");
                    tasksPage.putExtra("hours", String.valueOf(hours));
                    tasksPage.putExtra("minutes", String.valueOf(minutes));
                    tasksPage.putExtra("repeatDays",myAdapter.GetSelected());
                }

                boolean flag = true ;
                try {
                    Integer.parseInt(setTime);

                } catch(NumberFormatException e){
                    flag = false;
                }
                if (MainActivity.toList.size()>=2 && hours!=-1 && minutes!=-1 &&flag  ) {


                    int index = repeated ("readyTime");
                    if (index == -1) {
                        toEntry entry = new toEntry(Integer.parseInt(setTime) * 60, null, "readyTime");
                        MainActivity.toList.add(entry);
                    }
                    else
                    {
                        toEntry entry = new toEntry(Integer.parseInt(setTime) * 60, null, "readyTime");
                        MainActivity.toList.set(index,entry);
                    }
                    startActivity(tasksPage);
                }
                else
                {
                    errorText.setText("Please Make Sure You Entered All Values Correctly");
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



    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timePicked.setText(hourOfDay + " :" + minute);
        hours = hourOfDay;
        minutes = minute;
        ADC.SetTime(hourOfDay, minute);
        timeSet = true;

        MainActivity.timeArrive = hours +":"+minutes ;

    }

    private boolean Initizalize_repeat(String Serialized, int position){
        if(Serialized.charAt(position)!='1'){
            return false;
        }
        else return true;
    }

    private void Initialize(TextView text ){
        String PrefName= getString(R.string.PrefName);
        AlarmId = getIntent().getStringExtra("AlarmId");
        // List<alarmDataClass> temp_List= new ArrayList<>();
        // alarmDataClass temp;
        JSONObject temp;
        temp = AD_2.LoadSingleData(getApplicationContext(), PrefName,"Alarm"+AlarmId);

        try {
            Hour = temp.getInt("alarmHr");
            min = temp.getInt("alarmMin");
            text.setText(Hour + " :" + min);
        } catch(JSONException j){
            j.printStackTrace();
        }


    }

    int repeated (String id )
    {
        for (int i=0 ;i<MainActivity.toList.size() ;i++)
        {
            if (MainActivity.toList.get(i).id.equals(id))
            {
                return i ;
            }
        }
        return -1 ;
    }
}
