package com.lzx.minecraftparticle.ui.fragment.particle;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.ParticleMathBinding;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveFourier;
import com.lzx.minecraftparticle.logic.model.Saves.SaveFunction;
import com.lzx.minecraftparticle.ui.activity.DrawActivity;

public class ParticleMathFragment extends Fragment{
    private ParticleMathBinding binding;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ParticleMathBinding.inflate(getLayoutInflater());
        
        //设置监听
        binding.function.findViewById(R.id.cardView).setOnClickListener(v -> {
            startDrawActivity(new SaveFunction());
        });
        binding.fourier.findViewById(R.id.cardView).setOnClickListener(v -> {
            startDrawActivity(new SaveFourier());
        });
        
        return binding.getRoot();
    }
    
    public void startDrawActivity(Save save) {
        Intent intent = new Intent(getActivity(), DrawActivity.class);
        intent.putExtra("save", new Gson().toJson(save));
        intent.putExtra("type", save.getType());
        intent.putExtra("mode", "create");
        getActivity().startActivity(intent);
    }
}
