package com.lzx.minecraftparticle.ui.fragment.save;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.viewModel.MainViewModel;


public class SaveItemManagementFragment extends Fragment{
    MainViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        View view = inflater.inflate(R.layout.fragment_save_management, container, false);
        MaterialButton modifyButton = view.findViewById(R.id.modifyButton);
        MaterialButton removeButton = view.findViewById(R.id.removeButton);
        
        modifyButton.setOnClickListener(v -> viewModel.toModify.setValue(true));
        
        removeButton.setOnClickListener(v -> viewModel.toRemove.setValue(true));
        
        return view;
    }
}
