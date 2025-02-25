package com.lzx.minecraftparticle.ui.main_save;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.lzx.minecraftparticle.R;


public class SaveItemManagementFragment extends Fragment{
    SaveItemDialogFragment dialog;
    
    public SaveItemManagementFragment(SaveItemDialogFragment dialog) {
        this.dialog = dialog;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_management, container, false);
        MaterialButton modifyButton = (MaterialButton)view.findViewById(R.id.modifyButton);
        MaterialButton removeButton = (MaterialButton)view.findViewById(R.id.removeButton);
        
        modifyButton.setOnClickListener(v -> {
            dialog.modifyItem();
        });
        
        removeButton.setOnClickListener(v -> {
            dialog.removeItem();
        });
        
        return view;
    }
}
