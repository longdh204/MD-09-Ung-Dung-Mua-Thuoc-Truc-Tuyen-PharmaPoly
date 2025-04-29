package com.md09.pharmapoly.Adapters;

import static com.md09.pharmapoly.utils.Constants.MAX_QUANTITY_PER_PRODUCT;
import static com.md09.pharmapoly.utils.Constants.formatCurrency;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.Cart;
import com.md09.pharmapoly.Models.CartItem;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.utils.CartItemListener;
import com.squareup.picasso.Picasso;


import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private Cart cart;
    private CartItemListener cartItemListener;
    public CartAdapter(Context context, Cart cart, CartItemListener cartItemListener) {
        this.context = context;
        this.cart = cart;
        this.cartItemListener = cartItemListener;
    }

    public void UpdateCart(Cart cart) {
        this.cart = cart;
        notifyDataSetChanged();
    }
    public void UpdateCartItem(CartItem updatedItem) {
        int position = cart.getCartItems().indexOf(updatedItem);
        if (position != -1) {
            CartItem cartItem = cart.getCartItems().get(position);
            cartItem.setQuantity(updatedItem.getQuantity());
            //cartItem.setTotal_price(cartItem.getQuantity() * cartItem.getDiscounted_price());
            notifyItemChanged(position);
        }
    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.item_cart,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);
        CartItem cartItem = cart.getCartItems().get(position);



        if (cartItem != null) {
            holder.tv_product_name.setText(cartItem.getProductType().getProduct().getName());
            holder.tv_original_price.setText(formatCurrency(cartItem.getOriginal_price(), "đ") + "/" + cartItem.getProductType().getProductType().getName());

            holder.edt_quantity.setText(String.valueOf(cartItem.getQuantity()));
            holder.edt_quantity.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    UpdateCartItemQuantity(cartItem,Integer.parseInt(holder.edt_quantity.getText().toString().trim()),holder);
                    holder.edt_quantity.clearFocus();
                    return true;
                }
                return false;
            });

            Picasso.get().load(cartItem.getProductType().getProduct().getImageUrl()).into(holder.img_product);

            String productStatus = cartItem.getProductType().getProduct().getStatus();

            if ("discontinued".equals(productStatus) || "paused".equals(productStatus) || "out_of_stock".equals(productStatus)) {
                holder.layout_price_and_quantity.setVisibility(View.GONE);
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.cb_selected_item.setVisibility(View.GONE);
                if ("discontinued".equals(productStatus)) {
                    holder.tv_status.setText(context.getString(R.string.product_sold_out));
                } else if ("paused".equals(productStatus)) {
                    holder.tv_status.setText(context.getString(R.string.product_suspended));
                } else if ("out_of_stock".equals(productStatus)) {
                    holder.tv_status.setText(context.getString(R.string.product_out_of_stock));
                }
                return;
            } else {
                holder.layout_price_and_quantity.setVisibility(View.VISIBLE);
                holder.tv_status.setVisibility(View.GONE);
            }

            holder.edt_quantity.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    holder.edt_quantity.post(() -> holder.edt_quantity.selectAll());
                }
            });
            holder.btn_decrease_quantity.setOnClickListener(v -> {
                if (cartItem.getQuantity() > 1) {
                    UpdateCartItemQuantity(cartItem, cartItem.getQuantity() - 1, holder);
                } else {
                    cartItemListener.onItemDeleted(cartItem);
                }
            });
            holder.btn_increase_quantity.setOnClickListener(v -> {
                if (cartItem.getQuantity() < MAX_QUANTITY_PER_PRODUCT) {
                    UpdateCartItemQuantity(cartItem, cartItem.getQuantity() + 1, holder);
                }
            });
            holder.btn_remove.setOnClickListener(v-> {
                cartItemListener.onItemDeleted(cartItem);
            });

            holder.cb_selected_item.setOnCheckedChangeListener(null);
            holder.cb_selected_item.setChecked(cartItem.isSelected());

            holder.cb_selected_item.setOnCheckedChangeListener((buttonView, isChecked) -> {
                cartItem.setSelected(isChecked);
                cartItemListener.onItemSelected(cartItem);
            });
        }
    }
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Map<String, Runnable> updateTasks = new HashMap<>();

    private void UpdateCartItemQuantity(CartItem cartItem, int newQuantity, CartAdapter.ViewHolder holder) {
        cartItem.setQuantity(newQuantity);
        holder.edt_quantity.setText(String.valueOf(newQuantity));

        String itemId = cartItem.get_id();

        if (updateTasks.containsKey(itemId)) {
            handler.removeCallbacks(updateTasks.get(itemId));
        }

        Runnable updateTask = () -> cartItemListener.onQuantityUpdated(cartItem);
        updateTasks.put(itemId, updateTask);
        handler.postDelayed(updateTask, 500);
    }


    @Override
    public int getItemCount() {
        return (cart != null && cart.getCartItems() != null) ? cart.getCartItems().size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout
                btn_decrease_quantity,
                btn_increase_quantity,
                btn_remove;
        private LinearLayout layout_price_and_quantity;
        private CheckBox cb_selected_item;
        private TextView
                //tv_price,
                tv_product_name,
                tv_original_price,
                tv_status;
                //tv_discount;
        private EditText edt_quantity;
        private ImageView img_product;
        //private CardView layout_discount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_decrease_quantity = itemView.findViewById(R.id.btn_decrease_quantity);
            btn_increase_quantity = itemView.findViewById(R.id.btn_increase_quantity);
            btn_remove = itemView.findViewById(R.id.btn_remove);
            //tv_price = itemView.findViewById(R.id.tv_price);
            tv_original_price = itemView.findViewById(R.id.tv_original_price);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_status = itemView.findViewById(R.id.tv_status);
            //tv_discount = itemView.findViewById(R.id.tv_price);
            edt_quantity = itemView.findViewById(R.id.edt_quantity);

            img_product = itemView.findViewById(R.id.img_product);

            cb_selected_item = itemView.findViewById(R.id.cb_selected_item);

            layout_price_and_quantity = itemView.findViewById(R.id.layout_price_and_quantity);
            //layout_discount = itemView.findViewById(R.id.layout_discount);
        }
    }
}
