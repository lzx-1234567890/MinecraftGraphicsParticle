package com.lzx.minecraftparticle.ui.fragment.draw.graphics;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.FragmentGraphicsBinding;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Vector;
import com.lzx.minecraftparticle.logic.utils.AssetsUtil;
import com.lzx.minecraftparticle.logic.viewModel.DrawViewModel;
import com.lzx.minecraftparticle.ui.adapter.GraphicsParticleAdapter;
import com.lzx.minecraftparticle.ui.renderer.GraphicsGLRenderer;

import java.util.ArrayList;

public class GraphicsFragment extends Fragment implements GraphicsGLRenderer.OnRenderListener {
    protected FragmentGraphicsBinding binding;
    private DrawViewModel viewModel;
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
        viewModel = new ViewModelProvider(requireActivity()).get(DrawViewModel.class);
        viewModel.toCreateGraphics.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    createGraphics(viewModel.getSave());
                    viewModel.toCreateGraphics.setValue(false);
                }
            }
        });
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        renderer = binding.glSurfaceView.getRenderer();
        renderer.setListener(this);
        SharedPreferences sp = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        
        //图标侧滑
        binding.settingsImageView.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });
        
        //帮助
        binding.helpImageView.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            builder.setTitle(getString(R.string.dialog_title_graphics_help));
            builder.setMessage(getString(R.string.dialog_message_graphics_help));
            builder.setPositiveButton(getString(R.string.close), null);
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
        
        int particleBitmapId = sp.getInt("particleBitmap", 0);
        binding.particleImageView.setImageBitmap(partilceBitmaps.get(particleBitmapId));
        renderer.setParticleBitmap(partilceBitmaps.get(particleBitmapId));
        
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
    @Override
    public void update() {
        getActivity().runOnUiThread(() -> {
            if(enableCameraPos) {
                float[] pos = renderer.getCameraPos();
                binding.cameraPosTextView.setText(String.format("%s(%.2f, %.2f, %.2f)", getString(R.string.draw_graphics_camera_position),pos[0], pos[1], pos[2]));
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
    public void createGraphics(Save save){
        renderer.setRendering(false);
        ArrayList<Vector> points = save.getPoints();
        if(points == null) {
            Toast.makeText(requireContext(), getString(R.string.graphics_render_error), Toast.LENGTH_SHORT).show();
        }else {
            renderer.setPoints(points);
            renderer.updateGraphicsVertices();
            renderer.updateGraphicsTextureCoords();
            renderer.updateGraphicsIndices();
            renderer.setRendering(true);
        }
    }

    @Override
    public float getTranslateX() {
        return viewModel.translateX.getValue();
    }

    @Override
    public float getTranslateY() {
        return viewModel.translateY.getValue();
    }

    @Override
    public float getTranslateZ() {
        return viewModel.translateZ.getValue();
    }

    @Override
    public float getAngleX() {
        return viewModel.angleX.getValue();
    }

    @Override
    public float getAngleY() {
        return viewModel.angleY.getValue();
    }

    @Override
    public void setTranslateX(float v) {
        viewModel.translateX.setValue(v);
    }

    @Override
    public void setTranslateY(float v) {
        viewModel.translateY.setValue(v);
    }

    @Override
    public void setTranslateZ(float v) {
        viewModel.translateZ.setValue(v);
    }

    @Override
    public void setAngleX(float v) {
        viewModel.angleX.setValue(v);
    }

    @Override
    public void setAngleY(float v) {
        viewModel.angleY.setValue(v);
    }


}
