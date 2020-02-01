package com.example.enactushack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

public abstract class PermissionUtils {
    public static void requestPermission(FragmentActivity activity, int requestId,
                                         String permission, boolean finishActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // TODO: display dialogue with rationale
        } else {

        }
    }
}
