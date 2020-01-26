package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.collect.Maps;

import org.w3c.dom.Text;

import java.util.List;

import javax.microedition.khronos.opengles.GL;


public class CustomAdapter extends ArrayAdapter<Task>{
    Context ctx;
    int rsc;
    List<Task> t;
    private imageClick pressListener;

    public interface imageClick {
        void getPressedListener(int position);

    }

    class MyViewHolder_c
    {

        TextView task_text;
        TextView duration_hr_text;
        TextView txt_min;
        ImageButton img_btn;
        ImageButton mapsbtn;


        MyViewHolder_c(View v)
        {
            task_text=v.findViewById(R.id.task_text);
            duration_hr_text=v.findViewById(R.id.duration_hr_text);
            txt_min=v.findViewById(R.id.txt_min);
            img_btn=v.findViewById(R.id.btn_img_2);
            mapsbtn=v.findViewById(R.id.mapsButton);

        }
    }


    public CustomAdapter (Context ctx , int resource , List <Task> t)
    {
        super(ctx, resource,t);
        this.ctx=ctx;
        this.rsc=resource;
        this.t=t;
    }

    @NonNull
    @Override

    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        MyViewHolder_c holder=null;
        if(row==null)
        {
            LayoutInflater inflater=(LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.list_item,parent, false);
            holder=new MyViewHolder_c(row);
            row.setTag(holder);
        }
        else{
            holder = (MyViewHolder_c) row.getTag();
        }
        holder.task_text.setText(t.get(position).getName());
        holder.duration_hr_text.setText(String.valueOf(t.get(position).getDuration_hr()));
        holder.txt_min.setText(String.valueOf(t.get(position).getDuration_min()));

        holder.img_btn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(pressListener!=null) {
                    pressListener.getPressedListener(position);
                }
            }
        });

        holder.mapsbtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = t.get(position).getName();
                String lastWord = name.substring(name.lastIndexOf(" ")+1);
                lastWord = lastWord.toLowerCase();


                Intent Maps = new Intent(getContext(), MapsActivity.class);
                Maps.putExtra("name", name);
                int time = tasks.userTasks.get(tasks.userTasks.size()-1).Duration_hr*60*60 +tasks.userTasks.get(tasks.userTasks.size()-1).Duration_min*60 ;
                Maps.putExtra("time", time);

                ctx.startActivity(Maps);
            }
        });

        return row;
    }
    public void setPressedListener(imageClick pressedListener)
    {
        this.pressListener=pressedListener;
    }

}
