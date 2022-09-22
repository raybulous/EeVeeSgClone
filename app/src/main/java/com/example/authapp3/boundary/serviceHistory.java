package com.example.authapp3.boundary;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authapp3.R;
import com.example.authapp3.control.ServiceHistoryAdapter;
import com.example.authapp3.entity.ServiceItem;

import java.util.ArrayList;

public class serviceHistory extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ServiceItem> serviceItem = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.servicehistory);

        RecyclerView rView = findViewById(R.id.recycleHistory);

        setupServiceHistory();


        RecyclerView.Adapter adapter = new ServiceHistoryAdapter(this,serviceItem);

        rView.setAdapter(adapter);
        rView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setupServiceHistory() {
        String[] serviceItemNames = getResources().getStringArray(R.array.serviceitemname);
        String[] serviceItemProvider= getResources().getStringArray(R.array.svcprovider);
        String[] serviceItemDate = getResources().getStringArray(R.array.svcdatetime);
        String[] serviceItemRating = getResources().getStringArray(R.array.svcrating);

        //firebase ???

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
