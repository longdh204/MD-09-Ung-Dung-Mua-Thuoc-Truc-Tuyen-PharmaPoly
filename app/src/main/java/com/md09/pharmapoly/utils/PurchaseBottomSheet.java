package com.md09.pharmapoly.utils;

import static com.md09.pharmapoly.utils.Constants.MAX_QUANTITY_PER_PRODUCT;
import static com.md09.pharmapoly.utils.Constants.formatCurrency;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.md09.pharmapoly.Models.CartItem;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.Models.ProductProductType;
import com.md09.pharmapoly.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PurchaseBottomSheet extends BottomSheetDialogFragment {
    private Product product;
    private AddToCartListener listener;
    public PurchaseBottomSheet(Product product,AddToCartListener listener) {
        this.product = product;
        this.listener = listener;
    }

    public interface AddToCartListener {
        void onAddToCart(CartItem cartItem);
    }
    private TextView
            tv_product_name,
            //tv_discounted_price,
            tv_original_price,
            tv_subtotal;
    private EditText edt_quantity;
    private ImageView img_product;
    private ImageButton btn_close;
    private LinearLayout
            btn_add_to_cart,
            btn_purchase,
            layout_product_type;
    private RelativeLayout
            btn_decrease_quantity,
            btn_increase_quantity;

    private CartItem cartItem;

    private ProductProductType currentProductType = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_quantity_selection, container, false);

        InitUI(view);
        cartItem = new CartItem(
                new SharedPrefHelper(getContext()).getCartId(),
                product.get_id(),
                1,
                product.getPrice()
//                product
        );

//        SpannableString spannable = new SpannableString(String.valueOf(product.getPrice()));
//        spannable.setSpan(new StrikethroughSpan(), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tv_original_price.setText(spannable);
//        tv_original_price.getPaint().setStrokeWidth(3);

        tv_original_price.setText(formatCurrency(product.getPrice(), "đ"));
        tv_subtotal.setText(formatCurrency(product.getPrice(), "đ"));

        tv_product_name.setText(product.getName());
        Picasso.get().load(product.getImageUrl()).into(img_product);

        btn_close.setOnClickListener(v -> dismiss());

        btn_decrease_quantity.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                UpdateCartItemQuantity(cartItem, cartItem.getQuantity() - 1);
            }
        });
        btn_increase_quantity.setOnClickListener(v -> {
            if (cartItem.getQuantity() < MAX_QUANTITY_PER_PRODUCT) {
                UpdateCartItemQuantity(cartItem, cartItem.getQuantity() + 1);
            }
        });

        btn_add_to_cart.setOnClickListener(v -> {
            listener.onAddToCart(cartItem);
        });

        List<View> typeViews = new ArrayList<>();
        for (ProductProductType item : product.getProduct_types()) {
            View view_product_type = LayoutInflater.from(getContext()).inflate(R.layout.item_product_type_wrap, null, false);
            CardView product_type = view_product_type.findViewById(R.id.product_type);
            TextView tv_type_name = view_product_type.findViewById(R.id.tv_type_name);
            tv_type_name.setText(item.getName());

            typeViews.add(view_product_type);

            view_product_type.setOnClickListener(v -> {
                currentProductType = item;
                cartItem.setProduct_product_type_id(item.get_id());
                for (View vType : typeViews) {
                    CardView card = vType.findViewById(R.id.product_type);
                    TextView tv = vType.findViewById(R.id.tv_type_name);
                    card.setCardBackgroundColor(getContext().getColor(R.color.gray_6F4));
                    tv.setTextColor(getContext().getColor(R.color.black_333));
                }


                product_type.setCardBackgroundColor(getContext().getColor(R.color.blue_CE4));
                tv_type_name.setTextColor(getContext().getColor(R.color.white_FFF));
                tv_original_price.setText(
                        formatCurrency(item.getPrice(), "đ") +
                                "/" +
                                item.getName());
                UpdateCartItemQuantity(cartItem, cartItem.getQuantity());
            });
            layout_product_type.addView(view_product_type);
        }
        if (!typeViews.isEmpty()) {
            typeViews.get(product.getSelectedTypeIndex()).performClick();
        }

        return view;
    }
    private void UpdateCartItemQuantity(CartItem cartItem, int newQuantity) {
        cartItem.setQuantity(newQuantity);
        edt_quantity.setText(String.valueOf(newQuantity));
        int price = newQuantity * currentProductType.getPrice();
        tv_subtotal.setText(formatCurrency(price, "đ"));
    }
    private void InitUI(View view) {
        tv_product_name = view.findViewById(R.id.tv_product_name);
        //tv_discounted_price = view.findViewById(R.id.tv_discounted_price);
        tv_original_price = view.findViewById(R.id.tv_original_price);

        img_product = view.findViewById(R.id.img_product);

        btn_close = view.findViewById(R.id.btn_close);

        btn_purchase = view.findViewById(R.id.btn_purchase);
        btn_add_to_cart = view.findViewById(R.id.btn_add_to_cart);

        edt_quantity = view.findViewById(R.id.edt_quantity);
        edt_quantity.setText(String.valueOf(1));

        tv_subtotal = view.findViewById(R.id.tv_subtotal);

        btn_decrease_quantity = view.findViewById(R.id.btn_decrease_quantity);
        btn_increase_quantity = view.findViewById(R.id.btn_increase_quantity);

        layout_product_type = view.findViewById(R.id.layout_product_type);
    }
}
