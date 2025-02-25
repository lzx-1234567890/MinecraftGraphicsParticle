package com.lzx.minecraftparticle.ui.main_particle.column;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.ParticleLineBinding;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.ui.draw.DrawActivity;

public class ParticleLineFragment extends Fragment{
    private ParticleLineBinding binding;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ParticleLineBinding.inflate(getLayoutInflater());
        
        //设置监听
        binding.line.findViewById(R.id.cardView).setOnClickListener(v -> {
            startDrawActivity(null);
        });
        binding.parabola.findViewById(R.id.cardView).setOnClickListener(v -> {
            startDrawActivity(null);
        });
        binding.helix.findViewById(R.id.cardView).setOnClickListener(v -> {
            startDrawActivity(null);
        });
        binding.spiralparabola.findViewById(R.id.cardView).setOnClickListener(v -> {
            startDrawActivity(null);
        });
        binding.arc.findViewById(R.id.cardView).setOnClickListener(v -> {
            startDrawActivity(null);
        });
        
        return binding.getRoot();
    }
    
    public void startDrawActivity(Save save) {
        Intent intent = new Intent(getActivity(), DrawActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("save", save);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }
}
