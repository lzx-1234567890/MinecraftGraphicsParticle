package com.lzx.minecraftparticle.ui.draw.param.regular;
import android.net.Uri;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.lzx.minecraftparticle.logic.Repository;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveCircle;

public class DrawCircleViewModel extends ViewModel{
    public boolean initialzation = false;
    
//    private MutableLiveData<Double> coordinate_x = new MutableLiveData<>();
//    private MutableLiveData<Double> coordinate_y = new MutableLiveData<>();
//    private MutableLiveData<Double> coordinate_z = new MutableLiveData<>();
//    private MutableLiveData<Double> hAngle = new MutableLiveData<>();
//    private MutableLiveData<Double> vAngle = new MutableLiveData<>();
//    private MutableLiveData<Double> radius = new MutableLiveData<>();
//    private MutableLiveData<Double> step = new MutableLiveData<>();
    
//    private MutableLiveData<String> coordinate_x = new MutableLiveData<>();
//    private MutableLiveData<String> coordinate_y = new MutableLiveData<>();
//    private MutableLiveData<String> coordinate_z = new MutableLiveData<>();
//    private MutableLiveData<String> hAngle = new MutableLiveData<>();
//    private MutableLiveData<String> vAngle = new MutableLiveData<>();
//    private MutableLiveData<String> radius = new MutableLiveData<>();
//    private MutableLiveData<String> step = new MutableLiveData<>();
//    private MutableLiveData<String> particle = new MutableLiveData<>();
//    private MutableLiveData<String> filePath = new MutableLiveData<>();
//    private MutableLiveData<String> fileName = new MutableLiveData<>();
    
//    public MutableLiveData<Double> getCoordinateX() {return coordinate_x;}
//    public MutableLiveData<Double> getCoordinateY() {return coordinate_y;}
//    public MutableLiveData<Double> getCoordinateZ() {return coordinate_z;}
//    public MutableLiveData<Double> getHAngle() {return hAngle;}
//    public MutableLiveData<Double> getVAngle() {return vAngle;}
//    public MutableLiveData<Double> getRadius() {return radius;}
//    public MutableLiveData<Double> getStep() {return step;}
    
//    public MutableLiveData<String> getCoordinateX() {return coordinate_x;}
//    public MutableLiveData<String> getCoordinateY() {return coordinate_y;}
//    public MutableLiveData<String> getCoordinateZ() {return coordinate_z;}
//    public MutableLiveData<String> getHAngle() {return hAngle;}
//    public MutableLiveData<String> getVAngle() {return vAngle;}
//    public MutableLiveData<String> getRadius() {return radius;}
//    public MutableLiveData<String> getStep() {return step;}
//    public MutableLiveData<String> getParticle() {return particle;}
//    public MutableLiveData<String> getFilePath() {return filePath;}
//    public MutableLiveData<String> getFileName() {return fileName;}
    
//    public void setCoordinateX(double v) {this.coordinate_x.setValue(v);}
//    public void setCoordinateY(double v) {this.coordinate_y.setValue(v);}
//    public void setCoordinateZ(double v) {this.coordinate_z.setValue(v);}
//    public void setHAngle(double v) {this.hAngle.setValue(v);}
//    public void setVAngle(double v) {this.vAngle.setValue(v);}
//    public void setRadius(double v) {this.radius.setValue(v);}
//    public void setStep(double v) {this.step.setValue(v);}
    
//    public void setCoordinateX(String v) {this.coordinate_x.setValue(v);}
//    public void setCoordinateY(String v) {this.coordinate_y.setValue(v);}
//    public void setCoordinateZ(String v) {this.coordinate_z.setValue(v);}
//    public void setHAngle(String v) {this.hAngle.setValue(v);}
//    public void setVAngle(String v) {this.vAngle.setValue(v);}
//    public void setRadius(String v) {this.radius.setValue(v);}
//    public void setStep(String v) {this.step.setValue(v);}
//    public void setParticle(String v) {this.particle.setValue(v);}
//    public void setFilePath(String v) {this.filePath.setValue(v);}
//    public void setFileName(String v) {this.fileName.setValue(v);}
    
//    private double coordinate_x;
//    private double coordinate_y;
//    private double coordinate_z;
//    private double hAngle;
//    private double vAngle;
//    private double radius;
//    private double step;
//    private String particle;
//    private String filePath;
//    private String fileName;
//    
//    public double getCoordinateX() {return coordinate_x;}
//    public double getCoordinateY() {return coordinate_y;}
//    public double getCoordinateZ() {return coordinate_z;}
//    public double getHAngle() {return hAngle;}
//    public double getVAngle() {return vAngle;}
//    public double getRadius() {return radius;}
//    public double getStep() {return step;}
//    public String getParticle() {return particle;}
//    public String getFilePath() {return filePath;}
//    public String getFileName() {return fileName;}
//    
//    public void setCoordinateX(double v) {this.coordinate_x = v;}
//    public void setCoordinateY(double v) {this.coordinate_y = v;}
//    public void setCoordinateZ(double v) {this.coordinate_z = v;}
//    public void setHAngle(double v) {this.hAngle = v;}
//    public void setVAngle(double v) {this.vAngle = v;}
//    public void setRadius(double v) {this.radius = v;}
//    public void setStep(double v) {this.step = v;}
//    public void setParticle(String v) {this.particle = v;}
//    public void setFilePath(String v) {this.filePath = v;}
//    public void setFileName(String v) {this.fileName = v;}
    
    private MutableLiveData<SaveCircle> save = new MutableLiveData<>();
    public MutableLiveData<SaveCircle> getSave() {return save;}
    public void setSave(SaveCircle save) {this.save.setValue(save);}
    
    
    public void save(String name, Uri imageUri, Integer[] size) {
        //Save save = new SaveCircle(coordinate_x.getValue(), coordinate_y.getValue(), coordinate_z.getValue(), hAngle.getValue(), vAngle.getValue(), radius.getValue(), step.getValue(), particle.getValue(), filePath.getValue(), fileName.getValue());
        save.getValue().setType(Save.Type.CIRCLE);
        save.getValue().setName(name);
        save.getValue().setImageUri(imageUri.toString());
        Repository.getInstance().save(save.getValue(), size);
    }
    
    public void modifySave() {
        Repository.getInstance().modifySave(this.save.getValue());
    }
    
    
}
