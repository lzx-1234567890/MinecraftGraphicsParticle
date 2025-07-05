package com.lzx.minecraftparticle.logic.model.Saves;

import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Vector;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.param.line.DrawParabolaFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SaveParabola extends Save{
    //详细数据
    private String coordinate_xs, coordinate_ys, coordinate_zs, reset_coordinate_xs, reset_coordinate_ys, reset_coordinate_zs;
    private String coordinate_xe, coordinate_ye, coordinate_ze, reset_coordinate_xe, reset_coordinate_ye, reset_coordinate_ze;
    private String gravity_x, gravity_y, gravity_z, reset_gravity_x, reset_gravity_y, reset_gravity_z;
    private String angle, reset_angle;
    private String step, reset_step;
    private String particle, reset_particle;
    private String filePath, fileName, reset_filePath, reset_fileName;

    //常量
    public final String CXS = "0", CYS = "0", CZS = "0";
    public final String CXE = "10", CYE = "-10", CZE = "10";
    public final String GX = "0", GY = "-1", GZ = "0";
    public final String ANGLE = "0";
    public final String STEP = "0.25";
    public final String PARTICLE = "minecraft:endrod";
    public final String FP = "/storage/emulated/0/Download";
    public final String FN = "parabola";


    public SaveParabola() {
        this.type = Type.PARABOLA;
        this.coordinate_xs = CXS;
        this.coordinate_ys = CYS;
        this.coordinate_zs = CZS;
        this.coordinate_xe = CXE;
        this.coordinate_ye = CYE;
        this.coordinate_ze = CZE;
        this.gravity_x = GX;
        this.gravity_y = GY;
        this.gravity_z = GZ;
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
    public String getGravityX() {return gravity_x;}
    public String getGravityY() {return gravity_y;}
    public String getGravityZ() {return gravity_z;}
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
    public void setGravityX(String v) {this.gravity_x = v;}
    public void setGravityY(String v) {this.gravity_y = v;}
    public void setGravityZ(String v) {this.gravity_z = v;}
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
        this.gravity_x = reset_gravity_x;
        this.gravity_y = reset_gravity_y;
        this.gravity_z = reset_gravity_z;
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
        this.reset_gravity_x = this.gravity_x;
        this.reset_gravity_y = this.gravity_y;
        this.reset_gravity_z = this.gravity_z;
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
        map.put(context.getString(R.string.save_information_line_vector_g), String.format("(%s, %s, %s)", gravity_x, gravity_y, gravity_z));
        map.put(context.getString(R.string.save_information_line_angle_direction), angle);
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
        if(this.gravity_x.isEmpty()) {
            return ERRORCODE.GX;
        }
        if(this.gravity_y.isEmpty()) {
            return ERRORCODE.GY;
        }
        if(this.gravity_z.isEmpty()) {
            return ERRORCODE.GZ;
        }
        if(this.angle.isEmpty() || Double.parseDouble(this.angle) >= 90 || Double.parseDouble(this.angle) <= -90) {
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
            DrawUtil.drawParabola(this);
        }else {
            Toast.makeText(fa, context.getString(R.string.permission_storage_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public ArrayList<Vector> getPoints() {
        return DrawUtil.getParabolaPoints(this);
    }

    @Override
    public DrawFragment getDrawFragment() {
        return new DrawParabolaFragment();
    }

}
