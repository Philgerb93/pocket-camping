package com.philippegerbeau.pocketcamping.activities;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.fragments.AlertsFragment;
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

        stayFragment = new StayFragment();
        alertsFragment = new AlertsFragment();
        profileFragment = new ProfileFragment();

        homeButton = (ImageButton) findViewById(R.id.action_home);
        alertsButton = (ImageButton) findViewById(R.id.action_alerts);
        profileButton = (ImageButton) findViewById(R.id.action_profile);

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container, stayFragment).commit();
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
}
