package com.md09.pharmapoly.ui.view.fragment;

import static com.md09.pharmapoly.utils.Constants.formatCurrency;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.md09.pharmapoly.Adapters.CartAdapter;
import com.md09.pharmapoly.Adapters.ProductAdapter;
import com.md09.pharmapoly.Models.Cart;
import com.md09.pharmapoly.Models.CartItem;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.CartItemListener;
import com.md09.pharmapoly.utils.DialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.md09.pharmapoly.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RetrofitClient retrofitClient;
    private RecyclerView rcv_cart_item;
    private CartAdapter cartAdapter;
    private CardView btn_purchase;
    private TextView tv_price, tv_purchase;
    private CheckBox cb_selected_all_item;
    private Cart cart;
    private CartViewModel cartViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        InitUI(view);

        SetUpRecyclerView();


        cartViewModel.GetCart().observe(getViewLifecycleOwner(), new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                CartFragment.this.cart = cart;
                cartAdapter.UpdateCart(cart);
                CalculateTotalPrice();
            }
        });
        cb_selected_all_item.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cart != null && cart.getCartItems() != null) {
                for (int i = 0; i < cart.getCartItems().size(); i++) {
                    CartItem item = cart.getCartItems().get(i);
                    if (item.isSelected() != isChecked) {
                        item.setSelected(isChecked);
                        cartAdapter.notifyItemChanged(i);
                    }
                }
                CalculateTotalPrice();
            }
        });

        return view;
    }

    private void SetUpRecyclerView() {

        cartAdapter = new CartAdapter(getContext(), null, new CartItemListener() {
            @Override
            public void onItemDeleted(CartItem cartItem) {
                RemoveItemFromCart(cartItem);
            }

            @Override
            public void onQuantityUpdated(CartItem cartItem) {
                UpdateCartItem(cartItem);
            }

            @Override
            public void onItemSelected(CartItem cartItem) {
                CalculateTotalPrice();
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        rcv_cart_item.setLayoutManager(layoutManager);
        rcv_cart_item.setAdapter(cartAdapter);
    }

    private void RemoveItemFromCart(CartItem cartItem) {
        DialogHelper.ShowConfirmationDialog(
                getContext(),
                "",
                getContext().getString(R.string.confirm_remove_cart),
                "OK",
                "Cancel",
                () -> {
                    RemoveCartItem(cartItem.get_id());
                });
    }

    private void RemoveCartItem(String id) {
        Map<String, Boolean> selectedItems = new HashMap<>();
        for (CartItem item : cart.getCartItems()) {
            selectedItems.put(item.get_id(), item.isSelected());
        }

        cartViewModel.RemoveCartItem(getContext(), id, new Consumer<Cart>() {
            @Override
            public void accept(Cart cart) {
//                if (cart.getCartItems() != null) {
//                    for (CartItem item : cart.getCartItems()) {
//                        if (selectedItems.containsKey(item.get_id())) {
//                            item.setSelected(selectedItems.get(item.get_id()));
//                        }
//                    }
//                }
//                CartFragment.this.cart = cart;
//                cartAdapter.UpdateCart(cart);
//                CalculateTotalPrice();
            }
        });
    }

    private void CalculateTotalPrice() {
        int totalPrice = 0;
        int selectedCount = 0;
        for (CartItem item : cart.getCartItems()) {
            if (item.isSelected()) {
                selectedCount++;
                totalPrice += item.getTotal_price();
            }
        }
        String baseText = getString(R.string.purchase);
        tv_purchase.setText(baseText + " (" + selectedCount + ")");
        tv_price.setText(formatCurrency(totalPrice, "đ"));
    }

    private void UpdateCartItem(CartItem cartItem) {
        retrofitClient.callAPI().updateCartItem(
                "Bearer " + new SharedPrefHelper(getContext()).getToken(),
                cartItem.get_id(),
                cartItem.getQuantity()
        ).enqueue(new Callback<ApiResponse<CartItem>>() {
            @Override
            public void onResponse(Call<ApiResponse<CartItem>> call, Response<ApiResponse<CartItem>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        CartItem updatedItem = response.body().getData();
                        cartItem.setQuantity(updatedItem.getQuantity());
                        if (cartItem.isSelected()) CalculateTotalPrice();
                        cartAdapter.UpdateCartItem(updatedItem);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CartItem>> call, Throwable t) {

            }
        });
    }

    private void InitUI(View view) {
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);

        retrofitClient = new RetrofitClient();
        rcv_cart_item = view.findViewById(R.id.rcv_cart_item);

        btn_purchase = view.findViewById(R.id.btn_purchase);

        tv_price = view.findViewById(R.id.tv_price);
        tv_purchase = view.findViewById(R.id.tv_purchase);
        String baseText = getString(R.string.purchase);
        tv_purchase.setText(baseText + " (0)");

        cb_selected_all_item = view.findViewById(R.id.cb_selected_all_item);
    }
}