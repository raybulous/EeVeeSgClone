package com.example.authapp3;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        String userID = user.getUid();
        String userEmail = user.getEmail();

        Button ev = findViewById(R.id.evButton);
        Button qr = findViewById(R.id.qrButton);
        Button profile = findViewById(R.id.profileButton);
        Button logout = findViewById(R.id.signOut);
        ev.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, EVPage.class);
            startActivity(intent);
        });
        qr.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, MemberQR.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });
        profile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileDetails.class);
            startActivity(intent);
        });
        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        final TextView fullNameTextView = findViewById(R.id.fullName);
        final TextView emailTextView = findViewById(R.id.emailAddress);
        final TextView evModelTextView = findViewById(R.id.evModel);
        final TextView evColourTextView = findViewById(R.id.evColour);
        final TextView batteryStatusTextView = findViewById(R.id.batteryStatus);

        reference.child(userID).child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);


                if(userProfile != null) {
                    String fullName = userProfile.Name;
                    fullNameTextView.setText(fullName);
                    emailTextView.setText(userEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent, options.toBundle());
                        break;
                    case R.id.ic_navigate:
                        Intent intent1 = new Intent(ProfileActivity.this, Navigate.class);
                        ActivityOptions options1 = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this,bottomNavigationView ,"BottomBar");
                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent1, options1.toBundle());
                        break;
                    case R.id.ic_ProfileActivity:
                        break;
                    case R.id.ic_Rewards:
                        Intent intent2 = new Intent(ProfileActivity.this, Rewards.class);
                        ActivityOptions options2 = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this,bottomNavigationView ,"BottomBar");
                        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent2, options2.toBundle());
                        break;
                }

                return false;
            }
        });

        /*BOTTOM NAV BAR END*/

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
    }

    /*Backbutton Transition Animation*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
    }
}
