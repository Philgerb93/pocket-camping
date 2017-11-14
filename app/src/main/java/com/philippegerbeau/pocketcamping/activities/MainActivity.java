package com.philippegerbeau.pocketcamping.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.philippegerbeau.pocketcamping.Handler;

public class MainActivity extends AppCompatActivity {
    FirebaseUser fbUser;
    DatabaseReference fbRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        fbRootRef = FirebaseDatabase.getInstance().getReference();

        Intent i;
        if (fbUser != null) {
            Handler.init();
            i = new Intent(this, HomeActivity.class);
        } else {
            i = new Intent(this, SignInActivity.class);
        }
        startActivity(i);
    }
}
