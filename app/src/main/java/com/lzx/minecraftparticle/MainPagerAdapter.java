package com.lzx.minecraftparticle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

public class MainPagerAdapter extends FragmentStateAdapter{
    Fragment[] fragments;
    
    public MainPagerAdapter(FragmentActivity fa, Fragment[] fragments){
        super(fa);
        this.fragments = fragments;
    }
    
    @Override
    public Fragment createFragment(int position) {
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


