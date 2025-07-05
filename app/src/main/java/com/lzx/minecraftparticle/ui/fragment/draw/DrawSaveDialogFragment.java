package com.lzx.minecraftparticle.ui.fragment.draw;
import android.app.Dialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.Repository;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.viewModel.DrawViewModel;
import java.io.FileNotFoundException;


public class DrawSaveDialogFragment extends DialogFragment{
    private ActivityResultLauncher<String> launcher;
    private Uri imageUri;
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DrawViewModel viewModel = new ViewModelProvider(requireActivity()).get(DrawViewModel.class);
        //builder
        View view = getLayoutInflater().inflate(R.layout.dialog_draw_save, null);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        
        builder.setTitle(getString(R.string.dialog_title_save));
        builder.setView(view);
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.setPositiveButton(getString(R.string.save), null);
        
        //控件设置
        TextInputEditText nameEditText = (TextInputEditText)view.findViewById(R.id.nameEditText);
        nameEditText.setText(viewModel.getSave().getType().getName());
        ShapeableImageView imageView = (ShapeableImageView)view.findViewById(R.id.imageView);
        imageView.setImageResource(viewModel.getSave().getType().getImage());
        imageView.setOnClickListener(v -> {
            launcher.launch("image/*");
        });

        imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
            + getResources().getResourcePackageName(viewModel.getSave().getType().getImage()) + "/"
            + getResources().getResourceTypeName(viewModel.getSave().getType().getImage()) + "/"
            + getResources().getResourceEntryName(viewModel.getSave().getType().getImage()));
        
        TextInputEditText widthEditText = (TextInputEditText)view.findViewById(R.id.widthEditText);
        TextInputEditText heightEditText = (TextInputEditText)view.findViewById(R.id.heightEditText);

        Integer[] size = getImageSize();
        int minSize = Math.min(size[0], size[1]);
        if(minSize > 250) {
            minSize = 250;
        }
        size[0] = minSize;
        size[1] = minSize;

        widthEditText.setText(Integer.toString(size[0]));
        heightEditText.setText(Integer.toString(size[1]));
        
        //launcher初始化
        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if(uri != null) {
                    imageView.setImageURI(uri);
                    imageUri = uri;
                    Integer[] _size = getImageSize();
                    widthEditText.setText(Integer.toString(_size[0]));
                    heightEditText.setText(Integer.toString(_size[1]));
                }
            });
        
        TextInputLayout nameTextInputLayout = (TextInputLayout)view.findViewById(R.id.nameTextInputLayout);
        TextInputLayout widthTextInputLayout = (TextInputLayout)view.findViewById(R.id.widthTextInputLayout);
        TextInputLayout heightTextInputLayout = (TextInputLayout)view.findViewById(R.id.heightTextInputLayout);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            ((AlertDialog)d).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                boolean isRight = true;

                if(nameEditText.getText().toString().isEmpty()) {
                    isRight = false;
                    nameTextInputLayout.setErrorEnabled(true);
                    nameTextInputLayout.setError(getString(R.string.dialog_draw_save_name_error));
                }else {
                    nameTextInputLayout.setErrorEnabled(false);
                }

                if(widthEditText.getText().toString().isEmpty() || Integer.parseInt(widthEditText.getText().toString()) <= 0) {
                    isRight = false;
                    widthTextInputLayout.setErrorEnabled(true);
                    widthTextInputLayout.setError(getString(R.string.dialog_draw_save_width_error));
                }else {
                    widthTextInputLayout.setErrorEnabled(false);
                }

                if(heightEditText.getText().toString().isEmpty() || Integer.parseInt(heightEditText.getText().toString()) <= 0) {
                    isRight = false;
                    heightTextInputLayout.setErrorEnabled(true);
                    heightTextInputLayout.setError(getString(R.string.dialog_draw_save_height_error));
                }else {
                    heightTextInputLayout.setErrorEnabled(false);
                }

                Save.ERRORCODE errorcode = viewModel.getSave().check();
                viewModel.errorcode.setValue(errorcode);

                if(viewModel.errorcode.getValue() == null) {
                    view.findViewById(R.id.paramErrorTextView).setVisibility(View.GONE);
                }else {
                    isRight = false;
                    view.findViewById(R.id.paramErrorTextView).setVisibility(View.VISIBLE);
                }

                if(isRight) {
                    Toast.makeText(getContext(), getString(R.string.save_successfully), Toast.LENGTH_SHORT).show();
                    viewModel.getSave().setName(nameEditText.getText().toString());
                    viewModel.getSave().setImageUri(imageUri.toString());
                    Repository.getInstance().save(viewModel.getSave(), new Integer[]{Integer.parseInt(widthEditText.getText().toString()), Integer.parseInt(heightEditText.getText().toString())});
                    d.dismiss();
                }
            });
        });
        return dialog;
    }
    
    public Integer[] getImageSize() {
        int width = 0;
        int height = 0;
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            bitmap.recycle();
        }catch (FileNotFoundException e) {
            throw new RuntimeException("获取图片大小时出错", e);
        }
        return new Integer[]{width, height};
    }
}
