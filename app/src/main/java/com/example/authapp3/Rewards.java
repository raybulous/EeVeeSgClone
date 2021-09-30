package com.example.authapp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Rewards extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

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
                        startActivity(intent);
                        break;
                    case R.id.ic_navigate:
                        Intent intent2 = new Intent(Rewards.this, Navigate.class);
                        startActivity(intent2);
                        break;
                    case R.id.ic_ProfileActivity:
                        Intent intent1 = new Intent(Rewards.this, ProfileActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_Rewards:
                        break;
                }

                return false;
            }
        });

        /*BOTTOM NAV BAR END*/
    }
}