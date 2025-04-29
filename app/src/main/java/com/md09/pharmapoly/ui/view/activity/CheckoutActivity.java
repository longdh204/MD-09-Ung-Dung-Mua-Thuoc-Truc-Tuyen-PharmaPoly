package com.md09.pharmapoly.ui.view.activity;

import static com.md09.pharmapoly.utils.Constants.ORDER_KEY;
import static com.md09.pharmapoly.utils.Constants.formatCurrency;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.md09.pharmapoly.Models.CartItem;
import com.md09.pharmapoly.Models.GHNResponse;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.data.model.User;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.PaymentMethod;
import com.md09.pharmapoly.utils.ProgressDialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.md09.pharmapoly.utils.SuccessMessageBottomSheet;
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
            if (!isUserInfoValid(user)) {
                Toast.makeText(this, getString(R.string.check_user_info), Toast.LENGTH_SHORT).show();
                return;
            }

            ProgressDialogHelper.showLoading(this);

            Map<String, Object> requestData = new HashMap<>();
            String payment_method = new SharedPrefHelper(this).getPaymentMethod().getValue();
            requestData.put("payment_method", payment_method);

            List<String> selectedCartItemIds = new ArrayList<>();
            for (CartItem item : selectedItems) {
                selectedCartItemIds.add(item.get_id());
            }
            requestData.put("cart_item_ids", selectedCartItemIds);

            new RetrofitClient()
                    .callAPI()
                    .createOrders(
                                requestData,
                                "Bearer " + new SharedPrefHelper(this).getToken()
                            )
                    .enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Map<String, Object>>> call, Response<ApiResponse<Map<String, Object>>> response) {
                            if (response.isSuccessful() && response.body().getStatus() == 200) {
                                Map<String, Object> data = response.body().getData();
                                String orderId = (String) data.get("order_id");
                                String qrCodeUrl = (String) data.get("qr_code_url");

                                if (payment_method.equals("COD")) {
                                    SuccessMessageBottomSheet bottomSheet = SuccessMessageBottomSheet.newInstance(getString(R.string.order_success));
                                    bottomSheet.setOnDismissListener(() -> {
                                        finish();
                                    });
                                    bottomSheet.show(getSupportFragmentManager(), "SuccessMessageBottomSheet");
                                } else {
                                    showQrDialog(orderId,qrCodeUrl);
                                }
                                new SharedPrefHelper(CheckoutActivity.this).setBooleanState(ORDER_KEY,true);
                                ProgressDialogHelper.hideLoading();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                            ProgressDialogHelper.hideLoading();
                        }
                    });
        });
        FillAddress();
    }
    private boolean isUserInfoValid(User user) {
        if (user == null) return false;
        if (user.getFull_name() == null || user.getFull_name().trim().isEmpty()) {
            return false;
        }

        if (user.getPhone_number() == null || user.getPhone_number().trim().isEmpty()) {
            return false;
        }

        if (user.getShipping_phone_number() == null || user.getShipping_phone_number().trim().isEmpty()) {
            return false;
        }

        if (user.getAddress() == null) {
            return false;
        }

        String paymentMethod = new SharedPrefHelper(this).getPaymentMethod().getValue();
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            return false;
        }

//        if (user.getStatus() == 0) {
//            return false;
//        }

        return true;
    }
//    private void ListenForPaymentStatus(String userId, Dialog dialog) {
//        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("payment_status")
//                .child(userId);
//
//        orderRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String status = snapshot.getValue(String.class);
//                    if ("PAID".equals(status)) {
//                        SuccessMessageBottomSheet bottomSheet = SuccessMessageBottomSheet.newInstance(getString(R.string.order_success));
//                        bottomSheet.show(getSupportFragmentManager(), "SuccessMessageBottomSheet");
//                        dialog.dismiss();
//                        DeleteOrderStatus();
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }

    private Handler handler;
    private Runnable paymentStatusRunnable;
    private int elapsedSeconds = 0;
    private void ListenForPaymentStatus(String orderId, Dialog dialog) {
        handler = new Handler(Looper.getMainLooper());
        elapsedSeconds = 0;

        paymentStatusRunnable = new Runnable() {
            @Override
            public void run() {
                new RetrofitClient().callAPI().getPaymentStatusStatus(orderId,
                                "Bearer " + new SharedPrefHelper(CheckoutActivity.this).getToken())
                        .enqueue(new Callback<ApiResponse<Void>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getStatus() == 200) {
                                        SuccessMessageBottomSheet bottomSheet = SuccessMessageBottomSheet.newInstance(getString(R.string.order_success));
                                        bottomSheet.setOnDismissListener(() -> {
                                            finish();
                                        });
                                        bottomSheet.show(getSupportFragmentManager(), "SuccessMessageBottomSheet");
                                        dialog.dismiss();
                                        stopListeningPaymentStatus();
                                    } else {
                                        scheduleNextCheck();
                                    }
                                } else {
                                    scheduleNextCheck();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                                scheduleNextCheck();
                            }
                        });
            }
        };

        handler.post(paymentStatusRunnable);
    }

    private void scheduleNextCheck() {
        elapsedSeconds += 10;
        if (elapsedSeconds < 600) { // 600 giây = 10 phút
            handler.postDelayed(paymentStatusRunnable, 10_000); // 10.000ms = 10s
        } else {
            stopListeningPaymentStatus(); // hết thời gian thì dừng
        }
    }

    private void stopListeningPaymentStatus() {
        if (handler != null && paymentStatusRunnable != null) {
            handler.removeCallbacks(paymentStatusRunnable);
        }
    }
//    private void ListenForPaymentStatus(String orderId, Dialog dialog) {
//        new RetrofitClient().callAPI().getPaymentStatusStatus( orderId,
//                "Bearer " + new SharedPrefHelper(this).getToken())
//                .enqueue(new Callback<ApiResponse<Void>>() {
//                    @Override
//                    public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            if (response.body().getStatus() == 200) {
//                                SuccessMessageBottomSheet bottomSheet = SuccessMessageBottomSheet.newInstance(getString(R.string.order_success));
//                                bottomSheet.setOnDismissListener(() -> {
//                                    finish();
//                                });
//                                bottomSheet.show(getSupportFragmentManager(), "SuccessMessageBottomSheet");
//                                dialog.dismiss();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
//
//                    }
//                });
//    }

    private void showQrDialog(String orderId,String qrCodeUrl) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_qr_code);
        dialog.setCancelable(false);

        ImageView qrImageView = dialog.findViewById(R.id.qr_image);
        Picasso.get().load(qrCodeUrl).into(qrImageView);

        dialog.show();
//        ListenForPaymentStatus(new SharedPrefHelper(this).getUser().get_id(), dialog);
        ListenForPaymentStatus(orderId, dialog);
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
                img_icon_payment_method.setBackground(getDrawable(R.drawable.ic_online_payment));
                tv_payment_method.setText(getString(R.string.online_payment));
                break;
        }
    }
    private void FillCartItem(CartItem cartItem) {
        View view = LayoutInflater.from(this).inflate(R.layout.checkout_item,null,false);

        TextView
                tv_quantity = view.findViewById(R.id.tv_quantity),
                tv_product_name = view.findViewById(R.id.tv_product_name),
                tv_original_price = view.findViewById(R.id.tv_original_price);
        ImageView img_product = view.findViewById(R.id.img_product);

        tv_quantity.setText("x" + cartItem.getQuantity());
        //tv_product_name.setText(cartItem.getProduct().getName());
        tv_product_name.setText(cartItem.getProductType().getProduct().getName());

        tv_original_price.setText(formatCurrency(cartItem.getOriginal_price(), "đ") + "/" + cartItem.getProductType().getProductType().getName());
        //Picasso.get().load(cartItem.getProduct().getImageUrl()).into(img_product);
        Picasso.get().load(cartItem.getProductType().getProduct().getImageUrl()).into(img_product);

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
            String fullName = (user.getFull_name() != null) ? user.getFull_name() : getString(R.string.customer);
            String addressLine = fullName + " | " +
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