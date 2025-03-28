package com.md09.pharmapoly.ui.view.fragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.md09.pharmapoly.Adapters.CategoryAdapter;
import com.md09.pharmapoly.Adapters.ProductAdapter;
import com.md09.pharmapoly.Adapters.SliderAdapter;
import com.md09.pharmapoly.Models.Category;
import com.md09.pharmapoly.Models.PageData;
import com.md09.pharmapoly.Models.PageDataClone;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.ui.view.activity.ChatbotActivity;
import com.md09.pharmapoly.ui.view.activity.Nav_FunctionalFoodActivity;
import com.md09.pharmapoly.ui.view.activity.Nav_Medical_Equiment;
import com.md09.pharmapoly.ui.view.activity.Nav_Medicine;
import com.md09.pharmapoly.ui.view.activity.Nav_Personalcare;
import com.md09.pharmapoly.ui.view.activity.Nav_Pharmaceutical_Cosmetics;
import com.md09.pharmapoly.ui.view.activity.NotificationsActivity;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.ui.view.activity.SearchActivity;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.md09.pharmapoly.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.Collection;
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
    private List<Category> categoryList;
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

        drawerLayout = view.findViewById(R.id.drawer_layout);
        ImageView menuIcon = view.findViewById(R.id.menu_icon);
        NavigationView navigationView = view.findViewById(R.id.navigation_view);

        ImageView bellIcon = view.findViewById(R.id.bell_icon);
        bellIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NotificationsActivity.class);
            startActivity(intent);
        });

        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(getContext(), 4));

        categoryList = new ArrayList<>();
        categoryList.add(new Category("Cần mua thuốc", R.drawable.ic_medicine));
        categoryList.add(new Category("Khám bác sĩ", R.drawable.ic_doctor));
        categoryList.add(new Category("Bảo hiểm", R.drawable.ic_insurance));
        categoryList.add(new Category("Thông tin", R.drawable.ic_calendar_2));
        categoryList.add(new Category("Ghi chú", R.drawable.ic_list1));
        categoryList.add(new Category("Đơn thuốc", R.drawable.ic_list2));
        categoryList.add(new Category("Lịch hẹn", R.drawable.ic_list3));

        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        recyclerViewCategory.setAdapter(categoryAdapter);

        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_notification) {
                Toast.makeText(getContext(), "Thông báo", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), NotificationsActivity.class));
                return true;
            } else if (id == R.id.nav_healthcare) {
                Toast.makeText(getContext(), "Thực phẩm chức năng", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Nav_FunctionalFoodActivity.class));
                return true;
            } else if (id == R.id.nav_pharmacy) {
                Toast.makeText(getContext(), "Dược mỹ phẩm", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Nav_Pharmaceutical_Cosmetics.class));
                return true;
            }else if (id == R.id.nav_medicine) {
                Toast.makeText(getContext(), "Thuốc", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Nav_Medicine.class));
                return true;
            }else if (id == R.id.nav_medical_equipment) {
                Toast.makeText(getContext(), "Thiết bị y tế", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Nav_Medical_Equiment.class));
                return true;
            }else if (id == R.id.nav_personalcare) {
                Toast.makeText(getContext(), "Chăm sóc cá nhân", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Nav_Personalcare.class));
                return true;
            }else if (id == R.id.nav_chatbot) {
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
        TextView textViewSearch = view.findViewById(R.id.textViewSearch);

        textViewSearch.setOnClickListener(v -> {
            // Khi nhấn vào TextView, chuyển sang màn hình tìm kiếm
            Intent intent = new Intent(getContext(), SearchActivity.class); // Chuyển tới Activity bạn muốn
            startActivity(intent);
        });


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

    private void SetupRecyclerView(View view) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2); // Hiển thị 2 cột
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new SpaceItemDecoration(20)); // Thêm khoảng cách giữa các item
//        recyclerView.setAdapter(adapter);

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
                        if(data == null || data.isEmpty()) {
                            lnTopRate.setVisibility(GONE);
                            lnNoiBat.setVisibility(GONE);
                            return;
                        }
                        lnTopRate.setVisibility(VISIBLE);
                        lnNoiBat.setVisibility(VISIBLE);

                        productList = (page.getData().size() >= 2*maxRow) ? page.getData().subList(0,2*maxRow) : page.getData() ;
                        productAdapter.Update(productList);

                        Collections.shuffle(data);
                        productListTopRate = (data.size() >= 2*maxRow) ? data.subList(0,2*maxRow) : data;
                        productAdapterTopRate = new ProductAdapter(getContext(),productListTopRate);
                        rvTopRate.setAdapter(productAdapterTopRate);
                        rvTopRate.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageData<List<Product>>>> call, Throwable t) {

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
                        if(data == null || data.isEmpty()) {
                            lnMostReview.setVisibility(GONE);
                            return;
                        }
                        lnMostReview.setVisibility(VISIBLE);
                        productListMostReview = (data.size() >= 2*maxRow) ? data.subList(0,2*maxRow) : data;
                        productAdapterMostReview = new ProductAdapter(getContext(),productListMostReview);
                        rvMostReview.setAdapter(productAdapterMostReview);
                        rvMostReview.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
    }
}