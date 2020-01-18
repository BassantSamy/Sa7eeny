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

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
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
    //private customAdapter adapter;
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
        rootLayout = findViewById(R.id.root);

        LET = new ArrayList<listEntry>();

        for (int i = 0; i < alarms.length; i++) {
            LE = new listEntry(alarms[i], ampm[i]);
            LET.add(LE);
        }
        adapter = new RVAdapter(this, LET);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(this);
        RV.setLayoutManager(LM);
        RV.setItemAnimator(new DefaultItemAnimator());
        //RV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback ITH = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(ITH).attachToRecyclerView(RV);
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

}
