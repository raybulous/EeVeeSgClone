package com.example.authapp3.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.authapp3.R;
import com.example.authapp3.control.Profile;
import com.example.authapp3.control.prefConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HelpMeMain extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        mAuth = FirebaseAuth.getInstance();

        keepLogIn = findViewById(R.id.keepLogInSwitch);
        keepLogIn.setChecked(prefConfig.loadKeepLoginFromPref(this));

        if (mAuth.getCurrentUser() != null) {
            if (keepLogIn.isChecked()) {
                startActivity(new Intent(MainActivity.this, HomePage.class));
                overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim);
            } else {
                mAuth.signOut();
            }
        }

        RelativeLayout profileTab = findViewById((R.id.profiletab));
        profileTab.setOnClickListener(this);

        RelativeLayout newService = findViewById((R.id.newservicetab));
        newService.setOnClickListener(this);

        RelativeLayout history = findViewById((R.id.historytab));
        newService.setOnClickListener(this);

        RelativeLayout explore = findViewById((R.id.exploretab));
        newService.setOnClickListener(this);

        RelativeLayout reviews = findViewById((R.id.reviewstab));
        newService.setOnClickListener(this);

        RelativeLayout map = findViewById((R.id.maptab));
        newService.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.profiletab:
                startActivity(new Intent(this, ProfileDetails.class));
                break;
            case R.id.newservicetab:
                startActivity(new Intent(this, ServiceForm.class));
                break;
            case R.id.historytab:
                //startActivity(new Intent(this, ForgotPassword.class)); //add history class
                break;
            case R.id.exploretab:
              //startActivity(new Intent(this, ProfileDetails.class)); //add explore class
                break;
            case R.id.reviewstab:
                //startActivity(new Intent(this, ServiceForm.class)); //add  reviews class
                break;
            case R.id.maptab:
                //startActivity(new Intent(this, ForgotPassword.class)); //add history class
                break;
        }

    }
}
