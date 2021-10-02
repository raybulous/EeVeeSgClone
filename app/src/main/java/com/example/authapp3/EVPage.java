package com.example.authapp3;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EVPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evpage);

        Button backTextView = findViewById(R.id.Back);
        backTextView.setOnClickListener(view -> EVPage.this.finish());
    }
}