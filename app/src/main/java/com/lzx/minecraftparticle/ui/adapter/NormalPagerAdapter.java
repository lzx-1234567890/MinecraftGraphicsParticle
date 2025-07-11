package com.lzx.minecraftparticle.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class NormalPagerAdapter extends FragmentStateAdapter {
    Fragment[] fragments;

    public NormalPagerAdapter(FragmentActivity fa, Fragment[] fragments){
        super(fa);
        this.fragments = fragments;
    }

    @Override
    public @NonNull Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }

    public Fragment getFragment(int position) {
        return fragments[position];
    }

    public void updateFragment(int position, Fragment f) {
        fragments[position] = f;
    }
}
