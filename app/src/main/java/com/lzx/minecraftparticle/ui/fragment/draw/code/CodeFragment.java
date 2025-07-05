package com.lzx.minecraftparticle.ui.fragment.draw.code;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.lzx.minecraftparticle.databinding.FragmentCodeBinding;

public class CodeFragment extends Fragment{
    private FragmentCodeBinding binding;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCodeBinding.inflate(inflater);
        binding.commandTextView.setText("particle %s %f %f %f");
        return binding.getRoot();
    }
    
}
