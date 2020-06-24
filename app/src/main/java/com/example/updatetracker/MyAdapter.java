package com.example.updatetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context ctx;
    private ArrayList<String> versionName, versionLink,versionCode;

    public MyAdapter(Context ctx, ArrayList<String> versionName, ArrayList<String>  versionCode,ArrayList<String> versionLink){
        this.ctx = ctx;
        this.versionName = versionName;
        this.versionLink = versionLink;
        this.versionCode = versionCode;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.versionName.setText(versionName.get(position));
        holder.versionLink.setText(versionLink.get(position)+"/tag/"+versionName.get(position));
        holder.versionCode.setText("Version min API: "+versionCode.get(position));
    }

    @Override
    public int getItemCount() {
        return versionLink.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView versionName, versionLink,versionCode;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            versionName = itemView.findViewById(R.id.versionName);
            versionLink = itemView.findViewById(R.id.versionLink);
            versionCode = itemView.findViewById(R.id.versionCode);
        }
    }
}
