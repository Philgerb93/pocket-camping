package com.philippegerbeau.pocketcamping.fragments;

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

import com.philippegerbeau.pocketcamping.Handler;
import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.adapters.ProfileViewPagerAdapter;

public class ProfileFragment extends Fragment {
    private TextView username;
    private TextView email;
    private ImageView profilePic;

    private Fragment historyFragment;
    private Fragment friendsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        historyFragment = new HistoryFragment();
        friendsFragment = new FriendsFragment();

        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);

        initViewPager(view);
        setUserInfo();

        return view;
    }

    private void initViewPager(View view) {
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter =
                new ProfileViewPagerAdapter(getChildFragmentManager(), getActivity(),
                        historyFragment, friendsFragment);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUserInfo() {
        username.setText(Handler.username);
        email.setText(Handler.email);
        //profilePic.setImageURI(fbUser.getPhotoUrl());
    }

    public void addFriend(View view) {
        ((FriendsFragment) friendsFragment).addFriend(view);
    }

    public void stopInput() {
        ((FriendsFragment) friendsFragment).stopInput();
    }

    public void submit(View view) {
        ((FriendsFragment) friendsFragment).submit(view);
    }
}
