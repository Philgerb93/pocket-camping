package com.philippegerbeau.pocketcamping.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.fragments.FriendsFragment;
import com.philippegerbeau.pocketcamping.fragments.HistoryFragment;

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public ProfileViewPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
        System.out.println("ADAPTER CREATED");
    }

    @Override
    public int getCount(){
        return 2;
    }

    @Override
    public Fragment getItem (int position) {
        switch (position){
            case 0:
                return new HistoryFragment();
            case 1:
                return new FriendsFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.history);
            case 1:
                return context.getResources().getString(R.string.friends);
            default:
                return null;
        }
    }
}