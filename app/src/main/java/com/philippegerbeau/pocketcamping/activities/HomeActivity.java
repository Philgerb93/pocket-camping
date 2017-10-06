package com.philippegerbeau.pocketcamping.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.fragments.AlertsFragment;
import com.philippegerbeau.pocketcamping.fragments.ProfileFragment;
import com.philippegerbeau.pocketcamping.fragments.StayFragment;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView navigation;

    Fragment stayFragment;
    Fragment alertsFragment;
    Fragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigation = (BottomNavigationView) (findViewById(R.id.navigation));

        stayFragment = new StayFragment();
        alertsFragment = new AlertsFragment();
        profileFragment = new ProfileFragment();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        getSupportFragmentManager().beginTransaction().replace(
                                R.id.fragment_container, stayFragment).commit();
                        break;
                    case R.id.action_alerts:
                        getSupportFragmentManager().beginTransaction().replace(
                                R.id.fragment_container, alertsFragment).commit();
                        break;
                    case R.id.action_profile:
                        getSupportFragmentManager().beginTransaction().replace(
                                R.id.fragment_container, profileFragment).commit();
                        break;
                }
                return true;
            }
        });
        navigation.setSelectedItemId(R.id.action_home);
    }
}
