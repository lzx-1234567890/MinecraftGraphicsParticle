package com.lzx.minecraftparticle.ui.draw;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.ui.draw.param.DrawFragment;


public class DrawResetDialogFragment extends DialogFragment{
    private DrawFragment drawFragment;
    
    public DrawResetDialogFragment(DrawFragment drawFragment){
        super();
        this.drawFragment = drawFragment;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(R.string.action_reset);
        builder.setMessage(R.string.dialog_draw_reset_message);
        builder.setNegativeButton("取消", (dialog, which) -> {});
        builder.setPositiveButton("确定", (dialog, which) -> {
            Toast.makeText(getContext(), "重置成功", Toast.LENGTH_SHORT).show();
            drawFragment.reset();
        });
        
        return builder.create();
    }
}
