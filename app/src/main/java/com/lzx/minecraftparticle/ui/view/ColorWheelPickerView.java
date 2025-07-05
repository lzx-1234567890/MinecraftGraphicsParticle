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

public class ColorWheelPickerView extends View {
    private boolean hasInit = false;
    private Paint wheelPaint;
    private Paint circlePaint;
    float[] selectorP = new float[2];
    private float radius;
    private int color;

    private OnColorWheelSelectedListener listener;

    public interface OnColorWheelSelectedListener {
        void colorWheelSelected(int color);
    }

    public void setListener(OnColorWheelSelectedListener listener) {
        this.listener = listener;
    }


    public ColorWheelPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorWheelPickerView(Context context) {
        super(context);
        init();
    }

    public void init() {
        wheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(1);
        circlePaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        radius = Math.min(cx, cy) - 50;

        //绘制色轮
        float[] hsv = {0f, 1f, 1f};
        for(int i = 0;i < 360;i++) {
            hsv[0] = i;
            wheelPaint.setColor(Color.HSVToColor(hsv));
            canvas.drawArc(
                    cx - radius, cy - radius, cx + radius, cy + radius,
                    i, 1, true, wheelPaint);
        }

        //绘制颜色选择小圆
        if(!hasInit) {
            float[] hsvC = new float[3];
            Color.colorToHSV(color, hsvC);
            selectorP[0] = (float) (hsvC[1] * radius * Math.cos(Math.toRadians(hsvC[0])) + cx);
            selectorP[1] = (float) (hsvC[1] * radius * Math.sin(Math.toRadians(hsvC[0])) + cy);

            hasInit = true;
        }
        canvas.drawCircle(selectorP[0], selectorP[1], 20, circlePaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                float cx = getWidth() / 2f;
                float cy = getHeight() / 2f;
                float distance = (float) Math.pow(Math.pow(x - cx, 2) + Math.pow(y - cy, 2), 0.5);
                float angle = (float) Math.toDegrees(Math.atan2(y - cy, x - cx));
                if(angle < 0) angle+=360;

                if(distance <= radius) {
                    selectorP[0] = x;
                    selectorP[1] = y;

                    float[] hsv = {angle, distance / radius, 1f};
                    color = Color.HSVToColor(hsv);
                }else {
                    selectorP[0] = cx + radius * (float) Math.cos(Math.toRadians(angle));
                    selectorP[1] = cy + radius * (float) Math.sin(Math.toRadians(angle));

                    float[] hsv = {angle, radius, 1f};

                    color = Color.HSVToColor(hsv);
                }
                listener.colorWheelSelected(color);
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
        radius = Math.min(w, h) - 50;
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
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        selectorP[0] = (float) (hsvC[1] * radius * Math.cos(Math.toRadians(hsvC[0])) + cx);
        selectorP[1] = (float) (hsvC[1] * radius * Math.sin(Math.toRadians(hsvC[0])) + cy);
        invalidate();
    }


}
