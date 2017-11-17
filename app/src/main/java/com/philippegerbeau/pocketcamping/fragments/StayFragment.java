package com.philippegerbeau.pocketcamping.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.philippegerbeau.pocketcamping.Handler;
import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.activities.ItemsActivity;
import com.philippegerbeau.pocketcamping.activities.StayEditActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StayFragment extends Fragment {
    private final String DATE_FORMAT = "d MMMM yyyy";

    private TextView stayLocation;
    private TextView stayDuration;
    private TextView staySpot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stay, container, false);

        stayLocation = view.findViewById(R.id.stay_location);
        stayDuration = view.findViewById(R.id.stay_duration);
        staySpot = view.findViewById(R.id.stay_spot);

        setStayListener();

        return view;
    }

    private void setStayListener() {
        Handler.fbStayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String location = dataSnapshot.child("location").getValue(String.class);

                Calendar arrival = Calendar.getInstance();
                Calendar departure = Calendar.getInstance();

                arrival.setTimeInMillis((long) dataSnapshot.child("arrival").getValue());
                departure.setTimeInMillis((long) dataSnapshot.child("departure").getValue());

                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

                String duration = sdf.format(arrival.getTime()) + " - "
                        + sdf.format(departure.getTime());

                String spot = getResources().getString(R.string.spot);
                spot += " " + dataSnapshot.child("spot").getValue(String.class);

                stayLocation.setText(location);
                stayDuration.setText(duration);
                staySpot.setText(spot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @SuppressWarnings("unused")
    public void stayEdit(View view) {
        Intent i = new Intent(getActivity(), StayEditActivity.class);
        startActivity(i);
    }

    @SuppressWarnings("unused")
    public void toItems(View view) {
        Intent i = new Intent(getActivity(), ItemsActivity.class);
        startActivity(i);
    }

    @SuppressWarnings("unused")
    public void toMeals(View view) {
        // TODO
    }
}
