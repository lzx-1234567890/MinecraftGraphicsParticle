package com.lzx.minecraftparticle.ui.draw;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.ActivityDrawBinding;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveCircle;
import com.lzx.minecraftparticle.ui.draw.code.CodeFragment;
import com.lzx.minecraftparticle.ui.draw.graphics.GraphicsCircleFragment;
import com.lzx.minecraftparticle.ui.draw.graphics.GraphicsFragment;
import com.lzx.minecraftparticle.ui.draw.param.DrawFragment;
import com.lzx.minecraftparticle.ui.draw.param.regular.DrawCircleFragment;


public class DrawActivity extends AppCompatActivity{
    private ActivityDrawBinding binding;
    private DrawViewModel viewModel;
    DrawPagerAdapter drawPagerAdapter;
    
    private String[] tabs = {"参数", "图形", "代码"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        //设置数据
        viewModel = new ViewModelProvider(this).get(DrawViewModel.class);
        setViewModel();
        
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
                            viewModel.getDrawFragment().getValue().updateParam(false);
                            viewModel.getGraphicsFragment().getValue().createGraphics(viewModel.getSave().getValue());
                        }
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {}
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        if(tab.getPosition() == 1) {
                            viewModel.getDrawFragment().getValue().updateParam(false);
                            viewModel.getGraphicsFragment().getValue().createGraphics(viewModel.getSave().getValue());
                        }
                    }
                });

        //ViewPager
        drawPagerAdapter = new DrawPagerAdapter(this, new Fragment[]{viewModel.getDrawFragment().getValue(), viewModel.getGraphicsFragment().getValue(), new CodeFragment()});
        binding.viewPager.setAdapter(drawPagerAdapter);
        binding.viewPager.setUserInputEnabled(false);
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
                                tab.setText(tabs[position]);
                            }
                        })
                .attach();
    }
        
    public void setViewModel() {
        Save save;
        //DrawFragment df;
        //GraphicsFragment gf;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                save = getIntent().getSerializableExtra("save", Save.class);
            }else {
                save = (Save)(getIntent().getSerializableExtra("save"));
            }
            if(save == null) {
                save = new SaveCircle();
            }
//            if(type == Save.Type.CIRCLE) {
//                df = new DrawCircleFragment();
//                gf = new GraphicsCircleFragment();
//            }else{
//                df = new DrawCircleFragment();
//                gf = new GraphicsCircleFragment();
//            }
        }else {
            //type = Save.Type.CIRCLE;
            //df = new DrawCircleFragment();
            //gf = new GraphicsCircleFragment();
            save = new SaveCircle();
        }
//        viewModel.setSave(df.getSave());
//        viewModel.setDrawFragment(df);
//        viewModel.setGraphicsFragment(gf);
        viewModel.setSave(save);
        viewModel.setDrawFragment(save.getDrawFragment(0));
        viewModel.setGraphicsFragment(save.getGraphicsFragment());
        binding.toolbar.setSubtitle(save.getType().getName());
    }
    
    public DrawFragment getDrawFragment() {
        return viewModel.getDrawFragment().getValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.draw_toolbar, menu);
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
        }else if(id == R.id.action_save) {
            FragmentManager fm = getSupportFragmentManager();
            new DrawSaveDialogFragment(viewModel.getDrawFragment().getValue()).show(fm, "保存");
            return true;
        }else if(id == R.id.action_reset) {
            FragmentManager fm = getSupportFragmentManager();
            new DrawResetDialogFragment(viewModel.getDrawFragment().getValue()).show(fm, "重置");
            return true;
        }
        return false;
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    
}

