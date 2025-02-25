package com.lzx.minecraftparticle.ui.main_particle;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.FragmentParticleBinding;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.ui.main_particle.column.ParticleLineFragment;
import com.lzx.minecraftparticle.ui.main_particle.column.ParticleMathFragment;
import com.lzx.minecraftparticle.ui.main_particle.column.ParticleRegularFragment;
import com.lzx.minecraftparticle.ui.main_particle.column.ParticleSpecialFragment;


public class ParticleFragment extends Fragment{
    private FragmentParticleBinding binding;
    private ParticlePagerAdapter particlePagerAdapter;
    
    Integer[] tabs = {R.string.draw_category_regular, R.string.draw_category_line, R.string.draw_category_math, R.string.draw_category_special};
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentParticleBinding.inflate(getLayoutInflater());
        
        //tabLayout
        binding.tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        binding.viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {}

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {}
                });
        
        //ViewPager
        particlePagerAdapter = new ParticlePagerAdapter(getActivity(), new Fragment[]{new ParticleRegularFragment(), new ParticleLineFragment(), new ParticleMathFragment(), new ParticleSpecialFragment()});
        binding.viewPager.setAdapter(particlePagerAdapter);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        // 关联
        new TabLayoutMediator(
                        binding.tabLayout,
                        binding.viewPager,
                        new TabLayoutMediator.TabConfigurationStrategy() {
                            @Override
                            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                tab.setText(getActivity().getString(tabs[position]));
                            }
                        })
                .attach();
        
        return binding.getRoot();
    }

}

