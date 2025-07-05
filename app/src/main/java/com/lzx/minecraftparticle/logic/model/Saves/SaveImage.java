package com.lzx.minecraftparticle.logic.model.Saves;

import android.graphics.Color;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Vector;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.param.special.DrawImageFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SaveImage extends Save{
    //详细数据
    private String uri, reset_uri;
    private String colorWheel, colorBar, colorAlpha, reset_colorWheel, reset_colorBar, reset_colorAlpha;
    private String unconfirmedCW, unconfirmedCB, unconfirmedCA;
    private String tolerance, reset_tolerance;
    private String coordinate_x, coordinate_y, coordinate_z, reset_coordinate_x, reset_coordinate_y, reset_coordinate_z;
    private String angle, reset_angle;
    private String wx, wy, wz, reset_wx, reset_wy, reset_wz;
    private String hx, hy, hz, reset_hx, reset_hy, reset_hz;
    private String scale, reset_scale;
    private String stepW, stepH, reset_stepW, reset_stepH;
    private String particle, reset_particle;
    private String filePath, fileName, reset_filePath, reset_fileName;

    //常量
    public final String COLORW = String.valueOf(Color.WHITE);
    public final String COLORB = String.valueOf(Color.WHITE);
    public final String COLORA = "255";
    public final String TOLERANCE = "0";
    public final String CX = "0", CY = "0", CZ = "0";
    public final String ANGLE = "0";
    public final String WX = "1", WY="0", WZ="0";
    public final String HX = "0", HY="0", HZ="1";
    public final String SCALE = "1";
    public final String STEPW = "0.5", STEPH = "0.5";
    public final String PARTICLE = "minecraft:endrod";
    public final String FP = "/storage/emulated/0/Download";
    public final String FN = "image";


    public SaveImage() {
        this.type = Type.IMAGE;
        this.colorWheel = COLORW;
        this.colorBar = COLORB;
        this.colorAlpha = COLORA;
        this.unconfirmedCW = COLORW;
        this.unconfirmedCB = COLORB;
        this.unconfirmedCA = COLORA;
        this.tolerance = TOLERANCE;
        this.coordinate_x = CX;
        this.coordinate_y = CY;
        this.coordinate_z = CZ;
        this.angle = ANGLE;
        this.wx = WX;
        this.wy = WY;
        this.wz = WZ;
        this.hx = HX;
        this.hy = HY;
        this.hz = HZ;
        this.scale = SCALE;
        this.stepW = STEPW;
        this.stepH = STEPH;
        this.particle = PARTICLE;
        this.filePath = FP;
        this.fileName = FN;
    }

    public String getUri() {return uri;}
    public String getColorW() {return colorWheel;}
    public String getColorB() {return colorBar;}
    public String getColorA() {return colorAlpha;}
    public String getUnconfirmedCW() {return unconfirmedCW;}
    public String getUnconfirmedCB() {return unconfirmedCB;}
    public String getUnconfirmedCA() {return unconfirmedCA;}
    public String getTolerance() {return tolerance;}
    public String getCoordinateX() {return coordinate_x;}
    public String getCoordinateY() {return coordinate_y;}
    public String getCoordinateZ() {return coordinate_z;}
    public String getAngle() {return angle;}
    public String getWX() {return wx;}
    public String getWY() {return wy;}
    public String getWZ() {return wz;}
    public String getHX() {return hx;}
    public String getHY() {return hy;}
    public String getHZ() {return hz;}
    public String getScale() {return scale;}
    public String getStepW() {return stepW;}
    public String getStepH() {return stepH;}
    public String getParticle() {return particle;}
    public String getFilePath() {return filePath;}
    public String getFileName() {return fileName;}

    public void setUri(String v) {this.uri = v;}
    public void setColorW(String v) {this.colorWheel = v;}
    public void setColorB(String v) {this.colorBar = v;}
    public void setColorA(String v) {this.colorAlpha = v;}
    public void setUnconfirmedCW(String v) {this.unconfirmedCW = v;}
    public void setUnconfirmedCB(String v) {this.unconfirmedCB = v;}
    public void setUnconfirmedCA(String v) {this.unconfirmedCA = v;}
    public void setTolerance(String v) {this.tolerance = v;}
    public void setCoordinateX(String v) {this.coordinate_x = v;}
    public void setCoordinateY(String v) {this.coordinate_y = v;}
    public void setCoordinateZ(String v) {this.coordinate_z = v;}
    public void setAngle(String v) {this.angle = v;}
    public void setWX(String v) {this.wx = v;}
    public void setWY(String v) {this.wy = v;}
    public void setWZ(String v) {this.wz = v;}
    public void setHX(String v) {this.hx = v;}
    public void setHY(String v) {this.hy = v;}
    public void setHZ(String v) {this.hz = v;}
    public void setScale(String v) {this.scale = v;}
    public void setStepW(String v) {this.stepW = v;}
    public void setStepH(String v) {this.stepH = v;}
    public void setParticle(String v) {this.particle = v;}
    public void setFilePath(String v) {this.filePath = v;}
    public void setFileName(String v) {this.fileName = v;}
    
    @Override
    public void reset() {
        this.uri = reset_uri;
        this.colorWheel = reset_colorWheel;
        this.colorBar = reset_colorBar;
        this.colorAlpha = reset_colorAlpha;
        this.tolerance = reset_tolerance;
        this.coordinate_x = reset_coordinate_x;
        this.coordinate_y = reset_coordinate_y;
        this.coordinate_z = reset_coordinate_z;
        this.angle = reset_angle;
        this.wx = reset_wx;
        this.wy = reset_wy;
        this.wz = reset_wz;
        this.hx = reset_hx;
        this.hy = reset_hy;
        this.hz = reset_hz;
        this.scale = reset_scale;
        this.stepW = reset_stepW;
        this.stepH = reset_stepH;
        this.particle = reset_particle;
        this.filePath = reset_filePath;
        this.fileName = reset_fileName;
    }

    @Override
    public void setReset(){
        this.reset_uri = this.uri;
        this.reset_colorWheel = this.colorWheel;
        this.reset_colorBar = this.colorBar;
        this.reset_colorAlpha = this.colorAlpha;
        this.reset_tolerance = this.tolerance;
        this.reset_coordinate_x = this.coordinate_x;
        this.reset_coordinate_y = this.coordinate_y;
        this.reset_coordinate_z = this.coordinate_z;
        this.reset_angle = this.angle;
        this.reset_wx = this.wx;
        this.reset_wy = this.wy;
        this.reset_wz = this.wz;
        this.reset_hx = this.hx;
        this.reset_hy = this.hy;
        this.reset_hz = this.hz;
        this.reset_scale = this.scale;
        this.reset_stepW = this.stepW;
        this.reset_stepH = this.stepH;
        this.reset_particle = this.particle;
        this.reset_filePath = this.filePath;
        this.reset_fileName = this.fileName;
    }

    @Override
    public LinkedHashMap<String, String> getDetailedInformation() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(context.getString(R.string.save_information_image_uri), uri);
        map.put(context.getString(R.string.save_information_image_color), mixColor());
        map.put(context.getString(R.string.save_information_image_tolerance), tolerance);
        map.put(context.getString(R.string.save_information_origin), String.format("(%s, %s, %s)", coordinate_x, coordinate_y, coordinate_z));
        map.put(context.getString(R.string.save_information_image_angle_rotation), angle);
        map.put(context.getString(R.string.save_information_image_vector_width), String.format("(%s, %s, %s)", wx, wy, wz));
        map.put(context.getString(R.string.save_information_image_vector_height), String.format("(%s, %s, %s)", hx, hy, hz));
        map.put(context.getString(R.string.save_information_image_scale), scale);
        map.put(context.getString(R.string.save_information_image_step_width), stepW);
        map.put(context.getString(R.string.save_information_image_step_height), stepH);
        map.put(context.getString(R.string.save_information_particle), particle);
        map.put(context.getString(R.string.save_information_filePath), filePath);
        map.put(context.getString(R.string.save_information_fileName), fileName);
        return map;
    }

    @Override
    public ERRORCODE check() {
        if(this.uri == null) {
            return ERRORCODE.URI;
        }
        if(this.tolerance.isEmpty()) {
            return ERRORCODE.TOLERANCE;
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
        if(this.angle.isEmpty() || Double.parseDouble(this.angle) < 0 || Double.parseDouble(this.angle) > 360) {
            return ERRORCODE.ANGLE;
        }
        if(this.wx.isEmpty()) {
            return ERRORCODE.WX;
        }
        if(this.wy.isEmpty()) {
            return ERRORCODE.WY;
        }
        if(this.wz.isEmpty()) {
            return ERRORCODE.WZ;
        }
        if(this.hx.isEmpty()) {
            return ERRORCODE.HX;
        }
        if(this.hy.isEmpty()) {
            return ERRORCODE.HY;
        }
        if(this.hz.isEmpty()) {
            return ERRORCODE.HZ;
        }
        if(this.scale.isEmpty() || Double.parseDouble(this.scale) <= 0) {
            return ERRORCODE.SCALE;
        }
        if(this.stepW.isEmpty() || Double.parseDouble(this.stepW) <= 0) {
            return ERRORCODE.STEPW;
        }
        if(this.stepH.isEmpty() || Double.parseDouble(this.stepH) <= 0) {
            return ERRORCODE.STEPH;
        }
        if(this.particle.isEmpty()) {
            return ERRORCODE.PARTICLE;
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
            DrawUtil.drawImage(this);
        }else {
            Toast.makeText(fa, context.getString(R.string.permission_storage_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public ArrayList<Vector> getPoints() {
        return DrawUtil.getImagePoints(this);
    }
    
    @Override
    public DrawFragment getDrawFragment() {
        return new DrawImageFragment();
    }

    public String mixColor() {
        float[] hsvW = new float[3];
        float[] hsvB = new float[3];
        Color.colorToHSV(Integer.parseInt(getColorW()), hsvW);
        Color.colorToHSV(Integer.parseInt(getColorB()), hsvB);
        float[] hsv = {hsvW[0], hsvW[1], hsvB[2]};
        int color = Color.HSVToColor(Integer.parseInt(getColorA()), hsv);
        return "#" + Integer.toHexString(color).toUpperCase();
    }

    
}
