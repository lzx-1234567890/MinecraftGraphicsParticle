package com.lzx.minecraftparticle.ui.renderer;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.lzx.minecraftparticle.logic.model.Vector;
import com.lzx.minecraftparticle.ui.fragment.draw.graphics.GraphicsFragment;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GraphicsGLRenderer implements GLSurfaceView.Renderer{
    //着色器
    private int program;
    private int positionHandle;
    private int textureCoordHandle;
    private int textureUniformHandle;
    private int useTextureHandle;
    private int colorHandle;
    private int mvpMatrixHandle;
    
    //相机初始位置
    private float[] cameraPos = {2f, 2f, 2f};
    private float[] cameraRotation = {45f, 135f};
    
    //相机平移和旋转
    private float translateX;
    private float translateY;
    private float translateZ;
    private float angleX;
    private float angleY;
    
    //相机基本参数
    private boolean isMoving = false;
    private boolean isRotating = false;
    private float[] cameraRealPos = new float[3];
    private float[] cameraRealRotation = new float[2];
    private float[] rightV = new float[4];
    private float[] upV = new float[4];
    private float[] forwardV = new float[4];
    
    //屏幕大小
    private int[] screenSize = new int[2];
    private float ratio;
    
    //坐标轴长度
    float axisWidth = 3f;
    float axisSizeX = 100f;
    float axisSizeY = 100f;
    float axisSizeZ = 100f;
    
    //坐标轴颜色
    private FloatBuffer axisColorBuffer;
    float[] axisColors = {
        // X 轴颜色（红色）
        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        // Y 轴颜色（绿色）
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        // Z 轴颜色（蓝色）
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f
    };
    
    //网格大小
    float gridWidth = 3f;
    float gridSize = 1f;
    int gridNum = 100;
    
    //网格颜色
    float[] gridColor = {0.5f, 0.5f, 0.5f, 1f};
    
    //图形
    private boolean rendering = false;
    
    float[] graphicsVertices, graphicsTextureCoords;
    int[] graphicsIndices;
    FloatBuffer graphicsVertexBuffer, graphicsTextureCoordBuffer;
    IntBuffer graphicsIndexBuffer;
    
    Bitmap particleBitmap;
    float particleSize = 0.2f;
    ArrayList<Vector> points;
    
    //设置
    private boolean enableGrid = true;
    private boolean enableAxis = true;
    private boolean enableDynamicRendering = true;

    private OnRenderListener listener;

    public interface OnRenderListener {
        float getTranslateX();
        float getTranslateY();
        float getTranslateZ();
        float getAngleX();
        float getAngleY();
        void setTranslateX(float v);
        void setTranslateY(float v);
        void setTranslateZ(float v);
        void setAngleX(float v);
        void setAngleY(float v);
        void update();
    }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        //初始化坐标轴颜色缓冲区
        axisColorBuffer = createFloatBuffer(axisColors);

        // 加载着色器
        program = createProgram();
        GLES20.glUseProgram(program);

        // 获取顶点属性句柄
        positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        textureCoordHandle = GLES20.glGetAttribLocation(program, "aTextureCoord");
        colorHandle = GLES20.glGetAttribLocation(program, "aColor");
        textureUniformHandle = GLES20.glGetUniformLocation(program, "uTexture");
        useTextureHandle = GLES20.glGetUniformLocation(program, "uUseTexture");
        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
    }
    
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        screenSize[0] = width;
        screenSize[1] = height;
        if (width > height) {
            ratio =(float)width / height;
            //GLES20.glViewport(0, 0, (int)(width / ratio), height);
        } else {
            ratio =(float)height / width;
            //GLES20.glViewport(0, 0, width, (int)(height / ratio));
        }
        GLES20.glViewport(0, 0, width, height);
    }
    
    @Override
    public void onDrawFrame(GL10 gl) {
        //清除屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        if(!rendering) {return;}

        //相机位置和旋转信息获取
        translateX = listener.getTranslateX();
        translateY = listener.getTranslateY();
        translateZ = listener.getTranslateZ();
        angleX = listener.getAngleX();
        angleY = listener.getAngleY();

        //模型矩阵
        float[] mModelMatrix = new float[16];
        Matrix.setIdentityM(mModelMatrix, 0);
        
        //视图矩阵
        float[] mViewMatrix = new float[16];
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -1f, 0, 0, 0, 0f, 1f, 0f);
        
        //创建平移矩阵
        float[] translateMatrix = new float[16];
        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, translateX + cameraPos[0], translateY - cameraPos[1], translateZ + cameraPos[2] + 1);
        
        // 创建绕Y轴的旋转矩阵
        float[] rotationYMatrix = new float[16];
        Matrix.setIdentityM(rotationYMatrix, 0);
        Matrix.rotateM(rotationYMatrix, 0, angleY + cameraRotation[1], 0f, 1f, 0f);
        
        // 创建绕X轴的旋转矩阵
        float[] rotationXMatrix = new float[16];
        Matrix.setIdentityM(rotationXMatrix, 0);
        Matrix.rotateM(rotationXMatrix, 0, angleX + cameraRotation[0], 1f, 0f, 0f);
        
        //应用平移
        Matrix.multiplyMM(mViewMatrix, 0, translateMatrix, 0, mViewMatrix, 0);
        
        //应用旋转
        Matrix.multiplyMM(mViewMatrix, 0, rotationYMatrix, 0, mViewMatrix, 0);
        Matrix.multiplyMM(mViewMatrix, 0, rotationXMatrix, 0, mViewMatrix, 0);
        
        //创建反向视图矩阵并反向旋转
        float[] _mViewMatrix = new float[16];
        Matrix.setLookAtM(_mViewMatrix, 0, 0, 0, 1f, 0, 0, 0, 0f, 1f, 0f);
        Matrix.rotateM(_mViewMatrix, 0, -angleY - cameraRotation[1], 0f, 1f, 0f);
        Matrix.rotateM(_mViewMatrix, 0, -angleX - cameraRotation[0], 1f, 0f, 0f);
        
        //设置相机参数
        cameraRealPos[0] = translateX + cameraPos[0];
        cameraRealPos[1] = -translateY + cameraPos[1];
        cameraRealPos[2] = translateZ + cameraPos[2];
        cameraRealRotation[0] = angleX + cameraRotation[0];
        cameraRealRotation[1] = angleY + cameraRotation[1];
        rightV[0] = _mViewMatrix[0];
        rightV[1] = _mViewMatrix[1];
        rightV[2] = _mViewMatrix[2];
        rightV[3] = _mViewMatrix[3];
        upV[0] = _mViewMatrix[4];
        upV[1] = _mViewMatrix[5];
        upV[2] = _mViewMatrix[6];
        upV[3] = _mViewMatrix[7];
        forwardV[0] = _mViewMatrix[8];
        forwardV[1] = _mViewMatrix[9];
        forwardV[2] = _mViewMatrix[10];
        forwardV[3] = _mViewMatrix[11];

        //投影矩阵(透视)
        float[] mProjectionMatrix = new float[16];
        //Matrix.frustumM(mProjectionMatrix, 0, -1f, 1f, -1f, 1f, 1f, 100f);
        if(screenSize[0] > screenSize[1]) {
            Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 100f);
        }else if(screenSize[0] < screenSize[1]) {
            Matrix.frustumM(mProjectionMatrix, 0, -1f, 1f, -ratio, ratio, 1f, 100f);
        }else {
            Matrix.frustumM(mProjectionMatrix, 0, -1f, 1f, -1f, 1f, 1f, 100f);
        }
        
        //mvp矩阵
        float[] mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mMVPMatrix, 0, mModelMatrix, 0);        

        // 将此变换应用于即将被绘制的所有物体上
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mMVPMatrix, 0);
        
        //设置混合模式
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        
        //更新视图
        listener.update();
        
        // 绘制图形
        if (enableGrid) {
            drawGrid();
        }
        if (enableAxis) {
            drawAxis();
        }
        drawGraphics();
    }
    
    public void drawGraphics() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        
        // 设置纹理过滤方式
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        // 设置纹理环绕方式
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, particleBitmap, 0);
        
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(textureUniformHandle, 0);
        GLES20.glUniform1i(useTextureHandle, 1);
        
        //动态更新粒子坐标
        if(enableDynamicRendering) {
            int size = points.size();
            graphicsVertices = new float[size * 3 * 4];
            
            //创建旋转矩阵
            float[] rotationYMatrix = new float[16];
            Matrix.setIdentityM(rotationYMatrix, 0);
            Matrix.rotateM(rotationYMatrix, 0, -cameraRealRotation[1], 0f, 1f, 0f);
    
            float[] rotationXMatrix = new float[16];
            Matrix.setIdentityM(rotationXMatrix, 0);
            Matrix.rotateM(rotationXMatrix, 0, -cameraRealRotation[0], 1f, 0f, 0f);
            
            for(int i = 0;i < size;i++) {
                float x = (float)points.get(i).x, y = (float)points.get(i).y, z = (float)points.get(i).z;
                
                float[][] particles = new float[][]{{-particleSize, particleSize, 0, 1}, {particleSize, particleSize, 0, 1}, {particleSize, -particleSize, 0, 1}, {-particleSize, -particleSize, 0, 1}};
                
                float[] translateMatrix = new float[16];
                Matrix.setIdentityM(translateMatrix, 0);
                Matrix.translateM(translateMatrix, 0, x, y, z);
                
                //对粒子坐标进行矩阵变换
                for(int ii = 0;ii < particles.length;ii++) {
                    Matrix.multiplyMV(particles[ii], 0, rotationYMatrix, 0, particles[ii], 0);
                    Matrix.multiplyMV(particles[ii], 0, rotationXMatrix, 0, particles[ii], 0);
                    Matrix.multiplyMV(particles[ii], 0, translateMatrix, 0, particles[ii], 0);
                }
                
                graphicsVertices[i * 3 * 4] = particles[0][0];
                graphicsVertices[i * 3 * 4 + 1] = particles[0][1];
                graphicsVertices[i * 3 * 4 + 2] = particles[0][2];
                
                graphicsVertices[i * 3 * 4 + 3] = particles[1][0];
                graphicsVertices[i * 3 * 4 + 4] = particles[1][1];
                graphicsVertices[i * 3 * 4 + 5] = particles[1][2];
                
                graphicsVertices[i * 3 * 4 + 6] = particles[2][0];
                graphicsVertices[i * 3 * 4 + 7] = particles[2][1];
                graphicsVertices[i * 3 * 4 + 8] = particles[2][2];
                
                graphicsVertices[i * 3 * 4 + 9] = particles[3][0];
                graphicsVertices[i * 3 * 4 + 10] = particles[3][1];
                graphicsVertices[i * 3 * 4 + 11] = particles[3][2];
            }
            
            graphicsVertexBuffer = createFloatBuffer(graphicsVertices);
        }
        
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glEnableVertexAttribArray(textureCoordHandle);
        
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, graphicsVertexBuffer);
        GLES20.glVertexAttribPointer(textureCoordHandle, 2, GLES20.GL_FLOAT, false, 0, graphicsTextureCoordBuffer);
        
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, graphicsIndices.length, GLES20.GL_UNSIGNED_INT, graphicsIndexBuffer);
        
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glDeleteTextures(1, textures, 0);
        
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(textureCoordHandle);
    }
    
    public void updateGraphicsVertices() {
        if(!enableDynamicRendering) {
            int size = points.size();
            graphicsVertices = new float[size * 3 * 4];
            for(int i = 0;i < size;i++) {
                float x = (float)points.get(i).x, y = (float)points.get(i).y / ratio, z = (float)points.get(i).z;
                float[][] particles = new float[][]{{-particleSize + x, particleSize + y, z, 1}, {particleSize + x, particleSize + y, z, 1}, {particleSize + x, -particleSize + y, z, 1}, {-particleSize + x, -particleSize + y, z, 1}};

                graphicsVertices[i * 3 * 4] = particles[0][0];
                graphicsVertices[i * 3 * 4 + 1] = particles[0][1];
                graphicsVertices[i * 3 * 4 + 2] = particles[0][2];

                graphicsVertices[i * 3 * 4 + 3] = particles[1][0];
                graphicsVertices[i * 3 * 4 + 4] = particles[1][1];
                graphicsVertices[i * 3 * 4 + 5] = particles[1][2];

                graphicsVertices[i * 3 * 4 + 6] = particles[2][0];
                graphicsVertices[i * 3 * 4 + 7] = particles[2][1];
                graphicsVertices[i * 3 * 4 + 8] = particles[2][2];

                graphicsVertices[i * 3 * 4 + 9] = particles[3][0];
                graphicsVertices[i * 3 * 4 + 10] = particles[3][1];
                graphicsVertices[i * 3 * 4 + 11] = particles[3][2];
            }
            graphicsVertexBuffer = createFloatBuffer(graphicsVertices);
        }
    }
    
    public void updateGraphicsTextureCoords()  {
        graphicsTextureCoords = new float[points.size() * 8];
        for (int i = 0; i < graphicsTextureCoords.length / 8; i++) {
            graphicsTextureCoords[i * 8] = 0f;
            graphicsTextureCoords[i * 8 + 1] = 0f;

            graphicsTextureCoords[i * 8 + 2] = 1f;
            graphicsTextureCoords[i * 8 + 3] = 0f;

            graphicsTextureCoords[i * 8 + 4] = 1f;
            graphicsTextureCoords[i * 8 + 5] = 1f;

            graphicsTextureCoords[i * 8 + 6] = 0f;
            graphicsTextureCoords[i * 8 + 7] = 1f;
        }
        graphicsTextureCoordBuffer = createFloatBuffer(graphicsTextureCoords);
    }
    
    public void updateGraphicsIndices() {
        graphicsIndices = new int[points.size() * 6];
        for (int i = 0; i < graphicsIndices.length / 6; i++) {
            graphicsIndices[i * 6] = i * 4;
            graphicsIndices[i * 6 + 1] = i * 4 + 1;
            graphicsIndices[i * 6 + 2] = i * 4 + 2;

            graphicsIndices[i * 6 + 3] = i * 4;
            graphicsIndices[i * 6 + 4] = i * 4 + 3;
            graphicsIndices[i * 6 + 5] = i * 4 + 2;
        }
        graphicsIndexBuffer = createIntBuffer(graphicsIndices);
    }
    
    public void drawAxis() {
        ArrayList<Float> axisList = new ArrayList<>();
        //x轴
        axisList.add(cameraRealPos[0] - axisSizeX);
        axisList.add(0f);
        axisList.add(0f);
        axisList.add(cameraRealPos[0] + axisSizeX);
        axisList.add(0f);
        axisList.add(0f);
        //y轴
        axisList.add(0f);
        axisList.add(cameraRealPos[1] - axisSizeY);
        axisList.add(0f);
        axisList.add(0f);
        axisList.add(cameraRealPos[1] + axisSizeY);
        axisList.add(0f);
        //z轴
        axisList.add(0f);
        axisList.add(0f);
        axisList.add(cameraRealPos[2] - axisSizeZ);
        axisList.add(0f);
        axisList.add(0f);
        axisList.add(cameraRealPos[2] + axisSizeZ);
        
        Float[] axisVerticesF = new Float[axisList.size()];
        axisVerticesF = axisList.toArray(axisVerticesF);
        float[] axisVertices = new float[axisVerticesF.length];
        for(int i = 0;i < axisVertices.length;i++) {
            axisVertices[i] = axisVerticesF[i];
        }
        
        FloatBuffer axisVertexBuffer = createFloatBuffer(axisVertices);
        
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glEnableVertexAttribArray(colorHandle);
        
        GLES20.glUniform1i(useTextureHandle, 0);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, axisVertexBuffer);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, axisColorBuffer);
        
        GLES20.glLineWidth(axisWidth);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, axisVertices.length / 3);
        
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);
    }
    
    public void drawGrid() {
        ArrayList<Float> gridList = new ArrayList<>();
        float[] gridPos = {(int)(cameraRealPos[0] / gridSize) * gridSize, (int)(cameraRealPos[2] / gridSize) * gridSize};
        float gridLength = gridSize * gridNum;
        //平行于x轴的线
        for(int i = 0;i < gridNum;i++) {
            gridList.add(gridPos[0] - gridLength);
            gridList.add(0f);
            gridList.add(gridPos[1] - i * gridSize);
            
            gridList.add(gridPos[0] + gridLength);
            gridList.add(0f);
            gridList.add(gridPos[1] - i * gridSize);
            
            gridList.add(gridPos[0] - gridLength);
            gridList.add(0f);
            gridList.add(gridPos[1] + i * gridSize);
            
            gridList.add(gridPos[0] + gridLength);
            gridList.add(0f);
            gridList.add(gridPos[1] + i * gridSize);
        }
        //平行于z轴的线
        for(int i =0;i < gridNum;i++) {
            gridList.add(gridPos[0] -  i * gridSize);
            gridList.add(0f);
            gridList.add(gridPos[1] - gridLength);
            
            gridList.add(gridPos[0] -  i * gridSize);
            gridList.add(0f);
            gridList.add(gridPos[1] + gridLength);
            
            gridList.add(gridPos[0] +  i * gridSize);
            gridList.add(0f);
            gridList.add(gridPos[1] - gridLength);
            
            gridList.add(gridPos[0] +  i * gridSize);
            gridList.add(0f);
            gridList.add(gridPos[1] + gridLength);
        }
        
        float[] gridColors = new float[gridList.size() / 3 * 4];
        for(int i = 0;i < gridList.size() / 3;i++) {
            gridColors[i * 4] = gridColor[0];
            gridColors[i * 4 + 1] = gridColor[1];
            gridColors[i * 4 + 2] = gridColor[2];
            gridColors[i * 4 + 3] = gridColor[3];
        }
        
        Float[] gridVerticesF = new Float[gridList.size()];
        gridVerticesF = gridList.toArray(gridVerticesF);
        float[] gridVertices = new float[gridVerticesF.length];
        for(int i = 0;i < gridVertices.length;i++) {
            gridVertices[i] = gridVerticesF[i];
        }
        
        FloatBuffer gridVertexBuffer = createFloatBuffer(gridVertices);
        FloatBuffer gridColorBuffer = createFloatBuffer(gridColors);
        
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glEnableVertexAttribArray(colorHandle);
        
        GLES20.glUniform1i(useTextureHandle, 0);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, gridVertexBuffer);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, gridColorBuffer);
        
        GLES20.glLineWidth(gridWidth);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, gridVertices.length / 3);
        
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);
    }
    
    //创建着色器项目
    private int createProgram() {
        String vertexShaderCode =
            "attribute vec4 aPosition;" +
            "attribute vec4 aColor;" +
            "attribute vec2 aTextureCoord;" +
            "varying vec2 vTextureCoord;" +
            "varying vec4 vColor;" +
            "uniform mat4 uMVPMatrix;" +
            "uniform bool uUseTexture;" +
            "void main() {" +
            "    gl_Position = uMVPMatrix * aPosition;" +
            "    if(uUseTexture) {" +
            "        vTextureCoord = aTextureCoord;" + 
            "    }else {" +
            "        vColor = aColor;" +
            "    }" + 
            "}";

        String fragmentShaderCode =
            "precision mediump float;" +
            "varying vec4 vColor;" +
            "varying vec2 vTextureCoord;" +
            "uniform sampler2D uTexture;" +
            "uniform bool uUseTexture;" +
            "void main() {" +
            "    if(uUseTexture) {" +
            "        gl_FragColor = texture2D(uTexture, vTextureCoord);" +
            "    }else {" + 
            "        gl_FragColor = vColor;" +
            "    }" + 
            "}";
        
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        return program;
    }
    
    //加载着色器
    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
    
    //创建float缓冲区
    public FloatBuffer createFloatBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb;
        fb = bb.asFloatBuffer();
        fb.put(array);
        fb.position(0);
        return fb;
    }
    
    //创建int缓冲区
    public IntBuffer createIntBuffer(int[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer ib;
        ib = bb.asIntBuffer();
        ib.put(array);
        ib.position(0);
        return ib;
    }
    
    public void setRendering(boolean s) {
        this.rendering = s;
    }
    
    public void setPoints(ArrayList<Vector> points) {
        this.points = points;
    }
    
    public void setParticleBitmap(Bitmap b) {
        this.particleBitmap = b;
    }
    
    public void setTranslateX(float translateX) {
        this.translateX = translateX;
        listener.setTranslateX(translateX);
    }

    public float getTranslateX() {
        return translateX;
    }

    public void setTranslateY(float translateY) {
        this.translateY = translateY;
        listener.setTranslateY(translateY);
    }

    public float getTranslateY() {
        return translateY;
    }
    
    public void setTranslateZ(float translateZ) {
        this.translateZ = translateZ;
        listener.setTranslateZ(translateZ);
    }

    public float getTranslateZ() {
        return translateZ;
    }
    
    public void setAngleX(float angle) {
        if(angle > 89 - cameraRotation[0]) {
            this.angleX = 89 - cameraRotation[0];
            listener.setAngleX(89 - cameraRotation[0]);
        }else if(angle < -89 - cameraRotation[0]) {
            this.angleX = -89 - cameraRotation[0];
            listener.setAngleX(-89 - cameraRotation[0]);
        }else {
            this.angleX = angle;
            listener.setAngleX(angle);
        }
    }

    public float getAngleX() {
        return angleX;
    }
    
    public void setAngleY(float angle) {
        this.angleY = angle;
        listener.setAngleY(angle);
    }

    public float getAngleY() {
        return angleY;
    }
    
    public void setIsMoving(boolean s) {
        this.isMoving = s;
    }
    
    public void setIsRotating(boolean s) {
        this.isRotating = s;
    }
    
    public float[] getCameraPos() {
    	return cameraRealPos;
    }
    
    public float[] getCameraRotation() {
    	return cameraRealRotation;
    }
    
    public float[] getRightV() {
    	return rightV;
    }
    
    public float[] getUpV() {
    	return upV;
    }
    
    public float[] getForwardV() {
    	return forwardV;
    }
    
    public void setEnableGrid(boolean s) {
        this.enableGrid = s;
    }
    
    public void setEnableAxis(boolean s) {
        this.enableAxis = s;
    }
    
    public void setEnableDynamicRendering(boolean s) {
        this.enableDynamicRendering = s;
    }

    public void setListener(OnRenderListener listener) {
        this.listener= listener;
    }
    

}
