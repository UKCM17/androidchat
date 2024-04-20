package com.example.newchatchat.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.newchatchat.Fragments.CallsFragment;
import com.example.newchatchat.Fragments.ChatFragment;
import com.example.newchatchat.Fragments.StatusFragment;

public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
    switch (position)
    {
        case 0: return new ChatFragment();
        case 1: return new StatusFragment();
        case 2: return new CallsFragment();
        default: return new ChatFragment();

    }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title=null;
        switch (position){
            case 0:title="CHATS";break;
            case 1:title="STATUS";break;
            case 2:title="CALLS";break;
        }
        return title;
    }
}
