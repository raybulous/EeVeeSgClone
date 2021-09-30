package com.example.authapp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private Button logout, navigate, ev, qr, profile;
    private String userID, userEmail;
    private FirebaseUser user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ev = (Button) findViewById(R.id.evButton);
        qr = (Button) findViewById(R.id.qrButton);
        profile = (Button) findViewById(R.id.profileButton);
        navigate = (Button) findViewById(R.id.Nav);
        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        // button to change to Navigate page
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, Navigate.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        userEmail = user.getEmail();

        final TextView profileHeaderTextView = (TextView) findViewById(R.id.profileHeader);
        final TextView fullNameTextView = (TextView) findViewById(R.id.fullName);
        final TextView emailTextView = (TextView) findViewById(R.id.emailAddress);
        final TextView evModelTextView = (TextView) findViewById(R.id.evModel);
        final TextView evColourTextView = (TextView) findViewById(R.id.evColour);
        final TextView batteryStatusTextView = (TextView) findViewById(R.id.batteryStatus);
        final TextView memberCodeTextView = (TextView) findViewById(R.id.memberCode);
        final TextView memberDescTextView = (TextView) findViewById(R.id.memberDesc);
        final TextView personalDetailsTextView = (TextView) findViewById(R.id.personalDetails);
        final TextView personalDescTextView = (TextView) findViewById(R.id.personalDesc);

        profileHeaderTextView.setText(R.string.profile_header);
        memberCodeTextView.setText(R.string.member_code);
        memberDescTextView.setText(R.string.member_desc);
        personalDetailsTextView.setText(R.string.personal_details);
        personalDescTextView.setText(R.string.personal_desc);

        reference.child(userID).child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);


                if(userProfile != null) {
                    String fullName = userProfile.Name;
                    String email = userEmail;
                    fullNameTextView.setText(fullName);
                    emailTextView.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });


        /*Transition*/
        Transition exitTrans = new Fade();
        exitTrans.excludeTarget("@+id/BottomNavigationView",true);
        getWindow().setExitTransition(exitTrans);

        Transition reenterTrans = new Fade();
        reenterTrans.excludeTarget("@+id/BottomNavigationView",true);
        getWindow().setReenterTransition(reenterTrans);

        /*BOTTOM NAVIGATION BAR*/



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        Intent intent = new Intent(ProfileActivity.this, HomePage.class);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this,bottomNavigationView ,"BottomBar");
                        startActivity(intent, options.toBundle());
                        break;
                    case R.id.ic_navigate:
                        Intent intent1 = new Intent(ProfileActivity.this, Navigate.class);
                        ActivityOptions options1 = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this,bottomNavigationView ,"BottomBar");
                        startActivity(intent1, options1.toBundle());
                        break;
                    case R.id.ic_ProfileActivity:
                        break;
                    case R.id.ic_Rewards:
                        Intent intent2 = new Intent(ProfileActivity.this, Rewards.class);
                        ActivityOptions options2 = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this,bottomNavigationView ,"BottomBar");
                        startActivity(intent2, options2.toBundle());
                        break;
                }

                return false;
            }
        });

        /*BOTTOM NAV BAR END*/



        reference.child(userID).child("EV").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EV evProfile = snapshot.getValue(EV.class);

                if(evProfile != null) {
                    String evModel = evProfile.Model;
                    String evColour = evProfile.Colour;
                    String batteryStatus = evProfile.BatteryStatus+"%";
                    evModelTextView.setText(evModel);
                    evColourTextView.setText(evColour);
                    batteryStatusTextView.setText(batteryStatus);
                } else {
                    evModelTextView.setText(R.string.no_ev);
                    evColourTextView.setText(R.string.unavailable);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });

    }
    /*Backbutton Transition Animation*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
    }
}
