package com.philippegerbeau.pocketcamping.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philippegerbeau.pocketcamping.R;

public class MainActivity extends AppCompatActivity {
    FirebaseUser fbUser;
    DatabaseReference fbRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        fbRootRef = FirebaseDatabase.getInstance().getReference().child("users");

        if (fbUser == null) {
            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
        } else {
            String userID = fbUser.getUid();
            fbRootRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Intent i;
                    if (snapshot.hasChild("stayID")) {
                        i = new Intent(MainActivity.this, HomeActivity.class);
                    } else {
                        i = new Intent(MainActivity.this, NoStayActivity.class);
                    }
                    startActivity(i);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }
}
