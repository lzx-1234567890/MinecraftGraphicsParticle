package com.lzx.minecraftparticle.logic.model;

public class Vector {
    public double x, y, z;
    
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void multiply(double v) {
        x *= v;
        y *= v;
        z *= v;
    }
}
