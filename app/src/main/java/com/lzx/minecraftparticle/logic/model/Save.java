package com.lzx.minecraftparticle.logic.model;
import androidx.fragment.app.FragmentActivity;
import com.lzx.minecraftparticle.MinecraftParticleApplication;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.ui.draw.graphics.GraphicsFragment;
import com.lzx.minecraftparticle.ui.draw.param.DrawFragment;
import java.io.Serializable;
import java.util.LinkedHashMap;

public class Save implements Serializable{
    public enum Type implements Serializable{
        CIRCLE(MinecraftParticleApplication.context.getString(R.string.type_circle), R.drawable.draw_circle),
        ELLIPSE(MinecraftParticleApplication.context.getString(R.string.type_ellipse), R.drawable.logo),
        POLYGON(MinecraftParticleApplication.context.getString(R.string.type_polygon), R.drawable.logo),
        STAR(MinecraftParticleApplication.context.getString(R.string.type_star), R.drawable.logo),
        SPHERE(MinecraftParticleApplication.context.getString(R.string.type_sphere), R.drawable.logo),
        LINE(MinecraftParticleApplication.context.getString(R.string.type_line), R.drawable.logo),
        PARABOLA(MinecraftParticleApplication.context.getString(R.string.type_parabola), R.drawable.logo),
        HELIX(MinecraftParticleApplication.context.getString(R.string.type_helix), R.drawable.logo),
        SPIRALPARABOLA(MinecraftParticleApplication.context.getString(R.string.type_spiralparabola), R.drawable.logo),
        ARC(MinecraftParticleApplication.context.getString(R.string.type_arc), R.drawable.logo),
        FOURIER(MinecraftParticleApplication.context.getString(R.string.type_fourier), R.drawable.logo),
        FUNCTION(MinecraftParticleApplication.context.getString(R.string.type_function), R.drawable.logo),
        IMAGE(MinecraftParticleApplication.context.getString(R.string.type_image), R.drawable.logo);
        
        
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
    
    //获取基本信息的map
    public LinkedHashMap<String, String> getBasicInformation() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("ID", Integer.toString(id));
        map.put("类型", type.getName());
        map.put("名称", name);
        map.put("创建日期", createDate);
        map.put("修改日期", modifyDate);
        return map;
    }
    
    //获取详细信息的map
    public LinkedHashMap<String, String> getDetailedInformation() {
        return new LinkedHashMap<String, String>();
    }
    
    //绘制
    public void draw(FragmentActivity fa) {}
    
    //修改
    public DrawFragment getDrawFragment(int mode) {return null;}
    public GraphicsFragment getGraphicsFragment() {return null;}
}
