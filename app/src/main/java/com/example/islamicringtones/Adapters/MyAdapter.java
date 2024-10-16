package com.example.islamicringtones.Adapters;

import android.content.Context;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.islamicringtones.Fragment.RingtonesFrag;
import com.example.islamicringtones.Fragment.StickerFrag;

import java.util.ArrayList;

public class MyAdapter extends FragmentPagerAdapter {

    private Context context;
    private int totalTabs;

    // Constructor
    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
          }

    // Add the updateList() method to update the adapter's data

    @Override
    public Fragment getItem(int position) {
        // Logic to return the fragment based on the position
        switch (position) {
            case 0:
                // Return fragment for SYSTEM APPS
                return new RingtonesFrag();
            case 1:
                // Return fragment for USER APPS
                return new StickerFrag();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}