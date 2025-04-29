package com.md09.pharmapoly.ui.view.activity;

import static com.md09.pharmapoly.utils.Constants.NOTIFICATION_READ_ALL_KEY;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.md09.pharmapoly.Adapters.NotificationAdapter;
import com.md09.pharmapoly.Models.Notification;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.ProgressDialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayout layout_notification_empty;
    private NotificationAdapter adapter;
    private List<Notification> notificationList = new ArrayList<>();
    private ImageView btnBack;
    private TextView read_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);

        InitUI();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this, notificationList);
        recyclerView.setAdapter(adapter);

        read_all.setOnClickListener(v -> {
            if (!hasUnreadNotifications()) {
                Toast.makeText(this, "Tất cả thông báo đã được đọc", Toast.LENGTH_SHORT).show();
                return;
            }
            ReadAll();
        });
        btnBack.setOnClickListener(v -> finish());

        GetNotification();
    }
    private boolean hasUnreadNotifications() {
        for (Notification noti : notificationList) {
            if (!noti.isIs_read()) {
                return true;
            }
        }
        return false;
    }
    private void ReadAll() {
        ProgressDialogHelper.showLoading(this);
        new RetrofitClient()
                .callAPI()
                .notificationReadAll("Bearer " + new SharedPrefHelper(this).getToken())
                .enqueue(new Callback<ApiResponse<List<Notification>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Notification>>> call, Response<ApiResponse<List<Notification>>> response) {
                        ProgressDialogHelper.hideLoading();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                List<Notification> notifications = response.body().getData();
                                adapter.Update(notifications);
                                if (notifications.size() > 0) {
                                    layout_notification_empty.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Notification>>> call, Throwable t) {
                        ProgressDialogHelper.hideLoading();
                    }
                });
    }

    private void GetNotification() {
        ProgressDialogHelper.showLoading(this);
        new RetrofitClient()
                .callAPI()
                .getNotification("Bearer " + new SharedPrefHelper(this).getToken())
                .enqueue(new Callback<ApiResponse<List<Notification>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Notification>>> call, Response<ApiResponse<List<Notification>>> response) {
                        ProgressDialogHelper.hideLoading();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                List<Notification> notifications = response.body().getData();
                                adapter.Update(notifications);
                                if (notifications.size() > 0) {
                                    layout_notification_empty.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                                new SharedPrefHelper(NotificationsActivity.this).setBooleanState(NOTIFICATION_READ_ALL_KEY,true);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Notification>>> call, Throwable t) {
                        ProgressDialogHelper.hideLoading();
                    }
                });
    }

    private void InitUI() {
        btnBack = findViewById(R.id.btn_back);
        read_all = findViewById(R.id.read_all);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);
        layout_notification_empty = findViewById(R.id.layout_notification_empty);
        layout_notification_empty.setVisibility(View.VISIBLE);
    }
}
