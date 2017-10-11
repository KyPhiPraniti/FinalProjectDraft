package com.kyphipraniti.fitnesstasks.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.kyphipraniti.fitnesstasks.fragments.CalendarFragment;
import com.kyphipraniti.fitnesstasks.fragments.SummaryFragment;
import com.kyphipraniti.fitnesstasks.fragments.WorkoutFragment;

public class SectionsPagerAdapter extends SmartFragmentStatePagerAdapter {
    private final static int NUM_ITEMS = 3;
    private final String tabTitles[] = new String[]{ "Calendar", "Workout", "Summary" };
    private Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CalendarFragment();
        } else if (position == 1) {
            return new WorkoutFragment();
        } else if (position == 2) {
            return new SummaryFragment();
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
