package com.philippegerbeau.pocketcamping;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Handler {
    public static String username;
    public static String email;
    public static String userID;
    public static String stayID;
    public static Uri photoUrl;

    public static DatabaseReference fbUserRef;
    public static DatabaseReference fbStayRef;

    public static void init() {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        assert fbUser != null;

        final DatabaseReference fbRootRef = FirebaseDatabase.getInstance().getReference();

        username = fbUser.getDisplayName();
        email = fbUser.getEmail();
        userID = fbUser.getUid();
        photoUrl = fbUser.getPhotoUrl();

        fbUserRef = fbRootRef.child("users").child(userID);
        fbUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("stayID")) {
                    stayID = (String) dataSnapshot.child("stayID").getValue();
                    fbStayRef = fbRootRef.child("stays").child(stayID);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
