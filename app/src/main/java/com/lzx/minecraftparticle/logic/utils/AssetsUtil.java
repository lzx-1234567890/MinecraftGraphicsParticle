package com.lzx.minecraftparticle.logic.utils;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;

public class AssetsUtil {
    
    public static Bitmap loadBitmap(Context context, String file, BitmapFactory.Options options) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        Bitmap bitmap = null;
        
        try {
            inputStream = assetManager.open(file);
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (Exception e) {
            throw new RuntimeException("加载图片时出错", e);
        }
        
        return bitmap;
    }
}
