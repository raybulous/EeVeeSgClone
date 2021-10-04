package com.example.authapp3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileDetails extends AppCompatActivity {

    private AlertDialog dialog;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
    private EditText editTextProfile;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ProgressBar progressBarProfile;
    private String editedValue, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(view -> ProfileDetails.this.finish());

        ImageButton editName = findViewById(R.id.editNameButton);
        ImageButton editEmail = findViewById(R.id.editEmailButton);
        ImageButton editContact = findViewById(R.id.editContactButton);
        ImageButton editAddress = findViewById(R.id.editAddressButton);
        ImageButton editPassword = findViewById(R.id.editPasswordButton);

        TextView pdName = findViewById(R.id.pd_name);
        TextView pdEmail = findViewById(R.id.pd_email);
        TextView pdContact = findViewById(R.id.pd_contact);
        TextView pdAddress = findViewById(R.id.pd_address);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        String userEmail = intent.getStringExtra("userEmail");
        pdEmail.setText(userEmail);

        reference.child(userID).child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    pdName.setText(userProfile.Name);
                    if(userProfile.Contact != null) pdContact.setText(userProfile.Contact);
                    if(userProfile.Address != null) pdAddress.setText(userProfile.Address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        editName.setOnClickListener(view -> editProfileDialog(0, pdName.getText().toString().trim()));
        editContact.setOnClickListener(view -> editProfileDialog(1, pdContact.getText().toString().trim()));
        editAddress.setOnClickListener(view -> editProfileDialog(2, pdAddress.getText().toString().trim()));
    }

    private void editProfileDialog(int option, String initialValue) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View profilePopupView = getLayoutInflater().inflate(R.layout.popup_editprofile, null);
        Button save = profilePopupView.findViewById(R.id.edit_profile_save);
        Button cancel = profilePopupView.findViewById(R.id.edit_profile_cancel);

        TextView editProfileDetails = profilePopupView.findViewById(R.id.edit_profile_details);
        editTextProfile = profilePopupView.findViewById(R.id.editTextProfile);
        progressBarProfile = profilePopupView.findViewById(R.id.progressBarProfile);

        editTextProfile.setText(initialValue);
        switch (option) {
            case 0:
                editProfileDetails.setText(R.string.edit_name);
                break;
            case 1:
                editProfileDetails.setText(R.string.edit_contact);
                break;
            case 2:
                editProfileDetails.setText(R.string.edit_address);
                break;
        }

        dialogBuilder.setView(profilePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        save.setOnClickListener(view -> {
            switch (option) {
                case 0:
                    editedValue = editTextProfile.getText().toString().trim();
                    editName(initialValue);
                    break;
                case 1:
                    editedValue = editTextProfile.getText().toString().trim();
                    editContact(initialValue);
                    break;
                case 2:
                    editedValue = editTextProfile.getText().toString().trim();
                    editAddress(initialValue);
                    break;
            }
        });

        cancel.setOnClickListener(view -> dialog.cancel());
    }

    private void editName(String initialValue) {
        if(editedValue.isEmpty()){
            editTextProfile.setError("Full Name is required!");
            editTextProfile.requestFocus();
            return;
        }
        if(editedValue.equals(initialValue)) {
            editTextProfile.setError("Cannot be same as previous");
            editTextProfile.requestFocus();
            return;
        }
        progressBarProfile.setVisibility(View.VISIBLE);
        reference.child(userID).child("Profile").child("Name").setValue(editedValue);
        Toast.makeText(ProfileDetails.this, "Name successfully updated", Toast.LENGTH_LONG).show();
        progressBarProfile.setVisibility(View.GONE);
        dialog.cancel();
    }

    private void editContact(String initialValue) {
        if(editedValue.equals(initialValue)) {
            editTextProfile.setError("Cannot be same as previous");
            editTextProfile.requestFocus();
            return;
        }
        if(editedValue.isEmpty()) {
            editTextProfile.setError("Number is required");
            editTextProfile.requestFocus();
            return;
        }
        try {
            int intValue = Integer.parseInt(editedValue);
        } catch (NumberFormatException e) {
            editTextProfile.setError("Must be contact number");
            editTextProfile.requestFocus();
            return;
        }
        if(editedValue.length() != 8) {
            editTextProfile.setError("Must be length 8");
            editTextProfile.requestFocus();
            return;
        }
        if(editedValue.charAt(0) != '6' & editedValue.charAt(0) != '8' & editedValue.charAt(0) != '9') {
            editTextProfile.setError("Must start with 6, 8 or 9");
            editTextProfile.requestFocus();
            return;
        }
        progressBarProfile.setVisibility(View.VISIBLE);
        reference.child(userID).child("Profile").child("Contact").setValue(editedValue);
        Toast.makeText(ProfileDetails.this, "Contact successfully updated", Toast.LENGTH_LONG).show();
        progressBarProfile.setVisibility(View.GONE);
        dialog.cancel();
    }

    private void editAddress(String initialValue) {
        if(editedValue.isEmpty()){
            editTextProfile.setError("Address is required!");
            editTextProfile.requestFocus();
            return;
        }
        if(editedValue.equals(initialValue)) {
            editTextProfile.setError("Cannot be same as previous");
            editTextProfile.requestFocus();
            return;
        }
        progressBarProfile.setVisibility(View.VISIBLE);
        reference.child(userID).child("Profile").child("Address").setValue(editedValue);
        Toast.makeText(ProfileDetails.this, "Address successfully updated", Toast.LENGTH_LONG).show();
        progressBarProfile.setVisibility(View.GONE);
        dialog.cancel();
    }
}