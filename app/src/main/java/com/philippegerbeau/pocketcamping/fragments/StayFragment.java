package com.philippegerbeau.pocketcamping.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.philippegerbeau.pocketcamping.Handler;
import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.activities.ItemsActivity;
import com.philippegerbeau.pocketcamping.activities.StayEditActivity;
import com.philippegerbeau.pocketcamping.data.Container;
import com.philippegerbeau.pocketcamping.data.Meal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StayFragment extends Fragment {
    private final String DATE_FORMAT = "d MMM yyyy";
    private final String TIME_FORMAT = "HH:mm";

    private TextView stayLocation;
    private TextView stayArrival;
    private TextView stayDeparture;
    private TextView staySpot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stay, container, false);

        stayLocation = view.findViewById(R.id.stay_location);
        stayArrival = view.findViewById(R.id.stay_arrival);
        stayDeparture = view.findViewById(R.id.stay_departure);
        staySpot = view.findViewById(R.id.stay_spot);

        setStayListener(view);
        setItemsListener(view);
        setMealsListener(view);

        return view;
    }

    @Override
    public void onStop() {


        super.onStop();
    }

    private void setStayListener(final View view) {
        Handler.fbStayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String location = dataSnapshot.child("location").getValue(String.class);

                Calendar arrival = Calendar.getInstance();
                Calendar departure = Calendar.getInstance();

                arrival.setTimeInMillis((long) dataSnapshot.child("arrival").getValue());
                departure.setTimeInMillis((long) dataSnapshot.child("departure").getValue());

                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
                SimpleDateFormat sdfT = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());

                String arrivalDate = sdf.format(arrival.getTime()) + " - "
                        + sdfT.format(arrival.getTime());

                String departureDate = sdf.format(departure.getTime()) + " - "
                        + sdfT.format(departure.getTime());

                String spot = view.getResources().getString(R.string.spot);
                spot += " " + dataSnapshot.child("spot").getValue(String.class);
                
                stayLocation.setText(location);
                stayArrival.setText(arrivalDate);
                stayDeparture.setText(departureDate);
                staySpot.setText(spot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setItemsListener(final View view) {
        Handler.fbStayRef.child("containers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProgressBar itemsPb = view.findViewById(R.id.items_pb);
                TextView itemsStatus = view.findViewById(R.id.items_status);

                int checked = 0;
                int total = 0;
                for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                    Container container = childSnap.getValue(Container.class);
                    if (container != null) {
                        total += container.getItemsList().size();
                        checked += container.getCheckedCount();
                    }
                }

                itemsPb.setProgress(checked);
                itemsPb.setMax(total);
                String status = checked + " / " + total;
                itemsStatus.setText(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setMealsListener(final View view) {
        Handler.fbStayRef.child("meals").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProgressBar mealsPb = view.findViewById(R.id.meals_pb);
                TextView mealsStatus = view.findViewById(R.id.meals_status);

                int checked = 0;
                int total = 0;
                for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                    Meal meal = childSnap.getValue(Meal.class);
                    if (meal != null) {
                        total += meal.getIngredientsList().size();
                        checked += meal.getCheckedCount();
                    }
                }

                mealsPb.setProgress(checked);
                mealsPb.setMax(total);
                String status = checked + " / " + total;
                mealsStatus.setText(status);
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
