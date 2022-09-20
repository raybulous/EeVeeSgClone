package com.example.authapp3.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authapp3.R;
import com.example.authapp3.control.ServiceItemAdapter;
import com.example.authapp3.entity.EV;
import com.example.authapp3.entity.ServiceItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExplorePage extends AppCompatActivity implements View.OnClickListener{

    ArrayList<ServiceItem> serviceItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_listv); // u

        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);

        setupServiceItem();

        ServiceItemAdapter adapter = new ServiceItemAdapter(this, serviceItem);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void setupServiceItem() {
        String[] serviceItemNames = getResources().getStringArray(R.array.serviceitemname);
        String[] serviceItemArea = getResources().getStringArray(R.array.svcarea);

        for (int i = 0; i < serviceItemNames.length; i++) {
            serviceItem.add(new ServiceItem(serviceItemNames[i], serviceItemArea[i]));
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
