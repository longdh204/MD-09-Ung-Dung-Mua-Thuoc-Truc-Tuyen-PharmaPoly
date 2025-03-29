package com.md09.pharmapoly.ui.view.fragment;

import static com.md09.pharmapoly.utils.Constants.CANCELED_KEY;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.md09.pharmapoly.Adapters.PurchasedOrdersAdapter;
import com.md09.pharmapoly.Models.Order;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.Constants;
import com.md09.pharmapoly.utils.ProgressDialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.md09.pharmapoly.utils.SuccessMessageBottomSheet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeliveredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveredFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeliveredFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeliveredFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeliveredFragment newInstance(String param1, String param2) {
        DeliveredFragment fragment = new DeliveredFragment();
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

    private List<Order> orders = new ArrayList<>();
    private RecyclerView rcv_order;
    private LinearLayout layout_empty;
    private PurchasedOrdersAdapter purchasedOrdersAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivered, container, false);

        InitUI(view);
        ProgressDialogHelper.showLoading(getContext());
        purchasedOrdersAdapter = new PurchasedOrdersAdapter(
                getContext(),
                null,
                Constants.OrderStatusGroup.DELIVERED,
                new PurchasedOrdersAdapter.OrderActionListener() {
                    @Override
                    public void onCancelOrder(Order order) {

                    }

                    @Override
                    public void onReturnOrExchangeOrder(Order order) {
                        ReturnOrExchangeOrder(order);
                    }
                });
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        rcv_order.setLayoutManager(layoutManager);
        rcv_order.setAdapter(purchasedOrdersAdapter);

        new RetrofitClient()
                .callAPI()
                .getOrders(
                        Constants.OrderStatusGroup.DELIVERED.toString().toLowerCase(),
                        "Bearer " + new SharedPrefHelper(getContext()).getToken()
                )
                .enqueue(new Callback<ApiResponse<List<Order>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Order>>> call, Response<ApiResponse<List<Order>>> response) {
                        ProgressDialogHelper.hideLoading();
                        if (response.isSuccessful() && response.body().getStatus() == 200) {
                            List<Order> orders = response.body().getData();

                            if (orders != null && orders.size() > 0) {
                                purchasedOrdersAdapter.Update(response.body().getData());
                                layout_empty.setVisibility(View.GONE);
                                rcv_order.setVisibility(View.VISIBLE);
                            } else {
                                layout_empty.setVisibility(View.VISIBLE);
                                rcv_order.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Order>>> call, Throwable t) {
                        ProgressDialogHelper.hideLoading();
                    }
                });
        return view;
    }

    private void ReturnOrExchangeOrder(Order order) {
        ProgressDialogHelper.showLoading(getContext());
        new RetrofitClient()
                .callAPI()
                .returnOrder(
                        order.get_id(),
                        "Bearer " + new SharedPrefHelper(getContext()).getToken()
                ).enqueue(new Callback<ApiResponse<Order>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                        if (response.isSuccessful() && response.body().getStatus() == 200) {
                            order.setReturn_request(true);
                            purchasedOrdersAdapter.UpdateItem(order);
                            SuccessMessageBottomSheet bottomSheet = SuccessMessageBottomSheet.newInstance(getString(R.string.return_request_success));
                            bottomSheet.show(getParentFragmentManager(), "SuccessMessageBottomSheet");
                            new SharedPrefHelper(getContext()).setBooleanState(CANCELED_KEY,true);
                        }
                        ProgressDialogHelper.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                        ProgressDialogHelper.hideLoading();
                    }
                });
    }

    private void InitUI(View view) {
        rcv_order = view.findViewById(R.id.rcv_order);
        layout_empty = view.findViewById(R.id.layout_empty);
    }
}