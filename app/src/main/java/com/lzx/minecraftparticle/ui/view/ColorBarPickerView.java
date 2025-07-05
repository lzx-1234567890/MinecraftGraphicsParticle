package com.lzx.minecraftparticle.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class ColorBarPickerView extends View {
    private boolean hasInit;
    private Paint barPaint;
    private Paint rectPaint;
    float[] selectorP = new float[]{0f, 0f};
    private int color;

    private final float RECTSTROKEWIDTH = 5f;
    private final float RECTHEIGHT = 40f;

    private OnColorBarSelectedListener listener;

    public interface OnColorBarSelectedListener {
        void colorBarSelected(int color);
    }

    public void setListener(OnColorBarSelectedListener listener) {
        this.listener = listener;
    }


    public ColorBarPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorBarPickerView(Context context) {
        super(context);
        init();
    }

    public void init() {
        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barPaint.setStrokeWidth(1);
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(5);
        rectPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        //绘制色条
        float[] hsv = {0f, 0f, 1f};
        float height = getHeight();
        float width = getWidth();
        for(float i = 0;i < height;i++) {
            hsv[2] -= 1 / height;
            barPaint.setColor(Color.HSVToColor(hsv));
            canvas.drawLine(0f, i, width, i, barPaint);
        }

        if(!hasInit) {
            float[] hsvC = new float[3];
            Color.colorToHSV(color, hsvC);
            float max = height - RECTSTROKEWIDTH - RECTHEIGHT;
            selectorP[1] = max * (1 - hsvC[2]);

            hasInit = true;
        }

        //绘制颜色选择矩形
        canvas.drawRect(RECTSTROKEWIDTH, selectorP[1] + RECTSTROKEWIDTH, width - RECTSTROKEWIDTH, selectorP[1] + RECTHEIGHT, rectPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();

        float height = getHeight();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                if(y < 0) {
                    selectorP[1] = 0;
                }else if(y > height - RECTSTROKEWIDTH - RECTHEIGHT) {
                    selectorP[1] = height - RECTSTROKEWIDTH - RECTHEIGHT;
                }else {
                    selectorP[1] = y;
                }
                float[] hsv = {0f, 0f, (height - RECTSTROKEWIDTH - RECTHEIGHT - y) / (height - RECTSTROKEWIDTH - RECTHEIGHT)};
                color = Color.HSVToColor(hsv);
                listener.colorBarSelected(color);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                return true;
        }


        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void update() {
        float[] hsvC = new float[3];
        Color.colorToHSV(color, hsvC);
        float max = getHeight() - RECTSTROKEWIDTH - RECTHEIGHT;
        selectorP[1] = max * (1 - hsvC[2]);
        invalidate();
    }


}
