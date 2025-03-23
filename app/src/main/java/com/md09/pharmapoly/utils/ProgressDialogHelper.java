package com.md09.pharmapoly.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.md09.pharmapoly.R;

public class ProgressDialogHelper {
    private static Dialog progressDialog;

    public static void showLoading(Context context) {
        if (progressDialog == null) {
            progressDialog = new Dialog(context);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.dialog_progress);
            progressDialog.setCancelable(false);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }
    public static boolean isShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }

    public static void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
