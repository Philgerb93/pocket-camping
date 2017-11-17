package com.philippegerbeau.pocketcamping.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.philippegerbeau.pocketcamping.R;

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private Fragment historyFragment;
    private Fragment friendsFragment;

    public ProfileViewPagerAdapter(FragmentManager fm, Context context,
                                   Fragment histFrag, Fragment friendsFrag){
        super(fm);
        this.context = context;
        this.historyFragment = histFrag;
        this.friendsFragment = friendsFrag;
    }

    @Override
    public int getCount(){
        return 2;
    }

    @Override
    public Fragment getItem (int position) {
        switch (position){
            case 0:
                return historyFragment;
            case 1:
                return friendsFragment;
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