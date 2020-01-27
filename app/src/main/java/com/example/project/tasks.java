package com.example.project;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class tasks extends AppCompatActivity implements chCustomAdapter.CheckboxCheckedListener, Diag.DiagListener, CustomAdapter.imageClick{

    ListView list1;
    ListView list2;

    List<Task> Suggested;
    static List<Task> userTasks;

    String[] Suggested_arr;

    ImageButton btn;
    chCustomAdapter chadapter;
    CustomAdapter adapter;
    ImageButton set ;

    TextView Suggested_title;

    boolean cancel_f=false;


    AccessData<Task> AD_task= new AccessData<>();

    String AlarmData;
    AccessData<toEntry> AD_toEntry;
    String Edit_str;
    boolean Edit;
    TextView errorText ;
    AccessData<chkEntry> AD_ckEntry= new AccessData<>();
    AccessData<alarmDataClass> AD_C= new AccessData<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        errorText = findViewById(R.id.errorText);

        Intent in = getIntent();
        Bundle extras = in.getExtras();


        Model m= new Model(getResources());
        final String PrefName= getString(R.string.PrefName);

        Edit_str=getIntent().getStringExtra("EditFlag");
        String AlarmId=getIntent().getStringExtra("AlarmId");
        if(Edit_str.equals("1")){
            Edit=true;
        }
        else{
            Edit=false;
        }

        if(Edit==true){
            userTasks= AD_task.LoadData_Task(getApplicationContext(), PrefName, "Tasks"+AlarmId);
            Suggested= AD_task.LoadData_Task(getApplicationContext(), PrefName, "Suggested"+AlarmId);
            if(userTasks==null){
                userTasks= new ArrayList<>();
            }
            if(Suggested==null){
                Suggested= new ArrayList<>();
            }

        }
        else{
            userTasks= new ArrayList<>();
            Suggested= new ArrayList<>();
            Suggested_arr=m.GetStuff(R.array.SuggestedTasks);
            for (int i=0; i<Suggested_arr.length; i++)
            {
                Suggested.add(new Task(Suggested_arr[i]));

            }
        }

        list1= findViewById(R.id.lv_1);
        list2=findViewById(R.id.lv_2);
        final LayoutInflater factory = getLayoutInflater();
        final View SLH = factory.inflate(R.layout.second_list_header, null);

        AD_toEntry = new AccessData<>();

        set = findViewById(R.id.iconImage);
        RelativeLayout lay = findViewById(R.id.RLid);

        Suggested_title=findViewById(R.id.title_1);


        View header = getLayoutInflater().inflate(R.layout.first_list_header, null);
        View header2 = getLayoutInflater().inflate(R.layout.second_list_header, null);
        btn = header2.findViewById(R.id.btn_img);
        chadapter= new chCustomAdapter(this, R.layout.checked_list_item,Suggested);
        list1.addHeaderView(header);
        list2.addHeaderView(header2, null, false);
        list1.setAdapter(chadapter);
        //justifyListViewHeightBasedOnChildren(list1);
        list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        adapter= new CustomAdapter(this, R.layout.list_item,userTasks);
        list2.setAdapter(adapter);

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDialog(position-1,1);
//                Toast toast =new Toast(getApplicationContext());
//                toast.makeText(getApplicationContext(),"why you not working", Toast.LENGTH_LONG).show();
//                toast.show();
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String AlarmId=getIntent().getStringExtra("AlarmId");
                String hours=getIntent().getStringExtra("hours");
                String minutes=getIntent().getStringExtra("minutes");
                String repeatDays = getIntent().getStringExtra("repeatDays");

                AD_task.saveData(userTasks, getApplicationContext(), PrefName, "Tasks"+AlarmId);
                AD_task.saveData(Suggested, getApplicationContext(), PrefName, "Suggested"+AlarmId);

                String AlarmData=getIntent().getStringExtra("alarmDetails");
                AD_task.saveData_str(AlarmData, getApplicationContext(),PrefName,"Alarm"+AlarmId );

//                checkDuration chk= new checkDuration();
//                chk.execute();
                Intent Set = new Intent(getApplicationContext(), MainActivity.class);
                Set.putExtra("AlarmId",AlarmId);
                AD_C.EditAlarmList(getApplicationContext(),PrefName, AlarmId,Integer.parseInt(hours) , Integer.parseInt(minutes));
                //Set.putExtra("EditFlag","1");
                if(Edit==true){
                    Set.putExtra("Edit", "1");
                }
                else {
                    Set.putExtra("Edit", "0");
                }
                Set.putExtra("hours",hours);
                Set.putExtra("minutes",minutes);
                Set.putExtra("repeatDays",repeatDays);


                if (MainActivity.toList.size()-3 == userTasks.size()) {

                    int ii = found(AlarmId);
                    if (ii== -1 )
                    {
                        checkDuration chk = new checkDuration();
                        chkEntry chE = new chkEntry(AlarmId ,chk);
                        MainActivity.chkDuration.add(chE);
                        chk.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,AlarmId);

                    }
                    else {
                        MainActivity.chkDuration.get(ii).cd.cancel(true);
                        checkDuration chk = new checkDuration();
                        chkEntry chE = new chkEntry(AlarmId ,chk);
                        MainActivity.chkDuration.set(ii,chE);
                        chk.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,AlarmId);
                    }

                    checkTimes();
                    startActivity(Set);

                }
                else
                {
                    errorText.setText("Please Make Sure You Entered All Values");
                }

                AD_toEntry.saveData(MainActivity.toList, getApplicationContext(),PrefName, "toList"+AlarmId);
                AD_ckEntry.saveData_str(MainActivity.timeArrive, getApplicationContext(), PrefName, "timeArrive"+AlarmId);
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean addF= true;
                for (int i=0; i<userTasks.size();i++){
                    if(userTasks.get(i).getName().equals("  "))
                    {
                        addF=false;
                    }
                }
                if(addF==true) {
                    adapter.notifyDataSetChanged();
                }
                openDialog();
            }

        });


        chadapter.setCheckedListener(this);
        adapter.setPressedListener(this);
    }

    @Override

    public void getCheckboxCheckedListener(int position)
    {

        // openDialog(position, 0);
        userTasks.add(new Task(Suggested.get(position).getName(),0,0));
        Suggested.remove(position);
        list1.removeAllViewsInLayout();
        chadapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        int h = justifyListViewHeightBasedOnChildren(list1);
        Log.d("height",String.valueOf(h));
        if(Suggested.isEmpty()==true) {
            if (h<=0) {
                Log.d("height",String.valueOf(h));
                RelativeLayout.LayoutParams mParam2 = new RelativeLayout.LayoutParams(0, 200);
                list1.setLayoutParams(mParam2);
            }else{
                RelativeLayout.LayoutParams mParam = new RelativeLayout.LayoutParams(0, h);
                list1.setLayoutParams(mParam);
            }
            chadapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getPressedListener(int position){
        String name = userTasks.get(position).getName();

        String lastWord = name.substring(name.lastIndexOf(" ")+1);
        lastWord =lastWord.toLowerCase();

        int index = repeated(lastWord);
        if (index != -1) {
            MainActivity.toList.remove(index);
        }
        userTasks.remove(position);
        adapter.notifyDataSetChanged();



    }

    public void openDialog(){
        Diag task_diag= new Diag();
        Bundle bundle = new Bundle();
        bundle.putString("Edit", "0");
        task_diag.setArguments(bundle);
        task_diag.show(getSupportFragmentManager(),"Add task dialog");


    }

    public  void openDialog(int position, int mode)
    {
        Diag task_diag= new Diag();
        Bundle bundle = new Bundle();
        if(mode==0) { // first list
            bundle.putString("TaskToEdit", Suggested.get(position).getName());
            bundle.putString("hours", String.valueOf(Suggested.get(position).getDuration_hr()));
            bundle.putString("minutes", String.valueOf(Suggested.get(position).getDuration_min()));
            bundle.putString("Edit", "1");
        }
        else if(mode==1) {// second list
            bundle.putString("TaskToEdit", userTasks.get(position).getName());
            bundle.putString("hours", String.valueOf(userTasks.get(position).getDuration_hr()));
            bundle.putString("minutes", String.valueOf(userTasks.get(position).getDuration_min()));
            bundle.putString("Edit", "1");
        }
        task_diag.setArguments(bundle);
        task_diag.show(getSupportFragmentManager(),"Add task dialog");

    }
    @Override
    public void applytexts(String task, String hour, String minute){
        userTasks.add(new Task(task, Integer.parseInt(hour),Integer.parseInt(minute)));
        adapter.notifyDataSetChanged();

    }

//    @Override
//        public  void C_flag(boolean f){
//            cancel_f=f;
//    }

    @Override
    public void applytexts_edit(String task_orig,String task, String hour, String minute){
        Task t= new Task("m",1,0);
//        int position= userTasks.indexOf(t);
        int position=0;
        Log.d("original string", task_orig);
        Log.d("change to string", task);
        Log.d("array length", String.valueOf(userTasks.size()));
        //Log.d("position", String.valueOf(position));
        for(Task d : userTasks){
            if(d.getName() != null && d.getName().equals(task_orig)){
                Log.d("position", String.valueOf(position));
                userTasks.get(position).setDuration_hr(Integer.parseInt(hour));
                userTasks.get(position).setDuration_min(Integer.parseInt(minute));
                userTasks.get(position).SetName(task);
                adapter.notifyDataSetChanged();
            }
            else
                position++;
        }
    }

    public static int justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return 0;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
        return par.height;
    }

    void checkTimes() {
        for (int i = 0; i < userTasks.size(); i++) {

            int time2  = 0 ;
            int time1 = 0;

            time1 = tasks.userTasks.get(i).Duration_hr * 60 * 60 + tasks.userTasks.get(i).Duration_min * 60; //ingex from tasks

            String name = userTasks.get(i).getName();
            String lastWord = name.substring(name.lastIndexOf(" ") + 1);
            lastWord = lastWord.toLowerCase();

            int index = repeated(lastWord);
            if (index != -1) {
                time2 = MainActivity.toList.get(index).timeSeconds;
            }

            if (time1 != time2) {
                toEntry entry = new toEntry(time1, MainActivity.toList.get(index).latLng, MainActivity.toList.get(index).id);
                MainActivity.toList.set(index, entry);
            }

        }
    }

    int found (String id)
    {
        for (int i=0 ;i<MainActivity.chkDuration.size() ;i++)
        {
            if (MainActivity.chkDuration.get(i).alarmId.equals(id) )
            {
                return i ;
            }
        }
        return -1 ;
    }


    int repeated (String id)
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