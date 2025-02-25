package com.lzx.minecraftparticle.logic.utils;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.io.IOException;
import java.io.InputStream;

public class AssetsUtil {
    
    public static Bitmap loadBitmap(Context context, String file, BitmapFactory.Options options) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        Bitmap bitmap = null;
        
        try {
            inputStream = assetManager.open(file);
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
        
        return bitmap;
    }
}
