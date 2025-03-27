package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.md09.pharmapoly.R;
import com.md09.pharmapoly.utils.PaymentMethod;
import com.md09.pharmapoly.utils.SharedPrefHelper;

public class PaymentMethodActivity extends AppCompatActivity {

    private LinearLayout
            layout_payment_online,
            payment_cod,
            payment_momo,
            payment_online;
    private CheckBox
            cb_momo_selected,
            cb_cod_selected;
    private Button btn_agree;
    private ImageView img_arrow_right;
    private PaymentMethod paymentMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_method);


        InitUI();

        paymentMethod = new SharedPrefHelper(this).getPaymentMethod();

        payment_online.setOnClickListener(v -> {
            if (layout_payment_online.getVisibility() == View.VISIBLE) {
                layout_payment_online.animate().scaleY(0).setDuration(300).withEndAction(() -> {
                    layout_payment_online.setVisibility(View.GONE);
                }).start();
                img_arrow_right.animate().rotation(0).setDuration(300).start();
            } else {
                layout_payment_online.setVisibility(View.VISIBLE);
                layout_payment_online.setScaleY(0);
                layout_payment_online.animate().scaleY(1).setDuration(300).start();
                img_arrow_right.animate().rotation(90).setDuration(300).start();
            }
        });

        payment_cod.setOnClickListener(v -> {
            if (cb_cod_selected.isChecked()) {
                cb_cod_selected.setChecked(false);
            }
        });
        payment_momo.setOnClickListener(v -> {
            if (cb_momo_selected.isChecked()) {
                cb_momo_selected.setChecked(false);
            }
        });

        cb_momo_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    paymentMethod = PaymentMethod.ONLINE;
                    cb_cod_selected.setChecked(false);
                }
            }
        });
        cb_cod_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    paymentMethod = PaymentMethod.COD;
                    cb_momo_selected.setChecked(false);
                }
            }
        });
        btn_agree.setOnClickListener(v -> {
            SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);
            sharedPrefHelper.savePaymentMethod(paymentMethod);
            finish();
        });

        if (paymentMethod == PaymentMethod.COD) {
            cb_cod_selected.setChecked(true);
        } else {
            cb_momo_selected.setChecked(true);
        }
    }

    private void InitUI() {
        layout_payment_online = findViewById(R.id.layout_payment_online);
        payment_cod = findViewById(R.id.payment_cod);
        payment_momo = findViewById(R.id.payment_momo);
        payment_online = findViewById(R.id.payment_online);

        cb_cod_selected = findViewById(R.id.cb_cod_selected);
        cb_momo_selected = findViewById(R.id.cb_momo_selected);

        img_arrow_right = findViewById(R.id.img_arrow_right);

        btn_agree = findViewById(R.id.btn_agree);
    }
}