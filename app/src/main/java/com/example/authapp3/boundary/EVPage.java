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

import com.example.authapp3.R;
import com.example.authapp3.entity.EV;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EVPage extends AppCompatActivity {

    private AlertDialog dialog;
    private DatabaseReference userReference;
    private EditText evModelError, evColourError, evBatteryLevel;
    private int batteryStatus;
    private List<String> evModels, evColours;
    private ProgressBar progressBarEV;
    private Spinner spinnerEVModel, spinnerEVColour;
    private String userid, selectedEVModel, selectedEVColour, selectedEVBatteryLevelText, evModel, chargingStatus = "Not Charging";
    private String[] evModelsList, evColoursList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evpage);

        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(view -> EVPage.this.finish());

        TextView evModelTextView = findViewById(R.id.ev_model);
        TextView batteryPercentTextView = findViewById(R.id.battery_percent);
        ImageView batteryIconImageView = findViewById(R.id.battery_icon);

        userReference = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference evModelsReference = FirebaseDatabase.getInstance().getReference("EVModels");
        Intent intent = getIntent();
        userid = intent.getStringExtra("userID");

        Button linkEV = findViewById(R.id.link_ev_button);
        linkEV.setOnClickListener(view -> linkEVDialog());

        Button bluetoothConnect = findViewById(R.id.bluetooth_button);

        userReference.child(userid).child("EV").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EV evProfile = snapshot.getValue(EV.class);

                if(evProfile != null) {
                    evModel = evProfile.getModel();
                    batteryStatus = evProfile.getBatteryStatus();
                    String batteryStatusDisplay = batteryStatus+"%";
                    chargingStatus = evProfile.getChargeStatus();
                    evModelTextView.setText(evModel);
                    batteryPercentTextView.setText(batteryStatusDisplay);
                    if(!evProfile.isManualInput()){
                        bluetoothConnect.setText(R.string.reconnect);
                    }
                    batteryIconImageView.setImageResource(evProfile.findBatteryImage());
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
        evBatteryLevel = evPopupView.findViewById(R.id.evBatteryLevel);
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
            selectedEVBatteryLevelText = evBatteryLevel.getText().toString().trim();
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
        int selectedEVBatteryLevelInt;
        try
        {
            selectedEVBatteryLevelInt = Integer.parseInt(selectedEVBatteryLevelText);
            if(selectedEVBatteryLevelInt < 0 | selectedEVBatteryLevelInt > 100) {
                evBatteryLevel.setError("Must be an integer between 0 to 100");
                evBatteryLevel.requestFocus();
                return;
            }
        }
        catch (NumberFormatException e)
        {
            evBatteryLevel.setError("Must be an integer between 0 to 100");
            evBatteryLevel.requestFocus();
            return;
        }
        progressBarEV.setVisibility(View.VISIBLE);
        EV ev = new EV(chargingStatus, selectedEVColour, selectedEVModel, selectedEVBatteryLevelInt, true);
        userReference.child(userid).child("EV").setValue(ev);
        Toast.makeText(EVPage.this, "EV successfully linked", Toast.LENGTH_LONG).show();
        progressBarEV.setVisibility(View.GONE);
        dialog.cancel();
    }
}