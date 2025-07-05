package com.lzx.minecraftparticle.logic.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.lzx.minecraftparticle.logic.model.Save;

public class DrawViewModel extends ViewModel {
    private Save save;
    private String mode;
    private Object data;
    public MutableLiveData<Boolean> toReset = new MutableLiveData<>();
    public MutableLiveData<Boolean> toCreateGraphics = new MutableLiveData<>();
    public MutableLiveData<Save.ERRORCODE> errorcode = new MutableLiveData<>();
    public MutableLiveData<String> particleSelected = new MutableLiveData<>();

    //颜色选择
    public MutableLiveData<Boolean> colorChanged = new MutableLiveData<>();

    //图形渲染视图
    public MutableLiveData<Float> translateX = new MutableLiveData<>();
    public MutableLiveData<Float> translateY = new MutableLiveData<>();
    public MutableLiveData<Float> translateZ = new MutableLiveData<>();
    public MutableLiveData<Float> angleX = new MutableLiveData<>();
    public MutableLiveData<Float> angleY = new MutableLiveData<>();

    public DrawViewModel() {
        translateX.setValue(0f);
        translateY.setValue(0f);
        translateZ.setValue(0f);
        angleX.setValue(0f);
        angleY.setValue(0f);
    }

    public void setSave(Save save){
        this.save = save;
    }

    public Save getSave() {
        return save;
    }

    public void setMode(String mode){
        this.mode = mode;
    }

    public String  getMode() {
        return mode;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

}
