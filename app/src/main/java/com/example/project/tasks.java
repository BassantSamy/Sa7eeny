package com.example.project;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class tasks extends AppCompatActivity implements chCustomAdapter.CheckboxCheckedListener, Diag.DiagListener, CustomAdapter.imageClick{

    ListView list1;
    ListView list2;

    List<Task> Suggested;
    List<Task> userTasks;

    String[] Suggested_arr;

    ImageButton btn;
    chCustomAdapter chadapter;
    CustomAdapter adapter;

    TextView Suggested_title;

    boolean cancel_f=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        Model m= new Model(getResources());
        list1= findViewById(R.id.lv_1);
        list2=findViewById(R.id.lv_2);
        btn=findViewById(R.id.btn_img);

        Suggested_title=findViewById(R.id.title_1);


        Suggested= new ArrayList<>();
        Suggested_arr=m.GetStuff(R.array.SuggestedTasks);
        for (int i=0; i<Suggested_arr.length; i++)
        {
            Suggested.add(new Task(Suggested_arr[i]));
            Log.d("da5alt",String.valueOf(Suggested.size()));
        }

        chadapter= new chCustomAdapter(this, R.layout.checked_list_item,Suggested);
        list1.setAdapter(chadapter);
        list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        userTasks= new ArrayList<>();
        adapter= new CustomAdapter(this, R.layout.list_item,userTasks);
        list2.setAdapter(adapter);

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDialog(position,1);
                Log.d("hna", "msh sha3'al leih");
//                Toast toast =new Toast(getApplicationContext());
//                toast.makeText(getApplicationContext(),"why you not working", Toast.LENGTH_LONG).show();
//                toast.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                boolean addF= true;
//                for (int i=0; i<userTasks.size();i++){
//                    if(userTasks.get(i).getName().equals("  "))
//                    {
//                        addF=false;
//                    }
//                }
//                if(addF==true) {
//                    userTasks.add(new Task("  "));
//                    adapter.notifyDataSetChanged();
//                }
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
        chadapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        if(Suggested.isEmpty()==true)
            Suggested_title.setVisibility(View.GONE);
    }

    @Override
    public void getPressedListener(int position){
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
}
