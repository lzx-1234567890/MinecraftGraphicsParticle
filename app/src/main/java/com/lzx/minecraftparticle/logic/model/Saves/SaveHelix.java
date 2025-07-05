package com.lzx.minecraftparticle.logic.model.Saves;

import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Vector;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.param.line.DrawHelixFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SaveHelix extends Save{
    //详细数据
    private String coordinate_xs, coordinate_ys, coordinate_zs, reset_coordinate_xs, reset_coordinate_ys, reset_coordinate_zs;
    private String coordinate_xe, coordinate_ye, coordinate_ze, reset_coordinate_xe, reset_coordinate_ye, reset_coordinate_ze;
    private String radius, reset_radius;
    private String distance, reset_distance;
    private String angle, reset_angle;
    private String step, reset_step;
    private String particle, reset_particle;
    private String filePath, fileName, reset_filePath, reset_fileName;

    //常量
    public final String CXS = "0", CYS = "0", CZS = "0";
    public final String CXE = "10", CYE = "10", CZE = "10";
    public final String RADIUS = "2.5";
    public final String D = "2";
    public final String ANGLE = "0";
    public final String STEP = "0.05";
    public final String PARTICLE = "minecraft:endrod";
    public final String FP = "/storage/emulated/0/Download";
    public final String FN = "helix";


    public SaveHelix() {
        this.type = Type.HELIX;
        this.coordinate_xs = CXS;
        this.coordinate_ys = CYS;
        this.coordinate_zs = CZS;
        this.coordinate_xe = CXE;
        this.coordinate_ye = CYE;
        this.coordinate_ze = CZE;
        this.radius = RADIUS;
        this.distance = D;
        this.angle = ANGLE;
        this.step = STEP;
        this.particle = PARTICLE;
        this.filePath = FP;
        this.fileName = FN;
    }
    
    public String getCoordinateXS() {return coordinate_xs;}
    public String getCoordinateYS() {return coordinate_ys;}
    public String getCoordinateZS() {return coordinate_zs;}
    public String getCoordinateXE() {return coordinate_xe;}
    public String getCoordinateYE() {return coordinate_ye;}
    public String getCoordinateZE() {return coordinate_ze;}
    public String getRadius() {return radius;}
    public String getDistance() {return  distance;}
    public String getAngle() {return angle;}
    public String getStep() {return step;}
    public String getParticle() {return particle;}
    public String getFilePath() {return filePath;}
    public String getFileName() {return fileName;}
    
    public void setCoordinateXS(String v) {this.coordinate_xs = v;}
    public void setCoordinateYS(String v) {this.coordinate_ys = v;}
    public void setCoordinateZS(String v) {this.coordinate_zs = v;}
    public void setCoordinateXE(String v) {this.coordinate_xe = v;}
    public void setCoordinateYE(String v) {this.coordinate_ye = v;}
    public void setCoordinateZE(String v) {this.coordinate_ze = v;}
    public void setRadius(String v) {this.radius = v;}
    public void setDistance(String v) {this.distance = v;}
    public void setAngle(String v) {this.angle = v;}
    public void setStep(String v) {this.step = v;}
    public void setParticle(String v) {this.particle = v;}
    public void setFilePath(String v) {this.filePath = v;}
    public void setFileName(String v) {this.fileName = v;}
    
    @Override
    public void reset() {
        this.coordinate_xs = reset_coordinate_xs;
        this.coordinate_ys = reset_coordinate_ys;
        this.coordinate_zs = reset_coordinate_zs;
        this.coordinate_xe = reset_coordinate_xe;
        this.coordinate_ye = reset_coordinate_ye;
        this.coordinate_ze = reset_coordinate_ze;
        this.radius = reset_radius;
        this.distance = reset_distance;
        this.angle = reset_angle;
        this.step = reset_step;
        this.particle = reset_particle;
        this.filePath = reset_filePath;
        this.fileName = reset_fileName;
    }

    @Override
    public void setReset(){
        this.reset_coordinate_xs = this.coordinate_xs;
        this.reset_coordinate_ys = this.coordinate_ys;
        this.reset_coordinate_zs = this.coordinate_zs;
        this.reset_coordinate_xe = this.coordinate_xe;
        this.reset_coordinate_ye = this.coordinate_ye;
        this.reset_coordinate_ze = this.coordinate_ze;
        this.reset_radius = this.radius;
        this.reset_distance = this.distance;
        this.reset_angle = this.angle;
        this.reset_step = this.step;
        this.reset_particle = this.particle;
        this.reset_filePath = this.filePath;
        this.reset_fileName = this.fileName;
    }

    @Override
    public LinkedHashMap<String, String> getDetailedInformation() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(context.getString(R.string.save_information_line_start), String.format("(%s, %s, %s)", coordinate_xs, coordinate_ys, coordinate_zs));
        map.put(context.getString(R.string.save_information_line_end), String.format("(%s, %s, %s)", coordinate_xe, coordinate_ye, coordinate_ze));
        map.put(context.getString(R.string.save_information_helix_radius), radius);
        map.put(context.getString(R.string.save_information_helix_distance), distance);
        map.put(context.getString(R.string.save_information_line_angle_rotation), angle);
        map.put(context.getString(R.string.save_information_line_step), step);
        map.put(context.getString(R.string.save_information_particle), particle);
        map.put(context.getString(R.string.save_information_filePath), filePath);
        map.put(context.getString(R.string.save_information_fileName), fileName);
        return map;
    }

    @Override
    public ERRORCODE check() {
        if(this.coordinate_xs.isEmpty()) {
            return ERRORCODE.CXS;
        }
        if(this.coordinate_ys.isEmpty()) {
            return ERRORCODE.CYS;
        }
        if(this.coordinate_zs.isEmpty()) {
            return ERRORCODE.CZS;
        }
        if(this.coordinate_xe.isEmpty()) {
            return ERRORCODE.CXE;
        }
        if(this.coordinate_ye.isEmpty()) {
            return ERRORCODE.CYE;
        }
        if(this.coordinate_ze.isEmpty()) {
            return ERRORCODE.CZE;
        }
        if(this.radius.isEmpty() || Double.parseDouble(this.radius) <= 0) {
            return ERRORCODE.RADIUS;
        }
        if(this.distance.isEmpty() || Double.parseDouble(this.distance) == 0) {
            return ERRORCODE.DISTANCE;
        }
        if(this.angle.isEmpty() || Double.parseDouble(this.angle) < 0 || Double.parseDouble(this.angle) > 360) {
            return ERRORCODE.ANGLE;
        }
        if(this.step.isEmpty() || Double.parseDouble(this.step) <= 0) {
            return ERRORCODE.STEP;
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
            DrawUtil.drawHelix(this);
        }else {
            Toast.makeText(fa, context.getString(R.string.permission_storage_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public ArrayList<Vector> getPoints() {
        return DrawUtil.getHelixPoints(this);
    }
    
    @Override
    public DrawFragment getDrawFragment() {
        return new DrawHelixFragment();
    }
    
}
