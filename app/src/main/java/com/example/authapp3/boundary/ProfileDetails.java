package com.example.authapp3.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

import com.example.authapp3.R;
import com.example.authapp3.control.Profile;
import com.example.authapp3.entity.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class    ProfileDetails extends AppCompatActivity {

    private AlertDialog dialog;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
    private EditText editTextProfile, editTextSensitive, editTextSensitiveNew, editTextSensitiveConfirm;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ProgressBar progressBarProfile, progressBarSensitive;
    private String editedValue, sensitive1, sensitive2, sensitive3, userID, userEmail;
    private TextView pdEmail;

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
        TextView pdContact = findViewById(R.id.pd_contact);
        TextView pdAddress = findViewById(R.id.pd_address);

        pdEmail = findViewById(R.id.pd_email);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userEmail = user.getEmail();
        pdEmail.setText(userEmail);

        reference.child(userID).child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    pdName.setText(userProfile.getName());
                    if(userProfile.getContact() != null) pdContact.setText(userProfile.getContact());
                    if(userProfile.getAddress() != null) pdAddress.setText(userProfile.getAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        editName.setOnClickListener(view -> editProfileDialog(0, pdName.getText().toString().trim()));
        editEmail.setOnClickListener(view -> editSensitiveDialog(0, userEmail));
        editContact.setOnClickListener(view -> editProfileDialog(1, pdContact.getText().toString().trim()));
        editAddress.setOnClickListener(view -> editProfileDialog(2, pdAddress.getText().toString().trim()));
        editPassword.setOnClickListener(view -> editSensitiveDialog(1, userEmail));
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
                editTextProfile.setHint(R.string.name);
                break;
            case 1:
                editProfileDetails.setText(R.string.edit_contact);
                editTextProfile.setHint(R.string.contact);
                break;
            case 2:
                editProfileDetails.setText(R.string.edit_address);
                editTextProfile.setHint(R.string.address);
                break;
        }

        dialogBuilder.setView(profilePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        save.setOnClickListener(view -> {
            editedValue = editTextProfile.getText().toString().trim();
            editProfile(option, initialValue);
        });

        cancel.setOnClickListener(view -> dialog.cancel());
    }

    private void editSensitiveDialog(int option, String initialEmail) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View sensitivePopupView = getLayoutInflater().inflate(R.layout.popup_editsensitive, null);
        Button save = sensitivePopupView.findViewById(R.id.edit_sensitive_save);
        Button cancel = sensitivePopupView.findViewById(R.id.edit_sensitive_cancel);

        TextView editSensitiveDetails = sensitivePopupView.findViewById(R.id.edit_sensitive_details);
        editTextSensitive = sensitivePopupView.findViewById(R.id.editTextSensitive);
        editTextSensitiveNew = sensitivePopupView.findViewById(R.id.editTextSensitiveNew);
        editTextSensitiveConfirm = sensitivePopupView.findViewById(R.id.editTextSensitiveConfirm);
        progressBarSensitive = sensitivePopupView.findViewById(R.id.progressBarSensitive);

        if(option==0) {
            editSensitiveDetails.setText(R.string.change_email);
            editTextSensitive.setHint(R.string.password);
            editTextSensitiveNew.setHint(R.string.new_email);
            editTextSensitiveNew.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            editTextSensitiveConfirm.setHint(R.string.confirm_email);
            editTextSensitiveConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }

        dialogBuilder.setView(sensitivePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        save.setOnClickListener(view -> {
            sensitive1 = editTextSensitive.getText().toString().trim();
            sensitive2 = editTextSensitiveNew.getText().toString().trim();
            sensitive3 = editTextSensitiveConfirm.getText().toString().trim();
            switch (option) {
                case 0:
                    changeEmail(initialEmail);
                    break;
                case 1:
                    changePassword(initialEmail);
                    break;
            }
        });

        cancel.setOnClickListener(view -> dialog.cancel());
    }

    private void editProfile(int option, String initialValue) {
        String check, update, display;
        switch (option) {
            case 0:
                check = Profile.checkName(editedValue, initialValue);
                update = "name";
                break;
            case 1:
                check = Profile.checkContact(editedValue, initialValue);
                update = "contact";
                break;
            case 2:
                check = Profile.checkAddress(editedValue, initialValue);
                update = "address";
                break;
            default:
                check = "No Error";
                update = "";
        }
        if(!check.equals("No Error")){
            editTextProfile.setError(check);
            editTextProfile.requestFocus();
            return;
        }
        display = update+" successfully updated";
        progressBarProfile.setVisibility(View.VISIBLE);
        reference.child(userID).child("Profile").child(update).setValue(editedValue);
        Toast.makeText(ProfileDetails.this, display, Toast.LENGTH_LONG).show();
        progressBarProfile.setVisibility(View.GONE);
        dialog.cancel();
    }

    private void changeEmail(String initialEmail) {
        String passwordCheck = Profile.checkPassword(sensitive1);
        if(!passwordCheck.equals("No Error")){
            editTextSensitive.setError(passwordCheck);
            editTextSensitive.requestFocus();
            return;
        }
        if(sensitive2.equals(initialEmail)){
            editTextSensitiveNew.setError("New email cannot be same as old email");
            editTextSensitiveNew.requestFocus();
            return;
        }
        String emailCheck;
        emailCheck = Profile.checkEmail(sensitive2);
        if(!emailCheck.equals("No Error")){
            editTextSensitiveNew.setError(emailCheck);
            editTextSensitiveNew.requestFocus();
            return;
        }
        if(!(sensitive2.equals(sensitive3))){
            editTextSensitiveNew.setError("Emails do not match");
            editTextSensitiveConfirm.setError("Emails do not match");
            editTextSensitiveNew.requestFocus();
            return;
        }
        emailCheck = Profile.checkEmail(sensitive3);
        if(!emailCheck.equals("No Error")){
            editTextSensitiveConfirm.setError(emailCheck);
            editTextSensitiveConfirm.requestFocus();
            return;
        }

        progressBarSensitive.setVisibility(View.VISIBLE);

        AuthCredential credential = EmailAuthProvider.getCredential(initialEmail, sensitive1);
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                user.updateEmail(sensitive2).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(ProfileDetails.this, "Email successfully changed", Toast.LENGTH_SHORT).show();
                        progressBarSensitive.setVisibility(View.GONE);
                        userEmail = sensitive2;
                        pdEmail.setText(userEmail);
                        dialog.cancel();
                    }else{
                        Toast.makeText(ProfileDetails.this, "Failed to update!", Toast.LENGTH_LONG).show();
                        progressBarSensitive.setVisibility(View.GONE);
                    }
                });
            }else{
                editTextSensitive.setError("Wrong password!");
                editTextSensitive.requestFocus();
                progressBarSensitive.setVisibility(View.GONE);
            }
        });

    }

    private void changePassword(String initialEmail) {
        String passwordCheck;
        passwordCheck = Profile.checkPassword(sensitive1);
        if(!passwordCheck.equals("No Error")){
            editTextSensitive.setError(passwordCheck);
            editTextSensitive.requestFocus();
            return;
        }
        if(sensitive2.equals(sensitive1)){
            editTextSensitiveNew.setError("New password cannot be same as old password");
            editTextSensitiveNew.requestFocus();
            return;
        }
        passwordCheck = Profile.checkPassword(sensitive2);
        if(!passwordCheck.equals("No Error")){
            editTextSensitiveNew.setError(passwordCheck);
            editTextSensitiveNew.requestFocus();
            return;
        }
        if(!(sensitive2.equals(sensitive3))){
            editTextSensitiveNew.setError("New passwords do not match");
            editTextSensitiveConfirm.setError("New passwords do not match");
            editTextSensitiveNew.requestFocus();
            return;
        }
        passwordCheck = Profile.checkPassword(sensitive3);
        if(!passwordCheck.equals("No Error")){
            editTextSensitiveConfirm.setError(passwordCheck);
            editTextSensitiveConfirm.requestFocus();
            return;
        }

        progressBarSensitive.setVisibility(View.VISIBLE);

        AuthCredential credential = EmailAuthProvider.getCredential(initialEmail, sensitive1);
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                user.updatePassword(sensitive2).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(ProfileDetails.this, "Password successfully changed", Toast.LENGTH_LONG).show();
                        progressBarSensitive.setVisibility(View.GONE);
                        dialog.cancel();
                    }else{
                        Toast.makeText(ProfileDetails.this, "Failed to update!", Toast.LENGTH_LONG).show();
                        progressBarSensitive.setVisibility(View.GONE);
                    }
                });
            }else{
                editTextSensitive.setError("Wrong password!");
                editTextSensitive.requestFocus();
                progressBarSensitive.setVisibility(View.GONE);
            }
        });
    }
}