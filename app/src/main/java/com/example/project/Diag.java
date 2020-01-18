package com.example.project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

public class Diag extends AppCompatDialogFragment {

   private TextView et;
    private TextView et_hr;
   private TextView et_min;
   private String task_before_change;
   private DiagListener diagListener;




    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.diag, null);

       final EditText et=view.findViewById(R.id.task_text_diag);
       final EditText et_hr=view.findViewById(R.id.duration_hr_text_diag);
        final EditText et_min=view.findViewById(R.id.txt_min_diag);
        final TextView temp= view.findViewById(R.id.error);

        Bundle bundle = getArguments();
        final String flag = bundle.getString("Edit");
        if(flag.equals("1")){

            String task_toedit=bundle.getString("TaskToEdit");
            task_before_change=task_toedit;
            String hours=bundle.getString("hours");
            String minutes=bundle.getString("minutes");
            et.setText(task_toedit);
            et_hr.setText(hours);
            et_min.setText(minutes);
        }


        builder.setView(view).setTitle("Add Task").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

//        builder.setView(view);
        final AlertDialog dialog= builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TaskName=et.getText().toString();
                String hour=et_hr.getText().toString();
                String minute= et_min.getText().toString();
                Log.d("hour", "z"+hour+"z");

                if(TaskName.isEmpty()||hour.isEmpty()||minute.isEmpty()){

                    temp.setText("Please Enter a valid Duration / Task name");
                    Log.d("hnaaaa","hn");
                }
                else {
                    if (Integer.parseInt(hour) > 24 || Integer.parseInt(minute) > 60) {
                        temp.setText("Please Enter a valid Duration");
//                   Toast.makeText(MainActivity.getContext(),"Please Enter a valid Duration", Toast.LENGTH_LONG).show();
                    } else {

                        if (flag.equals("0")) {
                            diagListener.applytexts(TaskName, hour, minute);
                        } else
                            diagListener.applytexts_edit(task_before_change, TaskName, hour, minute);
                        dialog.dismiss();

                    }
                }
            }
        });
     //   return builder.create();
       // return null;
        return dialog;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            diagListener = (DiagListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+"must implemenet listener for dialog");
        }
    }
    public interface DiagListener{
       void applytexts(String task, String hour, String minute);
       void applytexts_edit(String task_orig, String task, String hour, String minute);
//       void C_flag(boolean f);
    }

}
