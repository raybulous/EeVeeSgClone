package com.example.authapp3;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    private TextView count;
    private int minteger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

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



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationView);
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
                        Intent intent = new Intent(HomePage.this,Navigate.class);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomePage.this,bottomNavigationView ,"BottomBar");
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent, options.toBundle());
                        break;

                    case R.id.ic_ProfileActivity:
                        Intent intent1 = new Intent(HomePage.this,ProfileActivity.class);
                        ActivityOptions options1 = ActivityOptions.makeSceneTransitionAnimation(HomePage.this,bottomNavigationView ,"BottomBar");
                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent1, options1.toBundle());
                        break;

                    case R.id.ic_Rewards:
                        Intent intent2 = new Intent(HomePage.this,Rewards.class);
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
    private boolean doubleBackToLogoutPressedOnce = false, showWarningToast = true;
    @Override
    public void onBackPressed() {
        if (doubleBackToLogoutPressedOnce) {
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

    // Creating Battery Alert Setting
    /*public void increaseInteger(View view)
    {
        minteger = minteger + 1;
        display(minteger);
    }

    public void decreaseInteger(View view)
    {
        if (minteger > 0) {
            minteger = minteger - 1;
            display(minteger);
        }
    }

    private void display(int number)
    {
        TextView displayInteger = (TextView) findViewById(R.id.counter);
        displayInteger.setText("" + number + "%");
    }
*/




}