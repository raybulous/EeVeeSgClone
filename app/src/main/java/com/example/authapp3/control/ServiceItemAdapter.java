package com.example.authapp3.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authapp3.R;
import com.example.authapp3.boundary.ExplorePage;
import com.example.authapp3.entity.ServiceItem;

import java.util.ArrayList;

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<ServiceItem> serviceItem;

    public ServiceItemAdapter(ExplorePage context, ArrayList<ServiceItem> serviceItem){
        this.context= context;
        this.serviceItem = serviceItem;
    }

    @NonNull
    @Override
    public ServiceItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerow, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceItemAdapter.MyViewHolder holder, int position) {
        //assign value to views
        //based on pos of recycle view

        holder.tvName.setText(serviceItem.get(position).getServiceName());
        holder.tvArea.setText(serviceItem.get(position).getServiceArea());

    }

    @Override
    public int getItemCount() {
        return serviceItem.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvArea;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.recycleServiceName);
            tvName = itemView.findViewById(R.id.recycleArea);

        }
    }
}
