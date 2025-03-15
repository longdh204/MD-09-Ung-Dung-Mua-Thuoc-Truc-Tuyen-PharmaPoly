package com.md09.pharmapoly.Adapters;

import static com.md09.pharmapoly.utils.Constants.MAX_QUANTITY_PER_PRODUCT;
import static com.md09.pharmapoly.utils.Constants.formatCurrency;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.Cart;
import com.md09.pharmapoly.Models.CartItem;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.ui.view.fragment.CartFragment;
import com.md09.pharmapoly.utils.CartItemListener;
import com.md09.pharmapoly.utils.DialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.squareup.picasso.Picasso;


import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            cartItem.setTotal_price(cartItem.getQuantity() * cartItem.getPrice());
            notifyItemChanged(position);
        }
    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.cart_item,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        CartItem cartItem = cart.getCartItems().get(position);
        if (cartItem != null) {

            holder.tv_product_name.setText(cartItem.getProduct().getName());
            holder.tv_price.setText(formatCurrency(cartItem.getPrice(), "đ"));
            holder.edt_quantity.setText(String.valueOf(cartItem.getQuantity()));

            Picasso.get().load(cartItem.getProduct().getImageUrl()).into(holder.img_product);

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
                } else {
                    cartItemListener.onItemDeleted(cartItem);
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
        private CheckBox cb_selected_item;
        private TextView tv_price,tv_product_name;
        private EditText edt_quantity;
        private ImageView img_product;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_decrease_quantity = itemView.findViewById(R.id.btn_decrease_quantity);
            btn_increase_quantity = itemView.findViewById(R.id.btn_increase_quantity);
            btn_remove = itemView.findViewById(R.id.btn_remove);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            edt_quantity = itemView.findViewById(R.id.edt_quantity);

            img_product = itemView.findViewById(R.id.img_product);

            cb_selected_item = itemView.findViewById(R.id.cb_selected_item);
        }
    }
}
