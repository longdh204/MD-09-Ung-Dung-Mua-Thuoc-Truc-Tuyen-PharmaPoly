package com.md09.pharmapoly.ui.view.activity;

import static com.md09.pharmapoly.utils.Constants.findObjectById;
import static com.md09.pharmapoly.utils.Constants.formatCurrency;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.md09.pharmapoly.Models.CartItem;
import com.md09.pharmapoly.Models.GHNResponse;
import com.md09.pharmapoly.Models.Order;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.Models.Province;
import com.md09.pharmapoly.Models.UserAddress;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.data.model.User;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.PaymentMethod;
import com.md09.pharmapoly.utils.ProgressDialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private TextView
            tv_address,
            tv_total_payment_1,
            tv_total_payment_2,
            tv_total_amount,
            tv_subtotal,
            tv_total_amount_quantity,
            tv_total_shipping_fee,
            tv_payment_method;
    private LinearLayout
            btn_selectedAddress,
            selected_payment_method,
            layout_cart_item;
    private User user;
    private ImageButton btn_back;
    private ImageView img_icon_payment_method;
    private List<CartItem> selectedItems = new ArrayList<>();
    private RelativeLayout btn_order;
    private int total_quantity = 0;
    private int total_price = 0;
    private int total_shipping_fee = 0;
    private int total_payment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        ProgressDialogHelper.showLoading(this);
        InitUI();
        this.user = new SharedPrefHelper(this).getUser();
        user.setAddress(null);
        String jsonSelectedItems = getIntent().getStringExtra("selected_items");

        if (jsonSelectedItems != null) {
            Gson gson = new Gson();
            this.selectedItems = gson.fromJson(jsonSelectedItems, new TypeToken<List<CartItem>>(){}.getType());
        }
        if (selectedItems != null && selectedItems.size() > 0) {

            for (CartItem item: this.selectedItems) {
                total_quantity += item.getQuantity();
                total_price += item.getTotal_price();
                FillCartItem(item);
            }
            tv_total_amount_quantity.setText(
                    getString(R.string.total_amount) +
                            " (" + total_quantity + " " + getString(R.string.product).toLowerCase() + ")");
            tv_total_amount.setText(formatCurrency(total_price,"đ"));
            tv_subtotal.setText(formatCurrency(total_price,"đ"));

        }
        btn_back.setOnClickListener(v -> {
            finish();
        });
        selected_payment_method.setOnClickListener(v -> {
            startActivity(new Intent(CheckoutActivity.this,PaymentMethodActivity.class));
        });
        btn_selectedAddress.setOnClickListener(v -> {
            startActivity(new Intent(CheckoutActivity.this,AddressActivity.class));
        });
        btn_order.setOnClickListener(v -> {
            Map<String, String> requestData = new HashMap<>();
            requestData.put("payment_method", new SharedPrefHelper(this).getPaymentMethod().getValue());
            requestData.put("items", new Gson().toJson(selectedItems));

//            new RetrofitClient()
//                    .callAPI()
//                    .createOrders(
//                                requestData,
//                                "Bearer " + new SharedPrefHelper(this).getToken()
//                            )
//                    .enqueue(new Callback<ApiResponse<Order>>() {
//                        @Override
//                        public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
//
//                        }
//                    });
        });
        FillAddress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.user = new SharedPrefHelper(this).getUser();
        UpdatePaymentMethod();
        FillAddress();
    }
    private void UpdatePaymentMethod() {
        PaymentMethod paymentMethod = new SharedPrefHelper(this).getPaymentMethod();
        switch (paymentMethod) {
            case COD:
                img_icon_payment_method.setBackground(getDrawable(R.drawable.ic_money));
                tv_payment_method.setText(getString(R.string.cod));
                break;
            case ONLINE:
                img_icon_payment_method.setBackground(getDrawable(R.drawable.ic_momo));
                tv_payment_method.setText(getString(R.string.momo_payment));
                break;
        }
    }
    private void FillCartItem(CartItem cartItem) {
        View view = LayoutInflater.from(this).inflate(R.layout.checkout_item,null,false);

        TextView
                tv_quantity = view.findViewById(R.id.tv_quantity),
                tv_product_name = view.findViewById(R.id.tv_product_name),
                tv_discounted_price = view.findViewById(R.id.tv_discounted_price);
        ImageView img_product = view.findViewById(R.id.img_product);

        tv_quantity.setText("x" + cartItem.getQuantity());
        tv_product_name.setText(cartItem.getProduct().getName());
        tv_discounted_price.setText(formatCurrency(cartItem.getDiscounted_price(), "đ"));
        Picasso.get().load(cartItem.getProduct().getImageUrl()).into(img_product);

        layout_cart_item.addView(view);
    }
    private void FillAddress() {
        if (user.getAddress() == null) {
            tv_address.setText(getString(R.string.no_shipping_address));
            total_payment = total_price;
            tv_total_payment_1.setText(formatCurrency(total_payment,"đ"));
            tv_total_payment_2.setText(formatCurrency(total_payment,"đ"));
            tv_total_shipping_fee.setText(formatCurrency(0, "đ"));
            ProgressDialogHelper.hideLoading();
        } else {
            String addressLine = user.getFull_name() + " | " +
                    user.getShipping_phone_number() + "\n" +
                    user.getAddress().getStreet_address() + ", ";
            addressLine += user.getAddress().getWard().getWardName() + ", ";
            addressLine += user.getAddress().getDistrict().getDistrictName() + ", ";
            addressLine += user.getAddress().getProvince().getProvinceName();
            tv_address.setText(addressLine);
            CalculateShippingFee();
        }
    }
    private void CalculateShippingFee() {
        Map<String, String> params = new HashMap<>();
        params.put("to_district_id", String.valueOf(user.getAddress().getDistrict_id()));
        params.put("to_ward_code", user.getAddress().getWard_id());

        new RetrofitClient()
                .callAPI()
                .calculateShippingFee("Bearer " + new SharedPrefHelper(this).getToken(),params)
                .enqueue(new Callback<GHNResponse<Object>>() {
                    @Override
                    public void onResponse(Call<GHNResponse<Object>> call, Response<GHNResponse<Object>> response) {
                        ProgressDialogHelper.hideLoading();
                        if (response.isSuccessful() && response.body().getCode() == 200) {
                            Map<String, Object> data = (Map<String, Object>) response.body().getData();
                            CheckoutActivity.this.total_shipping_fee = ((Number) data.get("total")).intValue();
                            total_payment = total_price + total_shipping_fee;
                            tv_total_payment_1.setText(formatCurrency(total_payment,"đ"));
                            tv_total_payment_2.setText(formatCurrency(total_payment,"đ"));
                            tv_total_shipping_fee.setText(formatCurrency(total_shipping_fee, "đ"));
                        }
                    }

                    @Override
                    public void onFailure(Call<GHNResponse<Object>> call, Throwable t) {
                        ProgressDialogHelper.hideLoading();
                    }
                });
    }
    private void InitUI() {
        btn_selectedAddress = findViewById(R.id.btn_selectedAddress);
        tv_address = findViewById(R.id.tv_address);
        tv_total_payment_1 = findViewById(R.id.tv_total_payment_1);
        tv_total_payment_2 = findViewById(R.id.tv_total_payment_2);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        tv_total_amount_quantity = findViewById(R.id.tv_total_amount_quantity);
        tv_total_shipping_fee = findViewById(R.id.tv_total_shipping_fee);
        tv_subtotal = findViewById(R.id.tv_subtotal);
        tv_payment_method = findViewById(R.id.tv_payment_method);

        layout_cart_item = findViewById(R.id.layout_cart_item);

        btn_back = findViewById(R.id.btn_back);
        selected_payment_method = findViewById(R.id.selected_payment_method);

        img_icon_payment_method = findViewById(R.id.img_icon_payment_method);

        btn_order = findViewById(R.id.btn_order);
    }
}