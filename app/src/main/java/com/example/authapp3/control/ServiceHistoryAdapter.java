package com.example.authapp3.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authapp3.boundary.serviceHistory;
import com.example.authapp3.entity.ServiceItem;
import com.example.authapp3.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ServiceHistoryAdapter extends RecyclerView.Adapter<ServiceHistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<ServiceItem> serviceItem; // import of class entity

    public ServiceHistoryAdapter(serviceHistory context, ArrayList<ServiceItem> serviceItems){
        this.context = context;
        this.serviceItem = serviceItems;
    }

    @NonNull
    @Override
    public ServiceHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerowhistory, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceHistoryAdapter.MyViewHolder holder, int position) {

        holder.hName.setText(serviceItem.get(position).getServiceName());
        holder.hProvider.setText(serviceItem.get(position).getServiceProvider());
        holder.hDateTime.setText((CharSequence) serviceItem.get(position).getServiceDate());
        holder.hRating.setText(serviceItem.get(position).getServiceRating());




    }

    @Override
    public int getItemCount() {
        return serviceItem.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView hName, hProvider, hDateTime, hRating;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            hName = itemView.findViewById(R.id.hServiceName);
            hProvider = itemView.findViewById(R.id.hServiceProvider);
            hDateTime = itemView.findViewById(R.id.hDateTime);
            hRating = itemView.findViewById(R.id.hRating);

        }
    }
}
