package com.philippegerbeau.pocketcamping.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User {
    private String name;
    private String userID;
    private String stayID;
    private String email;

    private DatabaseReference fbUserRef;
    private DatabaseReference fbStayRef;

    public User() {
        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        final DatabaseReference fbRootRef = FirebaseDatabase.getInstance().getReference();

        // Name
        name = fbAuth.getCurrentUser().getDisplayName();

        // UserID
        userID = fbAuth.getUid();
        fbUserRef = fbRootRef.child("users").child(userID);

        // StayID
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

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }

    public String getStayID() {
        return stayID;
    }

    public void setStayID(String stayID) {
        this.stayID = stayID;
    }
}
