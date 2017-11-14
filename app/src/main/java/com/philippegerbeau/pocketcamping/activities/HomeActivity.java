package com.philippegerbeau.pocketcamping.activities;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

        alertsFragment = new AlertsFragment();
        profileFragment = new ProfileFragment();

        homeButton = findViewById(R.id.action_home);
        alertsButton = findViewById(R.id.action_alerts);
        profileButton = findViewById(R.id.action_profile);

        setStayFragment();
    }

    private void setStayFragment() {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = "";
        if (fbUser != null) {
            userID = fbUser.getUid();
        }
        DatabaseReference fbUserRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userID);

        fbUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("stayID").exists()) {
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
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container, stayFragment).commit();

        homeButton.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
        alertsButton.setColorFilter(ContextCompat.getColor(this, R.color.colorTextPrimary));
        profileButton.setColorFilter(ContextCompat.getColor(this, R.color.colorTextPrimary));
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
}
