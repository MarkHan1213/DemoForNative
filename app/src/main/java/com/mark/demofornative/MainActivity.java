package com.mark.demofornative;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.NativeActivity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;


public class MainActivity extends NativeActivity {

    // Used to load the 'demofornative' library on application startup.
    static {
        System.loadLibrary("openxr_loader");
        System.loadLibrary("VR_demo");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!hasPermissionsGranted(REQUEST_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS, PERMISSION_REQUEST_CODE);
        }
        //sdcard/Android/data/com.mark.demofornative/files/Download
        String fileDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        CommonUtils.copyAssetsDirToSDCard(MainActivity.this, "poly", fileDir + "/model");
        CommonUtils.copyAssetsDirToSDCard(MainActivity.this, "fonts", fileDir);
        CommonUtils.copyAssetsDirToSDCard(MainActivity.this, "yuv", fileDir);
    }

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String[] REQUEST_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
    };

    protected boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}