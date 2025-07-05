package com.lzx.minecraftparticle.ui.fragment.save;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textview.MaterialTextView;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.viewModel.MainViewModel;

import java.util.LinkedHashMap;


public class SaveItemInformationFragment extends Fragment{
    MainViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        Save save = viewModel.save.getValue();
        View view = inflater.inflate(R.layout.fragment_save_information, container, false);
        LinearLayout layout = view.findViewById(R.id.layout);
        LinkedHashMap<String, String> map = save.getBasicInformation();
        map.putAll(save.getDetailedInformation());
        map.forEach((k, v) -> {
            @SuppressLint("InflateParams") View item = inflater.inflate(R.layout.save_information_item, null);
            MaterialTextView informationTextView = item.findViewById(R.id.informationTextView);
            informationTextView.setText(String.format("%s:%s", k, v));
            layout.addView(item);
        });
        return view;
    }
}

