package com.lzx.minecraftparticle.ui.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.Saves.SaveImage;
import com.lzx.minecraftparticle.logic.viewModel.DrawViewModel;
import com.lzx.minecraftparticle.ui.view.ColorBarPickerView;
import com.lzx.minecraftparticle.ui.view.ColorWheelPickerView;

public class ColorPickerDialogFragment extends DialogFragment implements ColorWheelPickerView.OnColorWheelSelectedListener, ColorBarPickerView.OnColorBarSelectedListener {
    DrawViewModel viewModel;
    AppCompatEditText et;
    View colorView;
    boolean isInput = true;
    boolean isDrag = false;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(DrawViewModel.class);
        SaveImage save = (SaveImage) viewModel.getSave();

        //创建对话
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(R.string.dialog_title_color);
        View view = getLayoutInflater().inflate(R.layout.dialog_colorpicker, null);
        builder.setView(view);
        builder.setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
            save.setColorW(save.getUnconfirmedCW());
            save.setColorB(save.getUnconfirmedCB());
            save.setColorA(save.getUnconfirmedCA());
            viewModel.colorChanged.setValue(true);
            dialog.dismiss();
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            save.setUnconfirmedCW(save.getColorW());
            save.setUnconfirmedCB(save.getColorB());
            save.setUnconfirmedCA(save.getColorA());
            dialog.dismiss();
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        //设置颜色选择
        ColorWheelPickerView colorWheelPickerView = view.findViewById(R.id.colorWheel);
        ColorBarPickerView colorBarPickerView = view.findViewById(R.id.colorBar);
        colorWheelPickerView.setListener(this);
        colorBarPickerView.setListener(this);
        colorWheelPickerView.setColor(Integer.parseInt(save.getUnconfirmedCW()));
        colorBarPickerView.setColor(Integer.parseInt(save.getUnconfirmedCB()));

        //透明度拖动
        AppCompatEditText aet = view.findViewById(R.id.alphaEditText);
        aet.setText(save.getUnconfirmedCA());
        AppCompatSeekBar seekBar = view.findViewById(R.id.seekBar);
        seekBar.setProgress(Integer.parseInt(save.getUnconfirmedCA()));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                isDrag = true;
                aet.setText(String.valueOf(i));
                save.setUnconfirmedCA(String.valueOf(i));
                colorChanged();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        aet.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if(isDrag) {
                    isDrag = false;
                }else {
                    String text = editable.toString();
                    if(!text.isEmpty()) {
                        int alpha = Integer.parseInt(text);
                        if (alpha > 255) {
                            seekBar.setProgress(255);
                            aet.setText("255");
                        } else if (alpha < 0) {
                            seekBar.setProgress(0);
                            aet.setText("0");
                        }else {
                            seekBar.setProgress(alpha);
                        }
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });

        //颜色确定
        int color = mixColor();
        et = view.findViewById(R.id.colorEditText);
        et.setText("#" + String.format("%08X", color));
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if(isInput) {
                        int color = Color.parseColor(editable.toString());
                        int alpha = Color.alpha(color);
                        float[] hsv = new float[3];
                        Color.colorToHSV(color, hsv);
                        save.setUnconfirmedCW(String.valueOf(Color.HSVToColor(new float[]{hsv[0], hsv[1], 1f})));
                        save.setUnconfirmedCB(String.valueOf(Color.HSVToColor(new float[]{0f, 0f, hsv[2]})));
                        save.setUnconfirmedCA(String.valueOf(alpha));
                        colorWheelPickerView.setColor(Integer.parseInt(save.getUnconfirmedCW()));
                        colorBarPickerView.setColor(Integer.parseInt(save.getUnconfirmedCB()));
                        colorWheelPickerView.update();
                        colorBarPickerView.update();
                        seekBar.setProgress(Integer.parseInt(save.getUnconfirmedCA()));
                        colorView.setBackgroundColor(color);
                    }else {
                        isInput = true;
                    }
                }catch (Exception e){}
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });

        colorView = view.findViewById(R.id.colorView);
        colorView.setBackgroundColor(color);

        return dialog;
    }

    @Override
    public void colorWheelSelected(int color) {
        SaveImage save = (SaveImage) viewModel.getSave();
        save.setUnconfirmedCW(String.valueOf(color));
        colorChanged();
    }

    @Override
    public void colorBarSelected(int color) {
        SaveImage save = (SaveImage) viewModel.getSave();
        save.setUnconfirmedCB(String.valueOf(color));
        colorChanged();
    }

    public void colorChanged() {
        int color = mixColor();
        String hex = "#" + String.format("%08X", color);
        isInput = false;
        et.setText(hex);
        colorView.setBackgroundColor(color);
    }


    public int mixColor() {
        SaveImage save = (SaveImage) viewModel.getSave();
        float[] hsvW = new float[3];
        float[] hsvB = new float[3];
        Color.colorToHSV(Integer.parseInt(save.getUnconfirmedCW()), hsvW);
        Color.colorToHSV(Integer.parseInt(save.getUnconfirmedCB()), hsvB);
        float[] hsv = {hsvW[0], hsvW[1], hsvB[2]};
        int color = Color.HSVToColor(Integer.parseInt(save.getUnconfirmedCA()), hsv);
        return color;
    }

}
