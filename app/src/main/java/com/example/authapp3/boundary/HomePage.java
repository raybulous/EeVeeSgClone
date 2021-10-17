package com.example.authapp3.boundary;

import android.app.ActivityOptions;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.authapp3.R;
import com.example.authapp3.control.prefConfig;
import com.example.authapp3.entity.EV;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {

    private boolean doubleBackToLogoutPressedOnce = false, showWarningToast = true, alert = true;
    private FirebaseUser user;
    private int minteger, batteryLevel;
    private String userID, batteryStatus;
    private TextView count;
    private static final String CHANNEL_ID = "channel1";
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        createNotificationChannel();

        //To Extract EV from Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        TextView evModelTextView = findViewById(R.id.carModel);
        TextView batteryStatusTextView = findViewById(R.id.homePageBatt);
        ImageView batteryIconImageView = findViewById(R.id.homePageBattery_icon);
        TextView chargingStatus = findViewById(R.id.chargeStatus);

        notificationManager = NotificationManagerCompat.from(this);

        // Save Counter State
        count = findViewById(R.id.counter);
        Button buttonIncrease = findViewById(R.id.buttonIncrease);
        Button buttonDecrease = findViewById(R.id.buttonDecrease);
        minteger = prefConfig.loadTotalFromPref(this);
        String alertThreshold = minteger+"%";
        count.setText(alertThreshold);
        buttonIncrease.setOnClickListener(view -> {
            minteger += 5;
            if (minteger > 50)
            {
                minteger = 50;
            }
            prefConfig.saveTotalInPref(getApplicationContext(),minteger);
            String alertThreshold1 = minteger+"%";
            count.setText(alertThreshold1);
            checkAlert();
        });

        buttonDecrease.setOnClickListener(view -> {
            minteger -= 5;
            if (minteger < 0)
            {
                minteger = 0;
            }

            prefConfig.saveTotalInPref(getApplicationContext(), minteger);
            String alertThreshold12 = minteger+"%";
            count.setText(alertThreshold12);
            checkAlert();
        });

        reference.child(userID).child("EV").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EV evProfile = snapshot.getValue(EV.class);

                if(evProfile != null) {
                    String evModel = evProfile.getModel();
                    batteryLevel = evProfile.getBatteryStatus();
                    batteryStatus = batteryLevel+"%";
                    evModelTextView.setText(evModel);
                    batteryStatusTextView.setText(batteryStatus);
                    batteryIconImageView.setImageResource(evProfile.findBatteryImage());
                    chargingStatus.setText(evProfile.getChargeStatus());

                    checkAlert();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });




        /*will be removed DO NOT REMOVE-ray*/
        Button tempbutton = findViewById(R.id.tempbutton);
        tempbutton.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, SearchLocation.class);
            startActivity(intent);
        });


        /*Transition*/
        Transition exitTrans = new Fade();
        exitTrans.excludeTarget("@+id/BottomNavigationView",true);
        getWindow().setExitTransition(exitTrans);
        //final View view = findViewById(R.id.BottomNavigationView);

        Transition reenterTrans = new Fade();
        reenterTrans.excludeTarget("@+id/BottomNavigationView",true);
        getWindow().setReenterTransition(reenterTrans);



        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.ic_home:
                        break;

                    case R.id.ic_navigate:
                        Intent intent = new Intent(HomePage.this, Navigate.class);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomePage.this,bottomNavigationView ,"BottomBar");
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent, options.toBundle());
                        break;

                    case R.id.ic_ProfileActivity:
                        Intent intent1 = new Intent(HomePage.this, ProfileActivity.class);
                        ActivityOptions options1 = ActivityOptions.makeSceneTransitionAnimation(HomePage.this,bottomNavigationView ,"BottomBar");
                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent1, options1.toBundle());
                        break;

                    case R.id.ic_Rewards:
                        Intent intent2 = new Intent(HomePage.this, Rewards.class);
                        ActivityOptions options2 = ActivityOptions.makeSceneTransitionAnimation(HomePage.this,bottomNavigationView ,"BottomBar");
                        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent2, options2.toBundle());
                        break;
                }



                return false;
            }
        });

/*        NavController navController = Navigation.findNavController(this,  R.id.fragmentContainerView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.home,R.id.navigate,R.id.profileActivity,R.id.rewards).build();*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
    }

    /*Backbutton Transition Animation*/
    @Override
    public void onBackPressed() {
        if (doubleBackToLogoutPressedOnce) {
            prefConfig.saveKeepLoginInPref(this,false);
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomePage.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (showWarningToast) {
            Toast.makeText(this, this.getResources().getString(R.string.logout_toast), Toast.LENGTH_SHORT).show();
            showWarningToast = false;
            new Handler().postDelayed(() -> doubleBackToLogoutPressedOnce = true, 1500);
            new Handler().postDelayed(() -> {
                doubleBackToLogoutPressedOnce = false;
                showWarningToast = true;
            }, 4000);
        }
        overridePendingTransition(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alert channel";
            String description = "Alerts when low battery";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendAlert(String batteryStatus) {
        Intent intent = new Intent(this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String message = "Current EV battery is at " + batteryStatus;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Low Battery")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }

    private void checkAlert() {
        int previous = prefConfig.loadAlertFromPref(getApplicationContext());
        if (batteryLevel < minteger) {
            if (alert) {
                alert = false;
                prefConfig.saveAlertInPref(getApplicationContext(), batteryLevel);
                sendAlert(batteryStatus);
            } else if (previous - batteryLevel > 10) {
                prefConfig.saveAlertInPref(getApplicationContext(), batteryLevel);
                sendAlert(batteryStatus);
            }
        } else {
            alert = true;
        }
    }
}