package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.Maps;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {


    private RecyclerView RV;
    private List<listEntry> LET;
    private RVAdapter adapter;
    private CoordinatorLayout rootLayout;
    String [] alarms;
    String [] ampm;
    listEntry LE;
    ImageView iv;
    TextView addAlarm;
    Switch aSwitch;
    ImageButton imgBtn;
    String setTime="";
    String setHours;
    String setMinutes;
    static ArrayList <toEntry> toList = new ArrayList<toEntry>(); //db
    static String timeArrive = null; //db (arrival time)
    static ArrayList <chkEntry>chkDuration  = new ArrayList <chkEntry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imgBtn= findViewById(R.id.searchImageButton);
        RV = findViewById(R.id.RV);
        iv = findViewById(R.id.sleepy);
        addAlarm = findViewById(R.id.addalarm);
        alarms = getResources().getStringArray(R.array.alarms);
        ampm = getResources().getStringArray(R.array.ampm);
        rootLayout = findViewById(R.id.root);
        aSwitch = findViewById(R.id.switch1);
        LET = new ArrayList<listEntry>();
        Intent first = getIntent();
        if(first.getExtras() != null){
            setHours = getIntent().getExtras().getString("hours");
            setMinutes = getIntent().getExtras().getString("minutes");
            Calendar cal = Calendar.getInstance();
            Log.d("hour",setHours);
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(setHours));
            cal.set(Calendar.MINUTE, Integer.parseInt(setMinutes));
            cal.set(Calendar.SECOND, 0);
            UpdateTime(cal);
            setAlarm(cal);
            LE = new listEntry(setTime, "am");
            LET.add(LE);
        }



//        for (int i = 0; i < alarms.length; i++) {
//            LE = new listEntry(alarms[i], ampm[i]);
//            LET.add(LE);
//        }
        adapter = new RVAdapter(this, LET);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(this);
        RV.setLayoutManager(LM);
        RV.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.SimpleCallback ITH = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(ITH).attachToRecyclerView(RV);
        RV.setAdapter(adapter);
        Disp(LET);

        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int id =0 ;
                Intent editIntent = new Intent(getApplicationContext(),AddAlarm.class);
                editIntent.putExtra("alarmId", id);
                //LOAD FROM DB NEW ARRAY LIST
                startActivity(editIntent);
            }

            @Override
            public void onSwitchClick(int position) {

            }
        });

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id =0 ;
                Intent Set = new Intent(getApplicationContext(), AddAlarm.class);
                Set.putExtra("alarmId", id);
//                ArrayList <toEntry> toList = new ArrayList<toEntry>(); //db
//                String timeArrive = null; //db (arrival time)
                startActivity(Set);
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position){
        if (viewHolder instanceof RVAdapter.MyViewHolder){
            final listEntry lEntry = LET.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(deletedIndex);
            Disp(LET);

            Snackbar snack = Snackbar.make(rootLayout, "Alarm was removed!", Snackbar.LENGTH_LONG);
            snack.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(lEntry, deletedIndex);
                    Disp(LET);
                }
            });
            snack.setActionTextColor(Color.YELLOW);
            snack.setBackgroundTint(getResources().getColor(R.color.darkPeach));
            snack.show();
        }
    }

    public void Disp(List<listEntry> LEt){
        if (LEt.size() == 0){
            iv.setImageResource(R.drawable.owl1);
            addAlarm.setText("No Alarms Are Currently set.\n To set an Alarm click on the Alarm button at the upper right corner.");
        }else
        {
            iv.setImageDrawable(null);
            addAlarm.setText("");
        }
    }

    private void UpdateTime(Calendar c) {

        setTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
    }

    private void setAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Notifications.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        //mTextView.setText("Alarm canceled");
    }
}
