package com.lzx.minecraftparticle.logic.utils;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveCircle;
import com.lzx.minecraftparticle.logic.model.Vector;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DrawUtil {
    //获取基向量(以Z轴正向为始边，Z轴正向与X轴负向为正方向)
    public static Vector[] getBaseVector(double hAngle, double vAngle) {
        hAngle = Math.toRadians(hAngle);
        vAngle = Math.toRadians(vAngle);
        Vector vector1 = new Vector(-Math.cos(hAngle), 0, -Math.sin(hAngle));
        Vector vector2 = new Vector(-Math.sin(hAngle) * Math.cos(vAngle), -Math.sin(vAngle), Math.cos(hAngle) * Math.cos(vAngle));
        return new Vector[]{vector1, vector2};
    }
    
    //创建文件
    public static void createDocument(ArrayList<Vector> points, String particle, String path, String name) {
        String filename = name;
        if(!filename.endsWith(".mcfunction")) {
            filename = filename + ".mcfunction";
        }
        File file = new File(path, filename);
        BufferedWriter writer = null;
        String command = "particle %s %f %f %f";
        try {
            writer = new BufferedWriter(new FileWriter(file));
            for(Vector v : points) {
                writer.write(String.format(command, particle, v.x, v.y, v.z) + "\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    //获取点坐标
    public static ArrayList<Vector> getPoints(Vector[] bv, Double[] coordinates, double length1, double length2, double av1, double av2, double step) {
        ArrayList<Vector> points = new ArrayList<>();
        for(double i = 0;i <= 360;i += step) {
            double radian1 = Math.toRadians(i * av1);
            double radian2 = Math.toRadians(i * av2);
            points.add(new Vector(
                    coordinates[0] + (bv[0].x * Math.cos(radian1) * length1 + bv[1].x * Math.sin(radian1) * length1) + (bv[0].x * Math.cos(-radian2) * length2 + bv[1].x * Math.sin(-radian2) * length2), 
                    coordinates[1] + (bv[0].y * Math.cos(radian1) * length1 + bv[1].y * Math.sin(radian1) * length1) + (bv[0].x * Math.cos(-radian2) * length2 + bv[1].x * Math.sin(-radian2) * length2), 
                    coordinates[2] + (bv[0].z * Math.cos(radian1) * length1 + bv[1].z * Math.sin(radian1) * length1) + (bv[0].x * Math.cos(-radian2) * length2 + bv[1].x * Math.sin(-radian2) * length2)));
        }
        return points;
    }
    
    //正圆
    public static void drawCircle(SaveCircle save) {
        createDocument(getCirclePoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
    }
    
    public static ArrayList<Vector> getCirclePoints(SaveCircle save) {
        ArrayList<Vector> points = getPoints(
                getBaseVector(Double.parseDouble(save.getHAngle()), Double.parseDouble(save.getVAngle())),
                new Double[]{Double.parseDouble(save.getCoordinateX()), Double.parseDouble(save.getCoordinateY()), Double.parseDouble(save.getCoordinateZ())},
                Double.parseDouble(save.getRadius()), 0,
                1, 0,
                Double.parseDouble(save.getStep()));
        return points;
    }
    
//    public static void drawByBaseVector(Vector[] bv, Double[] coordinates, double length1, double length2, double av1, double av2, double step, String particle, String path, String name) {
//        //获取点坐标
//        ArrayList<Vector> points = new ArrayList<>();
//        for(double i = 0;i <= 360;i += step) {
//            double radian1 = Math.toRadians(i * av1);
//            double radian2 = Math.toRadians(i * av2);
//            points.add(new Vector(
//                    coordinates[0] + (bv[0].x * Math.cos(radian1) * length1 + bv[1].x * Math.sin(radian1) * length1) + (bv[0].x * Math.cos(-radian2) * length2 + bv[1].x * Math.sin(-radian2) * length2), 
//                    coordinates[1] + (bv[0].y * Math.cos(radian1) * length1 + bv[1].y * Math.sin(radian1) * length1) + (bv[0].x * Math.cos(-radian2) * length2 + bv[1].x * Math.sin(-radian2) * length2), 
//                    coordinates[2] + (bv[0].z * Math.cos(radian1) * length1 + bv[1].z * Math.sin(radian1) * length1) + (bv[0].x * Math.cos(-radian2) * length2 + bv[1].x * Math.sin(-radian2) * length2)));
//        }
//        
//        //输出文件
//        String filename = name;
//        if(!filename.endsWith(".mcfunction")) {
//            filename = filename + ".mcfunction";
//        }
//        File file = new File(path, filename);
//        BufferedWriter writer = null;
//        String command = "particle %s %f %f %f";
//        try {
//            writer = new BufferedWriter(new FileWriter(file));
//            for(Vector v : points) {
//                writer.write(String.format(command, particle, v.x, v.y, v.z) + "\n");
//            }
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                writer.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        
//        
//    }
}
