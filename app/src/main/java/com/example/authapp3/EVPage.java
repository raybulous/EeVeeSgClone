package com.example.authapp3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EVPage extends AppCompatActivity {

    private String[] evModelsList, evColoursList;
    private List<String> evModels, evColours;
    private AlertDialog dialog;
    private ProgressBar progressBarEV;
    private String userid, selectedEVModel, selectedEVColour, evModel, chargingStatus = "Not Charging";
    private int batteryStatus = 100;
    private DatabaseReference userReference;
    private Spinner spinnerEVModel, spinnerEVColour;
    private EditText evModelError, evColourError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evpage);

        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(view -> EVPage.this.finish());

        TextView evModelTextView = findViewById(R.id.ev_model);
        TextView batteryPercentTextView = findViewById(R.id.battery_percent);

        userReference = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference evModelsReference = FirebaseDatabase.getInstance().getReference("EVModels");
        Intent intent = getIntent();
        userid = intent.getStringExtra("userID");

        Button linkEV = findViewById(R.id.link_ev);
        linkEV.setOnClickListener(view -> linkEVDialog());

        userReference.child(userid).child("EV").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EV evProfile = snapshot.getValue(EV.class);

                if(evProfile != null) {
                    evModel = evProfile.Model;
                    batteryStatus = evProfile.BatteryStatus;
                    String batteryStatusDisplay = batteryStatus+"%";
                    chargingStatus = evProfile.ChargeStatus;
                    evModelTextView.setText(evModel);
                    batteryPercentTextView.setText(batteryStatusDisplay);
                    linkEV.setText(R.string.relink_ev);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        evModels = new ArrayList<>();
        evModelsReference.child("ModelList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                evModels.clear();
                evModels.add("Model");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String model = postSnapshot.getValue(String.class);
                    evModels.add(model);
                }
                evModelsList = evModels.toArray(new String[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        evColours = new ArrayList<>();
        evModelsReference.child("ColourList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                evColours.clear();
                evColours.add("Colour");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String colour = postSnapshot.getValue(String.class);
                    evColours.add(colour);
                }
                evColoursList = evColours.toArray(new String[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void linkEVDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View evPopupView = getLayoutInflater().inflate(R.layout.popup_linkev, null);
        Button save = evPopupView.findViewById(R.id.link_ev_save);
        Button cancel = evPopupView.findViewById(R.id.link_ev_cancel);

        evModelError = evPopupView.findViewById(R.id.evModelError);
        evColourError = evPopupView.findViewById(R.id.evColourError);
        progressBarEV = evPopupView.findViewById(R.id.progressBarEV);

        spinnerEVModel = evPopupView.findViewById(R.id.spinnerEVModel);

        ArrayAdapter<String> adapterEVModel = new ArrayAdapter<>(EVPage.this, android.R.layout.simple_spinner_item, evModelsList);
        adapterEVModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEVModel.setAdapter(adapterEVModel);

        spinnerEVColour = evPopupView.findViewById(R.id.spinnerEVColour);

        ArrayAdapter<String> adapterEVColour = new ArrayAdapter<>(EVPage.this, android.R.layout.simple_spinner_item, evColoursList);
        adapterEVColour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEVColour.setAdapter(adapterEVColour);

        dialogBuilder.setView(evPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        save.setOnClickListener(view -> {
            selectedEVModel = spinnerEVModel.getSelectedItem().toString();
            selectedEVColour = spinnerEVColour.getSelectedItem().toString();
            checkAndLink();
        });

        cancel.setOnClickListener(view -> dialog.cancel());
    }

    private void checkAndLink() {
        evModelError.setError(null);
        evColourError.setError(null);
        if(selectedEVModel.equals("Model")) {
            evModelError.setError("Please select model!");
            evModelError.requestFocus();
            return;
        }
        if(selectedEVColour.equals("Colour")) {
            evColourError.setError("Please select colour!");
            evColourError.requestFocus();
            return;
        }
        progressBarEV.setVisibility(View.VISIBLE);
        EV ev = new EV(chargingStatus, selectedEVColour, selectedEVModel, batteryStatus);
        userReference.child(userid).child("EV").setValue(ev);
        Toast.makeText(EVPage.this, "EV successfully linked", Toast.LENGTH_LONG).show();
        progressBarEV.setVisibility(View.GONE);
        dialog.cancel();
    }
}