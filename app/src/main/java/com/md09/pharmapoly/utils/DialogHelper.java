package com.md09.pharmapoly.utils;

import android.app.AlertDialog;
import android.content.Context;

public class DialogHelper {
    public static void ShowConfirmationDialog(Context context, String title, String message,
                                              String positiveText, String negativeText,
                                              Runnable onConfirm) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, (dialog, which) -> {
                    if (onConfirm != null) {
                        onConfirm.run();
                    }
                })
                .setNegativeButton(negativeText, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}
