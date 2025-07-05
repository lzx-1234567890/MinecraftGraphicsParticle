package com.lzx.minecraftparticle.logic.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lzx.minecraftparticle.logic.model.Save;

public class MainViewModel extends ViewModel {
    public MutableLiveData<Integer> preSaveNum = new MutableLiveData<>();
    public MutableLiveData<Integer> saveNum = new MutableLiveData<>();
    public MutableLiveData<Save> save = new MutableLiveData<>();
    //去修改
    public MutableLiveData<Boolean> toModify = new MutableLiveData<>();
    //修改完成
    public MutableLiveData<Boolean> finishModify = new MutableLiveData<>();
    //去重置
    public MutableLiveData<Boolean> toRemove = new MutableLiveData<>();
    //确定重置
    public MutableLiveData<Boolean> confirmRemove = new MutableLiveData<>();

    MainViewModel() {
        preSaveNum.setValue(-1);
        saveNum.setValue(-1);
    }
}
