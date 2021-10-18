package com.example.authapp3.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authapp3.R;
import com.example.authapp3.control.prefConfig;
import com.google.firebase.auth.FirebaseAuth;

public class VoucherQR extends AppCompatActivity {
    private boolean doubleBackToFinish = false, showWarningToast = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_qr);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> warnUser());

        Intent intent = getIntent();
        String voucherId = intent.getStringExtra("voucherId");

        WebView qrCode = findViewById(R.id.qr_webView);
        String URL = "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data="+voucherId;
        qrCode.loadUrl(URL);
    }

    @Override
    public void onBackPressed() {
        warnUser();
    }

    private void warnUser() {
        if (doubleBackToFinish) {
            VoucherQR.this.finish();
            overridePendingTransition(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
        } else if (showWarningToast) {
            Toast.makeText(this, this.getResources().getString(R.string.warning_toast), Toast.LENGTH_LONG).show();
            showWarningToast = false;
            new Handler().postDelayed(() -> doubleBackToFinish = true, 2000);
            new Handler().postDelayed(() -> {
                doubleBackToFinish = false;
                showWarningToast = true;
            }, 4000);
        }
    }
}