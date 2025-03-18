package com.md09.pharmapoly.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.md09.pharmapoly.R;

//
//public class CartSuccessBottomSheet extends BottomSheetDialogFragment {
//    public static CartSuccessBottomSheet newInstance() {
//        return new CartSuccessBottomSheet();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.bottom_sheet_cart_success, container, false);
//
//        new Handler(Looper.getMainLooper()).postDelayed(this::dismiss, 3000);
//
//        view.findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());
//        return view;
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (getDialog() != null && getDialog().getWindow() != null) {
//            getDialog().getWindow().setBackgroundDrawable(null);
//        }
//    }
//
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(STYLE_NO_TITLE, R.style.TransparentBottomSheet);
//    }
//}



public class CartSuccessBottomSheet extends DialogFragment {
    public static CartSuccessBottomSheet newInstance() {
        return new CartSuccessBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_cart_success, container, false);

        new Handler(Looper.getMainLooper()).postDelayed(this::dismiss, 3000);

        view.findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setDimAmount(0f);
            window.setGravity(android.view.Gravity.BOTTOM);
        }
    }
}