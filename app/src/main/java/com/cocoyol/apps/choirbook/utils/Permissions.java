package com.cocoyol.apps.choirbook.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.cocoyol.apps.choirbook.R;

public class Permissions {

    private Context context;
    private Activity activity;

    public Permissions(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void askForPermissions(String permission, int code) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        String message = context.getString(R.string.text_permission_explanation);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                requestPermission(permission, code);
            } else {
                showExplanation(context.getString(R.string.text_permission_needed), message, permission, code);
            }
            return;
        }
        requestPermission(permission, code);
    }

    private void showExplanation(String title, String message, final String permission, final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permissionName}, permissionRequestCode);
    }

}
