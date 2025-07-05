package com.lzx.minecraftparticle.logic.model.Saves;

import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Vector;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.param.math.DrawFunctionFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SaveFunction extends Save{
    //详细数据
    private String exp, reset_exp;
    private String xs, xe, reset_xs, reset_xe;
    private String coordinate_x, coordinate_y, coordinate_z, reset_coordinate_x, reset_coordinate_y, reset_coordinate_z;
    private String xAxisX, xAxisY, xAxisZ, reset_xAxisX, reset_xAxisY, reset_xAxisZ;
    private String yAxisX, yAxisY, yAxisZ, reset_yAxisX, reset_yAxisY, reset_yAxisZ;
    private String scaleX, scaleY, reset_scaleX, reset_scaleY;
    private String lengthX, lengthY, reset_lengthX, reset_lengthY;
    private String step, stepX, stepY, reset_step, reset_stepX, reset_stepY;
    private boolean xAxisP, yAxisP, xAxisN, yAxisN, reset_xAxisP, reset_yAxisP, reset_xAxisN, reset_yAxisN;
    private String particle, particleX, particleY, reset_particle, reset_particleX, reset_particleY;
    private String filePath, fileName, reset_filePath, reset_fileName;
    private int[] particleNum = new int[4];

    //常量
    public final String EXP = "x";
    public final String XS = "0", XE = "10";
    public final String CX = "0", CY = "0", CZ = "0";
    public final String XAX = "0", XAY = "0", XAZ = "1";
    public final String YAX = "0", YAY = "1", YAZ = "0";
    public final String SCALEX = "1", SCALEY = "1";
    public final String LX = "50", LY = "50";
    public final String STEP = "0.5", STEPX = "0.5", STEPY = "0.5";
    public final boolean XP = true, YP = true, XN = true, YN = true;
    public final String PARTICLE = "minecraft:basic_flame_particle", PARTICLEX = "minecraft:endrod", PARTICLEY="minecraft:endrod";
    public final String FP = "/storage/emulated/0/Download";
    public final String FN = "function";


    public SaveFunction() {
        this.type = Type.FUNCTION;
        this.exp = EXP;
        this.xs = XS;
        this.xe = XE;
        this.coordinate_x = CX;
        this.coordinate_y = CY;
        this.coordinate_z = CZ;
        this.xAxisX = XAX;
        this.xAxisY = XAY;
        this.xAxisZ = XAZ;
        this.yAxisX = YAX;
        this.yAxisY = YAY;
        this.yAxisZ = YAZ;
        this.scaleX = SCALEX;
        this.scaleY = SCALEY;
        this.lengthX = LX;
        this.lengthY = LY;
        this.step = STEP;
        this.stepX = STEPX;
        this.stepY = STEPY;
        this.xAxisP = XP;
        this.xAxisN = XN;
        this.yAxisP = YP;
        this.yAxisN = YN;
        this.particle = PARTICLE;
        this.particleX = PARTICLEX;
        this.particleY = PARTICLEY;
        this.filePath = FP;
        this.fileName = FN;
    }

    public String getExp() {return exp;}
    public String getXs() {return xs;}
    public String getXe() {return xe;}
    public String getCoordinateX() {return coordinate_x;}
    public String getCoordinateY() {return coordinate_y;}
    public String getCoordinateZ() {return coordinate_z;}
    public String getXAxisX() {return xAxisX;}
    public String getXAxisY() {return xAxisY;}
    public String getXAxisZ() {return xAxisZ;}
    public String getYAxisX() {return yAxisX;}
    public String getYAxisY() {return yAxisY;}
    public String getYAxisZ() {return yAxisZ;}
    public String getScaleX() {return scaleX;}
    public String getScaleY() {return scaleY;}
    public String getLengthX() {return lengthX;}
    public String getLengthY() {return lengthY;}
    public String getStep() {return step;}
    public String getStepX() {return stepX;}
    public String getStepY() {return stepY;}
    public boolean getXAxisP() {return xAxisP;}
    public boolean getXAxisN() {return xAxisN;}
    public boolean getYAxisP() {return yAxisP;}
    public boolean getYAxisN() {return yAxisN;}
    public String getParticle() {return particle;}
    public String getParticleX() {return particleX;}
    public String getParticleY() {return particleY;}
    public String getFilePath() {return filePath;}
    public String getFileName() {return fileName;}
    public int[] getParticleNum() {return particleNum;}

    public void setExp(String v) {this.exp = v;}
    public void setXs(String v) {this.xs = v;}
    public void setXe(String v) {this.xe = v;}
    public void setCoordinateX(String v) {this.coordinate_x = v;}
    public void setCoordinateY(String v) {this.coordinate_y = v;}
    public void setCoordinateZ(String v) {this.coordinate_z = v;}
    public void setXAxisX(String v) {this.xAxisX = v;}
    public void setXAxisY(String v) {this.xAxisY = v;}
    public void setXAxisZ(String v) {this.xAxisZ = v;}
    public void setYAxisX(String v) {this.yAxisX = v;}
    public void setYAxisY(String v) {this.yAxisY = v;}
    public void setYAxisZ(String v) {this.yAxisZ = v;}
    public void setScaleX(String v) {this.scaleX = v;}
    public void setScaleY(String v) {this.scaleY = v;}
    public void setLengthX(String v) {this.lengthX = v;}
    public void setLengthY(String v) {this.lengthY = v;}
    public void setStep(String v) {this.step = v;}
    public void setStepX(String v) {this.stepX = v;}
    public void setStepY(String v) {this.stepY = v;}
    public void setXAxisP(boolean v) {this.xAxisP = v;}
    public void setXAxisN(boolean v) {this.xAxisN = v;}
    public void setYAxisP(boolean v) {this.yAxisP = v;}
    public void setYAxisN(boolean v) {this.yAxisN = v;}
    public void setParticle(String v) {this.particle = v;}
    public void setParticleX(String v) {this.particleX = v;}
    public void setParticleY(String v) {this.particleY = v;}
    public void setFilePath(String v) {this.filePath = v;}
    public void setFileName(String v) {this.fileName = v;}
    
    @Override
    public void reset() {
        this.exp = reset_exp;
        this.xs = reset_xs;
        this.xe = reset_xe;
        this.coordinate_x = reset_coordinate_x;
        this.coordinate_y = reset_coordinate_y;
        this.coordinate_z = reset_coordinate_z;
        this.xAxisX = reset_xAxisX;
        this.xAxisY = reset_xAxisY;
        this.xAxisZ = reset_xAxisZ;
        this.yAxisX = reset_yAxisX;
        this.yAxisY = reset_yAxisY;
        this.yAxisZ = reset_yAxisZ;
        this.scaleX = reset_scaleX;
        this.scaleY = reset_scaleY;
        this.lengthX = reset_lengthX;
        this.lengthY = reset_lengthY;
        this.step = reset_step;
        this.stepX = reset_stepX;
        this.stepY = reset_stepY;
        this.xAxisP = reset_xAxisP;
        this.xAxisN = reset_xAxisN;
        this.yAxisP = reset_yAxisP;
        this.yAxisN = reset_yAxisN;
        this.particleX = reset_particleX;
        this.particleY = reset_particleY;
        this.particle = reset_particle;
        this.filePath = reset_filePath;
        this.fileName = reset_fileName;
    }

    @Override
    public void setReset(){
        this.reset_exp = this.exp;
        this.reset_xs = this.xs;
        this.reset_xe = this.xe;
        this.reset_coordinate_x = this.coordinate_x;
        this.reset_coordinate_y = this.coordinate_y;
        this.reset_coordinate_z = this.coordinate_z;
        this.reset_xAxisX = this.xAxisX;
        this.reset_xAxisY = this.xAxisY;
        this.reset_xAxisZ = this.xAxisZ;
        this.reset_yAxisX = this.yAxisX;
        this.reset_yAxisY = this.yAxisY;
        this.reset_yAxisZ = this.yAxisZ;
        this.reset_scaleX = this.scaleX;
        this.reset_scaleY = this.scaleY;
        this.reset_lengthX = this.lengthX;
        this.reset_lengthY = this.lengthY;
        this.reset_step = this.step;
        this.reset_stepX = this.stepX;
        this.reset_stepY = this.stepY;
        this.reset_xAxisP = this.xAxisP;
        this.reset_xAxisN = this.xAxisN;
        this.reset_yAxisP = this.yAxisP;
        this.reset_yAxisN = this.yAxisN;
        this.reset_particle = this.particle;
        this.reset_particleX = this.particleX;
        this.reset_particleY = this.particleY;
        this.reset_filePath = this.filePath;
        this.reset_fileName = this.fileName;
    }

    @Override
    public LinkedHashMap<String, String> getDetailedInformation() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(context.getString(R.string.save_information_function_exp), exp);
        map.put(context.getString(R.string.save_information_function_definition), String.format("[%s, %s]", xs, xe));
        map.put(context.getString(R.string.save_information_origin), String.format("(%s, %s, %s)", coordinate_x, coordinate_y, coordinate_z));
        map.put(context.getString(R.string.save_information_function_vector_x), String.format("(%s, %s, %s)", xAxisX, xAxisY, xAxisZ));
        map.put(context.getString(R.string.save_information_function_vector_y), String.format("(%s, %s, %s)", yAxisX, yAxisY, yAxisZ));
        map.put(context.getString(R.string.save_information_function_scale_x), scaleX);
        map.put(context.getString(R.string.save_information_function_scale_y), scaleY);
        map.put(context.getString(R.string.save_information_function_length_x), lengthX);
        map.put(context.getString(R.string.save_information_function_length_y), lengthY);
        map.put(context.getString(R.string.save_information_function_step), step);
        map.put(context.getString(R.string.save_information_function_step_x), stepX);
        map.put(context.getString(R.string.save_information_function_step_y), stepY);
        map.put(context.getString(R.string.save_information_function_draw_x_axis_p), xAxisP ? context.getString(R.string.yes) : context.getString(R.string.no));
        map.put(context.getString(R.string.save_information_function_draw_x_axis_n), xAxisN ? context.getString(R.string.yes) : context.getString(R.string.no));
        map.put(context.getString(R.string.save_information_function_draw_y_axis_p), yAxisP ? context.getString(R.string.yes) : context.getString(R.string.no));
        map.put(context.getString(R.string.save_information_function_draw_y_axis_n), yAxisN ? context.getString(R.string.yes) : context.getString(R.string.no));
        map.put(context.getString(R.string.save_information_function_particle), particle);
        map.put(context.getString(R.string.save_information_function_particle_x), particleX);
        map.put(context.getString(R.string.save_information_function_particle_y), particleY);
        map.put(context.getString(R.string.save_information_filePath), filePath);
        map.put(context.getString(R.string.save_information_fileName), fileName);
        return map;
    }

    @Override
    public ERRORCODE check() {
        if(this.exp.isEmpty()) {
            return ERRORCODE.EXP;
        }
        if(this.xs.isEmpty()) {
            return ERRORCODE.XS;
        }
        if(this.xe.isEmpty()) {
            return ERRORCODE.XE;
        }
        if(this.coordinate_x.isEmpty()) {
            return ERRORCODE.CX;
        }
        if(this.coordinate_y.isEmpty()) {
            return ERRORCODE.CY;
        }
        if(this.coordinate_z.isEmpty()) {
            return ERRORCODE.CZ;
        }
        if(this.xAxisX.isEmpty()) {
            return ERRORCODE.XAX;
        }
        if(this.xAxisY.isEmpty()) {
            return ERRORCODE.XAY;
        }
        if(this.xAxisZ.isEmpty()) {
            return ERRORCODE.XAZ;
        }
        if(this.yAxisX.isEmpty()) {
            return ERRORCODE.YAX;
        }
        if(this.yAxisY.isEmpty()) {
            return ERRORCODE.YAY;
        }
        if(this.yAxisZ.isEmpty()) {
            return ERRORCODE.YAZ;
        }
        if(this.scaleX.isEmpty() || Double.parseDouble(this.scaleX) <= 0) {
            return ERRORCODE.SCALEX;
        }
        if(this.scaleY.isEmpty() || Double.parseDouble(this.scaleY) <= 0) {
            return ERRORCODE.SCALEY;
        }
        if(this.lengthX.isEmpty() || Double.parseDouble(this.lengthX) <= 0) {
            return ERRORCODE.LX;
        }
        if(this.lengthY.isEmpty() || Double.parseDouble(this.lengthY) <= 0) {
            return ERRORCODE.LY;
        }
        if(this.step.isEmpty() || Double.parseDouble(this.step) <= 0) {
            return ERRORCODE.STEP;
        }
        if(this.stepX.isEmpty() || Double.parseDouble(this.stepX) <= 0) {
            return ERRORCODE.STEPX;
        }
        if(this.stepY.isEmpty() || Double.parseDouble(this.stepY) <= 0) {
            return ERRORCODE.STEPY;
        }
        if(this.particle.isEmpty()) {
            return ERRORCODE.PARTICLE;
        }
        if(this.particleX.isEmpty()) {
            return ERRORCODE.PARTICLEX;
        }
        if(this.particleY.isEmpty()) {
            return ERRORCODE.PARTICLEY;
        }
        if(this.filePath.isEmpty()) {
            return ERRORCODE.FP;
        }
        if(this.fileName.isEmpty()) {
            return ERRORCODE.FN;
        }
        return null;
    }

    @Override
    public void draw(FragmentActivity fa) {
        if(PermissionUtil.requestStorage(fa)) {
            DrawUtil.drawFunction(this);
        }else {
            Toast.makeText(fa, context.getString(R.string.permission_storage_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public ArrayList<Vector> getPoints() {
        return DrawUtil.getFunctionPoints(this);
    }
    
    @Override
    public DrawFragment getDrawFragment() {
        return new DrawFunctionFragment();
    }
    
}
