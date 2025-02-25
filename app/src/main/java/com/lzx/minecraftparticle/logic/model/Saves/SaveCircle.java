package com.lzx.minecraftparticle.logic.model.Saves;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import com.lzx.minecraftparticle.logic.utils.PermissionUtil;
import com.lzx.minecraftparticle.ui.draw.graphics.GraphicsCircleFragment;
import com.lzx.minecraftparticle.ui.draw.graphics.GraphicsFragment;
import com.lzx.minecraftparticle.ui.draw.param.DrawFragment;
import com.lzx.minecraftparticle.ui.draw.param.regular.DrawCircleFragment;
import java.io.Serializable;
import java.util.LinkedHashMap;

public class SaveCircle extends Save implements Serializable{
    //详细数据
    private String coordinate_x, coordinate_y, coordinate_z;
    private String hAngle, vAngle;
    private String radius;
    private String step;
    private String particle;
    private String filePath, fileName;
    
    //常量
    public final String CX = "0", CY = "0", CZ = "0";
    public final String HA = "0", VA = "0";
    public final String R = "5";
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
        this.radius = R;
        this.step = STEP;
        this.particle = PARTICLE;
        this.filePath = FP;
        this.fileName = FN;
    }
    
    public SaveCircle(String coordinate_x, String coordinate_y, String coordinate_z, String hAngle, String vAngle, String radius,String step, String particle, String filePath, String fileName) {
        this.type = Save.Type.CIRCLE;
        this.coordinate_x = coordinate_x;
        this.coordinate_y = coordinate_y;
        this.coordinate_z = coordinate_z;
        this.hAngle = hAngle;
        this.vAngle = vAngle;
        this.radius = radius;
        this.step = step;
        this.particle = particle;
        this.filePath = filePath;
        this.fileName = fileName;
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
    public LinkedHashMap<String, String> getDetailedInformation() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("X坐标", coordinate_x);
        map.put("Y坐标", coordinate_y);
        map.put("Z坐标", coordinate_z);
        map.put("水平角(度)", hAngle);
        map.put("竖直角(度)", vAngle);
        map.put("半径(格)", radius);
        map.put("步长", step);
        map.put("粒子", particle);
        map.put("保存路径", filePath);
        map.put("保存名称", fileName);
        return map;
    }
    
    @Override
    public void draw(FragmentActivity fa) {
        if(PermissionUtil.requestStorage(fa)) {
//            DrawUtil.drawByBaseVector(
//                DrawUtil.getBaseVector(Double.parseDouble(hAngle), Double.parseDouble(vAngle)),
//                new Double[]{
//                    Double.parseDouble(coordinate_x),
//                    Double.parseDouble(coordinate_y),
//                    Double.parseDouble(coordinate_z),
//                },
//                Double.parseDouble(radius), 0, 1, 0,
//                Double.parseDouble(step),
//                particle,
//                filePath,
//                fileName
//            );
            DrawUtil.drawCircle(this);
        }else {
            Toast.makeText(fa, "请先获取所有文件访问权限", Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    public DrawFragment getDrawFragment(int mode) {
        return new DrawCircleFragment(this, mode);
    }
    
    @Override
    public GraphicsFragment getGraphicsFragment() {
        return new GraphicsCircleFragment();
    }
    
}
