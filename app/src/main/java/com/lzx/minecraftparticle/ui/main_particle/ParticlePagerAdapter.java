package com.lzx.minecraftparticle.ui.main_particle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ParticlePagerAdapter extends FragmentStateAdapter{
    Fragment[] fragments;
    
    public ParticlePagerAdapter(FragmentActivity fa, Fragment[] fragments){
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
    
    public Fragment getCurrentFragment(int position) {
        return fragments[position];
    }

}
