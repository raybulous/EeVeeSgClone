package com.example.authapp3.boundary;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authapp3.R;
import com.example.authapp3.control.Profile;
import com.example.authapp3.control.prefConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PopUpRating extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.popout_rating);

        RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
        rb.setRating(2.5f);
        rb.setStepSize(0.5f);

        rb.setOnRatingBarChangeListener((ratingBar, v, b)
                -> Toast.makeText(this,"Rating: $rating", Toast.LENGTH_SHORT).show());



    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.rateSubmitButton:
                startActivity(new Intent(this, HelpMeMain.class));
                //send data of rating to database ???
                break;
            case R.id.ratecancelButton:
                startActivity(new Intent(this, HelpMeMain.class));
                //return back to main page
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
