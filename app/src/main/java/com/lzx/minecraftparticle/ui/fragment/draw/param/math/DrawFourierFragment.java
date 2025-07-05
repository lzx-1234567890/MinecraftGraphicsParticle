package com.lzx.minecraftparticle.ui.fragment.draw.param.math;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.caverock.androidsvg.SVG;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.databinding.FragmentDrawFourierBinding;
import com.lzx.minecraftparticle.logic.Repository;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveFourier;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.logic.viewModel.DrawViewModel;
import com.lzx.minecraftparticle.ui.fragment.draw.ParticleCategoryDialogFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DrawFourierFragment extends DrawFragment {
    private FragmentDrawFourierBinding binding;
    private DrawViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDrawFourierBinding.inflate(inflater);

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

        //svg监听
        ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    try {
                        requireContext().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }catch (Exception e) {
                        Toast.makeText(requireContext(), getString(R.string.draw_persistable_uri_error), Toast.LENGTH_LONG).show();
                    }
                }
                SaveFourier save = (SaveFourier) viewModel.getSave();
                save.setUri(uri.toString());
                binding.imageView.setImageDrawable(null);
                binding.pathRadioGroup.removeAllViews();
                save.getPathRadioButtons().clear();
                save.setPathId(0);
                updateSVG();
            }
        });

        binding.imageView.setOnClickListener(v -> {
            launcher.launch("image/svg+xml");
        });

        //xz轴监听
        binding.xRadioGroup.setOnCheckedChangeListener((v, id) -> {
            if(id == R.id.xPRadioButton) {
                ((SaveFourier) viewModel.getSave()).setXP(true);
            }else {
                ((SaveFourier) viewModel.getSave()).setXP(false);
            }
        });

        binding.zRadioGroup.setOnCheckedChangeListener((v, id) -> {
            if(id == R.id.zPRadioButton) {
                ((SaveFourier) viewModel.getSave()).setZP(true);
            }else {
                ((SaveFourier) viewModel.getSave()).setZP(false);
            }
        });

        //创建图形按钮监听
        binding.createButton.setOnClickListener(v -> {
            Save.ERRORCODE errorcode = viewModel.getSave().check();
            viewModel.errorcode.setValue(errorcode);
            if(errorcode == null) {
                if (PermissionUtil.requestStorage(getActivity())) {
                    if(viewModel.getMode().equals("create")) {
                        DrawUtil.drawFourier((SaveFourier) viewModel.getSave());
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
        //恢复SVG
        updateSVG();
    }


    @Override
    public void setData() {
        SaveFourier save = (SaveFourier) viewModel.getSave();
        //设置数据
        binding.coordinateXParamView.setInput(save.getCoordinateX());
        binding.coordinateYParamView.setInput(save.getCoordinateY());
        binding.coordinateZParamView.setInput(save.getCoordinateZ());
        binding.angleParamView.setInput(save.getAngle());
        binding.xPRadioButton.setChecked(save.getXP());
        binding.xNRadioBuuton.setChecked(!save.getXP());
        binding.zPRadioButton.setChecked(save.getZP());
        binding.zNRadioBuuton.setChecked(!save.getZP());
        binding.cycleParamView.setInput(save.getCycle());
        binding.scaleParamView.setInput(save.getScale());
        binding.numParamView.setInput(save.getNum());
        binding.pointNumParamView.setInput(save.getPointNum());
        binding.particleParamView.setInput(save.getParticle());
        binding.armorStandNameParamView.setInput(save.getASName());
        binding.filePathParamView.setInput(save.getFilePath());
        binding.fileNameParamView.setInput(save.getFileName());
        //设置文本监听
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
        binding.cycleParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setCycle(editable.toString());
            }
        });
        binding.scaleParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setScale(editable.toString());
            }
        });
        binding.numParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setNum(editable.toString());
            }
        });
        binding.pointNumParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setPointNum(editable.toString());
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
        binding.armorStandNameParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setASName(editable.toString());
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
        SaveFourier save = (SaveFourier) viewModel.getSave();
        binding.coordinateXParamView.setInput(save.getCoordinateX());
        binding.coordinateYParamView.setInput(save.getCoordinateY());
        binding.coordinateZParamView.setInput(save.getCoordinateZ());
        binding.angleParamView.setInput(save.getAngle());
        binding.xPRadioButton.setChecked(save.getXP());
        binding.xNRadioBuuton.setChecked(!save.getXP());
        binding.zPRadioButton.setChecked(save.getZP());
        binding.zNRadioBuuton.setChecked(!save.getZP());
        binding.cycleParamView.setInput(save.getCycle());
        binding.scaleParamView.setInput(save.getScale());
        binding.numParamView.setInput(save.getNum());
        binding.pointNumParamView.setInput(save.getPointNum());
        binding.particleParamView.setInput(save.getParticle());
        binding.armorStandNameParamView.setInput(save.getASName());
        binding.filePathParamView.setInput(save.getFilePath());
        binding.fileNameParamView.setInput(save.getFileName());
        updateSVG();
    }

    @Override
    public void clearError() {
        binding.coordinateXParamView.clearError();
        binding.coordinateYParamView.clearError();
        binding.coordinateZParamView.clearError();
        binding.angleParamView.clearError();
        binding.cycleParamView.clearError();
        binding.scaleParamView.clearError();
        binding.numParamView.clearError();
        binding.pointNumParamView.clearError();
        binding.particleParamView.clearError();
        binding.armorStandNameParamView.clearError();
        binding.filePathParamView.clearError();
        binding.fileNameParamView.clearError();
    }

    @Override
    public void setError(Save.ERRORCODE errorcode) {
        switch(errorcode) {
            case URI:
                Toast.makeText(requireContext(), getString(R.string.draw_param_fourier_uri_error), Toast.LENGTH_SHORT).show();
                break;
            case CX:
                binding.coordinateXParamView.setError(getString(R.string.draw_param_coordinateX_error));
                break;
            case CY:
                binding.coordinateYParamView.setError(getString(R.string.draw_param_coordinateY_error));
                break;
            case CZ:
                binding.coordinateZParamView.setError(getString(R.string.draw_param_coordinateZ_error));
                break;
            case ANGLE:
                binding.angleParamView.setError(getString(R.string.draw_param_angle_rotation_error));
                break;
            case CYCLE:
                binding.cycleParamView.setError(getString(R.string.draw_param_fourier_cycle_error));
                break;
            case SCALE:
                binding.scaleParamView.setError(getString(R.string.draw_param_scale_error));
                break;
            case NUM:
                binding.numParamView.setError(getString(R.string.draw_param_fourier_armor_stand_number_error));
                break;
            case PNUM:
                binding.pointNumParamView.setError(getString(R.string.draw_param_fourier_point_number_error));
                break;
            case PARTICLE:
                binding.particleParamView.setError(getString(R.string.draw_param_particle_error));
                break;
            case ASN:
                binding.armorStandNameParamView.setError(getString(R.string.draw_param_fourier_armor_stand_name_error));
                break;
            case FP:
                binding.filePathParamView.setError(getString(R.string.draw_param_filePath_error));
                break;
            case FN:
                binding.fileNameParamView.setError(getString(R.string.draw_param_fileName_error));
                break;
        }
    }

    public void updateSVG() {
        SaveFourier save = (SaveFourier)viewModel.getSave();
        String uri = save.getUri();
        try {
            if(uri != null) {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(Uri.parse(uri));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] data = new byte[4096];
                int b;
                while ((b = inputStream.read(data, 0, data.length)) != -1) {
                    baos.write(data, 0, b);
                }
                byte[] svgBytes = baos.toByteArray();

                //解析为SVG
                SVG svg;
                String content = new String(svgBytes, "UTF-8");
                svg = SVG.getFromString(content);
                binding.imageView.setImageDrawable(new PictureDrawable(svg.renderToPicture()));

                //解析path
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(content)));
                NodeList paths = doc.getElementsByTagName("path");

                binding.pathRadioGroup.removeAllViews();
                save.getPathRadioButtons().clear();
                if (paths.getLength() >= 1) {
                    for (int i = 0; i < paths.getLength(); i++) {
                        View view = getLayoutInflater().inflate(R.layout.fourier_path, binding.pathRadioGroup, false);

                        RadioButton pathRadioButton = view.findViewById(R.id.pathRadioButton);
                        pathRadioButton.setId(View.generateViewId());
                        pathRadioButton.setText(String.format(getString(R.string.draw_param_fourier_path), i + 1));
                        pathRadioButton.setTag(i);

                        pathRadioButton.setOnCheckedChangeListener((v, isChecked) -> {
                            if (isChecked) {
                                for (RadioButton rb : save.getPathRadioButtons()) {
                                    if (rb != v) {
                                        rb.setChecked(false);
                                    }
                                }
                                save.setPathId((int) v.getTag());
                                save.setPath(paths.item(save.getPathId()).getAttributes().getNamedItem("d").getNodeValue());
                            }
                        });

                        binding.pathRadioGroup.addView(view);
                        save.getPathRadioButtons().add(pathRadioButton);
                    }
                    save.getPathRadioButtons().get(save.getPathId()).setChecked(true);
                    save.setPath(paths.item(save.getPathId()).getAttributes().getNamedItem("d").getNodeValue());
                }else {
                    clearSVG();
                    Toast.makeText(requireContext(), getString(R.string.draw_fourier_no_path_error), Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e) {
            clearSVG();
            Toast.makeText(requireContext(), getString(R.string.draw_fourier_parse_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void clearSVG() {
        SaveFourier save = (SaveFourier)viewModel.getSave();
        binding.imageView.setImageDrawable(null);
        binding.pathRadioGroup.removeAllViews();
        save.getPathRadioButtons().clear();
        save.setPathId(0);
        save.setPath(null);
        save.setUri(null);
    }
}
