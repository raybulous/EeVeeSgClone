package com.example.authapp3.boundary;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authapp3.R;
import com.example.authapp3.control.prefConfig;
import com.example.authapp3.entity.EV;
import com.example.authapp3.entity.User;
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

    private FirebaseUser user;
    private String userID, userEmail;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        userEmail = user.getEmail();

        ImageButton ev = findViewById(R.id.evButton);
        ImageButton qr = findViewById(R.id.qrButton);
        ImageButton profile = findViewById(R.id.profileButton);
        Button logout = findViewById(R.id.signOut);
        ev.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, EVPage.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });
        qr.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, MemberQR.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });
        profile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileDetails.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });
        logout.setOnClickListener(view -> {
            prefConfig.saveKeepLoginInPref(this,false);
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        TextView fullNameTextView = findViewById(R.id.fullName);
        TextView evModelTextView = findViewById(R.id.evModel);
        TextView evColourTextView = findViewById(R.id.evColour);
        TextView batteryStatusTextView = findViewById(R.id.batteryStatus);

        emailTextView = findViewById(R.id.emailAddress);
        emailTextView.setText(userEmail);

        reference.child(userID).child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    fullNameTextView.setText(userProfile.getName());
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
                    String evModel = evProfile.getModel();
                    String evColour = evProfile.getColour();
                    String batteryStatus = evProfile.getBatteryStatus()+"%";
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



        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigationView);
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
                        Intent intent1 = new Intent(ProfileActivity.this, SearchLocation.class);
                        ActivityOptions options1 = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this,bottomNavigationView ,"BottomBar");
                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent1.putExtra("evModel",(String) evModelTextView.getText().toString());
                        startActivity(intent1, options1.toBundle());
                        break;
                    case R.id.ic_ProfileActivity:
                        break;
                    case R.id.ic_Rewards:
                        Intent intent2 = new Intent(ProfileActivity.this, Rewards.class);
                        ActivityOptions options2 = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this,bottomNavigationView ,"BottomBar");
                        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent2.putExtra("evModel",(String) evModelTextView.getText().toString());
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

        userEmail = user.getEmail();
        emailTextView.setText(userEmail);
    }

    /*Backbutton Transition Animation*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
    }
}
