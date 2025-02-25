package com.lzx.minecraftparticle.ui.main_save;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.ActivitySaveModifyBinding;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.ui.draw.DrawResetDialogFragment;
import com.lzx.minecraftparticle.ui.draw.code.CodeFragment;
import com.lzx.minecraftparticle.ui.draw.graphics.GraphicsFragment;
import com.lzx.minecraftparticle.ui.draw.param.DrawFragment;

public class SaveItemModifyActivity extends AppCompatActivity{
    private ActivitySaveModifyBinding binding;
    
    SaveItemModifyPagerAdapter saveItemModifyPagerAdapter;
    private Save save;
    
    private DrawFragment drawFragment;
    private GraphicsFragment graphicsFragment;
    
    private String[] tabs = {"参数", "图形", "代码"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySaveModifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        //toolbar
        MaterialToolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        //Tab
        binding.tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        binding.viewPager.setCurrentItem(tab.getPosition());
                        if(tab.getPosition() == 1) {
                            ((DrawFragment)(saveItemModifyPagerAdapter.getFragment(0))).updateParam(false);
                            ((GraphicsFragment)(saveItemModifyPagerAdapter.getFragment(1))).createGraphics(save);
                        }
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {}
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        binding.viewPager.setCurrentItem(tab.getPosition());
                        if(tab.getPosition() == 1) {
                            ((DrawFragment)(saveItemModifyPagerAdapter.getFragment(0))).updateParam(false);
                            ((GraphicsFragment)(saveItemModifyPagerAdapter.getFragment(1))).createGraphics(save);
                        }
                    }
                });
        
        //ViewPager
        setFragment();
        saveItemModifyPagerAdapter = new SaveItemModifyPagerAdapter(this, new Fragment[]{drawFragment, graphicsFragment, new CodeFragment()});
        binding.viewPager.setAdapter(saveItemModifyPagerAdapter);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.setUserInputEnabled(false);
        // 关联
        new TabLayoutMediator(
                        binding.tabLayout,
                        binding.viewPager,
                        new TabLayoutMediator.TabConfigurationStrategy() {
                            @Override
                            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                tab.setText(tabs[position]);
                            }
                        })
                .attach();
    }
    
    public void setFragment() {
    	Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                save = getIntent().getSerializableExtra("save", Save.class);
            }else {
                save = (Save)(getIntent().getSerializableExtra("save"));
            }
            if(save == null) {
                Toast.makeText(this, "错误的存档，修改失败!", Toast.LENGTH_SHORT).show();
                finish();
            }
            drawFragment = save.getDrawFragment(1);
            graphicsFragment = save.getGraphicsFragment();
            binding.toolbar.setSubtitle(save.getType().getName());
        }else {
            Toast.makeText(this, "错误的存档，修改失败!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_modify_toolbar, menu);
        for(int i = 0;i < menu.size();i++) {
            MenuItem item = menu.getItem(i);
            Drawable icon = item.getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(new PorterDuffColorFilter(getColor(R.color.icon), PorterDuff.Mode.SRC_IN));
                item.setIcon(icon);
            }
        }
        return true;
    }
        
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); 
        if (id == android.R.id.home) {
            finish();
            return true;
        }else if(id == R.id.action_reset) {
            FragmentManager fm = getSupportFragmentManager();
            new DrawResetDialogFragment(drawFragment).show(fm, "重置");
            return true;
        }
        return false;
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    
    
}
