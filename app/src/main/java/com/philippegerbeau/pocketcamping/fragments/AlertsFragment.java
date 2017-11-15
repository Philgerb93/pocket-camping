package com.philippegerbeau.pocketcamping.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philippegerbeau.pocketcamping.Handler;
import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.adapters.AlertsListViewAdapter;
import com.philippegerbeau.pocketcamping.data.Alert;
import com.philippegerbeau.pocketcamping.data.Container;

import java.util.ArrayList;

public class AlertsFragment extends Fragment {
    private ArrayList<Alert> alerts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alerts, container, false);
        final ListView listView = view.findViewById(R.id.list_view);
        final TextView noAlerts = view.findViewById(R.id.no_alerts);

        FirebaseDatabase.getInstance().getReference().child("users").child(Handler.user.getUserID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("stayID").exists()) {
                            noAlerts.setVisibility(View.INVISIBLE);

                            alerts = new ArrayList<>();
                            final AlertsListViewAdapter adapter = new AlertsListViewAdapter(getActivity(), alerts);
                            listView.setAdapter(adapter);

                            DatabaseReference fbStayRef = FirebaseDatabase.getInstance().getReference().child("stays")
                                    .child(Handler.user.getStayID());
                            fbStayRef.child("alerts").limitToLast(20)
                                    .addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            Alert alert = dataSnapshot.getValue(Alert.class);

                                            if (alert != null) {
                                                alerts.add(alert);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        } else {
                            noAlerts.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return view;
    }
}
