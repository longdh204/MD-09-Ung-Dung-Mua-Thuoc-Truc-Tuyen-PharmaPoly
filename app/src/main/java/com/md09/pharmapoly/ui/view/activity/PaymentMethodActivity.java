package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.md09.pharmapoly.Models.Bank;
import com.md09.pharmapoly.Models.BankResponse;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.PaymentMethod;
import com.md09.pharmapoly.utils.ProgressDialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethodActivity extends AppCompatActivity {

    private LinearLayout
            layout_payment_online,
            payment_cod,
            payment_onl,
            payment_online;
    private CheckBox
            cb_online_selected,
            cb_cod_selected;
    private Button btn_agree;
    private ImageView img_arrow_right;
    private PaymentMethod paymentMethod;
    private TextView tv_supported_banks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_method);

        ProgressDialogHelper.showLoading(this);
        InitUI();


        RetrofitClient
                .getVietQrApiService()
                .getBanks()
                .enqueue(new Callback<BankResponse>() {
                    @Override
                    public void onResponse(Call<BankResponse> call, Response<BankResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Bank> banks = response.body().getData();
                            FillBank(banks);
                        }
                        ProgressDialogHelper.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<BankResponse> call, Throwable t) {
                        ProgressDialogHelper.hideLoading();

                    }
                });

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
            } else {
                cb_cod_selected.setChecked(true);
            }
        });
        payment_onl.setOnClickListener(v -> {
            if (cb_online_selected.isChecked()) {
                cb_online_selected.setChecked(false);
            } else {
                cb_online_selected.setChecked(true);
            }
        });

        cb_online_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                    cb_online_selected.setChecked(false);
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
            cb_online_selected.setChecked(true);
        }
    }

    private void FillBank(List<Bank> banks) {
        tv_supported_banks.setText(getString(R.string.supported_banks) + "(" + banks.size() + ")");
        Collections.sort(banks, new Comparator<Bank>() {
            @Override
            public int compare(Bank b1, Bank b2) {
                return Integer.compare(b2.getSupport(), b1.getSupport());
            }
        });

        for (Bank item:banks) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_bank,null,false);
            TextView tv_bank_name = view.findViewById(R.id.tv_bank_name);
            ImageView img_bank_icon = view.findViewById(R.id.img_bank_icon);

            tv_bank_name.setText(item.getShort_name() + " - " + item.getName());
            Picasso.get().load(item.getLogo()).into(img_bank_icon);

            layout_payment_online.addView(view);
        }
    }

    private void InitUI() {
        layout_payment_online = findViewById(R.id.layout_payment_online);
        payment_cod = findViewById(R.id.payment_cod);
        payment_onl = findViewById(R.id.payment_onl);
        payment_online = findViewById(R.id.payment_online);

        cb_cod_selected = findViewById(R.id.cb_cod_selected);
        cb_online_selected = findViewById(R.id.cb_online_selected);

        img_arrow_right = findViewById(R.id.img_arrow_right);

        btn_agree = findViewById(R.id.btn_agree);
        tv_supported_banks = findViewById(R.id.tv_supported_banks);
    }
}