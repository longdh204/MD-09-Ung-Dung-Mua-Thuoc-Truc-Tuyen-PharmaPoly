package com.md09.pharmapoly.utils;

import android.content.DialogInterface;
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

import com.md09.pharmapoly.R;

public class SuccessMessageBottomSheet extends DialogFragment {
    private static final String ARG_MESSAGE = "message";
    private String message;
    public interface OnBottomSheetDismissListener {
        void onBottomSheetDismiss();
    }

    private OnBottomSheetDismissListener dismissListener;

    public void setOnDismissListener(OnBottomSheetDismissListener listener) {
        this.dismissListener = listener;
    }

    public static SuccessMessageBottomSheet newInstance(String message) {
        SuccessMessageBottomSheet fragment = new SuccessMessageBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onBottomSheetDismiss();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString(ARG_MESSAGE, "Success!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_cart_success, container, false);

        TextView tv_message = view.findViewById(R.id.tv_message);
        tv_message.setText(message);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded()) {
                dismiss();
            }
        }, 3000);


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