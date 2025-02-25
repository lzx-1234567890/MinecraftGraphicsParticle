package com.lzx.minecraftparticle;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

public class UpdateContentDialogFragment extends DialogFragment{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //builder
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_update_content, null);
        builder.setTitle(getString(R.string.dialog_update_content_title));
        //builder.setMessage(getString(R.string.dialog_update_content_message));
        builder.setView(view);
        ((MaterialTextView)(view.findViewById(R.id.contentTextView))).setText(getString(R.string.dialog_update_content_message));
        builder.setNegativeButton("关闭", null);
        builder.setPositiveButton("不再提示", (dialog, which) -> {
            SharedPreferences sp = getContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("no_update_content", true);
            editor.apply();
        });

        return builder.create();
    }
    
}
