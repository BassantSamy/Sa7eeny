package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class customAdapter extends RecyclerView.Adapter<customAdapter.ViewHolder> {

    private List<listEntry> listEntries;
    private Context cxt;

    public customAdapter(List<listEntry> listEntries, Context cxt) {
        this.listEntries = listEntries;
        this.cxt = cxt;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry, parent,  false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        listEntry le = listEntries.get(position);
        holder.TV1.setText(le.getTime());
        holder.TV2.setText(le.getAmpm());

    }

    @Override
    public int getItemCount() {
        return listEntries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView TV1;
        public TextView TV2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TV1 = itemView.findViewById(R.id.TV1);
            TV2 = itemView.findViewById(R.id.TV2);
        }
    }

}