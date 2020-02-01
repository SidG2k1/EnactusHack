package com.example.enactushack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public abstract class PermissionUtils {
    public static void requestPermission(AppCompatActivity activity, int requestId,
                                         String permission, boolean finishActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // TODO: display dialogue with rationale
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestId);
        }
    }
}
