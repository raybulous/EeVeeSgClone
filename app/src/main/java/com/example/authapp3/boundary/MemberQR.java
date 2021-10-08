package com.example.authapp3.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.authapp3.R;

public class MemberQR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_qr);

        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(view -> MemberQR.this.finish());

        final TextView qrAccountNumberTextView = findViewById(R.id.qrAccountNumber);

        Intent intent = getIntent();
        String userid = intent.getStringExtra("userID");
        String displayAccountNumber = "EVSG"+userid;
        qrAccountNumberTextView.setText(displayAccountNumber);

        WebView qrCode = findViewById(R.id.qrCodeDisplay);
        String URL = "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=EVSG"+userid;
        qrCode.loadUrl(URL);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
    }
}