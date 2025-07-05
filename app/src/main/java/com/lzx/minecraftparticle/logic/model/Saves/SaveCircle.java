package com.lzx.minecraftparticle.logic.model.Saves;

import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;

import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Vector;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.param.regular.DrawCircleFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SaveCircle extends Save{
    //详细数据
    private String coordinate_x, coordinate_y, coordinate_z, reset_coordinate_x, reset_coordinate_y, reset_coordinate_z;
    private String hAngle, vAngle, reset_hAngle, reset_vAngle;
    private String radius, reset_radius;
    private String step, reset_step;
    private String particle, reset_particle;
    private String filePath, fileName, reset_filePath, reset_fileName;
    
    //常量
    public final String CX = "0", CY = "0", CZ = "0";
    public final String HA = "0", VA = "0";
    public final String RADIUS = "5";
    public final String STEP = "4";
    public final String PARTICLE = "minecraft:endrod";
    public final String FP = "/storage/emulated/0/Download";
    public final String FN = "circle";
    
    
    public SaveCircle() {
        this.type = Save.Type.CIRCLE;
        this.coordinate_x = CX;
        this.coordinate_y = CY;
        this.coordinate_z = CZ;
        this.hAngle = HA;
        this.vAngle = VA;
        this.radius = RADIUS;
        this.step = STEP;
        this.particle = PARTICLE;
        this.filePath = FP;
        this.fileName = FN;
    }
    
    public String getCoordinateX() {return coordinate_x;}
    public String getCoordinateY() {return coordinate_y;}
    public String getCoordinateZ() {return coordinate_z;}
    public String getHAngle() {return hAngle;}
    public String getVAngle() {return vAngle;}
    public String getRadius() {return radius;}
    public String getStep() {return step;}
    public String getParticle() {return particle;}
    public String getFilePath() {return filePath;}
    public String getFileName() {return fileName;}
    
    public void setCoordinateX(String v) {this.coordinate_x = v;}
    public void setCoordinateY(String v) {this.coordinate_y = v;}
    public void setCoordinateZ(String v) {this.coordinate_z = v;}
    public void setHAngle(String v) {this.hAngle = v;}
    public void setVAngle(String v) {this.vAngle = v;}
    public void setRadius(String v) {this.radius = v;}
    public void setStep(String v) {this.step = v;}
    public void setParticle(String v) {this.particle = v;}
    public void setFilePath(String v) {this.filePath = v;}
    public void setFileName(String v) {this.fileName = v;}
    
    @Override
    public void reset() {
        this.coordinate_x = reset_coordinate_x;
        this.coordinate_y = reset_coordinate_y;
        this.coordinate_z = reset_coordinate_z;
        this.hAngle = reset_hAngle;
        this.vAngle = reset_vAngle;
        this.radius = reset_radius;
        this.step = reset_step;
        this.particle = reset_particle;
        this.filePath = reset_filePath;
        this.fileName = reset_fileName;
    }

    @Override
    public void setReset(){
        this.reset_coordinate_x = this.coordinate_x;
        this.reset_coordinate_y = this.coordinate_y;
        this.reset_coordinate_z = this.coordinate_z;
        this.reset_hAngle = this.hAngle;
        this.reset_vAngle = this.vAngle;
        this.reset_radius = this.radius;
        this.reset_step = this.step;
        this.reset_particle = this.particle;
        this.reset_filePath = this.filePath;
        this.reset_fileName = this.fileName;
    }

    @Override
    public LinkedHashMap<String, String> getDetailedInformation() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(context.getString(R.string.save_information_circle_coordinate), String.format("(%s, %s, %s)", coordinate_x, coordinate_y, coordinate_z));
        map.put(context.getString(R.string.save_information_regular_hAngle), hAngle);
        map.put(context.getString(R.string.save_information_regular_vAngle), vAngle);
        map.put(context.getString(R.string.save_information_regular_radius), radius);
        map.put(context.getString(R.string.save_information_regular_step), step);
        map.put(context.getString(R.string.save_information_particle), particle);
        map.put(context.getString(R.string.save_information_filePath), filePath);
        map.put(context.getString(R.string.save_information_fileName), fileName);
        return map;
    }

    @Override
    public ERRORCODE check() {
        if(this.coordinate_x.isEmpty()) {
            return ERRORCODE.CX;
        }
        if(this.coordinate_y.isEmpty()) {
            return ERRORCODE.CY;
        }
        if(this.coordinate_z.isEmpty()) {
            return ERRORCODE.CZ;
        }
        if(this.hAngle.isEmpty() || Double.parseDouble(this.hAngle) > 180 || Double.parseDouble(this.hAngle) < -180) {
            return ERRORCODE.HA;
        }
        if(this.vAngle.isEmpty() || Double.parseDouble(this.vAngle) > 90 || Double.parseDouble(this.vAngle) < -90) {
            return ERRORCODE.VA;
        }
        if(this.radius.isEmpty() || Double.parseDouble(this.radius) <= 0) {
            return ERRORCODE.RADIUS;
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
            DrawUtil.drawCircle(this);
        }else {
            Toast.makeText(fa, context.getString(R.string.permission_storage_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public ArrayList<Vector> getPoints() {
        return DrawUtil.getCirclePoints(this);
    }
    
    @Override
    public DrawFragment getDrawFragment() {
        return new DrawCircleFragment();
    }
    
}
