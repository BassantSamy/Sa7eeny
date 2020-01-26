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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
    String repeatDays;
    String AlarmId;
    Boolean Deleted;
    //    String PrefName;
    String ToBeDeletedID;
    String ItemID;
    String Edit="0";
    Calendar cal;
    boolean repeatFlag = false;

    static ArrayList <toEntry> toList = new ArrayList<toEntry>(); //db
    static String timeArrive = null; //db (arrival time)
    static ArrayList <chkEntry>chkDuration  = new ArrayList <chkEntry>();

    //    AlarmManager alarmManager;
//    PendingIntent pendingIntent;
    public class Triplet<T, U, V, W> {

        private final T first;
        private final U second;
        private final V third;
        private final W fourth;

        public Triplet(T first, U second, V third, W fourth) {
            this.first = first;
            this.second = second;
            this.third = third;
            this.fourth = fourth;
        }

        public T getFirst() { return first; }
        public U getSecond() { return second; }
        public V getThird() { return third; }
        public W getFourth() { return fourth; }
    }
    List<Triplet<String, PendingIntent, Calendar, Boolean>> alarmsList = new ArrayList<>();
    AccessData<listEntry> AD_1= new AccessData<>();

    String PrefName= "shared preference";

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
        //LET = new ArrayList<listEntry>();
        LET = AD_1.LoadData_listEntry(getApplicationContext(), PrefName, "AlarmList");
        if(LET== null) {
            LET = new ArrayList<>();
        }
        Intent first = getIntent();
        if(first.getExtras() != null){
            setHours = getIntent().getExtras().getString("hours");
            setMinutes = getIntent().getExtras().getString("minutes");
            repeatDays = getIntent().getExtras().getString("repeatDays");
            AlarmId = getIntent().getExtras().getString("AlarmId");
            Edit = getIntent().getExtras().getString("Edit");
            cal = Calendar.getInstance();
            Log.d("selDays",repeatDays);
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(setHours));
            cal.set(Calendar.MINUTE, Integer.parseInt(setMinutes));
            cal.set(Calendar.SECOND, 0);
            if((repeatDays.equals("0000000"))) {//here noha
                UpdateTime(cal);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
                int intentid = (int) System.currentTimeMillis();
                intent.putExtra("intentid", String.valueOf(intentid));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), intentid, intent, 0);
                if (cal.before(Calendar.getInstance())) {
                    cal.add(Calendar.DATE, 1);
                }
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                //alarmsList.add(new Triplet<>(Integer.parseInt(AlarmId), pendingIntent, cal, false));
                Toast.makeText(MainActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
                if(Edit.equals("0")) {
                    alarmsList.add(new Triplet<>(AlarmId, pendingIntent, cal, false));
                    LE = new listEntry(setTime, AlarmId);
                    LET.add(LE);
                    AD_1.saveData(LET, getApplicationContext(), PrefName, "AlarmList");
                }
//                LE = new listEntry(setTime, String.valueOf(System.currentTimeMillis()));  //this is ID
//                LET.add(LE);
            }
            Log.d("repDays",repeatDays);
            if(!repeatDays.equals("0000000")){
                if(repeatDays.charAt(0) == '1'){
                    setWeeklyAlarm(Calendar.SATURDAY);
                }
                if(repeatDays.charAt(1) == '1'){
                    setWeeklyAlarm(Calendar.SUNDAY);
                }
                if(repeatDays.charAt(2) == '1'){
                    setWeeklyAlarm(Calendar.MONDAY);
                }
                if(repeatDays.charAt(3) == '1'){
                    setWeeklyAlarm(Calendar.TUESDAY);
                }
                if(repeatDays.charAt(4) == '1'){
                    setWeeklyAlarm(Calendar.WEDNESDAY);
                }
                if(repeatDays.charAt(5) == '1'){
                    setWeeklyAlarm(Calendar.THURSDAY);
                }
                if(repeatDays.charAt(6) == '1'){
                    setWeeklyAlarm(Calendar.FRIDAY);
                }
            }
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
                ItemID = LET.get(position).getID();
                Intent editIntent = new Intent(getApplicationContext(),AddAlarm.class);
                editIntent.putExtra("EditFlag", "1");
                editIntent.putExtra("AlarmId", ItemID);

                ///LOAD FROM DB

                startActivity(editIntent);
            }

            @Override
            public void onSwitchClick(int position, CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){  //here 7aga
                    if (cal.before(Calendar.getInstance())) {
                        cal.add(Calendar.DATE, 1);
                    }
                    //this
                    if(alarmsList.size() != 0){
                        for(int i = 0; i < alarmsList.size(); i++){
                            if (alarmsList.get(i).getFirst().equals(LET.get(position).getID())){
                                AlarmManager alarmManager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                if(alarmsList.get(i).getFourth() == false){
                                    alarmManager2.setExact(AlarmManager.RTC_WAKEUP, alarmsList.get(i).getThird().getTimeInMillis(), alarmsList.get(i).getSecond());
                                }else{
                                    alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP, alarmsList.get(i).getThird().getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, alarmsList.get(i).getSecond());
                                }
                                Toast.makeText(MainActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }else{
                    //this
                    if(alarmsList.size() != 0){
                        for(int i = 0; i < alarmsList.size(); i++){
                            if (alarmsList.get(i).getFirst().equals(LET.get(position).getID())){
                                AlarmManager alarmManager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                alarmManager2.cancel(alarmsList.get(i).getSecond());
                                Toast.makeText(MainActivity.this, "ALARM OFF", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Set = new Intent(getApplicationContext(), AddAlarm.class);
                String id = String.valueOf(System.currentTimeMillis()) ;
                Set.putExtra("AlarmId", id);
                Set.putExtra("EditFlag", "0");

                 ArrayList <toEntry> toList = new ArrayList<toEntry>();
                 String timeArrive = null;

                startActivity(Set);
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position){
        if (viewHolder instanceof RVAdapter.MyViewHolder){
            final listEntry lEntry = LET.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            Deleted = true;
            ToBeDeletedID = lEntry.getID();
            //alarmManager.cancel(pendingIntent);
            //this
            if(alarmsList.size() != 0){
                for(int i = 0; i < alarmsList.size(); i++){
                    if (alarmsList.get(i).getFirst().equals(LET.get(position).getID())){
                        AlarmManager alarmManager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager2.cancel(alarmsList.get(i).getSecond());
                        Toast.makeText(MainActivity.this, "ALARM OFF", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            adapter.removeItem(deletedIndex);
            Disp(LET);

            Snackbar snack = Snackbar.make(rootLayout, "Alarm was removed!", Snackbar.LENGTH_SHORT);
            snack.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(lEntry, deletedIndex);
                    Disp(LET);
                    Deleted = false;
                }
            });
            snack.setActionTextColor(Color.YELLOW);
            snack.setBackgroundTint(getResources().getColor(R.color.darkPeach));
            snack.show();
            snack.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (Deleted == true) {
                        AD_1.remove_Data(getApplicationContext(), PrefName, ToBeDeletedID);
                    }
                }
            });
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

//    private void setAlarm(Calendar c) {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
//
//        if (c.before(Calendar.getInstance())) {
//            c.add(Calendar.DATE, 1);
//        }
//
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//    }

    private void setWeeklyAlarm(int dayOfWeek) {

//        setHours = getIntent().getExtras().getString("hours");
//        setMinutes = getIntent().getExtras().getString("minutes");
//        repeatDays = getIntent().getExtras().getString("repeatDays");
//        cal = Calendar.getInstance();
//        Log.d("selDays",repeatDays);
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
//        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(setHours));
//        cal.set(Calendar.MINUTE, Integer.parseInt(setMinutes));
//        cal.set(Calendar.SECOND, 0);
        Log.d("calNoha",String.valueOf(cal.getTimeInMillis()));
        if(cal.getTimeInMillis() < System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 7);
            Log.d("calNoha2",String.valueOf(cal.getTimeInMillis()));
        }
        UpdateTime(cal);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
        int intentid = (int)System.currentTimeMillis();
        intent.putExtra("intentid",String.valueOf(intentid));
        Log.d("intentid",String.valueOf(intentid));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), intentid, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        //alarmsList.add(new Triplet<>(LET.size(),pendingIntent,cal, true));
        Toast.makeText(MainActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
        if(Edit.equals("0")) {
            alarmsList.add(new Triplet<>(AlarmId,pendingIntent,cal, true));
            if(repeatFlag == false){
                LE = new listEntry(setTime, AlarmId);
                LET.add(LE);
                AD_1.saveData(LET, getApplicationContext(), PrefName, "AlarmList");
                repeatFlag = true;
            }
        }
//        LE = new listEntry(setTime, "am");  //this is ID
//        LET.add(LE);
        //Log.d("noha",String.valueOf((int)System.currentTimeMillis()));
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Notifications.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        //mTextView.setText("Alarm canceled");
    }

    public boolean check_date(String days){

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY: {
                if (days.charAt(1) == '1') {
                    return true;
                }
                break;
            }
            case Calendar.MONDAY: {
                if (days.charAt(2) == '1') {
                    return true;
                }
                break;
            }
            case Calendar.TUESDAY: {
                if (days.charAt(3) == '1') {
                    return true;
                }
                break;
            }
            case Calendar.WEDNESDAY: {
                if (days.charAt(4) == '1') {
                    return true;
                }
                break;
            }
            case Calendar.THURSDAY: {
                if (days.charAt(5) == '1') {
                    return true;
                }
                break;
            }
            case Calendar.FRIDAY: {
                if (days.charAt(6) == '1') {
                    return true;
                }
                break;
            }
            case Calendar.SATURDAY: {
                if (days.charAt(0) == '1') {
                    return true;
                }
                break;
            }
        }

        return false;
    }
}
