package com.lzx.minecraftparticle.ui.fragment.particle;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.ParticleLineBinding;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveArc;
import com.lzx.minecraftparticle.logic.model.Saves.SaveHelix;
import com.lzx.minecraftparticle.logic.model.Saves.SaveLine;
import com.lzx.minecraftparticle.logic.model.Saves.SaveParabola;
import com.lzx.minecraftparticle.logic.model.Saves.SaveSprialParabola;
import com.lzx.minecraftparticle.ui.activity.DrawActivity;

public class ParticleLineFragment extends Fragment{
    private ParticleLineBinding binding;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ParticleLineBinding.inflate(getLayoutInflater());
        
        //设置监听
        binding.line.findViewById(R.id.cardView).setOnClickListener(v -> {
            startDrawActivity(new SaveLine());
        });
        binding.parabola.findViewById(R.id.cardView).setOnClickListener(v -> {
            startDrawActivity(new SaveParabola());
        });
        binding.helix.findViewById(R.id.cardView).setOnClickListener(v -> {
            startDrawActivity(new SaveHelix());
        });
        binding.spiralparabola.findViewById(R.id.cardView).setOnClickListener(v -> {
            startDrawActivity(new SaveSprialParabola());
        });
        binding.arc.findViewById(R.id.cardView).setOnClickListener(v -> {
            startDrawActivity(new SaveArc());
        });
        
        return binding.getRoot();
    }
    
    public void startDrawActivity(Save save) {
        Intent intent = new Intent(getActivity(), DrawActivity.class);
        intent.putExtra("save", new Gson().toJson(save));
        intent.putExtra("type", save.getType());
        intent.putExtra("mode", "create");
        requireActivity().startActivity(intent);
    }
}
