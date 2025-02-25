package com.lzx.minecraftparticle.ui.main_about;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment{
    FragmentAboutBinding binding;
    AboutAdapter adapter;
    
    String[] items = new String[]{"使用教程", "作者信息", "更新介绍", "检查更新"};
    Integer[] imgs = new Integer[]{R.drawable.icon_book, R.drawable.icon_user, R.drawable.icon_sort, R.drawable.icon_cloud};
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(getLayoutInflater());
        
        //recyclerview
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AboutAdapter(getActivity(), items, imgs);
        recyclerView.setAdapter(adapter);

        //版本号
        MaterialTextView versionTextView = binding.versionTextView;
        try {
            PackageManager packageManager = getContext().getPackageManager();
            PackageInfo packageInfo;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), PackageManager.PackageInfoFlags.of(0));
            }else {
                packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
            }
            versionTextView.setText("当前版本：" + packageInfo.versionName);
        } catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        
        return binding.getRoot();
    }

}

