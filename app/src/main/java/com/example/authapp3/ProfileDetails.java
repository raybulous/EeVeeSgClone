package com.example.authapp3;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        Button backTextView = findViewById(R.id.Back);
        backTextView.setOnClickListener(view -> ProfileDetails.this.finish());
    }
}