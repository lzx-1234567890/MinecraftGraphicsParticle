package com.lzx.minecraftparticle.logic.model;

public class Vector {
    public double x, y, z;
    
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector multiply(double v) {
        x *= v;
        y *= v;
        z *= v;
        return this;
    }

    public double getLength() {
        return Math.pow(x * x + y * y + z * z, 0.5);
    }

    public Vector normalization() {
        double length = getLength();
        return new Vector(
                x / length,
                y / length,
                z / length
        );
    }

    public String print() {
        return "(" + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z) + ")";
    }

    public static double dot(Vector a, Vector b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static double getCosAngle(Vector a, Vector b) {
        return (dot(a, b)) / (a.getLength() / b.getLength());
    }

}
