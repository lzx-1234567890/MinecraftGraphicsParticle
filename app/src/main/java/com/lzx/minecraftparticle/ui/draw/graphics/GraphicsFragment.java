package com.lzx.minecraftparticle.ui.draw.graphics;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.FragmentGraphicsBinding;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.utils.AssetsUtil;
import java.util.ArrayList;

public abstract class GraphicsFragment extends Fragment{
    protected FragmentGraphicsBinding binding;
    protected GraphicsGLRenderer renderer;
    
    protected ArrayList<Bitmap> partilceBitmaps = new ArrayList<>();
    protected String[] particleNames = 
    {"basic_bubble_particle.png", "basic_flame_particle.png", "blue_flame_particle.png", "crop_growth.png", "heart_particle.png",
    "note_particle.png", "villager_angry.png", "particle0.png", "particle1.png", "particle2.png"};
    
    protected boolean enableCameraPos;
    
    protected float[] speedMax = {100f, 100f, 100f, 3600f, 3600f};
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGraphicsBinding.inflate(inflater);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        renderer = binding.glSurfaceView.getRenderer();
        renderer.setGraphicsFragment(this);
        SharedPreferences sp = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        
        //图标侧滑
        binding.settingsImageView.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });
        
        //帮助
        binding.helpImageView.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            builder.setTitle(getString(R.string.dialog_graphics_help_title));
            builder.setMessage(getString(R.string.dialog_graphics_help_message));
            builder.setPositiveButton("关闭", null);
            builder.show();
        });
        
        //粒子贴图
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        for(int i = 0;i < particleNames.length;i++) {
            Bitmap bitmap = AssetsUtil.loadBitmap(getActivity(), "particles/" + particleNames[i], options);
            Matrix matrix = new Matrix();
            matrix.postScale(10f, 10f);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            partilceBitmaps.add(bitmap);
        }
        ListPopupWindow popupWindow = new ListPopupWindow(getActivity());
        popupWindow.setAdapter(new GraphicsParticleAdapter(getActivity(), R.layout.graphics_particle_item, partilceBitmaps));
        popupWindow.setAnchorView(binding.particleImageView);
        popupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setOnItemClickListener((parent, vieww, position, id) -> {
            binding.particleImageView.setImageBitmap(partilceBitmaps.get(position));
            renderer.setParticleBitmap(partilceBitmaps.get(position));
            editor.putInt("particleBitmap", position);
            editor.apply();
            popupWindow.dismiss();
        });
        
        binding.particleImageView.setOnClickListener(v -> {
            popupWindow.show();
        });
        
        int partcileBitmapId = sp.getInt("particleBitmap", 0);
        binding.particleImageView.setImageBitmap(partilceBitmaps.get(partcileBitmapId));
        renderer.setParticleBitmap(partilceBitmaps.get(partcileBitmapId));
        
        //移动设置
        binding.speedtfEditText.setText(Float.toString(binding.glSurfaceView.getSpeedTranslateForward()));
        binding.speedtfEditText.setOnFocusChangeListener((v, f) -> {
            if(!f) {
                String s = binding.speedtfEditText.getText().toString();
                if(checkSpeedValue(s, 0)) {
                    float value = Float.parseFloat(s);
                    binding.glSurfaceView.setSpeedTranslateForward(value);
                    binding.speedtfEditText.setText(Float.toString(value));
                    editor.putFloat("graphics_speedtf", value);
                    editor.apply();
                }else {
                    binding.glSurfaceView.setSpeedTranslateForward(speedMax[0]);
                    binding.speedtfEditText.setText(Float.toString(speedMax[0]));
                    editor.putFloat("graphics_speedtf", speedMax[0]);
                    editor.apply();
                }
            }
        });
        
        binding.speedtrEditText.setText(Float.toString(binding.glSurfaceView.getSpeedTranslateRight()));
        binding.speedtrEditText.setOnFocusChangeListener((v, f) -> {
            if(!f) {
                String s = binding.speedtrEditText.getText().toString();
                if(checkSpeedValue(s, 1)) {
                    float value = Float.parseFloat(s);
                    binding.glSurfaceView.setSpeedTranslateRight(value);
                    binding.speedtrEditText.setText(Float.toString(value));
                    editor.putFloat("graphics_speedtr", value);
                    editor.apply();
                }else {
                    binding.glSurfaceView.setSpeedTranslateRight(speedMax[1]);
                    binding.speedtrEditText.setText(Float.toString(speedMax[1]));
                    editor.putFloat("graphics_speedtr", speedMax[1]);
                    editor.apply();
                }
            }
        });
        
        binding.speedtuEditText.setText(Float.toString(binding.glSurfaceView.getSpeedTranslateUp()));
        binding.speedtuEditText.setOnFocusChangeListener((v, f) -> {
            if(!f) {
                String s = binding.speedtuEditText.getText().toString();
                if(checkSpeedValue(s, 2)) {
                    float value = Float.parseFloat(s);
                    binding.glSurfaceView.setSpeedTranslateUp(value);
                    binding.speedtuEditText.setText(Float.toString(value));
                    editor.putFloat("graphics_speedtu", value);
                    editor.apply();
                }else {
                    binding.glSurfaceView.setSpeedTranslateUp(speedMax[2]);
                    binding.speedtuEditText.setText(Float.toString(speedMax[2]));
                    editor.putFloat("graphics_speedtu", speedMax[2]);
                    editor.apply();
                }
            }
        });
        
        binding.speedrxEditText.setText(Float.toString(binding.glSurfaceView.getSpeedRotateX()));
        binding.speedrxEditText.setOnFocusChangeListener((v, f) -> {
            if(!f) {
                String s = binding.speedrxEditText.getText().toString();
                if(checkSpeedValue(s, 3)) {
                    float value = Float.parseFloat(s);
                    binding.glSurfaceView.setSpeedRotateX(value);
                    binding.speedrxEditText.setText(Float.toString(value));
                    editor.putFloat("graphics_speedrx", value);
                    editor.apply();
                }else {
                    binding.glSurfaceView.setSpeedRotateX(speedMax[3]);
                    binding.speedrxEditText.setText(Float.toString(speedMax[3]));
                    editor.putFloat("graphics_speedrx", speedMax[3]);
                    editor.apply();
                }
            }
        });
        
        binding.speedryEditText.setText(Float.toString(binding.glSurfaceView.getSpeedRotateY()));
        binding.speedryEditText.setOnFocusChangeListener((v, f) -> {
            if(!f) {
                String s = binding.speedryEditText.getText().toString();
                if(checkSpeedValue(s, 4)) {
                    float value = Float.parseFloat(s);
                    binding.glSurfaceView.setSpeedRotateY(value);
                    binding.speedryEditText.setText(Float.toString(value));
                    editor.putFloat("graphics_speedry", value);
                    editor.apply();
                }else {
                    binding.glSurfaceView.setSpeedRotateY(speedMax[4]);
                    binding.speedryEditText.setText(Float.toString(speedMax[4]));
                    editor.putFloat("graphics_speedry", speedMax[4]);
                    editor.apply();
                }
            }
        });
        
        //显示设置
        binding.gridSwitch.setOnCheckedChangeListener((v, c) -> {
            renderer.setEnableGrid(c);
            editor.putBoolean("graphics_grid", c);
            editor.apply();
        });
        binding.axisSwitch.setOnCheckedChangeListener((v, c) -> {
            renderer.setEnableAxis(c);
            editor.putBoolean("graphics_axis", c);
            editor.apply();
        });
        binding.cameraPosSwitch.setOnCheckedChangeListener((v, c) -> {
            this.enableCameraPos = c;
            editor.putBoolean("graphics_camera_pos", c);
            editor.apply();
        });
        binding.dynamicRenderingSwitch.setOnCheckedChangeListener((v, c) -> {
            renderer.setEnableDynamicRendering(c);
            editor.putBoolean("graphics_dynamic_rendering", c);
            editor.apply();
        });
        
        //初始化设置
        binding.gridSwitch.setChecked(sp.getBoolean("graphics_grid", true));
        binding.axisSwitch.setChecked(sp.getBoolean("graphics_axis", true));
        binding.cameraPosSwitch.setChecked(sp.getBoolean("graphics_camera_pos", false));
        binding.dynamicRenderingSwitch.setChecked(sp.getBoolean("graphics_dynamic_rendering", true));
        
    }
    
    //更新视图
    public void update() {
        getActivity().runOnUiThread(() -> {
            if(enableCameraPos) {
                float[] pos = renderer.getCameraPos();
                binding.cameraPosTextView.setText(String.format("相机坐标(%.2f, %.2f, %.2f)", pos[0], pos[1], pos[2]));
            }else {
                binding.cameraPosTextView.setText("");
            }
        });
    }
    
    //检查速度的设置值
    public boolean checkSpeedValue(String v, int i) {
        try {
            if(v.equals("") || v == "") {
                return false;
            }
            float fv = Float.parseFloat(v);
            if(fv < 0 || fv > speedMax[i]) {
                return false;
            }
        }catch(Exception e) {
            return false;
        }
        return true;
    }
    
    //子类重写
    public abstract void createGraphics(Save save);
}
