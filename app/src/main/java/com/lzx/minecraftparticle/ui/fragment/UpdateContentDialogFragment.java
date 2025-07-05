package com.lzx.minecraftparticle.ui.fragment;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.lzx.minecraftparticle.R;

public class UpdateContentDialogFragment extends DialogFragment{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //builder
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_update_content, null);
        builder.setTitle(getString(R.string.dialog_title_update_content));
        //builder.setMessage(getString(R.string.dialog_update_content_message));
        builder.setView(view);
        ((MaterialTextView)(view.findViewById(R.id.contentTextView))).setText(getString(R.string.dialog_message_update_content));
        builder.setNegativeButton(getString(R.string.close), null);
        builder.setPositiveButton(getString(R.string.nolonger), (dialog, which) -> {
            SharedPreferences sp = requireContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("no_update_content", true);
            editor.apply();
        });

        return builder.create();
    }
    
}
