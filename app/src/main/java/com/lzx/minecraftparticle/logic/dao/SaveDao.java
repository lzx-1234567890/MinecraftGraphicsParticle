package com.lzx.minecraftparticle.logic.dao;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.lzx.minecraftparticle.MinecraftParticleApplication;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveArc;
import com.lzx.minecraftparticle.logic.model.Saves.SaveCircle;
import com.lzx.minecraftparticle.logic.model.Saves.SaveEllipse;
import com.lzx.minecraftparticle.logic.model.Saves.SaveFourier;
import com.lzx.minecraftparticle.logic.model.Saves.SaveFunction;
import com.lzx.minecraftparticle.logic.model.Saves.SaveHelix;
import com.lzx.minecraftparticle.logic.model.Saves.SaveImage;
import com.lzx.minecraftparticle.logic.model.Saves.SaveLine;
import com.lzx.minecraftparticle.logic.model.Saves.SaveParabola;
import com.lzx.minecraftparticle.logic.model.Saves.SavePolygon;
import com.lzx.minecraftparticle.logic.model.Saves.SaveSphere;
import com.lzx.minecraftparticle.logic.model.Saves.SaveSprialParabola;
import com.lzx.minecraftparticle.logic.model.Saves.SaveStar;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.Locale;

public class SaveDao {
    private static volatile SaveDao instance;
    
    private SaveDao() {}
    
    public static SaveDao getInstance() {
        if(instance == null) {
            synchronized(SaveDao.class) {
                if(instance == null) {
                    instance = new SaveDao();
                }
            }
        }
        return instance;
    }
    
    public void save(Save save, Integer[] size) {
        Context context = MinecraftParticleApplication.context;
        
        //获取id
        SharedPreferences sp = context.getSharedPreferences("Save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int id = sp.getInt("saveNum", -1);
        editor.putInt("saveNum", id + 1);
        editor.apply();
        
        //图片本地化保存
        float targetWidth = size[0];
        float targetHeight = size[1];
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.parse(save.getImageUri())), null, options);
            int width = options.outWidth;
            int height = options.outHeight;
            if(width > targetWidth || height > targetHeight) {
                int widthRatio = Math.round(width / targetWidth);
                int heightRatio = Math.round(height / targetHeight);
                options.inSampleSize = widthRatio > heightRatio ? widthRatio : heightRatio;
            }else {
                options.inSampleSize = 1;
            }
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.parse(save.getImageUri())), null, options);
        }catch (Exception e) {
            throw new RuntimeException("加载图片时出错", e);
        }
        
        File file = new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + (id + 1) + ".png");
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        }catch(Exception e) {
            throw new RuntimeException("写入图片时出错", e);
        }
        
        //设置基本信息
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = dateFormat.format(new Date());
        save.setId(id + 1);
        save.setCreateDate(date);
        save.setModifyDate(date);
        save.setImageUri(Uri.fromFile(new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + save.getId() + ".png")).toString());
        writeSave(save);
    }
    
    public Save getSave(int id) {
        Context context = MinecraftParticleApplication.context;
        //ObjectInputStream ois = null;
        BufferedReader br = null;
        Save save = null;
        try {
//            ois = new ObjectInputStream(
//                new FileInputStream(
//                new File(context.getExternalFilesDir("Saves"),
//                        "Save" + id + ".particle")));
            br = new BufferedReader(new FileReader(new File(context.getExternalFilesDir("Saves"),
                    "Save" + id + ".particle")));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            save = new Gson().fromJson(sb.toString(), Save.class);
            switch (save.getType()) {
                case CIRCLE:
                    save = new Gson().fromJson(sb.toString(), SaveCircle.class);
                    break;
                case ELLIPSE:
                    save = new Gson().fromJson(sb.toString(), SaveEllipse.class);
                    break;
                case POLYGON:
                    save = new Gson().fromJson(sb.toString(), SavePolygon.class);
                    break;
                case STAR:
                    save = new Gson().fromJson(sb.toString(), SaveStar.class);
                    break;
                case SPHERE:
                    save = new Gson().fromJson(sb.toString(), SaveSphere.class);
                    break;
                case LINE:
                    save = new Gson().fromJson(sb.toString(), SaveLine.class);
                    break;
                case PARABOLA:
                    save = new Gson().fromJson(sb.toString(), SaveParabola.class);
                    break;
                case HELIX:
                    save = new Gson().fromJson(sb.toString(), SaveHelix.class);
                    break;
                case SPIRALPARABOLA:
                    save = new Gson().fromJson(sb.toString(), SaveSprialParabola.class);
                    break;
                case ARC:
                    save = new Gson().fromJson(sb.toString(), SaveArc.class);
                    break;
                case FOURIER:
                    save = new Gson().fromJson(sb.toString(), SaveFourier.class);
                    break;
                case FUNCTION:
                    save = new Gson().fromJson(sb.toString(), SaveFunction.class);
                    break;
                case IMAGE:
                    save = new Gson().fromJson(sb.toString(), SaveImage.class);
                    break;
                default:
                    save = new Gson().fromJson(sb.toString(), SaveCircle.class);
            }
            //save = (Save)ois.readObject();
            //ois.close();
        } catch (Exception e) {
            throw new RuntimeException("获取保存时出错", e);
        }
        return save;
    }
    
    public void removeSave(int id) {
        Context context = MinecraftParticleApplication.context;
        
        //删除Save
        new File(context.getExternalFilesDir("Saves"), "Save" + id + ".particle").delete();
        new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + id + ".png").delete();
        SharedPreferences sp = context.getSharedPreferences("Save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("saveNum", sp.getInt("saveNum", 0) - 1);
        editor.apply();
        
        //重新排序Save
        for(int i = id + 1;i <= sp.getInt("saveNum", 0) + 1;i++) {
            //Save文件
            File saveFile = new File(context.getExternalFilesDir("Saves"), "Save" + i + ".particle");
            saveFile.renameTo(new File(context.getExternalFilesDir("Saves"), "Save" + (i - 1) + ".particle"));
            
            //Save图片
            File saveImage = new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + i + ".png");
            saveImage.renameTo(new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + (i - 1) + ".png"));
            
            //Save
            Save newSave = getSave(i - 1);
            newSave.setId(i - 1);
            newSave.setImageUri(Uri.fromFile(new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + newSave.getId() + ".png")).toString());
            writeSave(newSave);
        }
        
    }
    
    public void sortSave(int from, int to) {
        Context context = MinecraftParticleApplication.context;
        
        //from的暂时重命名
        File fromFile = new File(context.getExternalFilesDir("Saves"), "*Save" + from + ".particle");
        (new File(context.getExternalFilesDir("Saves"), "Save" + from + ".particle")).renameTo(fromFile);
        
        File fromImage = new File(context.getExternalFilesDir("SaveImages"), "*SaveImage" + from + ".png");
        new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + from + ".png").renameTo(fromImage);
        
        //排序
        if(from < to) {
            for(int i = from + 1;i <= to;i++) {
                File sortFile = new File(context.getExternalFilesDir("Saves"), "Save" + i + ".particle");
                File newSortFile = new File(context.getExternalFilesDir("Saves"), "Save" + (i - 1) + ".particle");;
                sortFile.renameTo(newSortFile);
                
                File sortImage = new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + i + ".png");
                File newSortImage = new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + (i - 1) + ".png");
                sortImage.renameTo(newSortImage);
                
                Save newSave = getSave(i - 1);
                newSave.setId(i - 1);
                newSave.setImageUri(Uri.fromFile(new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + newSave.getId() + ".png")).toString());
                writeSave(newSave);
            }
        }else if(from > to) {
            for(int i = from - 1;i >= to;i--) {
                File sortFile = new File(context.getExternalFilesDir("Saves"), "Save" + i + ".particle");
                File newSortFile = new File(context.getExternalFilesDir("Saves"), "Save" + (i + 1) + ".particle");;
                sortFile.renameTo(newSortFile);
                
                File sortImage = new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + i + ".png");
                File newSortImage = new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + (i + 1) + ".png");
                sortImage.renameTo(newSortImage);
                
                Save newSave = getSave(i + 1);
                newSave.setId(i + 1);
                newSave.setImageUri(Uri.fromFile(new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + newSave.getId() + ".png")).toString());
                writeSave(newSave);
            }
        }
        
        //from到to的重命名
        fromFile.renameTo(new File(context.getExternalFilesDir("Saves"), "Save" + to + ".particle"));
        fromImage.renameTo(new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + to + ".png"));
        
        Save newSave = getSave(to);
        newSave.setId(to);
        newSave.setImageUri(Uri.fromFile(new File(context.getExternalFilesDir("SaveImages"), "SaveImage" + newSave.getId() + ".png")).toString());
        writeSave(newSave);
    }
    
    public void modifySave(Save save) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = dateFormat.format(new Date());
        save.setModifyDate(date);
        writeSave(save);
    }
    
    public void writeSave(Save save) {
        Context context = MinecraftParticleApplication.context;
        
        //Save对象本地化保存
        //ObjectOutputStream oos = null;
        BufferedWriter bw = null;
        try {
            String json = new Gson().toJson(save);
            bw = new BufferedWriter(new FileWriter(new File(context.getExternalFilesDir("Saves"),
                    "Save" + save.getId() + ".particle")));
//            oos = new ObjectOutputStream(
//                new FileOutputStream(
//                new File(context.getExternalFilesDir("Saves"),
//                        "Save" + save.getId() + ".particle")));
            bw.write(json);
            bw.flush();
            bw.close();
            //oos.writeObject(save);
            //oos.close();
        } catch (Exception e) {
            throw new RuntimeException("写入保存时出错", e);
        }
    }
}
