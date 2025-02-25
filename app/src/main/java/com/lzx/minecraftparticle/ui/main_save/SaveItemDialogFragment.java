package com.lzx.minecraftparticle.ui.main_save;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.Save;

public class SaveItemDialogFragment extends DialogFragment{
    ActivityResultLauncher<Intent> launcher;
    SaveAdapter adapter;
    Save save;
    
    ViewPager2 viewPager;
    
    public SaveItemDialogFragment(SaveAdapter adapter, Save save) {
        super();
        this.adapter = adapter;
        this.save = save;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //builder
        View view = getLayoutInflater().inflate(R.layout.dialog_save_item, null);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(save.getName());
        builder.setView(view);
        builder.setNegativeButton("关闭", null);
        builder.setPositiveButton("生成", (dialog, which) -> {
            save.draw(getActivity());
            Toast.makeText(getActivity(), "生成成功", Toast.LENGTH_LONG).show();
        });
        
        //控件设置
        SegmentedButtonGroup segmentedButtonGroup = view.findViewById(R.id.segmentedButtonGroup);
        segmentedButtonGroup.setOnPositionChangedListener(position -> {
            viewPager.setCurrentItem(position);
        });
        
        //ViewPager
        viewPager = (ViewPager2)view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new SaveItemPagerAdapter(getActivity(), new Fragment[]{new SaveItemInformationFragment(save), new SaveItemManagementFragment(this)}));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setUserInputEnabled(false);
        
        //launcher
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    adapter.notifyItemChanged(save.getId());
                    SaveItemDialogFragment.this.dismiss();
                }
            }
        );
        
        Dialog dialog = builder.create();
        return dialog;
    }
    
    public void modifyItem() {
        Intent intent = new Intent(getActivity(), SaveItemModifyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("save", save);
        intent.putExtras(bundle);
        launcher.launch(intent);
    }
    
    public void removeItem() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(getString(R.string.action_remove));
        builder.setMessage(getString(R.string.dialog_save_item_remove_message));
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("移除", (dialog, which) -> {
            adapter.removeItem(save.getId());
            Toast.makeText(getContext(), "成功移除", Toast.LENGTH_SHORT).show();
            SaveItemDialogFragment.this.dismiss();
        });
        builder.show();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(getContext(), "若弹窗内容无法正常显示，请旋转屏幕。", Toast.LENGTH_SHORT).show();
    }
}
