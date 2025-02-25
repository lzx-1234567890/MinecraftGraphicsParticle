package com.lzx.minecraftparticle.ui.main_save;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.FragmentSaveBinding;


public class SaveFragment extends Fragment{
    FragmentSaveBinding binding;
    
    SaveAdapter adapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSaveBinding.inflate(getLayoutInflater());
        
        //recyclerview
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new SaveAdapter(this);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new SaveItemTouchHelperCallback(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        adapter.setItemTouchHelper(helper);
        return binding.getRoot();
    }
    
    public void refresh() {
        adapter.refresh();
    }
    
    public void setNonePage(int num) {
        if(num <= 0) {
            binding.noneLayout.setVisibility(View.VISIBLE);
        }else {
            binding.noneLayout.setVisibility(View.GONE);
        }
        
    }

}
