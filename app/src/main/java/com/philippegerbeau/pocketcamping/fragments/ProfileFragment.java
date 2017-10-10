package com.philippegerbeau.pocketcamping.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.adapters.ProfileViewPagerAdapter;

public class ProfileFragment extends Fragment {
    private FirebaseUser fbUser;

    private TextView username;
    private TextView status;
    private ImageView profilePic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        username = view.findViewById(R.id.username);
        status = view.findViewById(R.id.status);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        initViewPager(view);
        setUserInfo();

        return view;
    }

    private void initViewPager(View view) {
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter =
                new ProfileViewPagerAdapter(getChildFragmentManager(), getActivity());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUserInfo() {
        if (fbUser != null) {
            username.setText(fbUser.getDisplayName());
            //profilePic.setImageURI(fbUser.getPhotoUrl());

            final DatabaseReference fbUserRef = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(fbUser.getUid());
            fbUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("stayID")) {
                        setStatus(dataSnapshot);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    private void setStatus(DataSnapshot dataSnapshot) {
        String stayID = dataSnapshot.child("stayID").getValue().toString();
        DatabaseReference fbStayRef = FirebaseDatabase.getInstance()
                .getReference().child("stays").child(stayID);

        fbStayRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                status.setText(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
