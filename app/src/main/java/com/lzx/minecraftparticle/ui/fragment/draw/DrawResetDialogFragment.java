package com.lzx.minecraftparticle.ui.fragment.draw;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.viewModel.DrawViewModel;


public class DrawResetDialogFragment extends DialogFragment{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DrawViewModel viewModel = new ViewModelProvider(requireActivity()).get(DrawViewModel.class);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(getString(R.string.dialog_title_reset));
        builder.setMessage(getString(R.string.dialog_message_reset));
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {});
        builder.setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
            viewModel.toReset.setValue(true);
            Toast.makeText(getContext(), getString(R.string.reset_successfully), Toast.LENGTH_SHORT).show();
        });
        
        return builder.create();
    }
}
