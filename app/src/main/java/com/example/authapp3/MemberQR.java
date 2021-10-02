package com.example.authapp3;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MemberQR extends AppCompatActivity {
    private WebView qrCode;
    private String userID;
    private String memberCode;
    private TextView testttt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_qr);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        qrCode = (WebView) findViewById(R.id.qrCodeDisplay);
        String URL = "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=EeVeeSG"+userID;
        qrCode.loadUrl(URL);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
    }
}