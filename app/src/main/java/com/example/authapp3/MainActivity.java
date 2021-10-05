package com.example.authapp3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// hello world
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmail, editTextPassword;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView register = findViewById(R.id.register);
        register.setOnClickListener(this);

        Button signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);





    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.signIn:
                userLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {

            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if(email.isEmpty()){
                editTextEmail.setError("Email is required!");
                editTextEmail.requestFocus();
                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editTextEmail.setError("Please enter a valid email!");
                editTextEmail.requestFocus();
                return;
            }

            if(password.isEmpty()){
                editTextPassword.setError("Password is required!");
                editTextPassword.requestFocus();
                return;
            }

            if(password.length()<8){
                editTextPassword.setError("Password is minimum 8 characters!");
                editTextPassword.requestFocus();
                return;
            }

            progressBar.setVisibility(View.GONE);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    //redirect user profile
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, HomePage.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to verify account", Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "Failed to login, please check credentials!", Toast.LENGTH_LONG).show();
                }
            });
    }

    private boolean doubleBackToExitPressedOnce = false, showWarningToast = true;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        } else if (showWarningToast) {
            Toast.makeText(this, this.getResources().getString(R.string.exit_toast), Toast.LENGTH_SHORT).show();
            showWarningToast = false;
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = true, 1500);
            new Handler().postDelayed(() -> {
                doubleBackToExitPressedOnce = false;
                showWarningToast = true;
            }, 4000);
        }
    }
}