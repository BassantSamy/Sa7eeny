package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView RV;
    String [] alarms;
    String [] ampm;
    List<listEntry> LET;
    listEntry LE;
    ImageView iv;
    TextView addAlarm;
    private customAdapter adapter;
    ImageButton imgBtn ;

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
        LET = new ArrayList<listEntry>();

        for (int i = 0; i < alarms.length; i++) {
            LE = new listEntry(alarms[i], ampm[i]);
            LET.add(LE);
        }
        adapter = new customAdapter(LET, this);
        RV.setLayoutManager(new LinearLayoutManager(this));
        RV.setItemAnimator(new DefaultItemAnimator());
        RV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(RV);
        RV.setAdapter(adapter);
        Disp(LET);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Set = new Intent(getApplicationContext(), AddAlarm.class);
                startActivity(Set);
            }
        });
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            LET.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Alarm Deleted ", Toast.LENGTH_SHORT).show();
            Disp(LET);
        }
    };


    public void Disp(List<listEntry> LEt){
        if (LEt.size() == 0){
            iv.setImageResource(R.drawable.owl1);
            addAlarm.setText("No Alarms Are Currently set.\n To set an Alarm click on the Alarm button at the upper right corner.");
        }
    }

}
