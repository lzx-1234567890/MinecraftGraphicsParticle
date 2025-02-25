package com.lzx.minecraftparticle;
import android.app.Application;
import android.content.Context;
import com.google.android.material.color.DynamicColors;

public class MinecraftParticleApplication extends Application{
    public static Context context;
    
    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
        //DynamicColors.applyToActivitiesIfAvailable(this);
    }
}