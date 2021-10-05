package com.example.authapp3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private TextView banner, registerUser;
    private EditText editTextFullName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        banner = findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName = findViewById(R.id.fullName);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        progressBar = findViewById(R.id.progressBar);

    }

    public void onClick(View v){
        switch(v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextFullName.getText().toString().trim();

        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        if(name.isEmpty()){
            editTextFullName.setError("Full Name is required!");
            editTextFullName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 8){
            editTextPassword.setError("Password must have minimum 8 characters!");
            editTextPassword.requestFocus();
            return;
        }
        if (!UpperCasePatten.matcher(password).find()) {
            editTextPassword.setError("Password must have minimum 1 uppercase character!");
            editTextPassword.requestFocus();
            return;
        }
        if (!lowerCasePatten.matcher(password).find()) {
            editTextPassword.setError("Password must have minimum 1 lowercase character!");
            editTextPassword.requestFocus();
            return;
        }
        if (!digitCasePatten.matcher(password).find()) {
            editTextPassword.setError("Password must have minimum 1 digit character!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        User user = new User(name);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile")
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Rewards")
                                                .child("Points").setValue(0);
                                        Toast.makeText(RegisterUser.this, "User has been successfully registered", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        RegisterUser.this.finish();
                                    }else{
                                        Toast.makeText(RegisterUser.this, "Failed to register!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });



                    }else{
                        Toast.makeText(RegisterUser.this, "Failed to Register!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }
}