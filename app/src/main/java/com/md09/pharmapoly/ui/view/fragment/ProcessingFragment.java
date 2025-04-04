package com.md09.pharmapoly.ui.view.fragment;

import static com.md09.pharmapoly.utils.Constants.CANCELED_KEY;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
 * Use the {@link ProcessingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProcessingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProcessingFragment() {
        // Required empty public constructor
    }


    public static ProcessingFragment newInstance(String param1, String param2) {
        ProcessingFragment fragment = new ProcessingFragment();
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

        View view = inflater.inflate(R.layout.fragment_processing, container, false);

        InitUI(view);
        ProgressDialogHelper.showLoading(getContext());
        purchasedOrdersAdapter = new PurchasedOrdersAdapter(
                getContext(),
                null,
                Constants.OrderStatusGroup.PROCESSING,
                new PurchasedOrdersAdapter.OrderActionListener() {
                    @Override
                    public void onCancelOrder(Order order) {
                        CancelOrder(order);
                    }

//                    @Override
//                    public void onReturnOrExchangeOrder(Order order) {
//
//                    }
//
//                    @Override
//                    public void onConfirmReceived(Order order) {
//
//                    }
                });
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        rcv_order.setLayoutManager(layoutManager);
        rcv_order.setAdapter(purchasedOrdersAdapter);

        new RetrofitClient()
                .callAPI()
                .getOrders(
                        Constants.OrderStatusGroup.PROCESSING.toString().toLowerCase(),
                        "Bearer " + new SharedPrefHelper(getContext()).getToken()
                )
                .enqueue(new Callback<ApiResponse<List<Order>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Order>>> call, Response<ApiResponse<List<Order>>> response) {
                        if (response.isSuccessful() && response.body().getStatus() == 200) {
                            List<Order> orders = response.body().getData();
                            ProcessingFragment.this.orders = orders;

                            if (orders != null && orders.size() > 0) {
                                purchasedOrdersAdapter.Update(response.body().getData());
                                layout_empty.setVisibility(View.GONE);
                                rcv_order.setVisibility(View.VISIBLE);
                            } else {
                                layout_empty.setVisibility(View.VISIBLE);
                                rcv_order.setVisibility(View.GONE);
                            }
                        }
                        ProgressDialogHelper.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Order>>> call, Throwable t) {
                        ProgressDialogHelper.hideLoading();
                        Log.e("Check Error",t.getMessage());
//                        Log.e("Check Error",t.);
                    }
                });
        return view;
    }

    private void CancelOrder(Order order) {
        ProgressDialogHelper.showLoading(getContext());
        new RetrofitClient()
                .callAPI()
                .cancelOrder(
                        order.get_id(),
                        "Bearer " + new SharedPrefHelper(getContext()).getToken()
                )
                .enqueue(new Callback<ApiResponse<Order>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                        if (response.isSuccessful() && response.body().getStatus() == 200) {
                            order.setCancel_request(true);
                            purchasedOrdersAdapter.UpdateItem(order);
                            SuccessMessageBottomSheet bottomSheet = SuccessMessageBottomSheet.newInstance(getString(R.string.order_cancelled_success));
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

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        Re
//    }

    private void InitUI(View view) {
        rcv_order = view.findViewById(R.id.rcv_order);
        layout_empty = view.findViewById(R.id.layout_empty);
    }
}