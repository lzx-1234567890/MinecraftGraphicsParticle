package com.lzx.minecraftparticle.ui.fragment;
import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.FragmentAboutBinding;
import com.lzx.minecraftparticle.ui.adapter.AboutAdapter;

public class AboutFragment extends Fragment{
    FragmentAboutBinding binding;
    AboutAdapter adapter;
    

    Integer[] imgs = new Integer[]{R.drawable.icon_book, R.drawable.icon_user, R.drawable.icon_sort, R.drawable.icon_cloud};
    
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(getLayoutInflater());

        //tab
        String[] items = new String[]{getString(R.string.about_list_tutorial), getString(R.string.about_list_author), getString(R.string.about_list_update), getString(R.string.about_list_check)};

        //recyclerview
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AboutAdapter(getActivity(), items, imgs);
        recyclerView.setAdapter(adapter);
        //版本号
        MaterialTextView versionTextView = binding.versionTextView;
        try {
            PackageManager packageManager = requireContext().getPackageManager();
            PackageInfo packageInfo;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageInfo = packageManager.getPackageInfo(requireContext().getPackageName(), PackageManager.PackageInfoFlags.of(0));
            }else {
                packageInfo = packageManager.getPackageInfo(requireContext().getPackageName(), 0);
            }
            versionTextView.setText(getString(R.string.current_version) + packageInfo.versionName);
        } catch(PackageManager.NameNotFoundException e) {
            throw new RuntimeException("无法找到名称", e);
        }
        
        return binding.getRoot();
    }

}

