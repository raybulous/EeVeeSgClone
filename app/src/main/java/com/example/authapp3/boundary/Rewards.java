package com.example.authapp3.boundary;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authapp3.R;
import com.example.authapp3.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.CollationElementIterator;

public class Rewards extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseDatabase reward;
    private String userID;
    private String email;
    private DatabaseReference points;
    private String evModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        evModel = getIntent().getStringExtra("evModel");

        /*Transition*/
        Transition exitTrans = new Fade();
        exitTrans.excludeTarget("@+id/BottomNavigationView",true);
        getWindow().setExitTransition(exitTrans);

        Transition reenterTrans = new Fade();
        reenterTrans.excludeTarget("@+id/BottomNavigationView",true);
        getWindow().setReenterTransition(reenterTrans);

        //To Extract Data from Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        email = user.getEmail();
        userID = user.getUid();

        TextView displayNameTextView = findViewById(R.id.name);
        TextView emailTextView = findViewById(R.id.email_header);
        emailTextView.setText(email);

        reference.child(userID).child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    displayNameTextView.setText(userProfile.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        /*BOTTOM NAVIGATION BAR*/

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        Intent intent = new Intent(Rewards.this, HomePage.class);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Rewards.this,bottomNavigationView ,"BottomBar");
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent, options.toBundle());
                        break;
                    case R.id.ic_navigate:
                        Intent intent1 = new Intent(Rewards.this, SearchLocation.class);
                        ActivityOptions options1 = ActivityOptions.makeSceneTransitionAnimation(Rewards.this,bottomNavigationView ,"BottomBar");
                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent1.putExtra("evModel",(String) evModel);
                        startActivity(intent1, options1.toBundle());
                        break;
                    case R.id.ic_ProfileActivity:
                        Intent intent2 = new Intent(Rewards.this, ProfileActivity.class);
                        ActivityOptions options2 = ActivityOptions.makeSceneTransitionAnimation(Rewards.this,bottomNavigationView ,"BottomBar");
                        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent2, options2.toBundle());
                        break;
                    case R.id.ic_Rewards:
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