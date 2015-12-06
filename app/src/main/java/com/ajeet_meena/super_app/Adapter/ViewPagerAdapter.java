package com.ajeet_meena.super_app.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ajeet_meena.super_app.Fragments.Explore;
import com.ajeet_meena.super_app.Fragments.Picks;
import com.ajeet_meena.super_app.Fragments.Spotlights;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {

        switch ( position ) {
            case 0: {
                Explore explore = new Explore();
                return explore;
            }
            case 1: {
                Spotlights spotlights = new Spotlights();
                return spotlights;
            }
            case 2: {
                Picks picks = new Picks();
                return picks;
            }
            default: {
                return null;
            }

        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}