package com.lzx.minecraftparticle.ui.fragment.save;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lzx.minecraftparticle.databinding.FragmentSaveBinding;
import com.lzx.minecraftparticle.logic.viewModel.MainViewModel;
import com.lzx.minecraftparticle.ui.adapter.SaveAdapter;
import com.lzx.minecraftparticle.ui.callback.SaveItemTouchHelperCallback;


public class SaveFragment extends Fragment implements SaveAdapter.OnSaveAdapterRequestListener {
    FragmentSaveBinding binding;
    MainViewModel viewModel;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSaveBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        //观察存档数量是否改变
        viewModel.saveNum.observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Integer integer) {
                SaveAdapter adapter = (SaveAdapter) binding.recyclerView.getAdapter();
                if (viewModel.preSaveNum.getValue() < integer) {
                    adapter.notifyItemRangeInserted(viewModel.preSaveNum.getValue(), integer);
                }
                if(integer <= 0) {
                    binding.noneLayout.setVisibility(View.VISIBLE);
                }else {
                    binding.noneLayout.setVisibility(View.GONE);
                }
                viewModel.preSaveNum.setValue(integer);
            }
        });
        //保存修改完成后
        viewModel.finishModify.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    ((SaveAdapter)binding.recyclerView.getAdapter()).notifyItemChanged(viewModel.save.getValue().getId());
                    viewModel.finishModify.setValue(false);
                }
            }
        });
        //删除保存
        viewModel.confirmRemove.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    ((SaveAdapter)binding.recyclerView.getAdapter()).removeItem(viewModel.save.getValue().getId());
                    viewModel.confirmRemove.setValue(false);
                }
            }
        });
        
        //recyclerview
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        SaveAdapter adapter = new SaveAdapter(this);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new SaveItemTouchHelperCallback(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        adapter.setItemTouchHelper(helper);
        return binding.getRoot();
    }

    @Override
    public int getSaveNum() {
        return viewModel.saveNum.getValue();
    }

    @Override
    public void onRemoveSave(){
        int saveNum = requireActivity().getSharedPreferences("Save", Context.MODE_PRIVATE).getInt("saveNum", -1) + 1;
        viewModel.saveNum.setValue(saveNum);
    }

    @Override
    public void onSelected(SaveAdapter.ViewHolder holder) {
        viewModel.save.setValue(holder.getSave());
        new SaveItemDialogFragment().show(requireActivity().getSupportFragmentManager(), "保存项目");
    }

}
