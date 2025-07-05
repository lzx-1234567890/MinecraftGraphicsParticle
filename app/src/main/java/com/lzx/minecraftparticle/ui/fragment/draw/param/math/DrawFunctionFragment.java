package com.lzx.minecraftparticle.ui.fragment.draw.param.math;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.FragmentDrawFunctionBinding;
import com.lzx.minecraftparticle.logic.Repository;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveFunction;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.logic.viewModel.DrawViewModel;
import com.lzx.minecraftparticle.ui.fragment.draw.ParticleCategoryDialogFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;

public class DrawFunctionFragment extends DrawFragment {
    private FragmentDrawFunctionBinding binding;
    private DrawViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDrawFunctionBinding.inflate(inflater);

        //viewModel
        viewModel = new ViewModelProvider(requireActivity()).get(DrawViewModel.class);
        setData();

        //参数重置
        viewModel.toReset.observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    viewModel.toReset.setValue(false);
                    viewModel.getSave().reset();
                    updateData();
                }
            }
        });

        //粒子选择监听
        binding.particleFunctionParamView.findViewById(R.id.selectButton).setOnClickListener(v -> {
            viewModel.setData(0);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            new ParticleCategoryDialogFragment().show(fm, "选择粒子种类");
        });

        binding.particleXParamView.findViewById(R.id.selectButton).setOnClickListener(v -> {
            viewModel.setData(1);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            new ParticleCategoryDialogFragment().show(fm, "选择粒子种类");
        });

        binding.particleYParamView.findViewById(R.id.selectButton).setOnClickListener(v -> {
            viewModel.setData(2);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            new ParticleCategoryDialogFragment().show(fm, "选择粒子种类");
        });

        viewModel.particleSelected.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                switch ((int)viewModel.getData()) {
                    case 0:
                        binding.particleFunctionParamView.setInput(s);
                        break;
                    case 1:
                        binding.particleXParamView.setInput(s);
                        break;
                    case 2:
                        binding.particleYParamView.setInput(s);
                        break;
                }

            }
        });

        //launcher初始化
        binding.filePathParamView.setLauncher(registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(), uri -> {
            if (uri != null) {
                String path = binding.filePathParamView.getPath(uri);
                binding.filePathParamView.setInput(path);
                Toast.makeText(getActivity(), getString(R.string.has_selected) + path, Toast.LENGTH_SHORT).show();
            }
        }));

        //可用函数按钮监听
        binding.functionButton.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
            builder.setTitle(getString(R.string.dialog_title_function_list));
            builder.setMessage(getString(R.string.draw_param_function_list));
            builder.setPositiveButton(getString(R.string.close), null);
            builder.show();
        });

        //创建图形按钮监听
        binding.createButton.setOnClickListener(v -> {
            Save.ERRORCODE errorcode = viewModel.getSave().check();
            viewModel.errorcode.setValue(errorcode);
            if(errorcode == null) {
                if (PermissionUtil.requestStorage(getActivity())) {
                    if(viewModel.getMode().equals("create")) {
                        DrawUtil.drawFunction((SaveFunction) viewModel.getSave());
                        DrawUtil.displaySnackbar(binding.createButton, getString(R.string.create_successfully), Color.GREEN);
                    }else if(viewModel.getMode().equals("modify")) {
                        Repository.getInstance().modifySave(viewModel.getSave());
                        Intent intent = new Intent();
                        requireActivity().setResult(Activity.RESULT_OK, intent);
                        requireActivity().finish();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.permission_storage_error), Toast.LENGTH_LONG).show();
                }
            }
        });

        viewModel.errorcode.observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Save.ERRORCODE errorcode) {
                clearError();
                if (errorcode != null) {
                    setError(errorcode);
                    DrawUtil.displaySnackbar(binding.createButton, getString(R.string.draw_param_error), Color.RED);
                }
            }
        });

        //按钮文本显示
        if(viewModel.getMode().equals("create")) {
            binding.createButton.setText(getString(R.string.draw_param_create));
        }else if(viewModel.getMode().equals("modify")) {
            binding.createButton.setText(getString(R.string.draw_param_modify));
        }

        return binding.getRoot();
    }


    @Override
    public void setData() {
        SaveFunction save = (SaveFunction) viewModel.getSave();
        //设置数据
        binding.functionParamView.setInput(save.getExp());
        binding.xStartParamView.setInput(save.getXs());
        binding.xEndParamView.setInput(save.getXe());
        binding.coordinateXParamView.setInput(save.getCoordinateX());
        binding.coordinateYParamView.setInput(save.getCoordinateY());
        binding.coordinateZParamView.setInput(save.getCoordinateZ());
        binding.xAxisXParamView.setInput(save.getXAxisX());
        binding.xAxisYParamView.setInput(save.getXAxisY());
        binding.xAxisZParamView.setInput(save.getXAxisZ());
        binding.yAxisXParamView.setInput(save.getYAxisX());
        binding.yAxisYParamView.setInput(save.getYAxisY());
        binding.yAxisZParamView.setInput(save.getYAxisZ());
        binding.scaleXParamView.setInput(save.getScaleX());
        binding.scaleYParamView.setInput(save.getScaleY());
        binding.lengthXParamView.setInput(save.getLengthX());
        binding.lengthYParamView.setInput(save.getLengthX());
        binding.stepParamView.setInput(save.getStep());
        binding.stepXParamView.setInput(save.getStepX());
        binding.stepYParamView.setInput(save.getStepY());
        binding.xAxisPSwitch.setChecked(save.getXAxisP());
        binding.xAxisNSwitch.setChecked(save.getXAxisN());
        binding.yAxisPSwitch.setChecked(save.getYAxisP());
        binding.yAxisNSwitch.setChecked(save.getYAxisN());
        binding.particleFunctionParamView.setInput(save.getParticle());
        binding.particleXParamView.setInput(save.getParticleX());
        binding.particleYParamView.setInput(save.getParticleY());
        binding.filePathParamView.setInput(save.getFilePath());
        binding.fileNameParamView.setInput(save.getFileName());
        //设置文本监听
        binding.functionParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setExp(editable.toString());
            }
        });
        binding.xStartParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setXs(editable.toString());
            }
        });
        binding.xEndParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setXe(editable.toString());
            }
        });
        binding.coordinateXParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setCoordinateX(editable.toString());
            }
        });
        binding.coordinateYParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setCoordinateY(editable.toString());
            }
        });
        binding.coordinateZParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setCoordinateZ(editable.toString());
            }
        });
        binding.xAxisXParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setXAxisX(editable.toString());
            }
        });
        binding.xAxisYParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setXAxisY(editable.toString());
            }
        });
        binding.xAxisZParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setXAxisZ(editable.toString());
            }
        });
        binding.yAxisXParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setYAxisX(editable.toString());
            }
        });
        binding.yAxisYParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setYAxisY(editable.toString());
            }
        });
        binding.yAxisZParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setYAxisZ(editable.toString());
            }
        });
        binding.scaleXParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setScaleX(editable.toString());
            }
        });
        binding.scaleYParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setScaleY(editable.toString());
            }
        });
        binding.lengthXParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setLengthX(editable.toString());
            }
        });
        binding.lengthYParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setLengthY(editable.toString());
            }
        });
        binding.stepParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setStep(editable.toString());
            }
        });
        binding.stepXParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setStepX(editable.toString());
            }
        });
        binding.stepYParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setStepY(editable.toString());
            }
        });
        binding.particleFunctionParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setParticle(editable.toString());
            }
        });
        binding.particleXParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setParticleX(editable.toString());
            }
        });
        binding.particleYParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setParticleY(editable.toString());
            }
        });
        binding.filePathParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setFilePath(editable.toString());
            }
        });
        binding.fileNameParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setFileName(editable.toString());
            }
        });
        //绘制选项监听
        binding.xAxisPSwitch.setOnCheckedChangeListener((v, isChecked) -> {
            save.setXAxisP(isChecked);
        });
        binding.xAxisNSwitch.setOnCheckedChangeListener((v, isChecked) -> {
            save.setXAxisN(isChecked);
        });
        binding.yAxisPSwitch.setOnCheckedChangeListener((v, isChecked) -> {
            save.setYAxisP(isChecked);
        });
        binding.yAxisNSwitch.setOnCheckedChangeListener((v, isChecked) -> {
            save.setYAxisN(isChecked);
        });
    }

    @Override
    public void updateData() {
        SaveFunction save = (SaveFunction) viewModel.getSave();
        binding.functionParamView.setInput(save.getExp());
        binding.xStartParamView.setInput(save.getXs());
        binding.xEndParamView.setInput(save.getXe());
        binding.coordinateXParamView.setInput(save.getCoordinateX());
        binding.coordinateYParamView.setInput(save.getCoordinateY());
        binding.coordinateZParamView.setInput(save.getCoordinateZ());
        binding.xAxisXParamView.setInput(save.getXAxisX());
        binding.xAxisYParamView.setInput(save.getXAxisY());
        binding.xAxisZParamView.setInput(save.getXAxisZ());
        binding.yAxisXParamView.setInput(save.getYAxisX());
        binding.yAxisYParamView.setInput(save.getYAxisY());
        binding.yAxisZParamView.setInput(save.getYAxisZ());
        binding.scaleXParamView.setInput(save.getScaleX());
        binding.scaleYParamView.setInput(save.getScaleY());
        binding.lengthXParamView.setInput(save.getLengthX());
        binding.lengthYParamView.setInput(save.getLengthX());
        binding.stepParamView.setInput(save.getStep());
        binding.stepXParamView.setInput(save.getStepX());
        binding.stepYParamView.setInput(save.getStepY());
        binding.xAxisPSwitch.setChecked(save.getXAxisP());
        binding.xAxisNSwitch.setChecked(save.getXAxisN());
        binding.yAxisPSwitch.setChecked(save.getYAxisP());
        binding.yAxisNSwitch.setChecked(save.getYAxisN());
        binding.particleFunctionParamView.setInput(save.getParticle());
        binding.particleXParamView.setInput(save.getParticleX());
        binding.particleYParamView.setInput(save.getParticleY());
        binding.filePathParamView.setInput(save.getFilePath());
        binding.fileNameParamView.setInput(save.getFileName());
    }

    @Override
    public void clearError() {
        binding.functionParamView.clearError();
        binding.xStartParamView.clearError();
        binding.xEndParamView.clearError();
        binding.coordinateXParamView.clearError();
        binding.coordinateYParamView.clearError();
        binding.coordinateZParamView.clearError();
        binding.xAxisXParamView.clearError();
        binding.xAxisYParamView.clearError();
        binding.xAxisZParamView.clearError();
        binding.yAxisXParamView.clearError();
        binding.yAxisYParamView.clearError();
        binding.yAxisZParamView.clearError();
        binding.scaleXParamView.clearError();
        binding.scaleYParamView.clearError();
        binding.lengthXParamView.clearError();
        binding.lengthYParamView.clearError();
        binding.stepParamView.clearError();
        binding.scaleXParamView.clearError();
        binding.scaleYParamView.clearError();
        binding.particleFunctionParamView.clearError();
        binding.particleXParamView.clearError();
        binding.particleYParamView.clearError();
        binding.filePathParamView.clearError();
        binding.fileNameParamView.clearError();
    }

    @Override
    public void setError(Save.ERRORCODE errorcode) {
        switch(errorcode) {
            case EXP:
                binding.functionParamView.setError(getString(R.string.draw_param_function_exp_error));
                break;
            case XS:
                binding.xStartParamView.setError(requireContext().getString(R.string.draw_param_function_x_start_error));
                break;
            case XE:
                binding.xEndParamView.setError(requireContext().getString(R.string.draw_param_function_x_end_error));
                break;
            case CX:
                binding.coordinateXParamView.setError(requireContext().getString(R.string.draw_param_coordinateX_error));
                break;
            case CY:
                binding.coordinateYParamView.setError(requireContext().getString(R.string.draw_param_coordinateY_error));
                break;
            case CZ:
                binding.coordinateZParamView.setError(requireContext().getString(R.string.draw_param_coordinateZ_error));
                break;
            case XAX:
                binding.xAxisXParamView.setError(requireContext().getString(R.string.draw_param_vectorX_error));
                break;
            case XAY:
                binding.xAxisYParamView.setError(requireContext().getString(R.string.draw_param_vectorY_error));
                break;
            case XAZ:
                binding.xAxisZParamView.setError(requireContext().getString(R.string.draw_param_vectorZ_error));
                break;
            case YAX:
                binding.yAxisXParamView.setError(requireContext().getString(R.string.draw_param_vectorX_error));
                break;
            case YAY:
                binding.yAxisYParamView.setError(requireContext().getString(R.string.draw_param_vectorY_error));
                break;
            case YAZ:
                binding.yAxisZParamView.setError(requireContext().getString(R.string.draw_param_vectorZ_error));
                break;
            case SCALEX:
                binding.scaleXParamView.setError(requireContext().getString(R.string.draw_param_scale_error));
                break;
            case SCALEY:
                binding.scaleYParamView.setError(requireContext().getString(R.string.draw_param_scale_error));
                break;
            case LX:
                binding.lengthXParamView.setError(requireContext().getString(R.string.draw_param_regular_side_length_error));
                break;
            case LY:
                binding.lengthYParamView.setError(requireContext().getString(R.string.draw_param_regular_side_length_error));
                break;
            case STEP:
                binding.stepParamView.setError(requireContext().getString(R.string.draw_param_step_error));
                break;
            case STEPX:
                binding.stepXParamView.setError(requireContext().getString(R.string.draw_param_step_error));
                break;
            case STEPY:
                binding.stepYParamView.setError(requireContext().getString(R.string.draw_param_step_error));
                break;
            case PARTICLE:
                binding.particleFunctionParamView.setError(requireContext().getString(R.string.draw_param_particle_error));
                break;
            case PARTICLEX:
                binding.particleXParamView.setError(requireContext().getString(R.string.draw_param_particle_error));
                break;
            case PARTICLEY:
                binding.particleYParamView.setError(requireContext().getString(R.string.draw_param_particle_error));
                break;
            case FP:
                binding.filePathParamView.setError(requireContext().getString(R.string.draw_param_filePath_error));
                break;
            case FN:
                binding.fileNameParamView.setError(requireContext().getString(R.string.draw_param_fileName_error));
                break;
        }
    }
}
