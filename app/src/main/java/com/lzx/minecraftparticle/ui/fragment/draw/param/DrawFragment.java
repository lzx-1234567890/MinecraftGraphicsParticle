package com.lzx.minecraftparticle.ui.fragment.draw.param;

import androidx.fragment.app.Fragment;

import com.lzx.minecraftparticle.logic.model.Save;

public abstract class DrawFragment extends Fragment {
    //设置各控件的数据
    public abstract void setData();

    //更新控件数据
    public abstract void updateData();

    public abstract void clearError();
    public abstract void setError(Save.ERRORCODE errorcode);

}
