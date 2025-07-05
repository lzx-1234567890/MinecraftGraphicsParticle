package com.lzx.minecraftparticle.ui.fragment.draw.param.special;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.lzx.minecraftparticle.databinding.FragmentDrawImageBinding;
import com.lzx.minecraftparticle.logic.Repository;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveImage;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.logic.viewModel.DrawViewModel;
import com.lzx.minecraftparticle.ui.fragment.ColorPickerDialogFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.ParticleCategoryDialogFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;

import org.apache.tika.Tika;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class DrawImageFragment extends DrawFragment {
    private FragmentDrawImageBinding binding;
    private DrawViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDrawImageBinding.inflate(inflater);

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

        //图片监听
        ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    try {
                        requireContext().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }catch (Exception e) {
                        Toast.makeText(requireContext(), getString(R.string.draw_persistable_uri_error), Toast.LENGTH_LONG).show();
                    }
                }
                SaveImage save = (SaveImage) viewModel.getSave();
                binding.imageView.setImageDrawable(null);
                save.setUri(uri.toString());
                updateImage();
            }
        });

        binding.imageView.setOnClickListener(v -> {
            launcher.launch("image/*");
        });

        //颜色监听
        binding.colorView.setOnClickListener(v -> {
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            new ColorPickerDialogFragment().show(fm, "颜色");
        });

        viewModel.colorChanged.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    binding.colorView.setBackgroundColor(mixColor());
                }
            }
        });

        binding.colorView.setBackgroundColor(mixColor());

        //创建图形按钮监听
        binding.createButton.setOnClickListener(v -> {
            Save.ERRORCODE errorcode = viewModel.getSave().check();
            viewModel.errorcode.setValue(errorcode);
            if(errorcode == null) {
                if (PermissionUtil.requestStorage(getActivity())) {
                    if(viewModel.getMode().equals("create")) {
                        DrawUtil.drawImage((SaveImage) viewModel.getSave());
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
        //恢复图片
        updateImage();
    }


    @Override
    public void setData() {
        SaveImage save = (SaveImage) viewModel.getSave();
        //设置数据
        binding.toleranceParamView.setInput(save.getTolerance());
        binding.coordinateXParamView.setInput(save.getCoordinateX());
        binding.coordinateYParamView.setInput(save.getCoordinateY());
        binding.coordinateZParamView.setInput(save.getCoordinateZ());
        binding.angleParamView.setInput(save.getAngle());
        binding.widthXParamView.setInput(save.getWX());
        binding.widthYParamView.setInput(save.getWY());
        binding.widthZParamView.setInput(save.getWZ());
        binding.heightXParamView.setInput(save.getHX());
        binding.heightYParamView.setInput(save.getHY());
        binding.heightZParamView.setInput(save.getHZ());
        binding.scaleParamView.setInput(save.getScale());
        binding.stepWParamView.setInput(save.getStepW());
        binding.stepHParamView.setInput(save.getStepH());
        binding.particleParamView.setInput(save.getParticle());
        binding.filePathParamView.setInput(save.getFilePath());
        binding.fileNameParamView.setInput(save.getFileName());
        //设置文本监听
        binding.toleranceParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setTolerance(editable.toString());
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
        binding.widthXParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setWX(editable.toString());
            }
        });
        binding.widthYParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setWY(editable.toString());
            }
        });
        binding.widthZParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setWZ(editable.toString());
            }
        });
        binding.heightXParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setHX(editable.toString());
            }
        });
        binding.heightYParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setHY(editable.toString());
            }
        });
        binding.heightZParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setHZ(editable.toString());
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
        binding.stepWParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setStepW(editable.toString());
            }
        });
        binding.stepHParamView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                save.setStepH(editable.toString());
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
        SaveImage save = (SaveImage) viewModel.getSave();
        binding.toleranceParamView.setInput(save.getTolerance());
        binding.coordinateXParamView.setInput(save.getCoordinateX());
        binding.coordinateYParamView.setInput(save.getCoordinateY());
        binding.coordinateZParamView.setInput(save.getCoordinateZ());
        binding.angleParamView.setInput(save.getAngle());
        binding.widthXParamView.setInput(save.getWX());
        binding.widthYParamView.setInput(save.getWY());
        binding.widthZParamView.setInput(save.getWZ());
        binding.heightXParamView.setInput(save.getHX());
        binding.heightYParamView.setInput(save.getHY());
        binding.heightZParamView.setInput(save.getHZ());
        binding.scaleParamView.setInput(save.getScale());
        binding.stepWParamView.setInput(save.getStepW());
        binding.stepHParamView.setInput(save.getStepH());
        binding.particleParamView.setInput(save.getParticle());
        binding.filePathParamView.setInput(save.getFilePath());
        binding.fileNameParamView.setInput(save.getFileName());
        updateImage();
    }

    @Override
    public void clearError() {
        binding.toleranceParamView.clearError();
        binding.coordinateXParamView.clearError();
        binding.coordinateYParamView.clearError();
        binding.coordinateZParamView.clearError();
        binding.angleParamView.clearError();
        binding.widthXParamView.clearError();
        binding.widthYParamView.clearError();
        binding.widthZParamView.clearError();
        binding.heightXParamView.clearError();
        binding.heightYParamView.clearError();
        binding.heightZParamView.clearError();
        binding.scaleParamView.clearError();
        binding.stepWParamView.clearError();
        binding.stepHParamView.clearError();
        binding.particleParamView.clearError();
        binding.filePathParamView.clearError();
        binding.fileNameParamView.clearError();
    }

    @Override
    public void setError(Save.ERRORCODE errorcode) {
        switch(errorcode) {
            case URI:
                Toast.makeText(requireContext(), getString(R.string.draw_param_image_uri_error), Toast.LENGTH_SHORT).show();
                break;
            case TOLERANCE:
                binding.toleranceParamView.setError(getString(R.string.draw_param_image_tolerance_error));
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
            case WX:
                binding.widthXParamView.setError(getString(R.string.draw_param_vectorX_error));
                break;
            case WY:
                binding.widthYParamView.setError(getString(R.string.draw_param_vectorY_error));
                break;
            case WZ:
                binding.widthZParamView.setError(getString(R.string.draw_param_vectorZ_error));
                break;
            case HX:
                binding.heightXParamView.setError(getString(R.string.draw_param_vectorX_error));
                break;
            case HY:
                binding.heightYParamView.setError(getString(R.string.draw_param_vectorY_error));
                break;
            case HZ:
                binding.heightZParamView.setError(getString(R.string.draw_param_vectorZ_error));
                break;
            case SCALE:
                binding.scaleParamView.setError(getString(R.string.draw_param_scale_error));
                break;
            case STEPW:
                binding.stepWParamView.setError(getString(R.string.draw_param_step_error));
                break;
            case STEPH:
                binding.stepHParamView.setError(getString(R.string.draw_param_step_error));
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


    public void updateImage() {
        SaveImage save = (SaveImage) viewModel.getSave();
        String uri = save.getUri();
        try {
            if(uri != null) {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(Uri.parse(uri));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] data = new byte[4096];
                int b;
                while ((b = inputStream.read(data)) != -1) {
                    baos.write(data, 0, b);
                }
                byte[] imageBytes = baos.toByteArray();

                String type = new Tika().detect(imageBytes);
                if (type.equals("image/jpeg") || type.equals("image/png") || type.equals("image/webp")) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    binding.imageView.setImageBitmap(bitmap);
                } else if (type.equals("image/svg+xml")) {
                    SVG svg = SVG.getFromString(new String(imageBytes, "UTF-8"));
                    binding.imageView.setImageDrawable(new PictureDrawable(svg.renderToPicture()));
                } else {
                    clearImage();
                    Toast.makeText(requireContext(), getString(R.string.draw_image_type_error), Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e) {
            clearImage();
            Toast.makeText(requireContext(), getString(R.string.draw_image_parse_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void clearImage() {
        SaveImage save = (SaveImage) viewModel.getSave();
        save.setUri(null);
        binding.imageView.setImageDrawable(null);
    }

    public int mixColor() {
        SaveImage save = (SaveImage) viewModel.getSave();
        float[] hsvW = new float[3];
        float[] hsvB = new float[3];
        Color.colorToHSV(Integer.parseInt(save.getColorW()), hsvW);
        Color.colorToHSV(Integer.parseInt(save.getColorB()), hsvB);
        float[] hsv = {hsvW[0], hsvW[1], hsvB[2]};
        int color = Color.HSVToColor(Integer.parseInt(save.getColorA()), hsv);
        return color;
    }


}
