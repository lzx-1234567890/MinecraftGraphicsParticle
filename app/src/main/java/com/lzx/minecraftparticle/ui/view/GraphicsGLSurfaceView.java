package com.lzx.minecraftparticle.ui.view;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.lzx.minecraftparticle.ui.renderer.GraphicsGLRenderer;

public class GraphicsGLSurfaceView extends GLSurfaceView{
    GraphicsGLRenderer renderer;
    
    int mode = 0; //0无操作 1旋转 2平移和放缩
    int pointerCount = 0;
    
    //首触摸点位
    float preX = 0;
    float preY = 0;
    
    //双指距离
    float preDistance = 0;
    
    //上次参数
    float lastAngleX = 0;
    float lastAngleY = 0;
    float lastTranslateX = 0;
    float lastTranslateY = 0;
    float lastTranslateZ = 0;
    
    //速度
    float speedTranslateForward = 10f;
    float speedTranslateRight = 10f;
    float speedTranslateUp = 10f;
    float speedRotateX = 360f;
    float speedRotateY = 360f;
    
    public GraphicsGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        renderer = new GraphicsGLRenderer();
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        
        //获取设置
        SharedPreferences sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        speedTranslateForward = sp.getFloat("graphics_speedtf", 10f);
        speedTranslateRight = sp.getFloat("graphics_speedtr", 10f);
        speedTranslateUp = sp.getFloat("graphics_speedtu", 10f);
        speedRotateX = sp.getFloat("graphics_speedrx", 360f);
        speedRotateY = sp.getFloat("graphics_speedry", 360f);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                pointerCount = event.getPointerCount();
                mode = 1;
                toRotate(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                pointerCount = event.getPointerCount();
                if(pointerCount == 2) {
                    mode = 2;
                    toTranslate(event);
                }else {
                    mode = 0;
                    toReset();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mode == 1) {
                    //旋转
                    float x = event.getX();
                    float y = event.getY();
                    float angleX = (y - preY) / getHeight() * speedRotateX;
                    float angleY = (x - preX) / getWidth() * speedRotateY;
                    renderer.setAngleX(lastAngleX + angleX);
                    renderer.setAngleY(lastAngleY + angleY);
                }else if(mode == 2) {
                    //平移
                    float x = event.getX(1);
                    float y = event.getY(1);
                    float dx = x - preX;
                    float dy = preY - y;
                    
                    //获取右向量的投影向量
                    float[] rightV = renderer.getRightV();
                    float[] rightProjectionV = {rightV[0], 0, rightV[2]};
                    float rightProjectionVLength = (float)Math.sqrt(rightV[0] * rightV[0] + rightV[2] * rightV[2]);
                    rightProjectionV[0] = rightProjectionV[0] / rightProjectionVLength;
                    rightProjectionV[2] = rightProjectionV[2] / rightProjectionVLength;
                    
                    //创建上向量和获取前向量
                    float[] upV = {0, 1f, 0};
                    float[] forwardV = renderer.getForwardV();
                    
                    //计算移动距离
                    float rightL = dx / getWidth() * speedTranslateRight;
                    float upL = dy / getHeight() * speedTranslateUp;
                    float forwardL = (getDistance(event) - preDistance) / preDistance * speedTranslateForward;
                    
                    //进行平移
                    renderer.setTranslateX(lastTranslateX + rightL * rightProjectionV[0] + upL * upV[0] + forwardL * forwardV[0]);
                    renderer.setTranslateY(lastTranslateY + rightL * rightProjectionV[1] + upL * upV[1] + forwardL * forwardV[1]);
                    renderer.setTranslateZ(lastTranslateZ + rightL * rightProjectionV[2] + upL * upV[2] + forwardL * forwardV[2]);
                }
                requestRender();
                break;
            case MotionEvent.ACTION_UP:
                mode = 0;
                toReset();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // 重置触摸点
                pointerCount -= 1;
                if(pointerCount == 1) {
                    mode = 1;
                    toRotate(event);
                }else if(pointerCount == 2){
                    mode = 2;
                    toTranslate(event);
                }else {
                    mode = 0;
                    toReset();
                }
                break;
        }
        return true;
    }
    
    private void toReset() {
        renderer.setIsMoving(false);
        renderer.setIsRotating(false);
        preX = 0;
        preY = 0;
        preDistance = 0;
    }
    
    private void toRotate(MotionEvent event) {
        renderer.setIsRotating(true);
        int pointerCount = event.getPointerCount();
        if(pointerCount == 1) {
            preX = event.getX();
            preY = event.getY();
        }else if(pointerCount == 2) {
            int pointerIndex = event.getActionIndex();
            if(pointerIndex == 0) {
                pointerIndex = 1;
            }else if(pointerIndex == 1) {
                pointerIndex = 0;
            }
            preX = event.getX(pointerIndex);
            preY = event.getY(pointerIndex);
        }
        
        lastAngleX = renderer.getAngleX();
        lastAngleY = renderer.getAngleY();
    }
    
    private void toTranslate(MotionEvent event) {
        renderer.setIsMoving(true);
        preX = event.getX(1);
        preY = event.getY(1);
        preDistance = getDistance(event);
        
        lastTranslateX = renderer.getTranslateX();
        lastTranslateY = renderer.getTranslateY();
        lastTranslateZ = renderer.getTranslateZ();
    }
    
    private float getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
    
    public GraphicsGLRenderer getRenderer() {
    	return renderer;
    }
    
    public void setSpeedTranslateForward(float v) {
        this.speedTranslateForward = v;
    }
    
    public float getSpeedTranslateForward() {
        return this.speedTranslateForward;
    }
    
    public void setSpeedTranslateRight(float v) {
        this.speedTranslateRight = v;
    }
    
    public float getSpeedTranslateRight() {
        return this.speedTranslateRight;
    }
    
    public void setSpeedTranslateUp(float v) {
        this.speedTranslateUp = v;
    }
    
    public float getSpeedTranslateUp() {
        return this.speedTranslateUp;
    }
    
    public void setSpeedRotateX(float v) {
        this.speedRotateX = v;
    }
    
    public float getSpeedRotateX() {
        return this.speedRotateX;
    }
    
    public void setSpeedRotateY(float v) {
        this.speedRotateY = v;
    }
    
    public float getSpeedRotateY() {
        return this.speedRotateY;
    }
}
