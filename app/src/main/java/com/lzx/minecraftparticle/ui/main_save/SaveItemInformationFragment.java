package com.lzx.minecraftparticle.ui.main_save;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textview.MaterialTextView;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.Save;
import java.util.LinkedHashMap;


public class SaveItemInformationFragment extends Fragment{
    Save save;
    
    public SaveItemInformationFragment(Save save) {
        this.save = save;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_information, container, false);
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.layout);
        LinkedHashMap<String, String> map = save.getBasicInformation();
        map.putAll(save.getDetailedInformation());
        map.forEach((k, v) -> {
            View item = inflater.inflate(R.layout.save_information_item, null);
            MaterialTextView informationTextView = (MaterialTextView)item.findViewById(R.id.informationTextView);
            informationTextView.setText(String.format("%s:%s", k, v));
            layout.addView(item);
        });
        return view;
    }
}

