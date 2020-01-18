package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class chCustomAdapter  extends ArrayAdapter<Task> {

    Context ctx;
    int rsc;
    List<Task> t;
    private CheckboxCheckedListener checkedListener;

    public chCustomAdapter( Context ctx, int resource, List<Task> t) {
        super(ctx, resource, t);
        this.ctx=ctx;
        this.rsc=resource;
        this.t=t;
    }

    class MyViewHolder
    {
        CheckBox task_name;


        MyViewHolder(View v)
        {
            task_name=v.findViewById(R.id.task_text_ch);
        }
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater= LayoutInflater.from(ctx);
//        View view=inflater.inflate(rsc, null);
//        CheckedTextView txtview= view.findViewById(R.id.task_text_ch);
//        EditText hr_txt_view=view.findViewById(R.id.duration_hr_text_ch);
//        EditText min_txt_view=view.findViewById(R.id.duration_min_text_ch);
//
//
//        Task t_single= t.get(position);
//
//        txtview.setText(t_single.getName());
//        hr_txt_view.setText(t_single.getDuration_hr());
//        min_txt_view.setText(t_single.getDuration_min());
//        LayoutInflater inflater= LayoutInflater.from(ctx);
//        View view=inflater.inflate(rsc, null);
//        TextView txtview= view.findViewById(R.id.task_text_ch);
//        Task t_single= t.get(position);
//        txtview.setText(t_single.getName());
//        return view;

        View row=convertView;
        MyViewHolder holder=null;
        if(row==null)
        {
            LayoutInflater inflater=(LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.checked_list_item,parent, false);
            holder=new MyViewHolder(row);
            row.setTag(holder);
        }
        else{
            holder = (MyViewHolder) row.getTag();
        }
        holder.task_name.setText(t.get(position).getName());

     holder.task_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
             if(checkedListener!=null){
                 checkedListener.getCheckboxCheckedListener(position);
             }
         }

     });
        return row;
    }
public interface CheckboxCheckedListener{

        void getCheckboxCheckedListener(int position);

    }

    public void setCheckedListener(CheckboxCheckedListener checkedListener)
    {
        this.checkedListener=checkedListener;
    }



}
