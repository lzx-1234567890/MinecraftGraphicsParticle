package com.lzx.minecraftparticle.ui.draw;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.res.Configuration;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.ui.draw.param.DrawFragment;
import java.io.FileNotFoundException;


public class DrawSaveDialogFragment extends DialogFragment{
    //private DrawFragment drawFragment;
    private DrawFragment drawFragment;
    private ActivityResultLauncher<String> launcher;
    private Uri imageUri;
    
    public DrawSaveDialogFragment(DrawFragment df){
        super();
        this.drawFragment = df;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //builder
        View view = getLayoutInflater().inflate(R.layout.dialog_draw_save, null);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        
        builder.setTitle(R.string.action_save);
        builder.setView(view);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("保存", null);
        
        //控件设置
        TextInputEditText nameEditText = (TextInputEditText)view.findViewById(R.id.nameEditText);
        nameEditText.setText(drawFragment.getSave().getType().getName());
        ShapeableImageView imageView = (ShapeableImageView)view.findViewById(R.id.imageView);
        imageView.setImageResource(drawFragment.getSave().getType().getImage());
        imageView.setOnClickListener(v -> {
            launcher.launch("image/*");
        });
        imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
            + getResources().getResourcePackageName(drawFragment.getSave().getType().getImage()) + "/"
            + getResources().getResourceTypeName(drawFragment.getSave().getType().getImage()) + "/"
            + getResources().getResourceEntryName(drawFragment.getSave().getType().getImage()));
        
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
                if(!drawFragment.updateParam(false)) {
                    isRight = false;
                    ((MaterialTextView)view.findViewById(R.id.paramErrorTextView)).setVisibility(View.VISIBLE);
                }else {
                    ((MaterialTextView)view.findViewById(R.id.paramErrorTextView)).setVisibility(View.GONE);
                }
                if(isRight) {
                    Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                    drawFragment.save(nameEditText.getText().toString(), imageUri, new Integer[]{Integer.parseInt(widthEditText.getText().toString()), Integer.parseInt(heightEditText.getText().toString())});
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
            e.printStackTrace();
        }
        return new Integer[]{width, height};
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(getContext(), "若弹窗内容无法正常显示，请旋转屏幕。", Toast.LENGTH_SHORT).show();
    }
}
