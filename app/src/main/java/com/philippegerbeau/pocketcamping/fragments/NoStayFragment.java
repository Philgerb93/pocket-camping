package com.philippegerbeau.pocketcamping.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.activities.StayEditActivity;

public class NoStayFragment extends Fragment {
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_no_stay, container, false);
        fab = view.findViewById(R.id.fab);

        return view;
    }

    public void stayCreation(View view) {
        Intent i = new Intent(getActivity(), StayEditActivity.class);
        startActivity(i);
    }
}
