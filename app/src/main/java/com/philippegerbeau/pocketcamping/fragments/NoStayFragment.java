package com.philippegerbeau.pocketcamping.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.activities.StayEditActivity;

public class NoStayFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_no_stay, container, false);
    }

    @SuppressWarnings("unused")
    public void stayEdit(View view) {
        Intent i = new Intent(getActivity(), StayEditActivity.class);
        startActivity(i);
    }
}
