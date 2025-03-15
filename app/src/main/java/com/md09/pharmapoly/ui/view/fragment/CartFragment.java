package com.md09.pharmapoly.ui.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        InitUI(view);

        SetUpRecyclerView();

        retrofitClient.callAPI().cart("Bearer " + new SharedPrefHelper(getContext()).getToken()).enqueue(new Callback<ApiResponse<Cart>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        cartAdapter.UpdateCart(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cart>> call, Throwable t) {

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
                retrofitClient.callAPI().updateCartItem(
                        "Bearer " + new SharedPrefHelper(getContext()).getToken(),
                        cartItem.get_id(),
                        cartItem.getQuantity()
                ).enqueue(new Callback<ApiResponse<CartItem>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<CartItem>> call, Response<ApiResponse<CartItem>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                cartAdapter.UpdateCartItem(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<CartItem>> call, Throwable t) {

                    }
                });
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
        retrofitClient.callAPI().removeCartItem(
                id,
                "Bearer " + new SharedPrefHelper(getContext()).getToken()
        ).enqueue(new Callback<ApiResponse<Cart>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        cartAdapter.UpdateCart(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cart>> call, Throwable t) {

            }
        });
    }
    private void InitUI(View view) {
        retrofitClient = new RetrofitClient();
        rcv_cart_item = view.findViewById(R.id.rcv_cart_item);
    }
}