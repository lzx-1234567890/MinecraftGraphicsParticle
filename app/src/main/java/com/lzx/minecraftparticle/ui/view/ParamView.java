package com.lzx.minecraftparticle.ui.view;
import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public abstract class ParamView extends LinearLayout{
    public ParamView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParamView(Context context) {
        super(context);
    }
} 
