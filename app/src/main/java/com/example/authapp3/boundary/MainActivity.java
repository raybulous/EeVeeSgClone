package com.example.authapp3.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.authapp3.R;
import com.example.authapp3.control.Profile;
import com.example.authapp3.control.prefConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private Switch keepLogIn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        TextView register = findViewById(R.id.register);
        register.setOnClickListener(this);

        Button signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        keepLogIn = findViewById(R.id.keepLogInSwitch);

        editTextEmail.setText(prefConfig.loadLoginEmailFromPref(this));
        editTextPassword.setText(prefConfig.loadLoginPassFromPref(this));
        keepLogIn.setChecked(prefConfig.loadKeepLoginFromPref(this));

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        if(keepLogIn.isChecked()) {
            userLogin();
        }
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

        String emailCheck = Profile.checkEmail(email);
        if(!emailCheck.equals("No Error")){
            editTextEmail.setError(emailCheck);
            editTextEmail.requestFocus();
            return;
        }

        String passwordCheck = Profile.checkPassword(password);
        if(!passwordCheck.equals("No Error")){
            editTextPassword.setError(passwordCheck);
            editTextPassword.requestFocus();
            return;
        }

        if(keepLogIn.isChecked()) {
            prefConfig.saveLoginEmailInPref(this,email);
            prefConfig.saveLoginPassInPref(this,password);
        }else{
            prefConfig.saveLoginEmailInPref(this,"");
            prefConfig.saveLoginPassInPref(this,"");
        }
        prefConfig.saveKeepLoginInPref(this,keepLogIn.isChecked());

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
            }else{
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