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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.FragmentDrawArcBinding;
import com.lzx.minecraftparticle.logic.Repository;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveArc;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.logic.viewModel.DrawViewModel;
import com.lzx.minecraftparticle.ui.fragment.draw.ParticleCategoryDialogFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;
import com.lzx.minecraftparticle.ui.view.NormalParamView;

import java.util.ArrayList;

public class DrawArcFragment extends DrawFragment {
    private FragmentDrawArcBinding binding;
    private DrawViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDrawArcBinding.inflate(inflater);

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

        //增加节点
        binding.nodeAddButton.setOnClickListener(v -> {
            SaveArc save = ((SaveArc)viewModel.getSave());
            //int nodeNum = save.getNodeNum() + 1;
            String[] newNode = new String[]{"0", "0", "0"};
            addNode(newNode, save.getNodes().size());
            save.getNodes().add(newNode);
        });

        //圆弧监听

        //创建图形按钮监听
        binding.createButton.setOnClickListener(v -> {
            Save.ERRORCODE errorcode = viewModel.getSave().check();
            viewModel.errorcode.setValue(errorcode);
            if(errorcode == null) {
                if (PermissionUtil.requestStorage(getActivity())) {
                    if(viewModel.getMode().equals("create")) {
                        DrawUtil.drawArc((SaveArc) viewModel.getSave());
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
    public void onViewCreated(@Nullable View vieww, @Nullable Bundle savedInstanceState) {
        //恢复节点
        updateNode();
    }


    @Override
    public void setData() {
        SaveArc save = (SaveArc) viewModel.getSave();
        ArrayList<String[]> nodes = save.getNodes();
        //设置数据
        binding.node1XParamView.setInput(nodes.get(0)[0]);
        binding.node1YParamView.setInput(nodes.get(0)[1]);
        binding.node1ZParamView.setInput(nodes.get(0)[2]);
        binding.node2XParamView.setInput(nodes.get(1)[0]);
        binding.node2YParamView.setInput(nodes.get(1)[1]);
        binding.node2ZParamView.setInput(nodes.get(1)[2]);
        binding.radiusParamView.setInput(save.getRadius());
        binding.angleParamView.setInput(save.getAngle());
        if(save.getMajorArc()) {
            binding.majorRadioButton.toggle();
        }else {
            binding.minorRadioBuuton.toggle();
        }
        binding.stepParamView.setInput(save.getStep());
        binding.stepSwitch.setChecked(save.getStepArc());
        binding.particleParamView.setInput(save.getParticle());
        binding.filePathParamView.setInput(save.getFilePath());
        binding.fileNameParamView.setInput(save.getFileName());
        //上下按钮监听
        binding.up1Button.setOnClickListener(v -> {
            int n = nodes.size();
            String[] node1 = nodes.get(0);
            String[] node2 = nodes.get(n - 1);
            nodes.set(0, node2);
            nodes.set(n - 1, node1);
            LinearLayout layout1 = (LinearLayout) binding.nodeParamView.getChildAt(0);
            LinearLayout layout2 = (LinearLayout) binding.nodeParamView.getChildAt(n - 1);
            for(int i = 0;i < 3;i++) {
                ((NormalParamView)layout1.getChildAt(i)).setInput(node2[i]);
                ((NormalParamView)layout2.getChildAt(i)).setInput(node1[i]);
            }
        });
        binding.down1Button.setOnClickListener(v -> {
            String[] node1 = nodes.get(0);
            String[] node2 = nodes.get(1);
            nodes.set(0, node2);
            nodes.set(1, node1);
            LinearLayout layout1 = (LinearLayout) binding.nodeParamView.getChildAt(0);
            LinearLayout layout2 = (LinearLayout) binding.nodeParamView.getChildAt(1);
            for(int i = 0;i < 3;i++) {
                ((NormalParamView)layout1.getChildAt(i)).setInput(node2[i]);
                ((NormalParamView)layout2.getChildAt(i)).setInput(node1[i]);
            }
        });
        binding.up2Button.setOnClickListener(v -> {
            String[] node1 = nodes.get(0);
            String[] node2 = nodes.get(1);
            nodes.set(0, node2);
            nodes.set(1, node1);
            LinearLayout layout1 = (LinearLayout) binding.nodeParamView.getChildAt(0);
            LinearLayout layout2 = (LinearLayout) binding.nodeParamView.getChildAt(1);
            for(int i = 0;i < 3;i++) {
                ((NormalParamView)layout1.getChildAt(i)).setInput(node2[i]);
                ((NormalParamView)layout2.getChildAt(i)).setInput(node1[i]);
            }
        });
        binding.down2Button.setOnClickListener(v -> {
            int n = nodes.size();
            if(n >= 3) {
                String[] node1 = nodes.get(1);
                String[] node2 = nodes.get(2);
                nodes.set(1, node2);
                nodes.set(2, node1);
                LinearLayout layout1 = (LinearLayout) binding.nodeParamView.getChildAt(1);
                LinearLayout layout2 = (LinearLayout) binding.nodeParamView.getChildAt(2);
                for(int i = 0;i < 3;i++) {
                    ((NormalParamView)layout1.getChildAt(i)).setInput(node2[i]);
                    ((NormalParamView)layout2.getChildAt(i)).setInput(node1[i]);
                }
            }else {
                String[] node1 = nodes.get(0);
                String[] node2 = nodes.get(1);
                nodes.set(0, node2);
                nodes.set(1, node1);
                LinearLayout layout1 = (LinearLayout) binding.nodeParamView.getChildAt(0);
                LinearLayout layout2 = (LinearLayout) binding.nodeParamView.getChildAt(1);
                for(int i = 0;i < 3;i++) {
                    ((NormalParamView)layout1.getChildAt(i)).setInput(node2[i]);
                    ((NormalParamView)layout2.getChildAt(i)).setInput(node1[i]);
                }
            }
        });

        //设置文本监听
        binding.node1XParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.modifyNodeX(0, editable.toString());
            }
        });
        binding.node1YParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.modifyNodeY(0, editable.toString());
            }
        });
        binding.node1ZParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.modifyNodeZ(0, editable.toString());
            }
        });
        binding.node2XParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.modifyNodeX(1, editable.toString());
            }
        });
        binding.node2YParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.modifyNodeY(1, editable.toString());
            }
        });
        binding.node2ZParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.modifyNodeZ(1, editable.toString());
            }
        });
        binding.radiusParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setRadius(editable.toString());
            }
        });
        binding.angleParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setAngle(editable.toString());
            }
        });
        binding.arcRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if(i == R.id.majorRadioButton) {
                save.setMajorArc(true);
            }else {
                save.setMajorArc(false);
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
        binding.stepSwitch.setOnCheckedChangeListener((v, isChecked) -> {
            save.setStepArc(isChecked);
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
        SaveArc save = (SaveArc) viewModel.getSave();
        binding.radiusParamView.setInput(save.getRadius());
        binding.angleParamView.setInput(save.getAngle());
        if(save.getMajorArc()) {
            binding.majorRadioButton.toggle();
        }else {
            binding.minorRadioBuuton.toggle();
        }
        binding.stepParamView.setInput(save.getStep());
        binding.stepSwitch.setChecked(save.getStepArc());
        binding.particleParamView.setInput(save.getParticle());
        binding.filePathParamView.setInput(save.getFilePath());
        binding.fileNameParamView.setInput(save.getFileName());
        //更新现有节点
        updateNode();
    }

    @Override
    public void clearError() {
        binding.radiusParamView.clearError();
        binding.angleParamView.clearError();
        binding.stepParamView.clearError();
        binding.particleParamView.clearError();
        binding.filePathParamView.clearError();
        binding.fileNameParamView.clearError();
        SaveArc save = (SaveArc) viewModel.getSave();
        for(int i = 0;i < save.getNodes().size();i++) {
            LinearLayout layout = (LinearLayout) binding.nodeParamView.getChildAt(i);
            for(int ii = 0;ii < 3;ii++) {
                ((NormalParamView)layout.getChildAt(ii)).clearError();
            }
        }
    }

    @Override
    public void setError(Save.ERRORCODE errorcode) {
        int i = 0;
        switch(errorcode) {
            case RADIUS:
                binding.radiusParamView.setError(getString(R.string.draw_param_arc_radius_error));
                break;
            case ANGLE:
                binding.angleParamView.setError(getString(R.string.draw_param_line_angle_rotation_error));
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
            case NODEX:
                i = (int)errorcode.getData();
                ((NormalParamView)(((LinearLayout)binding.nodeParamView.getChildAt(i)).getChildAt(0))).setError(getString(R.string.draw_param_coordinateX_error));
                break;
            case NODEY:
                i = (int)errorcode.getData();
                ((NormalParamView)(((LinearLayout)binding.nodeParamView.getChildAt(i)).getChildAt(1))).setError(getString(R.string.draw_param_coordinateY_error));
                break;
            case NODEZ:
                i = (int)errorcode.getData();
                ((NormalParamView)(((LinearLayout)binding.nodeParamView.getChildAt(i)).getChildAt(2))).setError(getString(R.string.draw_param_coordinateZ_error));
                break;
        }
    }

    //更新节点控件，当删除或重置节点时调用
    public void updateNode() {
        SaveArc save = (SaveArc) viewModel.getSave();
        binding.nodeParamView.removeViews(2, binding.nodeParamView.getChildCount() - 2);
        for(int n = 2;n < save.getNodes().size();n++) {
            addNode(save.getNodes().get(n), n);
        }
    }

    //增加相应的节点控件，不改变Save数据
    public void addNode(String[] node, int n) {
        SaveArc save = (SaveArc) viewModel.getSave();
        View view = getLayoutInflater().inflate(R.layout.node, binding.nodeParamView, false);

        LinearLayout nodeLayout = view.findViewById(R.id.nodeLayout);
        nodeLayout.setTag(n);
        nodeLayout.setId(View.generateViewId());

        NormalParamView nodeX = view.findViewById(R.id.nodeXParamView);
        nodeX.reId();
        nodeX.setHint(String.format("%d(X)", n + 1));
        nodeX.setInput(node[0]);
        nodeX.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                int index = (int)nodeLayout.getTag();
                save.modifyNodeX(index, editable.toString());
            }
        });

        NormalParamView nodeY = view.findViewById(R.id.nodeYParamView);
        nodeY.reId();
        nodeY.setHint(String.format("%d(Y)", n + 1));
        nodeY.setInput(node[1]);
        nodeY.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                int index = (int)nodeLayout.getTag();
                save.modifyNodeY(index, editable.toString());
            }
        });

        NormalParamView nodeZ = view.findViewById(R.id.nodeZParamView);
        nodeZ.reId();
        nodeZ.setHint(String.format("%d(Z)", n + 1));
        nodeZ.setInput(node[2]);
        nodeZ.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                int index = (int)nodeLayout.getTag();
                save.modifyNodeZ(index, editable.toString());
            }
        });

        MaterialButton upButton = view.findViewById(R.id.upButton);
        upButton.setOnClickListener(vv -> {
            int index = (int)nodeLayout.getTag();
            ArrayList<String[]> nodes = save.getNodes();

            String[] node1 = nodes.get(index);
            String[] node2 = nodes.get(index - 1);
            nodes.set(index, node2);
            nodes.set(index - 1, node1);

            LinearLayout layout1 = (LinearLayout) binding.nodeParamView.getChildAt(index);
            LinearLayout layout2 = (LinearLayout) binding.nodeParamView.getChildAt(index - 1);
            for(int i = 0;i < 3;i++) {
                ((NormalParamView)layout1.getChildAt(i)).setInput(node2[i]);
                ((NormalParamView)layout2.getChildAt(i)).setInput(node1[i]);
            }
        });
        upButton.setId(View.generateViewId());

        MaterialButton downButton = view.findViewById(R.id.downButton);
        downButton.setOnClickListener(vv -> {
            int index = (int)nodeLayout.getTag();
            ArrayList<String[]> nodes = save.getNodes();

            if(index == nodes.size() - 1) {
                String[] node1 = nodes.get(index);
                String[] node2 = nodes.get(0);
                nodes.set(index, node2);
                nodes.set(0, node1);

                LinearLayout layout1 = (LinearLayout) binding.nodeParamView.getChildAt(index);
                LinearLayout layout2 = (LinearLayout) binding.nodeParamView.getChildAt(0);
                for (int i = 0; i < 3; i++) {
                    ((NormalParamView) layout1.getChildAt(i)).setInput(node2[i]);
                    ((NormalParamView) layout2.getChildAt(i)).setInput(node1[i]);
                }
            }else {
                String[] node1 = nodes.get(index);
                String[] node2 = nodes.get(index + 1);
                nodes.set(index, node2);
                nodes.set(index + 1, node1);

                LinearLayout layout1 = (LinearLayout) binding.nodeParamView.getChildAt(index);
                LinearLayout layout2 = (LinearLayout) binding.nodeParamView.getChildAt(index + 1);
                for (int i = 0; i < 3; i++) {
                    ((NormalParamView) layout1.getChildAt(i)).setInput(node2[i]);
                    ((NormalParamView) layout2.getChildAt(i)).setInput(node1[i]);
                }
            }
        });
        downButton.setId(View.generateViewId());

        MaterialButton removeButton = view.findViewById(R.id.removeButton);
        removeButton.setOnClickListener(vv -> {
            save.getNodes().remove((int)nodeLayout.getTag());
            LinearLayout layout = (LinearLayout) binding.nodeParamView.getChildAt((int)nodeLayout.getTag());
            ((ViewGroup)layout.getParent()).removeView(layout);
            updateNode();
        });
        removeButton.setId(View.generateViewId());

        binding.nodeParamView.addView(view);
    }

}
