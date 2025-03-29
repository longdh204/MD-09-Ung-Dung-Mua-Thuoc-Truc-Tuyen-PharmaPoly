package com.md09.pharmapoly.ui.view.activity;

import static com.md09.pharmapoly.utils.Constants.formatCurrency;
import static com.md09.pharmapoly.utils.Constants.getDisplayStatus;
import static com.md09.pharmapoly.utils.Constants.getStatusColor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.md09.pharmapoly.Models.Order;
import com.md09.pharmapoly.Models.OrderItem;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.ProgressDialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderInfoActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private Order order;
    private TextView
            tv_address,
            tv_total_payment,
            tv_subtotal,
            tv_total_shipping_fee,
            tv_created_at,
            tv_status,
            tv_order_code,
            tv_delivery_date;
    private LinearLayout
            layout_order_item,
            layout_ghn_shipping_code,
            layout_delivery_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_info);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        ProgressDialogHelper.showLoading(this);

        InitUI();
        String orderId = getIntent().getStringExtra("order_id");

        new RetrofitClient()
                .callAPI()
                .getOrderDetail(
                        orderId,
                        "Bearer " + new SharedPrefHelper(this).getToken()
                )
                .enqueue(new Callback<ApiResponse<Order>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                        if (response.isSuccessful() && response.body().getStatus() == 200) {
                            OrderInfoActivity.this.order = response.body().getData();
                            FillAddress();
                            FillOrder();
                        }
                        ProgressDialogHelper.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                        ProgressDialogHelper.hideLoading();

                    }
                });

        btn_back.setOnClickListener(v -> {
            finish();
        });
    }

    private void FillOrder() {
        int subtotal = 0;
        for (OrderItem item : order.getItems()) {
            subtotal += item.getQuantity() * item.getPrice();
            View view = LayoutInflater.from(this).inflate(R.layout.checkout_item, null, false);

            TextView tv_quantity = view.findViewById(R.id.tv_quantity);
            TextView tv_product_name = view.findViewById(R.id.tv_product_name);
            TextView tv_original_price = view.findViewById(R.id.tv_original_price);
            ImageView img_product = view.findViewById(R.id.img_product);

            tv_quantity.setText("x" + item.getQuantity());
            tv_product_name.setText(item.getProduct().getName());
            tv_original_price.setText(formatCurrency(item.getPrice(), "đ"));
            Picasso.get().load(item.getProduct().getImageUrl()).into(img_product);

            layout_order_item.addView(view);
        }
        //if (order.)
        tv_subtotal.setText(formatCurrency(subtotal,"đ"));
        tv_total_shipping_fee.setText(formatCurrency(order.getShipping_fee(),"đ"));
        tv_total_payment.setText(formatCurrency(order.getTotal_price(),"đ"));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(order.getCreated_at());
        tv_created_at.setText(formattedDate);
        tv_status.setText(getDisplayStatus(this, order.getStatus()));
        tv_status.setTextColor(getStatusColor(this, order.getStatus()));
        if (order.getOrder_code() != null && !order.getOrder_code().trim().isEmpty()) {
            layout_ghn_shipping_code.setVisibility(View.VISIBLE);
            tv_order_code.setText(order.getOrder_code());
        }
    }

    private void FillAddress() {
        String addressLine = order.getTo_name() + " | " +
                order.getTo_phone() + "\n" +
                order.getTo_address() + ", ";
        addressLine += order.getWard().getWardName() + ", ";
        addressLine += order.getDistrict().getDistrictName() + ", ";
        addressLine += order.getProvince().getProvinceName();
        tv_address.setText(addressLine);
    }

    private void InitUI() {
        btn_back = findViewById(R.id.btn_back);
        tv_address = findViewById(R.id.tv_address);
        tv_total_payment = findViewById(R.id.tv_total_payment);
        tv_subtotal = findViewById(R.id.tv_subtotal);
        tv_total_shipping_fee = findViewById(R.id.tv_total_shipping_fee);
        tv_created_at = findViewById(R.id.tv_created_at);
        tv_status = findViewById(R.id.tv_status);
        tv_order_code = findViewById(R.id.tv_order_code);

        layout_ghn_shipping_code = findViewById(R.id.layout_ghn_shipping_code);
        layout_order_item = findViewById(R.id.layout_order_item);
    }
}