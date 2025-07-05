package com.lzx.minecraftparticle.logic.model.Saves;

import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Vector;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.param.regular.DrawStarFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SaveStar extends Save {
    //详细数据
    private String coordinate_x, coordinate_y, coordinate_z, reset_coordinate_x, reset_coordinate_y, reset_coordinate_z;
    private String hAngle, vAngle, reset_hAngle, reset_vAngle;
    private String offset, reset_offset;
    private String sideL, sideBN, sideTN, reset_sideL, reset_sideBN, reset_sideTN;
    private String step, reset_step;
    private String particle, reset_particle;
    private String filePath, fileName, reset_filePath, reset_fileName;

    //常量
    public final String CX = "0", CY = "0", CZ = "0";
    public final String HA = "0", VA = "0";
    public final String OFFSET = "0";
    public final String SL = "10", SBN = "3", STN = "3";
    public final String STEP = "4";
    public final String PARTICLE = "minecraft:endrod";
    public final String FP = "/storage/emulated/0/Download";
    public final String FN = "star";

    public SaveStar() {
        this.type = Type.STAR;
        this.coordinate_x = CX;
        this.coordinate_y = CY;
        this.coordinate_z = CZ;
        this.hAngle = HA;
        this.vAngle = VA;
        this.offset = OFFSET;
        this.sideL = SL;
        this.sideBN = SBN;
        this.sideTN = STN;
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
    public String getOffset() {return offset;};
    public String getSideL() {return sideL;}
    public String getSideBN() {return sideBN;}
    public String getSideTN() {return sideTN;}
    public String getStep() {return step;}
    public String getParticle() {return particle;}
    public String getFilePath() {return filePath;}
    public String getFileName() {return fileName;}

    public void setCoordinateX(String v) {this.coordinate_x = v;}
    public void setCoordinateY(String v) {this.coordinate_y = v;}
    public void setCoordinateZ(String v) {this.coordinate_z = v;}
    public void setHAngle(String v) {this.hAngle = v;}
    public void setVAngle(String v) {this.vAngle = v;}
    public void setOffset(String v) {this.offset = v;};
    public void setSideL(String v) {this.sideL = v;}
    public void setSideBN(String v) {this.sideBN = v;}
    public void setSideTN(String v) {this.sideTN = v;}
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
        this.offset = reset_offset;
        this.sideL = reset_sideL;
        this.sideBN = reset_sideBN;
        this.sideTN = reset_sideTN;
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
        this.reset_offset = this.offset;
        this.reset_sideL = this.sideL;
        this.reset_sideBN = this.sideBN;
        this.reset_sideTN = this.sideTN;
        this.reset_step = this.step;
        this.reset_particle = this.particle;
        this.reset_filePath = this.filePath;
        this.reset_fileName = this.fileName;
    }

    @Override
    public LinkedHashMap<String, String> getDetailedInformation() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(context.getString(R.string.save_information_regular_coordinate), String.format("(%s, %s, %s)", coordinate_x, coordinate_y, coordinate_z));
        map.put(context.getString(R.string.save_information_regular_hAngle), hAngle);
        map.put(context.getString(R.string.save_information_regular_vAngle), vAngle);
        map.put(context.getString(R.string.save_information_regular_offset), offset);
        map.put(context.getString(R.string.save_information_regular_side_length), sideL);
        map.put(context.getString(R.string.save_information_star_base), sideBN);
        map.put(context.getString(R.string.save_information_star_target), sideTN);
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
        if(this.offset.isEmpty()) {
            return ERRORCODE.OFFSET;
        }
        if(this.sideL.isEmpty() || Double.parseDouble(this.sideL) <= 0) {
            return ERRORCODE.SL;
        }
        if(this.sideBN.isEmpty() || Integer.parseInt(this.sideBN) <= 2 || (Integer.parseInt(this.sideBN) != 4 && Integer.parseInt(this.sideBN) % 2 == 0)) {
            return ERRORCODE.SBN;
        }
        if(this.sideTN.isEmpty() || Integer.parseInt(this.sideTN) <= 2) {
            return ERRORCODE.STN;
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
            DrawUtil.drawStar(this);
        }else {
            Toast.makeText(fa, context.getString(R.string.permission_storage_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public ArrayList<Vector> getPoints() {
        return DrawUtil.getStarPoints(this);
    }

    @Override
    public DrawFragment getDrawFragment() {
        return new DrawStarFragment();
    }
}
