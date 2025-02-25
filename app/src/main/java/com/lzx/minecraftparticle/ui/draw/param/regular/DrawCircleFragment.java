package com.lzx.minecraftparticle.ui.draw.param.regular;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.FragmentDrawCircleBinding;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveCircle;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.ui.draw.param.DrawFragment;


public class DrawCircleFragment extends DrawFragment{
    private FragmentDrawCircleBinding binding;
    //private DrawCircleViewModel viewModel;
    private View snackbar;
    
    public DrawCircleFragment(SaveCircle save, int mode) {
        this.mode = mode;
        this.save = save;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDrawCircleBinding.inflate(inflater);
        snackbar = (View)binding.createButton;

        //设置数据
//        viewModel = new ViewModelProvider(this).get(DrawCircleViewModel.class);
//        if(viewModel.getSave().getValue() == null) {
//            viewModel.setSave(this.save);
//        }else {
//            this.save = viewModel.getSave().getValue();
//        }
//        viewModel.getSave().observe(this, input -> {
//            this.save = input;
//        });
        setInputResetData();
        
        //设置监听
        binding.createButton.setOnClickListener(v -> {
            if(updateParam(true)) {
                switch(mode) {
                    case 0:
                        if(PermissionUtil.requestStorage(getActivity())) {
                            DrawUtil.drawCircle((SaveCircle)save);
                            Snackbar s = Snackbar.make(snackbar, "生成成功", Snackbar.LENGTH_SHORT);
                            ((TextView)s.getView().findViewById(com.google.android.material.R.id.snackbar_text)).setTextColor(Color.GREEN);
                            s.show();
                        }else {
                            Toast.makeText(getActivity(), "请先获取所有文件访问权限", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        if(PermissionUtil.requestStorage(getActivity())) {
                            modify();
                            Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                            getActivity().setResult(Activity.RESULT_OK, null);
                            getActivity().finish();
                        }else {
                            Toast.makeText(getActivity(), "请先获取所有文件访问权限", Toast.LENGTH_LONG).show();
                        }
                        
                        
                        break;
                }
                
            };
        });
        
        //设置DrawFragment
        binding.particleParamView.setDrawFragment(this);
        
        //launcher初始化
        binding.filePathParamView.setLauncher(registerForActivityResult(
            new ActivityResultContracts.OpenDocumentTree(),
            uri -> {
                if(uri != null){
                    String path = binding.filePathParamView.getPath(uri);
                    binding.filePathParamView.setInput(path);
                    Toast.makeText(getActivity(), "已选择" + path, Toast.LENGTH_SHORT).show();
                }
        }));
        
        return binding.getRoot();
    }
    
    //设置控件重置数据
    public void setInputResetData() {
        SaveCircle save = (SaveCircle)this.save;
        
        binding.coordinateParamView.setInput(new String[]{save.getCoordinateX(), save.getCoordinateY(), save.getCoordinateZ()});
        binding.hAngleParamView.setInput(save.getHAngle());
        binding.vAngleParamView.setInput(save.getVAngle());
        binding.radiusParamView.setInput(save.getRadius());
        binding.stepParamView.setInput(save.getStep());
        binding.particleParamView.setInput(save.getParticle());
        binding.filePathParamView.setInput(save.getFilePath());
        binding.fileNameParamView.setInput(save.getFileName());
        
        
        binding.coordinateParamView.setResetInput(new String[]{save.getCoordinateX(), save.getCoordinateY(), save.getCoordinateZ()});
        binding.hAngleParamView.setResetInput(save.getHAngle());
        binding.vAngleParamView.setResetInput(save.getVAngle());
        binding.radiusParamView.setResetInput(save.getRadius());
        binding.stepParamView.setResetInput(save.getStep());
        binding.particleParamView.setResetInput(save.getParticle());
        binding.filePathParamView.setResetInput(save.getFilePath());
        binding.fileNameParamView.setResetInput(save.getFileName());
    }
    
    //进行重置
    @Override
    public void reset() {
        SaveCircle save = (SaveCircle)this.save;
        
        binding.coordinateParamView.resetInput();
        binding.hAngleParamView.resetInput();
        binding.vAngleParamView.resetInput();
        binding.radiusParamView.resetInput();
        binding.stepParamView.resetInput();
        binding.particleParamView.resetInput();
        binding.filePathParamView.resetInput();
        binding.fileNameParamView.resetInput();
        save.setCoordinateX(binding.coordinateParamView.getInput(0));
        save.setCoordinateY(binding.coordinateParamView.getInput(1));
        save.setCoordinateZ(binding.coordinateParamView.getInput(2));
        save.setHAngle(binding.hAngleParamView.getInput());
        save.setVAngle(binding.vAngleParamView.getInput());
        save.setRadius(binding.radiusParamView.getInput());
        save.setStep(binding.stepParamView.getInput());
        save.setParticle(binding.particleParamView.getInput());
        save.setFilePath(binding.filePathParamView.getInput());
        save.setFileName(binding.fileNameParamView.getInput());
    }
    
    
    //更新参数
    @Override
    public boolean updateParam(boolean output) {
        try {
            boolean isRight = true;
            //获取数据
            SaveCircle save = (SaveCircle)this.save;
            String coordinate_x = binding.coordinateParamView.getInput(0);
            String coordinate_y = binding.coordinateParamView.getInput(1);
            String coordinate_z = binding.coordinateParamView.getInput(2);
            String hAngle = binding.hAngleParamView.getInput();
            String vAngle = binding.vAngleParamView.getInput();
            String radius = binding.radiusParamView.getInput();
            String step = binding.stepParamView.getInput();
            String particle = binding.particleParamView.getInput();
            String filePath = binding.filePathParamView.getInput();
            String fileName = binding.fileNameParamView.getInput();
            
            //检测
            if(coordinate_x.isEmpty()) {
                binding.coordinateParamView.setError(0);
                isRight = false;
            }else {
                binding.coordinateParamView.clearError(0);
                save.setCoordinateX(coordinate_x);
                //viewModel.getSave().getValue().setCoordinateX(coordinate_x);
                //viewModel.setCoordinateX(coordinate_x);
            }
            
            if(coordinate_y.isEmpty()) {
                binding.coordinateParamView.setError(1);
                isRight = false;
            }else {
                binding.coordinateParamView.clearError(1);
                save.setCoordinateY(coordinate_y);
                //viewModel.getSave().getValue().setCoordinateY(coordinate_y);
                //viewModel.setCoordinateY(coordinate_y);
            }
            
            if(coordinate_z.isEmpty()) {
                binding.coordinateParamView.setError(2);
                isRight = false;
            }else {
                binding.coordinateParamView.clearError(2);
                save.setCoordinateZ(coordinate_z);
                //viewModel.getSave().getValue().setCoordinateZ(coordinate_z);
                //viewModel.setCoordinateZ(coordinate_z);
            }
            
            if(hAngle.isEmpty() || Double.parseDouble(hAngle) > 180 || Double.parseDouble(hAngle) < -180) {
                binding.hAngleParamView.setError("水平角输入错误");
                isRight = false;
            }else {
                binding.hAngleParamView.clearError();
                save.setHAngle(hAngle);
                //viewModel.getSave().getValue().setHAngle(hAngle);
                //viewModel.setHAngle(hAngle);
            }
            
            if(vAngle.isEmpty() || Double.parseDouble(vAngle) > 90 || Double.parseDouble(vAngle) < -90) {
                binding.vAngleParamView.setError("竖直角输入错误");
                isRight = false;
            }else {
                binding.vAngleParamView.clearError();
                save.setVAngle(vAngle);
                //viewModel.getSave().getValue().setVAngle(vAngle);
                //viewModel.setVAngle(vAngle);
            }
            
            if(radius.isEmpty() || Double.parseDouble(radius) <= 0) {
                binding.radiusParamView.setError("半径输入错误");
                isRight = false;
            }else {
                binding.radiusParamView.clearError();
                save.setRadius(radius);
                //viewModel.getSave().getValue().setRadius(radius);
                //viewModel.setRadius(radius);
            }
            
            if(step.isEmpty() || Double.parseDouble(step) <= 0) {
                binding.stepParamView.setError("步长输入错误");
                isRight = false;
            }else {
                binding.stepParamView.clearError();
                save.setStep(step);
                //viewModel.getSave().getValue().setStep(step);
                //viewModel.setStep(step);
            }
            
            if(particle.isEmpty()) {
                binding.particleParamView.setError("粒子输入错误");
                isRight = false;
            }else {
                binding.particleParamView.clearError();
                save.setParticle(particle);
                //viewModel.getSave().getValue().setParticle(particle);
                //viewModel.setParticle(particle);
            }
            
            if(filePath.isEmpty()) {
                binding.filePathParamView.setError("保存路径输入错误");
                isRight = false;
            }else {
                binding.filePathParamView.clearError();
                save.setFilePath(filePath);
                //viewModel.getSave().getValue().setFilePath(filePath);
                //viewModel.setFilePath(filePath);
            }
            
            if(fileName.isEmpty()) {
                binding.fileNameParamView.setError("保存名称输入错误");
                isRight = false;
            }else {
                binding.fileNameParamView.clearError();
                save.setFileName(fileName);
                //viewModel.getSave().getValue().setFileName(fileName);
                //viewModel.setFileName(fileName);
            }
            
            if(!isRight) {
                if(output) {
                    Snackbar s = Snackbar.make(snackbar, "输入有误", Snackbar.LENGTH_SHORT);
                    ((TextView)s.getView().findViewById(com.google.android.material.R.id.snackbar_text)).setTextColor(Color.RED);
                    s.show();
                }
            }
            
            return isRight;
        } catch (Exception e) {
            if(output) {
                Snackbar s = Snackbar.make(snackbar, "输入有误", Snackbar.LENGTH_SHORT);
                ((TextView)s.getView().findViewById(com.google.android.material.R.id.snackbar_text)).setTextColor(Color.RED);
                s.show();
            }
            return false;
        }
    }
}


