package com.lzx.minecraftparticle.ui.activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.viewModel.MainViewModel;
import com.lzx.minecraftparticle.ui.fragment.UpdateContentDialogFragment;
import com.lzx.minecraftparticle.databinding.ActivityMainBinding;
import com.lzx.minecraftparticle.ui.adapter.NormalPagerAdapter;
import com.lzx.minecraftparticle.ui.fragment.AboutFragment;
import com.lzx.minecraftparticle.ui.fragment.particle.ParticleFragment;
import com.lzx.minecraftparticle.ui.fragment.save.SaveFragment;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private NormalPagerAdapter pagerAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        int saveNum = getSharedPreferences("Save", Context.MODE_PRIVATE).getInt("saveNum", -1) + 1;
        viewModel.saveNum.setValue(saveNum);

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
            Log.e("包名错误", "Error:", e);
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
        pagerAdapter = new NormalPagerAdapter(this, new Fragment[]{new ParticleFragment(), new SaveFragment(), new AboutFragment()});
//        SaveFragment sf = (SaveFragment)getSupportFragmentManager().findFragmentByTag("f1");
//        if(sf != null) {
//            pagerAdapter.updateFragment(1, sf);
//        }
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.setUserInputEnabled(false);
        
        //底部导航栏
        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationViewListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        int saveNum = getSharedPreferences("Save", Context.MODE_PRIVATE).getInt("saveNum", -1) + 1;
        viewModel.saveNum.setValue(saveNum);
    }
    
    //底部导航栏监听接口
    class BottomNavigationViewListener implements BottomNavigationView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            //int saveNum = getSharedPreferences("Save", Context.MODE_PRIVATE).getInt("saveNum", -1) + 1;
            //viewModel.saveNum.setValue(saveNum);
            if (id == R.id.action_particle) {
                binding.viewPager.setCurrentItem(0);
                binding.toolbar.setSubtitle(getString(R.string.toolbar_title_particle));
                return true;
            } else if (id == R.id.action_save) {
                binding.viewPager.setCurrentItem(1);
                binding.toolbar.setSubtitle(getString(R.string.toolbar_title_save));
                return true;
            } else if (id == R.id.action_about) {
                binding.viewPager.setCurrentItem(2);
                binding.toolbar.setSubtitle(getString(R.string.toolbar_title_about));
                return true;
            }
            return false;
        }
    }

}
