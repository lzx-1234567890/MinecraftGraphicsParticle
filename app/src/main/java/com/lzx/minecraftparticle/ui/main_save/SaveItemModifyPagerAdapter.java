package com.lzx.minecraftparticle.ui.main_save;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SaveItemModifyPagerAdapter extends FragmentStateAdapter{
    Fragment[] fragments;
    
    public SaveItemModifyPagerAdapter(FragmentActivity fa, Fragment[] fragments){
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
    
    public Fragment getFragment(int index) {
        return fragments[index];
    }
    
}

