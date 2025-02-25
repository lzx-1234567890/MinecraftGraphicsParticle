package com.lzx.minecraftparticle;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lzx.minecraftparticle.databinding.ActivityMainBinding;
import com.lzx.minecraftparticle.ui.main_about.AboutFragment;
import com.lzx.minecraftparticle.ui.main_particle.ParticleFragment;
import com.lzx.minecraftparticle.ui.main_save.SaveFragment;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainPagerAdapter mainPagerAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        //更新内容弹窗
        long newCode = 0;
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.PackageInfoFlags.of(0));
            }else {
                packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                newCode = packageInfo.getLongVersionCode();
            }else {
                newCode = packageInfo.versionCode;
            }
        } catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SharedPreferences sp = getSharedPreferences("Global", Context.MODE_PRIVATE);
        long oldCode = sp.getLong("version_code", 0);
        
        if(newCode > oldCode) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("no_update_content", false);
            editor.putLong("version_code", newCode);
            editor.apply();
        }
        
        if(!sp.getBoolean("no_update_content", false)) {
            new UpdateContentDialogFragment().show(getSupportFragmentManager(), "更新内容");
        }
        
        
        //ViewPager
        mainPagerAdapter = new MainPagerAdapter(this, new Fragment[]{new ParticleFragment(), new SaveFragment(), new AboutFragment()});
        SaveFragment sf = (SaveFragment)getSupportFragmentManager().findFragmentByTag("f1");
        if(sf != null) {
            mainPagerAdapter.updateFragment(1, sf);
        }
        binding.viewPager.setAdapter(mainPagerAdapter);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.setUserInputEnabled(false);
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
        
        //底部导航栏
        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationViewListener());
    }
    
    //底部导航栏监听接口
    class BottomNavigationViewListener implements BottomNavigationView.OnItemSelectedListener {
        FragmentManager fragmentManager = getSupportFragmentManager();
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_particle) {
                binding.viewPager.setCurrentItem(0);
                binding.toolbar.setSubtitle(getString(R.string.main_particle));
                return true;
            } else if (id == R.id.action_save) {
                binding.viewPager.setCurrentItem(1);
                binding.toolbar.setSubtitle(getString(R.string.main_save));
                ((SaveFragment)(mainPagerAdapter.getFragment(1))).refresh();;
                return true;
            } else if (id == R.id.action_about) {
                binding.viewPager.setCurrentItem(2);
                binding.toolbar.setSubtitle(getString(R.string.main_about));
                return true;
            }
            return false;
        }
    }
}
