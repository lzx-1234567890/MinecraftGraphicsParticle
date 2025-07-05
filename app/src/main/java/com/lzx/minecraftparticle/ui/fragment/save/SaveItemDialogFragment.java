package com.lzx.minecraftparticle.ui.fragment.save;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.viewModel.MainViewModel;
import com.lzx.minecraftparticle.ui.activity.DrawActivity;
import com.lzx.minecraftparticle.ui.adapter.NormalPagerAdapter;
import com.lzx.minecraftparticle.ui.adapter.SaveAdapter;
//import com.lzx.minecraftparticle.ui.activity.SaveItemModifyActivity;

public class SaveItemDialogFragment extends DialogFragment{
    ActivityResultLauncher<Intent> launcher;
    MainViewModel viewModel;
    ViewPager2 viewPager;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.toModify.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    modifyItem();
                    viewModel.toModify.setValue(false);
                }
            }
        });
        viewModel.toRemove.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    removeItem();
                    viewModel.toRemove.setValue(false);
                }
            }
        });
        //builder
        View view = getLayoutInflater().inflate(R.layout.dialog_save_item, null);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        Save save = viewModel.save.getValue();
        builder.setTitle(save.getName());
        builder.setView(view);
        builder.setNegativeButton(getString(R.string.close), null);
        builder.setPositiveButton(getString(R.string.create), (dialog, which) -> {
            save.draw(getActivity());
            Toast.makeText(getActivity(), getString(R.string.create_successfully), Toast.LENGTH_LONG).show();
        });
        
        //控件设置
        SegmentedButtonGroup segmentedButtonGroup = view.findViewById(R.id.segmentedButtonGroup);
        segmentedButtonGroup.setOnPositionChangedListener(position -> viewPager.setCurrentItem(position));
        
        //ViewPager
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new NormalPagerAdapter(getActivity(), new Fragment[]{new SaveItemInformationFragment(), new SaveItemManagementFragment()}));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setUserInputEnabled(false);
        
        //launcher
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    viewModel.finishModify.setValue(true);
                    Toast.makeText(getContext(), getString(R.string.modify_successfully), Toast.LENGTH_SHORT).show();
                    SaveItemDialogFragment.this.dismiss();
                }
            }
        );
        return builder.show();
    }
    
    public void modifyItem() {
        Intent intent = new Intent(getActivity(), DrawActivity.class);
        intent.putExtra("save", new Gson().toJson(viewModel.save.getValue()));
        intent.putExtra("type", viewModel.save.getValue().getType());
        intent.putExtra("mode", "modify");
        launcher.launch(intent);
    }
    
    public void removeItem() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(getString(R.string.dialog_title_remove));
        builder.setMessage(getString(R.string.dialog_message_remove));
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.setPositiveButton(R.string.remove, (dialog, which) -> {
            viewModel.confirmRemove.setValue(true);
            Toast.makeText(getContext(), R.string.remove_successfully, Toast.LENGTH_SHORT).show();
            SaveItemDialogFragment.this.dismiss();
        });
        builder.show();
    }
}
