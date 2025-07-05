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
import com.lzx.minecraftparticle.databinding.ParticleRegularBinding;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveCircle;
import com.lzx.minecraftparticle.logic.model.Saves.SaveEllipse;
import com.lzx.minecraftparticle.logic.model.Saves.SavePolygon;
import com.lzx.minecraftparticle.logic.model.Saves.SaveSphere;
import com.lzx.minecraftparticle.logic.model.Saves.SaveStar;
import com.lzx.minecraftparticle.ui.activity.DrawActivity;

public class ParticleRegularFragment extends Fragment{
    private  ParticleRegularBinding binding;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ParticleRegularBinding.inflate(getLayoutInflater());
        
        //设置监听
        binding.circle.findViewById(R.id.cardView).setOnClickListener(v -> startDrawActivity(new SaveCircle()));
        binding.ellipse.findViewById(R.id.cardView).setOnClickListener(v -> startDrawActivity(new SaveEllipse()));
        binding.polygon.findViewById(R.id.cardView).setOnClickListener(v -> startDrawActivity(new SavePolygon()));
        binding.star.findViewById(R.id.cardView).setOnClickListener(v -> startDrawActivity(new SaveStar()));
        binding.sphere.findViewById(R.id.cardView).setOnClickListener(v -> startDrawActivity(new SaveSphere()));
        
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
