package com.md09.pharmapoly.viewmodel;

import static com.md09.pharmapoly.utils.Constants.PRODUCT_ADDED_TO_CART_KEY;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.md09.pharmapoly.Models.Cart;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartViewModel extends ViewModel {
    private MutableLiveData<Cart> cartLiveData = new MutableLiveData<>();
    private RetrofitClient retrofitClient;
    public LiveData<Cart> GetCart() {
        return cartLiveData;
    }

    public void FetchCartData(Context context) {
        new SharedPrefHelper(context).resetBooleanState(PRODUCT_ADDED_TO_CART_KEY);
        retrofitClient = new RetrofitClient();
        new Thread(() -> {
            retrofitClient.callAPI().cart("Bearer " + new SharedPrefHelper(context).getToken())
                    .enqueue(new Callback<ApiResponse<Cart>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                                cartLiveData.postValue(response.body().getData());
                                new SharedPrefHelper(context).saveCartId(response.body().getData().get_id());
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<Cart>> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }).start();
    }

    public void RemoveCartItem(Context context, String id, Consumer<Cart> onSuccess) {
        retrofitClient = new RetrofitClient();
        retrofitClient.callAPI().removeCartItem(
                id,
                "Bearer " + new SharedPrefHelper(context).getToken()
        ).enqueue(new Callback<ApiResponse<Cart>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    Cart currentCart = cartLiveData.getValue();
                    if (currentCart != null && currentCart.getCartItems() != null) {
                        currentCart.getCartItems().removeIf(item -> item.get_id().equals(id));

                        cartLiveData.postValue(currentCart);
                    }
                    if (onSuccess != null) {
                        onSuccess.accept(currentCart);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cart>> call, Throwable t) {
            }
        });
    }
}
