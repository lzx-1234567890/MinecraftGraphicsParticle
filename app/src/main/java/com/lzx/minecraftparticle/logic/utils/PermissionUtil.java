package com.lzx.minecraftparticle.logic.utils;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.lzx.minecraftparticle.MinecraftParticleApplication;

public class PermissionUtil {
    enum Code {
        STORAGE,
        Media,
    }
    
    public static boolean requestStorage(Activity activity) {
        //安卓11+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //获取所有文件访问权限
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivity(intent);
                return false;
            }else {
                return true;
            }
        //安卓6+
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获得读写权限
            if((ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                Code.STORAGE.ordinal());
                return false;
            }else {
                return true;
            }
        }else {
            return true;
        }
    }

    public static boolean requestMedia(Activity activity) {
        //安卓13+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //访问媒体图片
            if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        Code.Media.ordinal()
                );
                //安卓14+ 部分权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }else {
                        return true;
                    }
                }
                return false;
            }else {
                return true;
            }
        }
        return true;

    }
}
