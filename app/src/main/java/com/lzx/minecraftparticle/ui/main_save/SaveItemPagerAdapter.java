package com.lzx.minecraftparticle.ui.main_save;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SaveItemPagerAdapter extends FragmentStateAdapter{
    Fragment[] fragments;
    
    public SaveItemPagerAdapter(FragmentActivity fa, Fragment[] fragments){
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
    
    public Fragment[] getFragments() {
        return fragments;
    }
    
}
