package com.md09.pharmapoly.ui.view.fragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.md09.pharmapoly.utils.Constants.NOTIFICATION_READ_ALL_KEY;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.md09.pharmapoly.Adapters.CategoryAdapter;
import com.md09.pharmapoly.Adapters.CategoryListAdapter;
import com.md09.pharmapoly.Adapters.ProductAdapter;
import com.md09.pharmapoly.Adapters.SliderAdapter;
import com.md09.pharmapoly.MedicineReminderActivity;
import com.md09.pharmapoly.Models.Category;
import com.md09.pharmapoly.Models.ItemCategoryHome;
import com.md09.pharmapoly.Models.PageData;
import com.md09.pharmapoly.Models.PageDataClone;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.data.model.User;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.ui.view.activity.CancerInfoActivity;
import com.md09.pharmapoly.ui.view.activity.ChatbotActivity;
import com.md09.pharmapoly.ui.view.activity.CustomTypefaceSpan;
import com.md09.pharmapoly.ui.view.activity.MainActivity;
import com.md09.pharmapoly.ui.view.activity.Nav_FunctionalFoodActivity;
import com.md09.pharmapoly.ui.view.activity.Nav_Medical_Equiment;
import com.md09.pharmapoly.ui.view.activity.Nav_Medicine;
import com.md09.pharmapoly.ui.view.activity.Nav_Personalcare;
import com.md09.pharmapoly.ui.view.activity.Nav_Pharmaceutical_Cosmetics;
import com.md09.pharmapoly.ui.view.activity.NotificationsActivity;
import com.md09.pharmapoly.ui.view.activity.PharmacyMapActivity;
import com.md09.pharmapoly.ui.view.activity.SearchActivity;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.md09.pharmapoly.viewmodel.CartViewModel;
import com.md09.pharmapoly.ui.view.activity.OrderManagementActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ProductAdapter productAdapterMostReview;
    private ProductAdapter productAdapterTopRate;
    private List<Product> productList;
    private List<Product> productListMostReview;
    private List<Product> productListTopRate;
    private String token;
    private RecyclerView recyclerViewCategory;
    private CategoryAdapter categoryAdapter;
    private List<ItemCategoryHome> categoryList;
    private DrawerLayout drawerLayout;

    private ViewPager2 viewPager2;
    private SliderAdapter sliderAdapter;
    private CircleIndicator3 circleIndicator;
    private SharedPrefHelper sharedPrefHelper;
    private RetrofitClient retrofitClient;
    private CartViewModel cartViewModel;

    private RecyclerView rvTopRate;

    private RecyclerView rvMostReview;

    private int maxRow = 2;
    private LinearLayout lnNoiBat;
    private LinearLayout lnTopRate;
    private LinearLayout lnMostReview;
    private CategoryListAdapter categoryListAdapter;

    private LinearLayout
            btnthucphamchucnang,
            btnduocmypham,
            btnthuoc,
            btnchamsoc,
            btnthietbiyte,
            btnthuocbovitamin,
            layout_search;
    private TextView txt_greeting;
    private RelativeLayout layout_badge_notification;
    private TextView tv_badge_notification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        InitUI(view);
        token = sharedPrefHelper.getToken();
        System.out.println("token" + token);

        SetupRecyclerView(view);
        GetTopRatedProducts();
        GetMostReviewProducts();

        List<Integer> sliderImageIds = new ArrayList<>();
        sliderImageIds.add(R.drawable.bn1);
        sliderImageIds.add(R.drawable.bn2);
        sliderImageIds.add(R.drawable.bn3);
        sliderImageIds.add(R.drawable.bn4);
        sliderImageIds.add(R.drawable.bn5);
        sliderImageIds.add(R.drawable.bn6);

        sliderAdapter = new SliderAdapter(sliderImageIds);
        viewPager2.setAdapter(sliderAdapter);

        circleIndicator.setViewPager(viewPager2);

        drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        ImageView menuIcon = view.findViewById(R.id.menu_icon);
        NavigationView navigationView = requireActivity().findViewById(R.id.navigation_view);
        Typeface customFont = ResourcesCompat.getFont(requireContext(), R.font.be_vietnam_pro_bold);
        navigationView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));

        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SpannableString s = new SpannableString(menuItem.getTitle());
            s.setSpan(new CustomTypefaceSpan("", customFont), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            s.setSpan(new AbsoluteSizeSpan(14, true), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); // 18sp
            menuItem.setTitle(s);
        }


//        ImageView bellIcon = view.findViewById(R.id.bell_icon);
        RelativeLayout bellIcon = view.findViewById(R.id.bell_icon);
        bellIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NotificationsActivity.class);
            startActivity(intent);
        });


        // Khởi tạo RecyclerView và danh sách category
        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(getContext(), 4));

        categoryList = new ArrayList<>();

        // Thêm các danh mục vào categoryList dưới dạng ItemCategoryHome
        categoryList.add(new ItemCategoryHome("Đơn của tôi", R.drawable.ic_doncuatoi));
        categoryList.add(new ItemCategoryHome("Tìm nhà thuốc", R.drawable.ic_timnhathuoc));
        categoryList.add(new ItemCategoryHome("Nhắc uống thuốc", R.drawable.ic_nhacuongthuoc));

        categoryListAdapter = new CategoryListAdapter(getContext(), categoryList); // Sử dụng ItemCategoryHome ở đây
        recyclerViewCategory.setAdapter(categoryListAdapter);

        // Xử lý sự kiện click vào category
        categoryListAdapter.setOnCategoryClickListener(category -> {
            if (category.getName().equals("Tìm nhà thuốc")) {
                Intent intent = new Intent(getContext(), PharmacyMapActivity.class);
                startActivity(intent);
            } else if (category.getName().equals("Nhắc uống thuốc")) {
                Intent intent = new Intent(getContext(), MedicineReminderActivity.class);
                startActivity(intent);
            } else if (category.getName().equals("Đơn của tôi")) {
                Intent intent = new Intent(getActivity(), OrderManagementActivity.class);
                intent.putExtra("order_status", 0); // 0 là trạng thái "Đơn của tôi" (đang xử lý)
                startActivity(intent);
            }
        });

        // Sửa sự kiện mở Drawer để luôn gọi DrawerLayout của MainActivity
        menuIcon.setOnClickListener(v -> {
            drawerLayout.openDrawer(androidx.core.view.GravityCompat.START);
        });
        navigationView.setNavigationItemSelectedListener(item -> {
            Log.d("HomeFragment", "btnthucphamchucnang clicked");
            int id = item.getItemId();
            if (id == R.id.nav_notification) {
                Toast.makeText(getContext(), "Thông báo", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), NotificationsActivity.class));
                return true;
            } else if (id == R.id.nav_healthcare) {
                Log.d("HomeFragment", "btnthucphamchucnang clicked");
                Toast.makeText(getContext(), "Thực phẩm chức năng", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Nav_FunctionalFoodActivity.class));
                return true;
            } else if (id == R.id.nav_pharmacy) {
                Toast.makeText(getContext(), "Dược mỹ phẩm", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Nav_Pharmaceutical_Cosmetics.class));
                return true;
            } else if (id == R.id.nav_medicine) {
                Toast.makeText(getContext(), "Thuốc", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Nav_Medicine.class));
                return true;
            } else if (id == R.id.nav_medical_equipment) {
                Toast.makeText(getContext(), "Thiết bị y tế", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Nav_Medical_Equiment.class));
                return true;
            } else if (id == R.id.nav_personalcare) {
                Toast.makeText(getContext(), "Chăm sóc cá nhân", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Nav_Personalcare.class));
                return true;
            } else if (id == R.id.nav_chatbot) {
                Toast.makeText(getContext(), "ChatBot", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), ChatbotActivity.class));
                return true;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
        // Lấy ImageView từ layout
        ImageView circleImage = view.findViewById(R.id.circleImage);
        // Set OnClickListener cho ImageView
        circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến Activity mới khi nhấn vào cục tròn ảnh
                Intent intent = new Intent(getActivity(), ChatbotActivity.class);
                startActivity(intent);
            }
        });
        // Trong phương thức onCreateView của HomeFragment
        //TextView textViewSearch = view.findViewById(R.id.textViewSearch);
        layout_search.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivity(intent);
        });

        btnthucphamchucnang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HomeFragment", "btnthucphamchucnang clicked");
                Intent intent = new Intent(getContext(), Nav_FunctionalFoodActivity.class); // Chuyển tới Activity bạn muốn
                startActivity(intent);
            }
        });
        btnduocmypham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HomeFragment", "btnduocmypham clicked");
                Intent intent = new Intent(getContext(), Nav_Pharmaceutical_Cosmetics.class); // Chuyển tới Activity bạn muốn
                startActivity(intent);
            }
        });
        btnthuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HomeFragment", "btnthuoc clicked");
                Intent intent = new Intent(getContext(), Nav_Medicine.class); // Chuyển tới Activity bạn muốn
                startActivity(intent);
            }
        });
        btnchamsoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Nav_Personalcare.class); // Chuyển tới Activity bạn muốn
                startActivity(intent);
            }
        });
        btnthietbiyte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Nav_Medical_Equiment.class); // Chuyển tới Activity bạn muốn
                startActivity(intent);
            }
        });
        GetNotificationUnreadCount();
        return view;

    }


    //    @Override
    //    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        super.onActivityResult(requestCode, resultCode, data);
    //
    //        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
    //            boolean isProductAdded = data.getBooleanExtra("isProductAdded", false);
    //            Toast.makeText(getContext(), isProductAdded + "", Toast.LENGTH_SHORT).show();
    //            if (isProductAdded) {
    //                Toast.makeText(getContext(), "Check A", Toast.LENGTH_SHORT).show();
    //                cartViewModel.FetchCartData(getContext());
    //            } else {
    //                Toast.makeText(getContext(), "Check B", Toast.LENGTH_SHORT).show();
    //
    //            }
    //        }
    //    }


    @Override
    public void onResume() {
        super.onResume();
        GetNotificationUnreadCount();
    }

    private void GetNotificationUnreadCount() {
        new RetrofitClient()
                .callAPI()
                .notificationUnreadCount("Bearer " + new SharedPrefHelper(getContext()).getToken())
                .enqueue(new Callback<ApiResponse<Integer>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                        if (response.isSuccessful() && response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                int unreadCount = response.body().getData();
                                if (unreadCount == 0) {
                                    layout_badge_notification.setVisibility(GONE);
                                } else {
                                    layout_badge_notification.setVisibility(VISIBLE);
                                    tv_badge_notification.setText(String.valueOf(unreadCount));
                                }
                                new SharedPrefHelper(getContext()).resetBooleanState(NOTIFICATION_READ_ALL_KEY);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {

                    }
                });
    }

    private void SetupRecyclerView(View view) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2); // Hiển thị 2 cột
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new SpaceItemDecoration(20)); // Thêm khoảng cách giữa các item
//        recyclerView.setAdapter(adapter);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(productAdapter);
    }

    private void GetTopRatedProducts() {
        retrofitClient.callAPI().getTopRatedProducts(1, 10, "Bearer " + token).enqueue(new Callback<ApiResponse<PageData<List<Product>>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageData<List<Product>>>> call, Response<ApiResponse<PageData<List<Product>>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        PageData<List<Product>> page = response.body().getData();
                        List<Product> data = response.body().getData().getData();
                        if (data == null || data.isEmpty()) {
                            lnTopRate.setVisibility(GONE);
                            lnNoiBat.setVisibility(GONE);
                            return;
                        }
                        lnTopRate.setVisibility(VISIBLE);
                        lnNoiBat.setVisibility(VISIBLE);

                        productList = (page.getData().size() >= 3 * maxRow) ? page.getData().subList(0, 3 * maxRow) : page.getData();
                        productAdapter.Update(productList);

                        Collections.shuffle(data);
                        productListTopRate = (data.size() >= 3 * maxRow) ? data.subList(0, 3 * maxRow) : data;
                        productAdapterTopRate = new ProductAdapter(getContext(), productListTopRate);
                        rvTopRate.setAdapter(productAdapterTopRate);

                        // Sử dụng LinearLayoutManager với chế độ cuộn ngang
                        rvTopRate.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageData<List<Product>>>> call, Throwable t) {
                // Xử lý lỗi ở đây
            }
        });
    }


    private void GetMostReviewProducts() {
        retrofitClient.callAPI().getMostReviewProducts(1, 10, "Bearer " + token).enqueue(new Callback<ApiResponse<PageDataClone<List<Product>>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageDataClone<List<Product>>>> call, Response<ApiResponse<PageDataClone<List<Product>>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        List<Product> data = response.body().getData().getData();
                        if (data == null || data.isEmpty()) {
                            lnMostReview.setVisibility(GONE);
                            return;
                        }
                        lnMostReview.setVisibility(VISIBLE);
                        productListMostReview = (data.size() >= 5 * maxRow) ? data.subList(0, 5 * maxRow) : data;
                        productAdapterMostReview = new ProductAdapter(getContext(), productListMostReview);
                        rvMostReview.setAdapter(productAdapterMostReview);
                        rvMostReview.setLayoutManager(new GridLayoutManager(getContext(), 2));

                        // Sử dụng LinearLayoutManager với chế độ cuộn ngang
                        rvMostReview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageDataClone<List<Product>>>> call, Throwable t) {

            }
        });
    }

    private void InitUI(View view) {
        retrofitClient = new RetrofitClient();

        tv_badge_notification = view.findViewById(R.id.tv_badge_notification);
        layout_badge_notification = view.findViewById(R.id.layout_badge_notification);

        sharedPrefHelper = new SharedPrefHelper(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        viewPager2 = view.findViewById(R.id.viewPagerSlider);
        circleIndicator = view.findViewById(R.id.dotsIndicator);
        rvTopRate = view.findViewById(R.id.rvTopRate);
        rvMostReview = view.findViewById(R.id.rvMostReview);
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        lnNoiBat = view.findViewById(R.id.lnNoiBat);
        lnTopRate = view.findViewById(R.id.lnTopRate);
        lnMostReview = view.findViewById(R.id.lnMostReview);

        btnthucphamchucnang = view.findViewById(R.id.btnthucphamchucnang);
        btnduocmypham = view.findViewById(R.id.btnduocmypham);
        btnthuoc = view.findViewById(R.id.btnthuoc);
        btnchamsoc = view.findViewById(R.id.btnchamsoc);
        btnthietbiyte = view.findViewById(R.id.btnthietbiyte);
        btnthuocbovitamin = view.findViewById(R.id.btnthuocbovitamin);
        layout_search = view.findViewById(R.id.layout_search);

        // Add click handler for cancer info button
        view.findViewById(R.id.btn_cancer_info).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CancerInfoActivity.class);
            startActivity(intent);
        });

        txt_greeting = view.findViewById(R.id.txt_greeting);
        User user = new SharedPrefHelper(getContext()).getUser();
        if (user.getFull_name() != null && !user.getFull_name().isEmpty()) {
            txt_greeting.setText(getString(R.string.hi) + ", " + user.getFull_name());
        } else {
            txt_greeting.setText(getString(R.string.hi));
        }
    }
}