package com.lzx.minecraftparticle.logic.model.Saves;

import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Vector;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.param.math.DrawFourierFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SaveFourier extends Save{
    //详细数据
    private String coordinate_x, coordinate_y, coordinate_z, reset_coordinate_x, reset_coordinate_y, reset_coordinate_z;
    private String uri, reset_uri;
    private  transient ArrayList<RadioButton> pathRadioButtons = new ArrayList<>();
    private int pathId, reset_pathId;
    private String path;
    private String angle, reset_angle;
    private boolean xP, zP, reset_xP, reset_zP;
    private String cycle, reset_cycle;
    private String scale, reset_scale;
    private String num, reset_num;
    private String pointNum, reset_pointNum;
    private String particle, reset_particle;
    private String asName, reset_asName;
    private String filePath, fileName, reset_filePath, reset_fileName;

    //常量
    public final String CX = "0", CY = "0", CZ = "0";
    public final int PID = 0;
    public final String ANGLE = "0";
    public final boolean XP = true;
    public final boolean ZP = true;
    public final String CYCLE = "100";
    public final String SCALE = "1";
    public final String NUM = "50";
    public final String PNUM = "100";
    public final String PARTICLE = "minecraft:endrod";
    public final String ASN = "fourier";
    public final String FP = "/storage/emulated/0/Download";
    public final String FN = "fourier";


    public SaveFourier() {
        this.type = Type.FOURIER;
        this.coordinate_x = CX;
        this.coordinate_y = CY;
        this.coordinate_z = CZ;
        this.pathId = PID;
        this.angle = ANGLE;
        this.xP = XP;
        this.zP = ZP;
        this.cycle = CYCLE;
        this.scale = SCALE;
        this.num = NUM;
        this.pointNum = PNUM;
        this.particle = PARTICLE;
        this.asName = ASN;
        this.filePath = FP;
        this.fileName = FN;
    }
    
    public String getCoordinateX() {return coordinate_x;}
    public String getCoordinateY() {return coordinate_y;}
    public String getCoordinateZ() {return coordinate_z;}
    public String getUri() {return uri;}
    public ArrayList<RadioButton> getPathRadioButtons() {return pathRadioButtons;}
    public int getPathId() {return pathId;}
    public String getPath() {return path;}
    public String getAngle() {return angle;}
    public boolean getXP() {return xP;}
    public boolean getZP() {return zP;}
    public String getCycle() {return cycle;}
    public String getScale() {return scale;}
    public String getNum() {return num;}
    public String getPointNum() {return pointNum;}
    public String getParticle() {return particle;}
    public String getASName() {return asName;}
    public String getFilePath() {return filePath;}
    public String getFileName() {return fileName;}
    
    public void setCoordinateX(String v) {this.coordinate_x = v;}
    public void setCoordinateY(String v) {this.coordinate_y = v;}
    public void setCoordinateZ(String v) {this.coordinate_z = v;}
    public void setUri(String v) {this.uri = v;}
    public void setPathId(int id) {this.pathId = id;}
    public void setPath(String v) {this.path = v;}
    public void setAngle(String v) {this.angle = v;}
    public void setXP(boolean v) {this.xP = v;}
    public void setZP(boolean v) {this.zP = v;}
    public void setCycle(String v) {this.cycle = v;}
    public void setScale(String v) {this.scale = v;}
    public void setNum(String v) {this.num = v;}
    public void setPointNum(String v) {this.pointNum = v;}
    public void setParticle(String v) {this.particle = v;}
    public void setASName(String v) {this.asName = v;}
    public void setFilePath(String v) {this.filePath = v;}
    public void setFileName(String v) {this.fileName = v;}
    
    @Override
    public void reset() {
        this.coordinate_x = reset_coordinate_x;
        this.coordinate_y = reset_coordinate_y;
        this.coordinate_z = reset_coordinate_z;
        this.uri = reset_uri;
        this.pathId = reset_pathId;
        this.angle = reset_angle;
        this.xP = reset_xP;
        this.zP = reset_zP;
        this.cycle = reset_cycle;
        this.scale = reset_scale;
        this.num = reset_num;
        this.pointNum = reset_pointNum;
        this.particle = reset_particle;
        this.filePath = reset_filePath;
        this.asName = reset_asName;
        this.fileName = reset_fileName;
    }

    @Override
    public void setReset(){
        this.reset_coordinate_x = this.coordinate_x;
        this.reset_coordinate_y = this.coordinate_y;
        this.reset_coordinate_z = this.coordinate_z;
        this.reset_uri = this.uri;
        this.reset_pathId = this.pathId;
        this.reset_angle = this.angle;
        this.reset_xP = this.xP;
        this.reset_zP = this.zP;
        this.reset_cycle = this.cycle;
        this.reset_scale = this.scale;
        this.reset_num = this.num;
        this.reset_pointNum = this.pointNum;
        this.reset_particle = this.particle;
        this.reset_asName = this.asName;
        this.reset_filePath = this.filePath;
        this.reset_fileName = this.fileName;
    }

    @Override
    public LinkedHashMap<String, String> getDetailedInformation() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(context.getString(R.string.save_information_fourier_uri), uri);
        map.put(context.getString(R.string.save_information_fourier_path), String.valueOf(pathId + 1));
        map.put(context.getString(R.string.save_information_origin), String.format("(%s, %s, %s)", coordinate_x, coordinate_y, coordinate_z));
        map.put(context.getString(R.string.save_information_fourier_angle_rotation), angle);
        if(xP) {
            map.put(context.getString(R.string.save_information_fourier_x_axis_p), context.getString(R.string.yes));
        }else {
            map.put(context.getString(R.string.save_information_fourier_x_axis_n), context.getString(R.string.yes));
        }
        if(zP) {
            map.put(context.getString(R.string.save_information_fourier_z_axis_p), context.getString(R.string.yes));
        }else {
            map.put(context.getString(R.string.save_information_fourier_z_axis_n), context.getString(R.string.yes));
        }
        map.put(context.getString(R.string.save_information_fourier_cycle), cycle);
        map.put(context.getString(R.string.save_information_fourier_scale), scale);
        map.put(context.getString(R.string.save_information_fourier_armor_stand_number), num);
        map.put(context.getString(R.string.save_information_fourier_point_number), pointNum);
        map.put(context.getString(R.string.save_information_particle), particle);
        map.put(context.getString(R.string.save_information_fourier_armor_stand_name), asName);
        map.put(context.getString(R.string.save_information_filePath), filePath);
        map.put(context.getString(R.string.save_information_fileName), fileName);
        return map;
    }

    @Override
    public ERRORCODE check() {
        if(this.uri == null) {
            return ERRORCODE.URI;
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
        if(this.uri.isEmpty()) {
            return ERRORCODE.URI;
        }
        if(this.angle.isEmpty() || Double.parseDouble(this.angle) < 0 || Double.parseDouble(this.angle) > 360) {
            return ERRORCODE.ANGLE;
        }
        if(this.cycle.isEmpty() || Double.parseDouble(this.cycle) <= 0) {
            return ERRORCODE.CYCLE;
        }
        if(this.scale.isEmpty() || Double.parseDouble(this.scale) <= 0) {
            return ERRORCODE.SCALE;
        }
        if(this.num.isEmpty() || Double.parseDouble(this.num) <= 0 || Double.parseDouble(this.num) % 2 != 0) {
            return ERRORCODE.NUM;
        }
        if(this.pointNum.isEmpty() || Double.parseDouble(this.pointNum) <= 0) {
            return ERRORCODE.PNUM;
        }
        if(this.particle.isEmpty()) {
            return ERRORCODE.PARTICLE;
        }
        if(this.asName.isEmpty()) {
            return ERRORCODE.ASN;
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
            DrawUtil.drawFourier(this);
        }else {
            Toast.makeText(fa, context.getString(R.string.permission_storage_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public ArrayList<Vector> getPoints() {
        return DrawUtil.getFourierPoints(this);
    }
    
    @Override
    public DrawFragment getDrawFragment() {
        return new DrawFourierFragment();
    }
    
}
