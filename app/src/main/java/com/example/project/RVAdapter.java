package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder>{
    private Context context;
    private List<listEntry> LEt;


    public RVAdapter(Context context, List<listEntry> cartList) {
        this.context = context;
        this.LEt = cartList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final listEntry item = LEt.get(position);
        holder.TV1.setText(item.getTime());
        holder.TV2.setText(item.getAmpm());
    }

    @Override
    public int getItemCount() {
        return LEt.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TV1, TV2;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View itemView) {
            super(itemView);
            TV1 = itemView.findViewById(R.id.TV1);
            TV2 = itemView.findViewById(R.id.TV2);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }

    public void removeItem(int position) {
        LEt.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(listEntry item, int position) {
        LEt.add(position, item);
        notifyItemInserted(position);
    }
}
