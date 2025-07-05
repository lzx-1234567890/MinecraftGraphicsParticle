package com.lzx.minecraftparticle.logic.utils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Picture;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.graphics.PathParser;

import com.caverock.androidsvg.SVG;
import com.google.android.material.snackbar.Snackbar;
import com.lzx.minecraftparticle.MinecraftParticleApplication;
import com.lzx.minecraftparticle.R;
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
import com.lzx.minecraftparticle.logic.model.Vector;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import org.apache.tika.Tika;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class DrawUtil {
    //获取基向量(以Z轴正向为始边，Z轴正向与X轴负向为正方向)
    public static Vector[] getBaseVector(double hAngle, double vAngle, double offset) {
        hAngle = Math.toRadians(hAngle);
        vAngle = Math.toRadians(vAngle);
        offset = Math.toRadians(offset);
        Vector _vector1 = new Vector(-Math.cos(hAngle), 0, -Math.sin(hAngle));
        Vector _vector2 = new Vector(-Math.sin(hAngle) * Math.cos(vAngle), -Math.sin(vAngle), Math.cos(hAngle) * Math.cos(vAngle));
        Vector vector1 = new Vector(
                _vector1.x * Math.cos(offset) + _vector2.x * Math.sin(offset),
                _vector1.y * Math.cos(offset) + _vector2.y * Math.sin(offset),
                _vector1.z * Math.cos(offset) + _vector2.z * Math.sin(offset));
        Vector vector2 = new Vector(
                _vector1.x * Math.cos(offset + Math.PI / 2) + _vector2.x * Math.sin(offset + Math.PI / 2),
                _vector1.y * Math.cos(offset + Math.PI / 2) + _vector2.y * Math.sin(offset + Math.PI / 2),
                _vector1.z * Math.cos(offset + Math.PI / 2) + _vector2.z * Math.sin(offset + Math.PI / 2));
        return new Vector[]{vector1, vector2};
    }
    
    //创建文件
    @SuppressLint("DefaultLocale")
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
        } catch (Exception e) {
            throw new RuntimeException("写入.mcfunction时出错", e);
        }
    }

    //显示snackbar
    public static void displaySnackbar(View view, String text, int color) {
        Snackbar s = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        s.setTextColor(color);
        s.show();
    }

    //计算复数相乘
    public static Double[] complexMultiply(Double[] a, Double[] b) {
        return new Double[]{a[0] * b[0] - a[1] * b[1], a[0] * b[1] + a[1] * b[0]};
    }

    
    //获取点坐标
    public static ArrayList<Vector> getPoints(Vector[] bv, Double[] coordinates, double length1, double length2, double av1, double av2, double cycle, double step) {
        ArrayList<Vector> points = new ArrayList<>();
        for(double i = 0;i <= 360 * cycle;i += step) {
            double radian1 = Math.toRadians(i * av1);
            double radian2 = Math.toRadians(i * av2);
            points.add(new Vector(
                    coordinates[0] + (bv[0].x * Math.cos(radian1) * length1 + bv[1].x * Math.sin(radian1) * length1) + (bv[0].x * Math.cos(radian2) * length2 + bv[1].x * Math.sin(radian2) * length2),
                    coordinates[1] + (bv[0].y * Math.cos(radian1) * length1 + bv[1].y * Math.sin(radian1) * length1) + (bv[0].y * Math.cos(radian2) * length2 + bv[1].y * Math.sin(radian2) * length2),
                    coordinates[2] + (bv[0].z * Math.cos(radian1) * length1 + bv[1].z * Math.sin(radian1) * length1) + (bv[0].z * Math.cos(radian2) * length2 + bv[1].z * Math.sin(radian2) * length2)));
        }
        return points;
    }
    
    //正圆
    public static void drawCircle(SaveCircle save) {
        createDocument(getCirclePoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
    }
    
    public static ArrayList<Vector> getCirclePoints(SaveCircle save) {
        ArrayList<Vector> points = getPoints(
                getBaseVector(Double.parseDouble(save.getHAngle()), Double.parseDouble(save.getVAngle()), 0),
                new Double[]{Double.parseDouble(save.getCoordinateX()), Double.parseDouble(save.getCoordinateY()), Double.parseDouble(save.getCoordinateZ())},
                Double.parseDouble(save.getRadius()), 0,
                1, 0,
                1,
                Double.parseDouble(save.getStep()));
        return points;
    }

    //椭圆
    public static void drawEllipse(SaveEllipse save) {
        createDocument(getEllipsePoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
    }

    public static ArrayList<Vector> getEllipsePoints(SaveEllipse save) {
        double length1 = (Double.parseDouble(save.getRadiusA()) + Double.parseDouble(save.getRadiusB())) / 2;
        double length2 = (Double.parseDouble(save.getRadiusA()) - Double.parseDouble(save.getRadiusB())) / 2;
        ArrayList<Vector> points = getPoints(
                getBaseVector(Double.parseDouble(save.getHAngle()), Double.parseDouble(save.getVAngle()), Double.parseDouble(save.getOffset())),
                new Double[]{Double.parseDouble(save.getCoordinateX()), Double.parseDouble(save.getCoordinateY()), Double.parseDouble(save.getCoordinateZ())},
                length1, length2,
                1, -1,
                1,
                Double.parseDouble(save.getStep()));
        return points;
    }

    //多边形
    public static void drawPolygon(SavePolygon save) {
        createDocument(getPolygonPoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
    }

    public static ArrayList<Vector> getPolygonPoints(SavePolygon save) {
        double angle = Math.toRadians(180 / Double.parseDouble(save.getSideN()));
        double length1 = (Double.parseDouble(save.getSideL()) / 4) * (1 / Math.sin(angle) + 1 / Math.tan(angle));
        double length2 = (Double.parseDouble(save.getSideL()) / 4) * (1 / Math.sin(angle) - 1 / Math.tan(angle));
        ArrayList<Vector> points = getPoints(
                getBaseVector(Double.parseDouble(save.getHAngle()), Double.parseDouble(save.getVAngle()), Double.parseDouble(save.getOffset())),
                new Double[]{Double.parseDouble(save.getCoordinateX()), Double.parseDouble(save.getCoordinateY()), Double.parseDouble(save.getCoordinateZ())},
                length1, length2,
                1, 1 - Double.parseDouble(save.getSideN()),
                1,
                Double.parseDouble(save.getStep()));
        return points;
    }

    public static void drawStar(SaveStar save) {
        createDocument(getStarPoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
    }

    public static ArrayList<Vector> getStarPoints(SaveStar save) {
        double angle = 0;
        double av = 1;
        double cycle = 1;
        double offset = 0;
        double drawNum = Double.parseDouble(save.getSideTN()) / Double.parseDouble(save.getSideBN());
        if(Double.parseDouble(save.getSideBN()) == 4) {
            angle = Math.toRadians(45);
            av = -3;
            cycle = 1;
            offset = 360 / Double.parseDouble(save.getSideTN());
        }else {
            angle = Math.toRadians(90 * (Double.parseDouble(save.getSideBN()) - 1) / Double.parseDouble(save.getSideBN()));
            av = (1 + Double.parseDouble(save.getSideBN())) / (1 - Double.parseDouble(save.getSideBN()));
            cycle = (Double.parseDouble(save.getSideBN()) - 1) / 2;
            offset = 360 / drawNum;
        }
        double length1 = (Double.parseDouble(save.getSideL()) / 4) * (1 / Math.sin(angle) + 1 / Math.tan(angle));
        double length2 = (Double.parseDouble(save.getSideL()) / 4) * (1 / Math.sin(angle) - 1 / Math.tan(angle));
        ArrayList<Vector> points = new ArrayList<>();
        for(int i = 0;i < drawNum;i++) {
           points.addAll(getPoints(
                    getBaseVector(Double.parseDouble(save.getHAngle()), Double.parseDouble(save.getVAngle()), Double.parseDouble(save.getOffset()) + offset * i),
                    new Double[]{Double.parseDouble(save.getCoordinateX()), Double.parseDouble(save.getCoordinateY()), Double.parseDouble(save.getCoordinateZ())},
                    length1, length2,
                    1, av,
                    cycle,
                    Double.parseDouble(save.getStep())));
        }
        return points;
    }

    //球
    public static void drawSphere(SaveSphere save) {
        createDocument(getSpherePoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
    }

    public static ArrayList<Vector> getSpherePoints(SaveSphere save) {
        Vector[] _bv = getBaseVector(Double.parseDouble(save.getHAngle()), Double.parseDouble(save.getVAngle()), 0);
        Vector[] bv = new Vector[]{_bv[0], _bv[1], new Vector(
                _bv[0].y * _bv[1].z - _bv[0].z * _bv[1].y,
                _bv[0].z * _bv[1].x - _bv[0].x * _bv[1].z,
                _bv[0].x * _bv[1].y - _bv[0].y * _bv[1].x
        )};

        ArrayList<Vector> points = new ArrayList<>();
        double radius = Double.parseDouble(save.getRadius());
        double step = Double.parseDouble(save.getStep());
        double x = Double.parseDouble(save.getCoordinateX()), y = Double.parseDouble(save.getCoordinateY()), z = Double.parseDouble(save.getCoordinateZ());

        //纬度
        for(double i = 0;i <= 90;i += Double.parseDouble(save.getLatitude())) {
            double angle = Math.toRadians(i);
            double r = radius * Math.cos(angle);
            double h = radius * Math.sin(angle);
            for(double ii = 0;ii <= 360;ii += step) {
                double rad = Math.toRadians(ii);
                Vector point1 = new Vector(
                        x + (bv[0].x * Math.cos(rad) * r + bv[1].x * Math.sin(rad) * r) + bv[2].x * h,
                        y + (bv[0].y * Math.cos(rad) * r + bv[1].y * Math.sin(rad) * r) + bv[2].y * h,
                        z + (bv[0].z * Math.cos(rad) * r + bv[1].z * Math.sin(rad) * r) + bv[2].z * h
                );
                Vector point2 = new Vector(
                        x + (bv[0].x * Math.cos(rad) * r + bv[1].x * Math.sin(rad) * r) - bv[2].x * h,
                        y + (bv[0].y * Math.cos(rad) * r + bv[1].y * Math.sin(rad) * r) - bv[2].y * h,
                        z + (bv[0].z * Math.cos(rad) * r + bv[1].z * Math.sin(rad) * r) - bv[2].z * h
                );
                points.add(point1);
                points.add(point2);
            }
        }

        //经度
        for(double i = 0;i < 180;i += Double.parseDouble(save.getLongitude())) {
            double angle = Math.toRadians(i);
            Vector newBV = new Vector(
                    bv[0].x * Math.cos(angle) + bv[1].x * Math.sin(angle),
                    bv[0].y * Math.cos(angle) + bv[1].y * Math.sin(angle),
                    bv[0].z * Math.cos(angle) + bv[1].z * Math.sin(angle)
            );
            for(double ii = 0;ii <= 360;ii += step) {
                double rad = Math.toRadians(ii);
                Vector point = new Vector(
                        x + (newBV.x * Math.cos(rad) * radius + bv[2].x * Math.sin(rad) * radius),
                        y + (newBV.y * Math.cos(rad) * radius + bv[2].y * Math.sin(rad) * radius),
                        z + (newBV.z * Math.cos(rad) * radius + bv[2].z * Math.sin(rad) * radius)
                );
                points.add(point);
            }
        }
        return points;
    }

    //直线
    public static void drawLine(SaveLine save) {
        createDocument(getLinePoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
    }

    public static ArrayList<Vector> getLinePoints(SaveLine save) {
        Vector sv = new Vector(
                Double.parseDouble(save.getCoordinateXS()),
                Double.parseDouble(save.getCoordinateYS()),
                Double.parseDouble(save.getCoordinateZS())
        );
        Vector v = new Vector(
                Double.parseDouble(save.getCoordinateXE()) - Double.parseDouble(save.getCoordinateXS()),
                Double.parseDouble(save.getCoordinateYE()) - Double.parseDouble(save.getCoordinateYS()),
                Double.parseDouble(save.getCoordinateZE()) - Double.parseDouble(save.getCoordinateZS())
        );
        Vector bv = v.normalization();
        double step = Double.parseDouble(save.getStep());
        double length = v.getLength();
        ArrayList<Vector> points = new ArrayList<>();
        for(double i = 0;i <= length;i += step) {
            points.add(new Vector(
                    i * bv.x + sv.x,
                    i * bv.y + sv.y,
                    i * bv.z + sv.z
            ));
        }
        return points;
    }

    //抛物线
    public static void drawParabola(SaveParabola save) {
        createDocument(getParabolaPoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
    }

    public static ArrayList<Vector> getParabolaPoints(SaveParabola save) {
        //抛物线方程 y = ax^2 + bx
        //初始位置
        Vector sv = new Vector(
                Double.parseDouble(save.getCoordinateXS()),
                Double.parseDouble(save.getCoordinateYS()),
                Double.parseDouble(save.getCoordinateZS())
        );
        //路径向量
        Vector v = new Vector(
                Double.parseDouble(save.getCoordinateXE()) - Double.parseDouble(save.getCoordinateXS()),
                Double.parseDouble(save.getCoordinateYE()) - Double.parseDouble(save.getCoordinateYS()),
                Double.parseDouble(save.getCoordinateZE()) - Double.parseDouble(save.getCoordinateZS())
        );
        //重力单位向量
        Vector g = new Vector(
                Double.parseDouble(save.getGravityX()),
                Double.parseDouble(save.getGravityY()),
                Double.parseDouble(save.getGravityZ())
        ).normalization();
        //X轴单位向量
        double value = Vector.dot(v, g);
        Vector xv = new Vector(
                v.x - value * g.x,
                v.y - value * g.y,
                v.z - value * g.z
        );

        double xL = xv.getLength();
        double yL = -value;

        Vector bxv = xv.normalization();
        Vector byv = new Vector(-g.x, -g.y, -g.z);
        //抛物线方程参数
        double b = Math.tan(Math.toRadians(Double.parseDouble(save.getAngle())));
        double a = (yL - xL * b) / Math.pow(xL, 2);

        double step = Double.parseDouble(save.getStep());
        ArrayList<Vector> points = new ArrayList<>();
        for(double i = 0;i <= xL;i += step) {
            double x = i;
            double y = a * x * x + b * x;
            points.add(new Vector(
                    x * bxv.x + y * byv.x + sv.x,
                    x * bxv.y + y * byv.y + sv.y,
                    x * bxv.z + y * byv.z + sv.z
            ));
        }
        return points;
    }

    //螺旋线
    public static void drawHelix(SaveHelix save) {
        createDocument(getHelixPoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
    }

    public static ArrayList<Vector> getHelixPoints(SaveHelix save) {
        //初始位置
        Vector sv = new Vector(
                Double.parseDouble(save.getCoordinateXS()),
                Double.parseDouble(save.getCoordinateYS()),
                Double.parseDouble(save.getCoordinateZS())
        );
        //路径向量
        Vector v = new Vector(
                Double.parseDouble(save.getCoordinateXE()) - Double.parseDouble(save.getCoordinateXS()),
                Double.parseDouble(save.getCoordinateYE()) - Double.parseDouble(save.getCoordinateYS()),
                Double.parseDouble(save.getCoordinateZE()) - Double.parseDouble(save.getCoordinateZS())
        );
        Vector bsv = v.normalization();
        //底面基向量
        double value = Math.pow(bsv.x * bsv.x + bsv.z * bsv.z, 0.5);
        Vector bv1 = new Vector(
                -bsv.z / value,
                0,
                bsv.x / value
        );
        Vector bv2 = new Vector(
                bsv.y * bv1.z - bsv.z * bv1.y,
                bsv.z * bv1.x - bsv.x * bv1.z,
                bsv.x * bv1.y - bsv.y * bv1.x
        );

        double step = Double.parseDouble(save.getStep());
        double length = v.getLength();
        double angle = Math.toRadians(Double.parseDouble(save.getAngle()));
        double d = Double.parseDouble(save.getDistance());
        double r = Double.parseDouble(save.getRadius());
        ArrayList<Vector> points = new ArrayList<>();
        for(double i = 0;i <= length;i += step) {
            double t = i / d;
            points.add(new Vector(
                    r * Math.cos(2 * Math.PI * t + angle) * bv1.x + r * Math.sin(2 * Math.PI * t + angle) * bv2.x + i * bsv.x + sv.x,
                    r * Math.cos(2 * Math.PI * t + angle) * bv1.y + r * Math.sin(2 * Math.PI * t + angle) * bv2.y + i * bsv.y + sv.y,
                    r * Math.cos(2 * Math.PI * t + angle) * bv1.z + r * Math.sin(2 * Math.PI * t + angle) * bv2.z + i * bsv.z + sv.z
            ));
        }
        return points;
    }

    //抛物线
    public static void drawSprialParabola(SaveSprialParabola save) {
        createDocument(getSprialParabolaPoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
    }

    public static ArrayList<Vector> getSprialParabolaPoints(SaveSprialParabola save) {
        //抛物线方程 y = ax^2 + bx
        //初始位置
        Vector sv = new Vector(
                Double.parseDouble(save.getCoordinateXS()),
                Double.parseDouble(save.getCoordinateYS()),
                Double.parseDouble(save.getCoordinateZS())
        );
        //路径向量
        Vector v = new Vector(
                Double.parseDouble(save.getCoordinateXE()) - Double.parseDouble(save.getCoordinateXS()),
                Double.parseDouble(save.getCoordinateYE()) - Double.parseDouble(save.getCoordinateYS()),
                Double.parseDouble(save.getCoordinateZE()) - Double.parseDouble(save.getCoordinateZS())
        );
        //重力单位向量
        Vector g = new Vector(
                Double.parseDouble(save.getGravityX()),
                Double.parseDouble(save.getGravityY()),
                Double.parseDouble(save.getGravityZ())
        ).normalization();
        //X轴单位向量
        double value = Vector.dot(v, g);
        Vector xv = new Vector(
                v.x - value * g.x,
                v.y - value * g.y,
                v.z - value * g.z
        );

        double xL = xv.getLength();
        double yL = -value;

        Vector bxv = xv.normalization();
        Vector byv = new Vector(-g.x, -g.y, -g.z);
        //抛物线方程参数
        double b = Math.tan(Math.toRadians(Double.parseDouble(save.getAngle())));
        double a = (yL - xL * b) / Math.pow(xL, 2);

        double angle = Math.toRadians(Double.parseDouble(save.getAngle2()));
        double r = Double.parseDouble(save.getRadius());
        double av = Double.parseDouble(save.getAngleV());
        double step = Double.parseDouble(save.getStep());
        ArrayList<Vector> points = new ArrayList<>();
        for(double i = 0;i <= xL;i += step) {
            double x = i;
            double y = a * x * x + b * x;
            //螺旋平面基向量
            double k = Math.atan(2 * a * x + b);
            Vector wv = new Vector(
                    Math.cos(k) * bxv.x + Math.sin(k) * byv.x,
                    Math.cos(k) * bxv.y + Math.sin(k) * byv.y,
                    Math.cos(k) * bxv.z + Math.sin(k) * byv.z
            );
            double value2 = Math.pow(wv.x * wv.x + wv.z * wv.z, 0.5);
            Vector bv1 = new Vector(
                    -wv.z / value2,
                    0,
                    wv.x / value2
            );
            Vector bv2 = new Vector(
                    wv.y * bv1.z - wv.z * bv1.y,
                    wv.z * bv1.x - wv.x * bv1.z,
                    wv.x * bv1.y - wv.y * bv1.x
            );

            points.add(new Vector(
                    r * Math.cos(av * x + angle) * bv1.x + r * Math.sin(av * x + angle) * bv2.x + x * bxv.x + y * byv.x + sv.x,
                    r * Math.cos(av * x + angle) * bv1.y + r * Math.sin(av * x + angle) * bv2.y + x * bxv.y + y * byv.y + sv.y,
                    r * Math.cos(av * x + angle) * bv1.z + r * Math.sin(av * x + angle) * bv2.z + x * bxv.z + y * byv.z + sv.z
            ));
        }
        return points;
    }

    //圆弧
    public static void drawArc(SaveArc save) {
        createDocument(getArcPoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
    }

    public static ArrayList<Vector> getArcPoints(SaveArc save) {
        ArrayList<String[]> snodes = save.getNodes();
        ArrayList<Double[]> nodes = new ArrayList<>();
        for(String[] node : snodes) {
            nodes.add(new Double[]{
                    Double.parseDouble(node[0]),
                    Double.parseDouble(node[1]),
                    Double.parseDouble(node[2])
            });
        }
        double r = Double.parseDouble(save.getRadius());
        double angle = Math.toRadians(Double.parseDouble(save.getAngle()));
        boolean majorArc = save.getMajorArc();
        double step = Double.parseDouble(save.getStep());
        boolean stepArc = save.getStepArc();
        //初始路径
        Vector sv = new Vector(
                nodes.get(1)[0] - nodes.get(0)[0],
                nodes.get(1)[1] - nodes.get(0)[1],
                nodes.get(1)[2] - nodes.get(0)[2]
        );
        double length = sv.getLength();
        //初始基向量
        Vector bv1 = new Vector(sv.x / length, sv.y / length, sv.z / length);
        double value = Math.pow(bv1.x * bv1.x + bv1.z * bv1.z, 0.5);
        Vector bv2 = new Vector(-bv1.z / value, 0, bv1.x / value);
        Vector bv3 = new Vector(
                bv1.y * bv2.z - bv1.z * bv2.y,
                bv1.z * bv2.x - bv1.x * bv2.z,
                bv1.x * bv2.y - bv1.y * bv2.x
        );
        //初始y向量
        double value2 = Math.pow(r * r - Math.pow(length / 2, 2), 0.5);
        Vector yv = new Vector(
                -value2 * Math.cos(angle) * bv2.x - value2 * Math.sin(angle) * bv3.x,
                -value2 * Math.cos(angle) * bv2.y - value2 * Math.sin(angle) * bv3.y,
                -value2 * Math.cos(angle) * bv2.z - value2 * Math.sin(angle) * bv3.z
        );
        //初始基xy向量
        Vector bxv = bv1;
        Vector byv = new Vector(yv.x / value2, yv.y / value2, yv.z / value2);
        //计算角度范围
        Vector rv = new Vector(
                sv.x / 2 - yv.x,
                sv.y / 2 - yv.y,
                sv.z / 2 - yv.z
        );
        double length2 = rv.getLength();
        double angleRange = Math.acos(-(Vector.dot(rv, byv)) / length2);
        Double[] range = majorArc == true ? new Double[]{Math.PI / 2 + angleRange, 2.5 * Math.PI - angleRange} : new Double[]{Math.PI / 2 - angleRange, Math.PI / 2 + angleRange};
        //初次绘制
        ArrayList<Vector> points = new ArrayList<>();
        for(double i = Math.toDegrees(range[0]);i <= Math.toDegrees(range[1]);i+=step) {
            double rad = Math.toRadians(i);
            Vector point = new Vector(
                    length2 * Math.cos(rad) * bxv.x + length2 * Math.sin(rad) * byv.x + rv.x + nodes.get(0)[0],
                    length2 * Math.cos(rad) * bxv.y + length2 * Math.sin(rad) * byv.y + rv.y + nodes.get(0)[1],
                    length2 * Math.cos(rad) * bxv.z + length2 * Math.sin(rad) * byv.z + rv.z + nodes.get(0)[2]
            );
            points.add(point);

        }

        //连接节点并绘制
        double arcLength = Math.toRadians(step) * r;
        int nodeNum = nodes.size();
        for (int i = 1; i < nodeNum - 1; i++) {
            //速度向量和路径向量
            Vector v = majorArc == true ? new Vector(
                    -Math.cos(angleRange) * bxv.x + Math.sin(angleRange) * byv.x,
                    -Math.cos(angleRange) * bxv.y + Math.sin(angleRange) * byv.y,
                    -Math.cos(angleRange) * bxv.z + Math.sin(angleRange) * byv.z
            ) :
                    new Vector(
                            Math.cos(angleRange) * bxv.x - Math.sin(angleRange) * byv.x,
                            Math.cos(angleRange) * bxv.y - Math.sin(angleRange) * byv.y,
                            Math.cos(angleRange) * bxv.z - Math.sin(angleRange) * byv.z
                    );
            sv = new Vector(
                    nodes.get(i + 1)[0] - nodes.get(i)[0],
                    nodes.get(i + 1)[1] - nodes.get(i)[1],
                    nodes.get(i + 1)[2] - nodes.get(i)[2]
            );
            length = sv.getLength();
            bxv = new Vector(sv.x / length, sv.y / length, sv.z / length);
            //计算半径向量的系数
            double a = Math.acos(Vector.dot(bxv, v));
            value2 = -((length / 2) * Math.tan(Math.PI / 2 - a)) / Math.sin(a);
            value = (length / 2) - value2 * Vector.dot(bxv, v);
            //获得半径向量的y向量
            rv = new Vector(
                    value * bxv.x + value2 * v.x,
                    value * bxv.y + value2 * v.y,
                    value * bxv.z + value2 * v.z
            );
            length2 = rv.getLength();
            yv = new Vector(
                    sv.x / 2 - rv.x,
                    sv.y / 2 - rv.y,
                    sv.z / 2 - rv.z
            );
            double length3 = yv.getLength();
            byv = new Vector(yv.x / length3, yv.y / length3, yv.z / length3);
            //确定角度范围
            angleRange = Math.acos(-(Vector.dot(rv, byv)) / length2);
            double value3 = Vector.dot(v, sv);
            if(value3 > 0) {
                range = new Double[]{Math.PI / 2 - angleRange, Math.PI / 2 + angleRange};
                majorArc = false;
            }else if(value3 < 0) {
                range = new Double[]{Math.PI / 2 + angleRange, 2.5 * Math.PI - angleRange};
                majorArc = true;
            }else {
                range = new Double[]{(double)0, Math.PI};
                majorArc = false;
            }
            //统一步长
            if(stepArc) {
                step = Math.toDegrees(arcLength / length2);
            }
            //绘制
            for(double ii = Math.toDegrees(range[0]);ii <= Math.toDegrees(range[1]);ii+=step) {
                double rad = Math.toRadians(ii);
                Vector point = new Vector(
                        length2 * Math.cos(rad) * bxv.x + length2 * Math.sin(rad) * byv.x + rv.x + nodes.get(i)[0],
                        length2 * Math.cos(rad) * bxv.y + length2 * Math.sin(rad) * byv.y + rv.y + nodes.get(i)[1],
                        length2 * Math.cos(rad) * bxv.z + length2 * Math.sin(rad) * byv.z + rv.z + nodes.get(i)[2]
                );
                points.add(point);
            }
        }

        return points;
    }


    //傅里叶级数
    @SuppressLint("DefaultLocale")
    public static void drawFourier(SaveFourier save) {
        //createDocument(getFourierPoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
        //解析路径
        int pointNum = Integer.parseInt(save.getPointNum());
        double T = Double.parseDouble(save.getCycle());
        double dt = T / pointNum;
        ArrayList<Vector> points = getFourierPoints(save);

        //计算系数
        Double[] c0 = new Double[]{(double) 0, (double) 0};
        ArrayList<Double[]> cn_p = new ArrayList<>();
        ArrayList<Double[]> cn_n = new ArrayList<>();
        ArrayList<Double> an_p = new ArrayList<>();
        ArrayList<Double> an_n = new ArrayList<>();
        ArrayList<Double> rn_p = new ArrayList<>();
        ArrayList<Double> rn_n = new ArrayList<>();
        ArrayList<Double> wn_p = new ArrayList<>();
        ArrayList<Double> wn_n = new ArrayList<>();

        for(int i = 0;i < pointNum;i++) {
            c0[0] += (1 / T) * points.get(i).x * dt;
            c0[1] += (1 / T) * points.get(i).z * dt;
        }


        int n = Integer.parseInt(save.getNum());
        for(int i = 1;i < (n / 2) + 1;i++) {
            wn_p.add(Math.toDegrees(-(2 * Math.PI) * i / T));
            wn_n.add(Math.toDegrees((2 * Math.PI) * i / T));
            cn_p.add(new Double[]{(double) 0, (double) 0});
            cn_n.add(new Double[]{(double) 0, (double) 0});
            for(int ii = 0;ii < pointNum;ii++) {
                Vector point = points.get(ii);
                double t = ii *  dt;
                Double[] c_p = complexMultiply(new Double[]{point.x, point.z}, new Double[]{Math.cos(-(2 * Math.PI) * i * t / T), Math.sin(-(2 * Math.PI) * i * t / T)});
                Double[] c_n = complexMultiply(new Double[]{point.x, point.z}, new Double[]{Math.cos((2 * Math.PI) * i * t / T), Math.sin((2 * Math.PI) * i * t / T)});
                cn_p.get(i - 1)[0] += (1 / T) * c_p[0] * dt;
                cn_p.get(i - 1)[1] += (1 / T) * c_p[1] * dt;
                cn_n.get(i - 1)[0] += (1 / T) * c_n[0] * dt;
                cn_n.get(i - 1)[1] += (1 / T) * c_n[1] * dt;
            }
            an_p.add(Math.toDegrees(Math.atan2(-cn_p.get(i - 1)[0], cn_p.get(i - 1)[1])));
            an_n.add(Math.toDegrees(Math.atan2(-cn_n.get(i - 1)[0], cn_n.get(i - 1)[1])));
            rn_p.add(Math.pow(cn_p.get(i - 1)[0] * cn_p.get(i - 1)[0] + cn_p.get(i - 1)[1] * cn_p.get(i - 1)[1], 0.5));
            rn_n.add(Math.pow(cn_n.get(i - 1)[0] * cn_n.get(i - 1)[0] + cn_n.get(i - 1)[1] * cn_n.get(i - 1)[1], 0.5));
        }

        //正负结合起来
        ArrayList<Double[]> cn = new ArrayList<>();
        cn.addAll(cn_p);
        cn.addAll(cn_n);
        ArrayList<Double> an = new ArrayList<>();
        an.addAll(an_p);
        an.addAll(an_n);
        ArrayList<Double> rn = new ArrayList<>();
        rn.addAll(rn_p);
        rn.addAll(rn_n);
        ArrayList<Double> wn = new ArrayList<>();
        wn.addAll(wn_p);
        wn.addAll(wn_n);

        //计算初始坐标
        ArrayList<Double[]> initC = new ArrayList<>();
        initC.add(c0);
        Double[] d = new Double[]{c0[0], c0[1]};
        for (int i = 0; i < n - 1; i++) {
            d[0] += cn.get(i)[0];
            d[1] += cn.get(i)[1];
            initC.add(new Double[]{d[0], d[1]});
        }

        //输出
        String name = save.getASName();
        LinkedHashMap<String, ArrayList<String>> map = new LinkedHashMap<>();
        map.put("summon", new ArrayList<>());
        map.put("init", new ArrayList<>());
        map.put("motion", new ArrayList<>());

        double x = Double.parseDouble(save.getCoordinateX());
        double y = Double.parseDouble(save.getCoordinateY());
        double z = Double.parseDouble(save.getCoordinateZ());
        for(int i = 0;i < n;i++) {
            map.get("summon").add(String.format(
                    "execute unless entity @e[type=armor_stand,name=%s] run summon armor_stand %s %f %f %f\n",
                    name + (i + 1), name + (i + 1), x + initC.get(i)[0], y, z + initC.get(i)[1]));

            map.get("init").add(String.format(
                    "execute as @e[type=armor_stand,name=%s,tag=!fourier] at @s rotated %f 0 run tp @s ~ ~ ~ ~ ~\n",
                    name + (i + 1), an.get(i)));
            map.get("init").add(String.format(
                    "tag @e[type=armor_stand,name=%s] add fourier\n",
                    name + (i + 1)));

            map.get("motion").add(String.format(
                    "execute as @e[type=armor_stand,name=%s,tag=fourier] at @s run tp @s ~ ~ ~ ~%f ~\n",
                    name + (i + 1), wn.get(i)));
            map.get("motion").add(String.format(
                    "execute as @e[type=armor_stand,name=%s,tag=fourier] at @s run tp @e[type=armor_stand,name=%s,tag=fourier] ^ ^ ^%f\n",
                    name + (i + 1), name + (i + 2), rn.get(i)));
        }
        map.get("init").remove(map.get("init").size() - 2);
        map.get("motion").remove(map.get("motion").size() - 1);

        String particleDraw = String.format(
                "execute as @e[type=armor_stand,name=%s,tag=fourier] at @s run particle %s ^ ^ ^%f",
                name + n, save.getParticle(), rn.get(n - 1));

        String filename = save.getFileName();
        if(!filename.endsWith(".mcfunction")) {
            filename = filename + ".mcfunction";
        }
        File file = new File(save.getFilePath(), filename);
        BufferedWriter writer = null;
        try {
            Context context = MinecraftParticleApplication.context;
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(context.getString(R.string.mcfunction_fourier_summon));
            for(String c : map.get("summon")) {
                writer.write(c);
            }
            writer.write(context.getString(R.string.mcfunction_fourier_init));
            for(String c : map.get("init")) {
                writer.write(c);
            }
            writer.write(context.getString(R.string.mcfunction_fourier_motion));
            for(String c : map.get("motion")) {
                writer.write(c);
            }
            writer.write(context.getString(R.string.mcfunction_fourier_particle));
            writer.write(particleDraw);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("写入mcfunction时出错", e);
        }

        //调试
//        ArrayList<Double[]> p = new ArrayList<>();
//        for(double t = 0;t < T;t+=dt) {
//            Double[] test = new Double[]{(double) 0, (double) 0};
//            test[0] += c0[0];
//            test[1] += c0[1];
//            for (int i = 0; i < n; i++) {
//                Double[] c = complexMultiply(cn.get(i), new Double[]{Math.cos(Math.toRadians(wn.get(i)) * t), Math.sin(Math.toRadians(wn.get(i)) * t)});
//                test[0] += c[0];
//                test[1] += c[1];
//            }
//            p.add(test);
//            Log.e("ddddddd", test[0] + "/" + test[1]);
//        }

    }

    public static ArrayList<Vector> getFourierPoints(SaveFourier save) {
        Path path = PathParser.createPathFromPathData(save.getPath());
        PathMeasure pm = new PathMeasure(path, false);
        float[] coord = new float[2];
        float length = pm.getLength();
        int pointNum = Integer.parseInt(save.getPointNum());
        double step = length / pointNum;

        //获取坐标点
        float scale = Float.parseFloat(save.getScale());
        double x = Double.parseDouble(save.getCoordinateX());
        double y = Double.parseDouble(save.getCoordinateY());
        double z = Double.parseDouble(save.getCoordinateZ());
        double angle = Double.parseDouble(save.getAngle());
        int xP = save.getXP() ? 1 : -1;
        int zP = save.getZP() ? 1 : -1;
        ArrayList<Vector> points = new ArrayList<>();
        //先绕原点旋转，再平移
        for (int i = 0;i < pointNum;i += 1) {
            pm.getPosTan((float) (i * step), coord, null);
            double cx = xP * coord[0] * scale;
            double cz = zP * coord[1] * scale;
            Double[] newC = complexMultiply(new Double[]{cx, cz}, new Double[]{Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle))});
            points.add(new Vector(newC[0] + x, y, newC[1] + z));
        }
        return points;
    }

    public static void drawFunction(SaveFunction save) {
        //createDocument(getFunctionPoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
        ArrayList<Vector> points = getFunctionPoints(save);
        String filename = save.getFileName();
        if(!filename.endsWith(".mcfunction")) {
            filename = filename + ".mcfunction";
        }
        File file = new File(save.getFilePath(), filename);
        BufferedWriter writer = null;
        String command = "particle %s %f %f %f";
        int[] num = save.getParticleNum();
        try {
            writer = new BufferedWriter(new FileWriter(file));
            for(int i = 0;i < num[0] + num[1];i++) {
                Vector v = points.get(i);
                writer.write(String.format(command, save.getParticleX(), v.x, v.y, v.z) + "\n");
            }
            for(int i = 0;i < num[2] + num[3];i++) {
                Vector v = points.get(i + num[0] + num[1]);
                writer.write(String.format(command, save.getParticleY(), v.x, v.y, v.z) + "\n");
            }
            for(int i = 0;i < points.size() - (num[0] + num[1] + num[2] + num[3]);i++) {
                Vector v = points.get(i + num[0] + num[1] + num[2] + num[3]);
                writer.write(String.format(command, save.getParticle(), v.x, v.y, v.z) + "\n");
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("写入.mcfunction时出错", e);
        }
    }

    public static ArrayList<Vector> getFunctionPoints(SaveFunction save) {
        ArrayList<Vector> points = new ArrayList<>();
        //先绘制xy轴
        Vector o = new Vector(
                Double.parseDouble(save.getCoordinateX()),
                Double.parseDouble(save.getCoordinateY()),
                Double.parseDouble(save.getCoordinateZ())
        );
        Vector xv = new Vector(
                Double.parseDouble(save.getXAxisX()),
                Double.parseDouble(save.getXAxisY()),
                Double.parseDouble(save.getXAxisZ())
        ).normalization();
        Vector yv = new Vector(
                Double.parseDouble(save.getYAxisX()),
                Double.parseDouble(save.getYAxisY()),
                Double.parseDouble(save.getYAxisZ())
        ).normalization();
        double xl = Double.parseDouble(save.getLengthX());
        double yl = Double.parseDouble(save.getLengthY());
        double stepX = Double.parseDouble(save.getStepX());
        double stepY = Double.parseDouble(save.getStepY());
        int[] num = save.getParticleNum();
        num[0] = 0;
        num[1] = 0;
        num[2] = 0;
        num[3] = 0;
        if(save.getXAxisP()) {
            for(double i = 0;i < xl;i+=stepX) {
                points.add(new Vector(
                        i * xv.x + o.x,
                        i * xv.y + o.y,
                        i * xv.z + o.z
                ));
                num[0]++;
            }
        }
        if(save.getXAxisN()) {
            for(double i = 0;i < xl;i+=stepX) {
                points.add(new Vector(
                        -i * xv.x + o.x,
                        -i * xv.y + o.y,
                        -i * xv.z + o.z
                ));
                num[1]++;
            }
        }
        if(save.getYAxisP()) {
            for(double i = 0;i < yl;i+=stepY) {
                points.add(new Vector(
                        i * yv.x + o.x,
                        i * yv.y + o.y,
                        i * yv.z + o.z
                ));
                num[2]++;
            }
        }
        if(save.getYAxisN()) {
            for(double i = 0;i < yl;i+=stepY) {
                points.add(new Vector(
                        -i * yv.x + o.x,
                        -i * yv.y + o.y,
                        -i * yv.z + o.z
                ));
                num[3]++;
            }
        }
        //绘制函数
        double xs = Double.parseDouble(save.getXs());
        double xe = Double.parseDouble(save.getXe());
        double step = Double.parseDouble(save.getStep());
        double scaleX = Double.parseDouble(save.getScaleX());
        double scaleY = Double.parseDouble(save.getScaleY());
        try {
            Expression exp = new ExpressionBuilder(save.getExp()).variables("x").build();
            for (double i = xs; i < xe; i += step) {
                exp.setVariable("x", i);
                double x = i * scaleX;
                double y = exp.evaluate() * scaleY;
                points.add(new Vector(
                        x * xv.x + y * yv.x + o.x,
                        x * xv.y + y * yv.y + o.y,
                        x * xv.z + y * yv.z + o.z
                ));
            }
        }catch (Exception e) {
            Toast.makeText(MinecraftParticleApplication.context, MinecraftParticleApplication.context.getString(R.string.draw_function_calculate_error), Toast.LENGTH_SHORT).show();
            return points;
        }
        return points;
    }

    public static void drawImage(SaveImage save) {
        createDocument(getImagePoints(save), save.getParticle(), save.getFilePath(), save.getFileName());
    }

    public static ArrayList<Vector> getImagePoints(SaveImage save) {
        ArrayList<Vector> points = new ArrayList<>();
        String uri = save.getUri();
        Vector o = new Vector(
                Double.parseDouble(save.getCoordinateX()),
                Double.parseDouble(save.getCoordinateY()),
                Double.parseDouble(save.getCoordinateZ())
        );
        Vector xv = new Vector(
                Double.parseDouble(save.getWX()),
                Double.parseDouble(save.getWY()),
                Double.parseDouble(save.getWZ())
        ).normalization();
        Vector yv = new Vector(
                Double.parseDouble(save.getHX()),
                Double.parseDouble(save.getHY()),
                Double.parseDouble(save.getHZ())
        ).normalization();
        double tolerance = Double.parseDouble(save.getTolerance());

        try {
            InputStream inputStream = MinecraftParticleApplication.context.getContentResolver().openInputStream(Uri.parse(uri));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int b;
            while ((b = inputStream.read(data)) != -1) {
                baos.write(data, 0, b);
            }
            byte[] imageBytes = baos.toByteArray();

            String type = new Tika().detect(imageBytes);
            Bitmap bitmap = null;
            double scale = Double.parseDouble(save.getScale());
            if (type.equals("image/jpeg") || type.equals("image/png") || type.equals("image/webp")) {
                Bitmap _bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                int width = _bitmap.getWidth();
                int height = _bitmap.getHeight();
                bitmap = Bitmap.createScaledBitmap(_bitmap, (int)(width * scale), (int)(height * scale), true);
            }else if (type.equals("image/svg+xml")) {
                SVG svg = SVG.getFromString(new String(imageBytes, "UTF-8"));
                Picture p = svg.renderToPicture();
                int width = p.getWidth();
                int height = p.getHeight();
                Bitmap _bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                p.draw(new Canvas((_bitmap)));
                bitmap = Bitmap.createScaledBitmap(_bitmap, (int)(width * scale), (int)(height * scale), true);
                _bitmap.recycle();
            }

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            double angle = Double.parseDouble(save.getAngle());
            for(int y = 0;y < height;y++) {
                for(int x = 0;x < width;x++) {
                    float[] hsvW = new float[3];
                    float[] hsvB = new float[3];
                    Color.colorToHSV(Integer.parseInt(save.getColorW()), hsvW);
                    Color.colorToHSV(Integer.parseInt(save.getColorB()), hsvB);
                    float[] hsv = {hsvW[0], hsvW[1], hsvB[2]};
                    int color = Color.HSVToColor(Integer.parseInt(save.getColorA()), hsv);
                    if(tolerance >= 0 && compareColor(color, pixels[y * width + x], tolerance)) {
                        Double[] c = complexMultiply(new Double[]{(double) x, (double) y}, new Double[]{Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle))});
                        points.add(new Vector(
                                c[0] * xv.x + c[1] * yv.x + o.x,
                                c[0] * xv.y + c[1] * yv.y + o.y,
                                c[0] * xv.z + c[1] * yv.z + o.z
                        ));
                    }else if(tolerance < 0) {
                        Double[] c = complexMultiply(new Double[]{(double) x, (double) y}, new Double[]{Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle))});
                        points.add(new Vector(
                                c[0] * xv.x + c[1] * yv.x + o.x,
                                c[0] * xv.y + c[1] * yv.y + o.y,
                                c[0] * xv.z + c[1] * yv.z + o.z
                        ));
                    }
                }
            }

        }catch (OutOfMemoryError e) {
            Toast.makeText(MinecraftParticleApplication.context, MinecraftParticleApplication.context.getString(R.string.out_of_memory_error), Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            throw new RuntimeException("为图像描点时出错", e);
        }
        return points;
    }

    public static boolean compareColor(int color1, int color2, double tolerance) {
        double da = Color.alpha(color1) - Color.alpha(color2);
        double dr = Color.red(color1) - Color.red(color2);
        double dg = Color.green(color1) - Color.green(color2);
        double db = Color.blue(color1) - Color.blue(color2);
        double dc = Math.sqrt(da * da + dr * dr + dg * dg + db * db);
        return dc <= tolerance;
    }


}
