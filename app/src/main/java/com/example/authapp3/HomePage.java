package com.example.authapp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.ActivityOptions;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarView;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


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
                        startActivity(intent, options.toBundle());
                        break;

                    case R.id.ic_ProfileActivity:
                        Intent intent1 = new Intent(HomePage.this,ProfileActivity.class);
                        ActivityOptions options1 = ActivityOptions.makeSceneTransitionAnimation(HomePage.this,bottomNavigationView ,"BottomBar");
                        startActivity(intent1, options1.toBundle());
                        break;

                    case R.id.ic_Rewards:
                        Intent intent2 = new Intent(HomePage.this,Rewards.class);
                        ActivityOptions options2 = ActivityOptions.makeSceneTransitionAnimation(HomePage.this,bottomNavigationView ,"BottomBar");
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
    /*Backbutton Transition Animation*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
    }
}