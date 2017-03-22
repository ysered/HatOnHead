package com.ysered.hatonhead.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains helper methods to check or request application permissions.
 */
public final class PermissionUtils {

    private final AppCompatActivity context;

    public PermissionUtils(AppCompatActivity context) {
        this.context = context;
    }

    /**
     * Checks whether write external storage and camera permissions are enabled for the application.
     * @return true if write external storage or camera permissions are disabled or false otherwise.
     */
    public boolean isRequireCameraPermissions() {
        return isRequirePermission(context, Manifest.permission.CAMERA)
                || isRequirePermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * Requests camera and write external storage permissions.
     * It usually shows runtime permission dialog on API 23 (Marshmallow) or higher.
     * @param requestCode
     */
    public void requestCameraPermissions(int requestCode) {
        final String[] requiredPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        final List<String> missingPermissions = new ArrayList<>(2);
        for (String permission : requiredPermissions) {
            if (isRequirePermission(context, permission)) {
                missingPermissions.add(permission);
            }
        }
        final String[] permissionsToRequest = missingPermissions.toArray(new String[missingPermissions.size()]);
        requestPermissions(context, permissionsToRequest, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions(AppCompatActivity activity, String[] permissions, int requestCode) {
        activity.requestPermissions(permissions, requestCode);
    }

    private boolean isRequirePermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED;
    }
}
