package com.lzx.minecraftparticle.logic.model.Saves;

import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Vector;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;
import com.lzx.minecraftparticle.ui.fragment.draw.param.line.DrawArcFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SaveArc extends Save{
    //详细数据
    private ArrayList<String[]> nodes = new ArrayList<>();
    private ArrayList<String[]> reset_nodes = new ArrayList<>();
    private String radius, reset_radius;
    private String angle, reset_angle;
    private boolean majorArc, reset_majorArc;
    private String step, reset_step;
    private boolean stepArc, reset_stepArc;
    private String particle, reset_particle;
    private String filePath, fileName, reset_filePath, reset_fileName;

    //常
    public final String RADIUS = "10";
    public final String ANGLE = "0";
    public final boolean MA = true;
    public final String STEP = "1";
    public final boolean SA = true;
    public final String PARTICLE = "minecraft:endrod";
    public final String FP = "/storage/emulated/0/Download";
    public final String FN = "arc";


    public SaveArc() {
        this.type = Type.ARC;
        this.nodes.add(new String[]{"0", "0", "0"});
        this.nodes.add(new String[]{"10", "0", "10"});
        this.radius = RADIUS;
        this.angle = ANGLE;
        this.majorArc = MA;
        this.step = STEP;
        this.stepArc = SA;
        this.particle = PARTICLE;
        this.filePath = FP;
        this.fileName = FN;
    }
    
    public ArrayList<String[]> getNodes() {return nodes;}
    public String getRadius() {return radius;}
    public String getAngle() {return angle;}
    public boolean getMajorArc() {return majorArc;}
    public String getStep() {return step;}
    public boolean getStepArc() {return stepArc;}
    public String getParticle() {return particle;}
    public String getFilePath() {return filePath;}
    public String getFileName() {return fileName;}

    public void modifyNodeX(int index, String v) {
        String[] node = nodes.get(index);
        node[0] = v;
    }
    public void modifyNodeY(int index, String v) {
        String[] node = nodes.get(index);
        node[1] = v;
    }
    public void modifyNodeZ(int index, String v) {
        String[] node = nodes.get(index);
        node[2] = v;
    }
    public void setRadius(String v) {this.radius = v;}
    public void setAngle(String v) {this.angle = v;}
    public void setMajorArc(boolean b) {this.majorArc = b;}
    public void setStep(String v) {this.step = v;}
    public void setStepArc(boolean b) {this.stepArc = b;}
    public void setParticle(String v) {this.particle = v;}
    public void setFilePath(String v) {this.filePath = v;}
    public void setFileName(String v) {this.fileName = v;}
    
    @Override
    public void reset() {
        this.nodes.clear();
        for(String[] n : reset_nodes) {
            this.nodes.add(n.clone());
        }
        this.radius = reset_radius;
        this.angle = reset_angle;
        this.majorArc = reset_majorArc;
        this.step = reset_step;
        this.stepArc = reset_stepArc;
        this.particle = reset_particle;
        this.filePath = reset_filePath;
        this.fileName = reset_fileName;
    }

    @Override
    public void setReset(){
        this.reset_nodes.clear();
        for(String[] n : this.nodes) {
            this.reset_nodes.add(n.clone());
        }
        this.reset_radius = this.radius;
        this.reset_angle = this.angle;
        this.reset_majorArc = this.majorArc;
        this.reset_step = this.step;
        this.reset_stepArc = this.stepArc;
        this.reset_particle = this.particle;
        this.reset_filePath = this.filePath;
        this.reset_fileName = this.fileName;
    }

    @Override
    public LinkedHashMap<String, String> getDetailedInformation() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for(int i = 0;i < nodes.size();i++) {
            String[] node = nodes.get(i);
            map.put(String.format(context.getString(R.string.save_information_arc_node), i + 1), String.format("(%s, %s, %s)", node[0], node[1], node[2]));
        }
        map.put(context.getString(R.string.save_information_arc_radius), radius);
        map.put(context.getString(R.string.save_information_line_angle_rotation), angle);
        if(majorArc) {
            map.put(context.getString(R.string.save_information_arc_type), context.getString(R.string.save_information_arc_major));
        }else {
            map.put(context.getString(R.string.save_information_arc_type), context.getString(R.string.save_information_arc_minor));
        }
        map.put(context.getString(R.string.save_information_regular_step), step);
        if(stepArc) {
            map.put(context.getString(R.string.save_information_arc_step), context.getString(R.string.yes));
        }else {
            map.put(context.getString(R.string.save_information_arc_step), context.getString(R.string.no));
        }
        map.put(context.getString(R.string.save_information_particle), particle);
        map.put(context.getString(R.string.save_information_filePath), filePath);
        map.put(context.getString(R.string.save_information_fileName), fileName);
        return map;
    }

    @Override
    public ERRORCODE check() {
        if(this.radius.isEmpty() || Double.parseDouble(this.radius) <= 0) {
            return ERRORCODE.RADIUS;
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
        for(int i = 0;i < nodes.size();i++) {
            String[] node = nodes.get(i);
            if(node[0].isEmpty()) {
                ERRORCODE e = ERRORCODE.NODEX;
                e.setData(i);
                return e;
            }
            if(node[1].isEmpty()) {
                ERRORCODE e = ERRORCODE.NODEY;
                e.setData(i);
                return e;
            }
            if(node[2].isEmpty()) {
                ERRORCODE e = ERRORCODE.NODEZ;
                e.setData(i);
                return e;
            }
        }
        return null;
    }

    @Override
    public void draw(FragmentActivity fa) {
        if(PermissionUtil.requestStorage(fa)) {
            DrawUtil.drawArc(this);
        }else {
            Toast.makeText(fa, context.getString(R.string.permission_storage_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public ArrayList<Vector> getPoints() {
        return DrawUtil.getArcPoints(this);
    }
    
    @Override
    public DrawFragment getDrawFragment() {
        return new DrawArcFragment();
    }
    
}
