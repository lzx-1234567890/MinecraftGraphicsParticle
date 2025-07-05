package com.lzx.minecraftparticle.ui.fragment.draw.param.line;

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

import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.FragmentDrawLineBinding;
import com.lzx.minecraftparticle.logic.Repository;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveLine;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.logic.viewModel.DrawViewModel;
import com.lzx.minecraftparticle.ui.fragment.draw.ParticleCategoryDialogFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;

public class DrawLineFragment extends DrawFragment {
    private FragmentDrawLineBinding binding;
    private DrawViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDrawLineBinding.inflate(inflater);

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
        binding.particleParamView.findViewById(R.id.selectButton).setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            new ParticleCategoryDialogFragment().show(fm, "选择粒子种类");
        });

        viewModel.particleSelected.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.particleParamView.setInput(s);
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

        //创建图形按钮监听
        binding.createButton.setOnClickListener(v -> {
            Save.ERRORCODE errorcode = viewModel.getSave().check();
            viewModel.errorcode.setValue(errorcode);
            if(errorcode == null) {
                if (PermissionUtil.requestStorage(getActivity())) {
                    if(viewModel.getMode().equals("create")) {
                        DrawUtil.drawLine((SaveLine) viewModel.getSave());
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
        SaveLine save = (SaveLine) viewModel.getSave();
        //设置数据
        binding.coordinateXSParamView.setInput(save.getCoordinateXS());
        binding.coordinateYSParamView.setInput(save.getCoordinateYS());
        binding.coordinateZSParamView.setInput(save.getCoordinateZS());
        binding.coordinateXEParamView.setInput(save.getCoordinateXE());
        binding.coordinateYEParamView.setInput(save.getCoordinateYE());
        binding.coordinateZEParamView.setInput(save.getCoordinateZE());
        binding.stepParamView.setInput(save.getStep());
        binding.particleParamView.setInput(save.getParticle());
        binding.filePathParamView.setInput(save.getFilePath());
        binding.fileNameParamView.setInput(save.getFileName());
        //设置文本监听
        binding.coordinateXSParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setCoordinateXS(editable.toString());
            }
        });
        binding.coordinateYSParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setCoordinateYS(editable.toString());
            }
        });
        binding.coordinateZSParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setCoordinateZS(editable.toString());
            }
        });
        binding.coordinateXEParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setCoordinateXE(editable.toString());
            }
        });
        binding.coordinateYEParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setCoordinateYE(editable.toString());
            }
        });
        binding.coordinateZEParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setCoordinateZE(editable.toString());
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
        binding.particleParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setParticle(editable.toString());
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
    }

    @Override
    public void updateData() {
        SaveLine save = (SaveLine) viewModel.getSave();
        binding.coordinateXSParamView.setInput(save.getCoordinateXS());
        binding.coordinateYSParamView.setInput(save.getCoordinateYS());
        binding.coordinateZSParamView.setInput(save.getCoordinateZS());
        binding.coordinateXEParamView.setInput(save.getCoordinateXE());
        binding.coordinateYEParamView.setInput(save.getCoordinateYE());
        binding.coordinateZEParamView.setInput(save.getCoordinateZE());
        binding.stepParamView.setInput(save.getStep());
        binding.particleParamView.setInput(save.getParticle());
        binding.filePathParamView.setInput(save.getFilePath());
        binding.fileNameParamView.setInput(save.getFileName());
    }

    @Override
    public void clearError() {
        binding.coordinateXSParamView.clearError();
        binding.coordinateYSParamView.clearError();
        binding.coordinateZSParamView.clearError();
        binding.coordinateXEParamView.clearError();
        binding.coordinateYEParamView.clearError();
        binding.coordinateZEParamView.clearError();
        binding.stepParamView.clearError();
        binding.particleParamView.clearError();
        binding.filePathParamView.clearError();
        binding.fileNameParamView.clearError();
    }

    @Override
    public void setError(Save.ERRORCODE errorcode) {
        switch(errorcode) {
            case CXS:
                binding.coordinateXSParamView.setError(getString(R.string.draw_param_coordinateX_error));
                break;
            case CYS:
                binding.coordinateYSParamView.setError(getString(R.string.draw_param_coordinateY_error));
                break;
            case CZS:
                binding.coordinateZSParamView.setError(getString(R.string.draw_param_coordinateZ_error));
                break;
            case CXE:
                binding.coordinateXEParamView.setError(getString(R.string.draw_param_coordinateX_error));
                break;
            case CYE:
                binding.coordinateYEParamView.setError(getString(R.string.draw_param_coordinateY_error));
                break;
            case CZE:
                binding.coordinateZEParamView.setError(getString(R.string.draw_param_coordinateZ_error));
                break;
            case STEP:
                binding.stepParamView.setError(getString(R.string.draw_param_step_error));
                break;
            case PARTICLE:
                binding.particleParamView.setError(getString(R.string.draw_param_particle_error));
                break;
            case FP:
                binding.filePathParamView.setError(getString(R.string.draw_param_filePath_error));
                break;
            case FN:
                binding.fileNameParamView.setError(getString(R.string.draw_param_fileName_error));
                break;
        }
    }
}
