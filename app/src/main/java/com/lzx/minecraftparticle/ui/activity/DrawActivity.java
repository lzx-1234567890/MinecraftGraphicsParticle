package com.lzx.minecraftparticle.ui.activity;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.ActivityDrawBinding;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveArc;
import com.lzx.minecraftparticle.logic.model.Saves.SaveCircle;
import com.lzx.minecraftparticle.logic.model.Saves.SaveEllipse;
import com.lzx.minecraftparticle.logic.model.Saves.SaveFourier;
import com.lzx.minecraftparticle.logic.model.Saves.SaveFunction;
import com.lzx.minecraftparticle.logic.model.Saves.SaveHelix;
import com.lzx.minecraftparticle.logic.model.Saves.SaveImage;
import com.lzx.minecraftparticle.logic.model.Saves.SaveLine;
import com.lzx.minecraftparticle.logic.model.Saves.SaveParabola;
import com.lzx.minecraftparticle.logic.model.Saves.SavePolygon;
import com.lzx.minecraftparticle.logic.model.Saves.SaveSphere;
import com.lzx.minecraftparticle.logic.model.Saves.SaveSprialParabola;
import com.lzx.minecraftparticle.logic.model.Saves.SaveStar;
import com.lzx.minecraftparticle.logic.viewModel.DrawViewModel;
import com.lzx.minecraftparticle.ui.adapter.NormalPagerAdapter;
import com.lzx.minecraftparticle.ui.fragment.draw.DrawResetDialogFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.DrawSaveDialogFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.code.CodeFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.graphics.GraphicsFragment;

import java.util.Objects;

public class DrawActivity extends AppCompatActivity{
    private ActivityDrawBinding binding;
    private NormalPagerAdapter pagerAdapter;
    private DrawViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载布局
        super.onCreate(savedInstanceState);
        binding = ActivityDrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //tab
        final String[] tabs = {getString(R.string.draw_tab_param), getString(R.string.draw_tab_graphics)};

        //viewModel的数据绑定
        viewModel = new ViewModelProvider(this).get(DrawViewModel.class);
        if(viewModel.getSave() == null) {
            initViewModel();
        }

        //toolbar
        MaterialToolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Tab
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        pagerAdapter = new NormalPagerAdapter(this, new Fragment[]{viewModel.getSave().getDrawFragment(), new GraphicsFragment()});
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if(position == 1) {
                    if(viewModel.getSave().check() == null) {
                        viewModel.toCreateGraphics.setValue(true);
                    }else {
                        Toast.makeText(DrawActivity.this, getString(R.string.param_to_graphics_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 关联
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> tab.setText(tabs[position])).attach();
    }

    private void initViewModel() {
        Save.Type type = (Save.Type) getIntent().getSerializableExtra("type");
        String json = getIntent().getStringExtra("save");
        String mode = getIntent().getStringExtra("mode");
        Save save;
        switch (type) {
            case CIRCLE:
                save = new Gson().fromJson(json, SaveCircle.class);
                break;
            case ELLIPSE:
                save = new Gson().fromJson(json, SaveEllipse.class);
                break;
            case POLYGON:
                save = new Gson().fromJson(json, SavePolygon.class);
                break;
            case STAR:
                save = new Gson().fromJson(json, SaveStar.class);
                break;
            case SPHERE:
                save = new Gson().fromJson(json, SaveSphere.class);
                break;
            case LINE:
                save = new Gson().fromJson(json, SaveLine.class);
                break;
            case PARABOLA:
                save = new Gson().fromJson(json, SaveParabola.class);
                break;
            case HELIX:
                save = new Gson().fromJson(json, SaveHelix.class);
                break;
            case SPIRALPARABOLA:
                save = new Gson().fromJson(json, SaveSprialParabola.class);
                break;
            case ARC:
                save = new Gson().fromJson(json, SaveArc.class);
                break;
            case FOURIER:
                save = new Gson().fromJson(json, SaveFourier.class);
                break;
            case FUNCTION:
                save = new Gson().fromJson(json, SaveFunction.class);
                break;
            case IMAGE:
                save = new Gson().fromJson(json, SaveImage.class);
                break;
            default:
                save = new Gson().fromJson(json, SaveCircle.class);
        }
        save.setReset();
        viewModel.setSave(save);
        viewModel.setMode(mode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.draw_toolbar, menu);
        for (int i = 0; i < menu.size(); i++) {
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
            new DrawSaveDialogFragment().show(fm, "保存");
            return true;
        }else if(id == R.id.action_reset) {
            FragmentManager fm = getSupportFragmentManager();
            new DrawResetDialogFragment().show(fm, "重置");
            return true;
        }
        return false;
    }
}
