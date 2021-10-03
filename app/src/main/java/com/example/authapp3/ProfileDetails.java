package com.example.authapp3;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(view -> ProfileDetails.this.finish());
    }
}