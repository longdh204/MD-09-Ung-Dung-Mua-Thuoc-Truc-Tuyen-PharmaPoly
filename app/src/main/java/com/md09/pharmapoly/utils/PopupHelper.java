package com.md09.pharmapoly.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.Nullable;

public class PopupHelper {
    public static AlertDialog ShowPopup(Context context, String title, String message, @Nullable DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        // Nếu không truyền sự kiện OK, mặc định chỉ đóng dialog
        if (okListener == null) {
            okListener = (dialog, which) -> dialog.dismiss();
        }

        builder.setPositiveButton("OK", okListener);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }
}

