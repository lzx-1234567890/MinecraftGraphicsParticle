package com.lzx.minecraftparticle.logic.behavior;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;

public class NoScrollBehavior extends AppBarLayout.Behavior {
    public NoScrollBehavior() {
    }

    public NoScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        return false; // 禁用滑动事件的拦截
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        return false; // 禁用触摸事件的处理
    }
}
