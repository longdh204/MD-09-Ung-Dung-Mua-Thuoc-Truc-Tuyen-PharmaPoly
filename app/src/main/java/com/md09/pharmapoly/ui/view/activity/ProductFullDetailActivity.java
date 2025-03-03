package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Adapters.ProductSectionAdapter;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFullDetailActivity extends AppCompatActivity {
    private TextView productName, productPrice, productDescription, productManufacturer, productOrigin,
            productCategory, productBrand, productShortDescription;
    private RecyclerView productSectionsRecyclerView;
    private ProductSectionAdapter sectionAdapter;
    private RetrofitClient retrofitClient;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_full_detail_activity);

        // Ánh xạ các view
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        productManufacturer = findViewById(R.id.productManufacturer);
        productOrigin = findViewById(R.id.productOrigin);
        productCategory = findViewById(R.id.productCategory);
        productBrand = findViewById(R.id.productBrand);
        productShortDescription = findViewById(R.id.productShortDescription);

        productSectionsRecyclerView = findViewById(R.id.productSectionsRecyclerView);
        productSectionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sectionAdapter = new ProductSectionAdapter(this, new ArrayList<>());
        productSectionsRecyclerView.setAdapter(sectionAdapter);

        // Lấy ID sản phẩm từ Intent
        productId = getIntent().getStringExtra("product_id");

        // Khởi tạo Retrofit client
        retrofitClient = new RetrofitClient();

        // Lấy thông tin chi tiết sản phẩm
        fetchProductDetails(productId);
    }

    private void fetchProductDetails(String productId) {
        retrofitClient.callAPI().getProductDetails(productId, "Bearer " + new SharedPrefHelper(this).getToken())
                .enqueue(new Callback<ApiResponse<Product>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Product product = response.body().getData();
                            displayProductDetails(product);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                        Toast.makeText(ProductFullDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayProductDetails(Product product) {
        productName.setText(product.getName());
        productPrice.setText(getString(product.getPrice())); //R.string.currency,
        productDescription.setText(product.getShort_description());
        productManufacturer.setText(product.getManufacturer());
        productOrigin.setText(product.getOrigin_country());
        productCategory.setText(product.getCategory().getName());
        productBrand.setText(product.getBrand().getName());
        productShortDescription.setText(product.getSpecification());

        // Cập nhật các section trong RecyclerView
        sectionAdapter.updateSections(product.getSections());
    }
}

