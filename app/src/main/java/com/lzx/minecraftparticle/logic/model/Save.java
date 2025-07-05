package com.lzx.minecraftparticle.logic.model;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import com.lzx.minecraftparticle.MinecraftParticleApplication;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.ui.fragment.draw.param.DrawFragment;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Save{
    public enum Type implements Serializable{
        CIRCLE(MinecraftParticleApplication.context.getString(R.string.type_circle), R.drawable.draw_circle),
        ELLIPSE(MinecraftParticleApplication.context.getString(R.string.type_ellipse), R.drawable.draw_ellipse),
        POLYGON(MinecraftParticleApplication.context.getString(R.string.type_polygon), R.drawable.draw_polygon),
        STAR(MinecraftParticleApplication.context.getString(R.string.type_star), R.drawable.draw_star),
        SPHERE(MinecraftParticleApplication.context.getString(R.string.type_sphere), R.drawable.draw_sphere),
        LINE(MinecraftParticleApplication.context.getString(R.string.type_line), R.drawable.draw_line),
        PARABOLA(MinecraftParticleApplication.context.getString(R.string.type_parabola), R.drawable.draw_parabola),
        HELIX(MinecraftParticleApplication.context.getString(R.string.type_helix), R.drawable.draw_helix),
        SPIRALPARABOLA(MinecraftParticleApplication.context.getString(R.string.type_spiralparabola), R.drawable.draw_spiralparabola),
        ARC(MinecraftParticleApplication.context.getString(R.string.type_arc), R.drawable.draw_arc),
        FOURIER(MinecraftParticleApplication.context.getString(R.string.type_fourier), R.drawable.draw_fourier),
        FUNCTION(MinecraftParticleApplication.context.getString(R.string.type_function), R.drawable.draw_function),
        IMAGE(MinecraftParticleApplication.context.getString(R.string.type_image), R.drawable.draw_image);
        
        
        private String name;
        private int image;
        
        Type(String name, int image) {
            this.name = name;
            this.image = image;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getImage() {
            return this.image;
        }
        
        
    }

    public enum ERRORCODE{
        CX, CY, CZ,
        CXS, CYS, CZS, CXE, CYE, CZE,
        XAX, XAY, XAZ, YAX, YAY, YAZ,
        WX, WY, WZ, HX, HY, HZ,
        EXP,
        TOLERANCE,
        XS, XE,
        URI,
        NODEX, NODEY, NODEZ,
        HA, VA,
        ANGLE, ANGLE2, ANGLEV,
        LX, LY,
        DISTANCE,
        GX, GY, GZ,
        OFFSET,
        CYCLE,
        SL, SN, SBN, STN,
        RADIUS, RADIUSA, RADIUSB, RADIUSAB,
        SCALE, SCALEX, SCALEY,
        NUM, PNUM,
        LATITUDE, LONGITUDE,
        STEP, STEPX, STEPY, STEPW, STEPH,
        PARTICLE, PARTICLEX, PARTICLEY,
        ASN,
        FP, FN;

        Object data = null;

        public void setData(Object data) {
            this.data = data;
        }

        public Object getData() {
            return this.data;
        }
    }

    protected transient Context context = MinecraftParticleApplication.context;
    
    //基本数据
    protected int id;
    protected Type type;
    protected String name, imageUri, createDate, modifyDate;
    
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    
    public Type getType() {return type;}
    public void setType(Type type) {this.type = type;}
    
    public String getImageUri() {return imageUri;}
    public void setImageUri(String imageUri) {this.imageUri = imageUri;}
    
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    
    public String getCreateDate() {return createDate;}
    public void setCreateDate(String createDate) {this.createDate = createDate;}
    
    public String getModifyDate() {return modifyDate;}
    public void setModifyDate(String modifyDate) {this.modifyDate = modifyDate;}

    public void reset(){}

    public void setReset(){}

    //获取基本信息的map
    public LinkedHashMap<String, String> getBasicInformation() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(context.getString(R.string.save_information_basic_id), Integer.toString(id));
        map.put(context.getString(R.string.save_information_basic_type), type.getName());
        map.put(context.getString(R.string.save_information_basic_name), name);
        map.put(context.getString(R.string.save_information_basic_createDate), createDate);
        map.put(context.getString(R.string.save_information_basic_modifyDate), modifyDate);
        return map;
    }
    
    //获取详细信息的map
    public LinkedHashMap<String, String> getDetailedInformation() {
        return new LinkedHashMap<>();
    }

    //检查
    public ERRORCODE check(){return null;}
    //绘制(用于在保存项目中绘制图形)
    public void draw(FragmentActivity fa){}
    //获取点
    public ArrayList<Vector> getPoints(){return null;}
    public DrawFragment getDrawFragment(){return null;}
}
