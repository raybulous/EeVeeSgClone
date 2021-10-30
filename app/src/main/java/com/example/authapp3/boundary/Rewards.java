package com.example.authapp3.boundary;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authapp3.R;
import com.example.authapp3.entity.HistoryEntry;
import com.example.authapp3.entity.User;
import com.example.authapp3.entity.Voucher;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Rewards extends AppCompatActivity {
    private FirebaseUser user;
    private String userID;
    private String email;
    private Integer points;
    private String evModel;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private DatabaseReference userReference;
    private DatabaseReference catalogueReference;

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
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        catalogueReference = FirebaseDatabase.getInstance().getReference("RewardCatalogue");
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        userID = user.getUid();

        TextView displayNameTextView = findViewById(R.id.name);
        TextView emailTextView = findViewById(R.id.email_header);
        TextView pointsTextView= findViewById(R.id.pointsBalance);
        emailTextView.setText(email);

        // Set Name of User at the Top//
        userReference.child(userID).child("Profile").addValueEventListener(new ValueEventListener() {
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

        // Update User Points//
        userReference.child(userID).child("Rewards").child("points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                points = snapshot.getValue(Integer.class);
                pointsTextView.setText(points.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        recyclerView = findViewById(R.id.recyclerVoucher);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();

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
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null){
            adapter.stopListening();
            adapter.notifyDataSetChanged();
        }
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView voucher_name;
        public TextView cost;
        public Button claim;
        public boolean confirmClaim = true, showWarningToast = true;

        public ViewHolder(View itemView) {
            super(itemView);
            voucher_name = itemView.findViewById(R.id.voucher_name);
            cost = itemView.findViewById(R.id.points_cost);
            claim = itemView.findViewById(R.id.claim_voucher);
        }

        public void setVoucher_name(String string) {
            voucher_name.setText(string);
        }

        public void setCost(String string) {
            String formatted = string + " points to claim";
            cost.setText(formatted);
        }

        public void setClaim(String name, int cost) {
            claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (points < cost) {
                        Toast.makeText(Rewards.this, Rewards.this.getResources().getString(R.string.not_enough_points), Toast.LENGTH_SHORT).show();
                    } else if (confirmClaim) {
                        if (showWarningToast) {
                            Toast.makeText(Rewards.this, Rewards.this.getResources().getString(R.string.confirm_claim_toast), Toast.LENGTH_SHORT).show();
                            showWarningToast = false;
                            new Handler().postDelayed(() -> confirmClaim = false, 1000);
                            new Handler().postDelayed(() -> {
                                confirmClaim = true;
                                showWarningToast = true;
                            }, 3000);
                        }
                    } else {
                        int finalPoints = points - cost;
                        userReference.child(userID).child("Rewards").child("points").setValue(finalPoints);
                        String voucherId = createVoucher(name, cost);
                        showQR(voucherId);
                    }

                }
            });
        }
    }

    private void fetch() {

        Query query = catalogueReference;

        FirebaseRecyclerOptions<Voucher> options =
                new FirebaseRecyclerOptions.Builder<Voucher>()
                        .setQuery(query, Voucher.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Voucher, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_vouchers, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ViewHolder holder, int position, Voucher model) {
                holder.setVoucher_name(model.getDetails());
                holder.setCost(((Integer) model.getCost()).toString());
                holder.setClaim(model.getName(), model.getCost());
                // Bind the Chat object to the ChatHolder
                // ...
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private String createVoucher(String name, int cost) {
        DatabaseReference historyReference = userReference.child(userID).child("Rewards").child("History").push();
        DatabaseReference rewardsReference = FirebaseDatabase.getInstance().getReference("Rewards");
        String historyKey = historyReference.getKey();
        DatabaseReference voucherReference = rewardsReference.push();
        String voucherId = "EVSG_" + name + "_" + userID + "_" + historyKey;
        voucherReference.setValue(voucherId);
        historyReference.setValue(new HistoryEntry(name, cost));
        return voucherId;
    }

    private void showQR(String voucherId) {
        Intent intent = new Intent(Rewards.this, VoucherQR.class);
        intent.putExtra("voucherId", voucherId);
        startActivity(intent);
    }
}