package com.lzx.minecraftparticle.ui.fragment.particle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.FragmentParticleBinding;
import com.lzx.minecraftparticle.ui.adapter.NormalPagerAdapter;

public class ParticleFragment extends Fragment{
    private FragmentParticleBinding binding;
    private NormalPagerAdapter pagerAdapter;
    
    Integer[] tabs = {R.string.draw_category_regular, R.string.draw_category_line, R.string.draw_category_math, R.string.draw_category_special};
    
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentParticleBinding.inflate(getLayoutInflater());
        
        //tabLayout
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        
        //ViewPager
        pagerAdapter = new NormalPagerAdapter(getActivity(), new Fragment[]{new ParticleRegularFragment(), new ParticleLineFragment(), new ParticleMathFragment(), new ParticleSpecialFragment()});
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(3);

        // 关联
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> tab.setText(getString(tabs[position]))).attach();
        
        return binding.getRoot();
    }

}

