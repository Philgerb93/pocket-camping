package com.philippegerbeau.pocketcamping.activities;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.philippegerbeau.pocketcamping.Handler;
import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.fragments.AlertsFragment;
import com.philippegerbeau.pocketcamping.fragments.NoStayFragment;
import com.philippegerbeau.pocketcamping.fragments.ProfileFragment;
import com.philippegerbeau.pocketcamping.fragments.StayFragment;

public class HomeActivity extends AppCompatActivity {
    Fragment stayFragment;
    Fragment alertsFragment;
    Fragment profileFragment;

    ImageButton homeButton;
    ImageButton alertsButton;
    ImageButton profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setStayListener();
        alertsFragment = new AlertsFragment();
        profileFragment = new ProfileFragment();

        homeButton = findViewById(R.id.action_home);
        alertsButton = findViewById(R.id.action_alerts);
        profileButton = findViewById(R.id.action_profile);
    }

    private void setStayListener() {
        Handler.fbUserRef.child("stayID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    stayFragment = new StayFragment();
                } else {
                    stayFragment = new NoStayFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(
                        R.id.fragment_container, stayFragment).commitAllowingStateLoss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void navigate(View view) {
        switch (view.getId()) {
            case R.id.action_home:
                toHome();
                break;
            case R.id.action_alerts:
                toAlerts();
                break;
            case R.id.action_profile:
                toProfile();
                break;
        }
    }

    private void toHome() {
        if (stayFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragment_container, stayFragment).commit();

            homeButton.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
            alertsButton.setColorFilter(ContextCompat.getColor(this, R.color.colorTextPrimary));
            profileButton.setColorFilter(ContextCompat.getColor(this, R.color.colorTextPrimary));
        }
    }

    private void toAlerts() {
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container, alertsFragment).commit();

        homeButton.setColorFilter(ContextCompat.getColor(this, R.color.colorTextPrimary));
        alertsButton.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
        profileButton.setColorFilter(ContextCompat.getColor(this, R.color.colorTextPrimary));
    }

    private void toProfile() {
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container, profileFragment).commit();

        homeButton.setColorFilter(ContextCompat.getColor(this, R.color.colorTextPrimary));
        alertsButton.setColorFilter(ContextCompat.getColor(this, R.color.colorTextPrimary));
        profileButton.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    public void stayEdit(View view) {
        if (view.getId() == R.id.creation_fab) {
            ((NoStayFragment)stayFragment).stayEdit(view);
        } else {
            ((StayFragment)stayFragment).stayEdit(view);
        }
    }

    public void toItems(View view) {
        ((StayFragment) stayFragment).toItems(view);
    }

    public void toMeals(View view) {
        ((StayFragment) stayFragment).toMeals(view);
    }

    public void addFriend(View view) {
        ((ProfileFragment) profileFragment).addFriend(view);
    }

    public void stopInput() {
        ((ProfileFragment) profileFragment).stopInput();
    }

    public void showNav() {
        LinearLayout nav = findViewById(R.id.navigation);
        nav.setVisibility(View.VISIBLE);
    }

    public void hideNav() {
        LinearLayout nav = findViewById(R.id.navigation);
        nav.setVisibility(View.GONE);
    }

    public void submit(View view) {
        ((ProfileFragment) profileFragment).submit(view);
    }
}
