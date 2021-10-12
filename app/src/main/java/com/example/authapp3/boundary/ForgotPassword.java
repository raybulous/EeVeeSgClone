package com.example.authapp3.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.authapp3.R;
import com.example.authapp3.control.Profile;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetPasswordButton;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = (EditText) findViewById(R.id.email);
        resetPasswordButton = (Button) findViewById(R.id.resetPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(view -> resetPassword());
    }

    private void resetPassword(){
        String email = emailEditText.getText().toString().trim();

        String emailCheck = Profile.checkEmail(email);
        if(!emailCheck.equals("No Error")){
            emailEditText.setError(emailCheck);
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.GONE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(ForgotPassword.this, "Check your email to reset your password",Toast.LENGTH_LONG).show();
                ForgotPassword.this.finish();
            }
            else{
                Toast.makeText(ForgotPassword.this, "Try again! Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });
    }
}