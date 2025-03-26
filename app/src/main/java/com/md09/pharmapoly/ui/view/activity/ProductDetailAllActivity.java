package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.Models.ProductSection;
import com.md09.pharmapoly.Models.ProductSectionDetail;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailAllActivity extends AppCompatActivity {
    private TextView productName, shortDescription, specification, originCountry, manufacturer, category, brand, averageRating, reviewCount;
    private TextView productBrandDescription, productTypeName, productSections;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.product_detailall);

        // Ánh xạ các TextView từ layout
        productName = findViewById(R.id.productName);
        shortDescription = findViewById(R.id.shortDescription);
        specification = findViewById(R.id.specification);
        originCountry = findViewById(R.id.originCountry);
        manufacturer = findViewById(R.id.manufacturer);
        category = findViewById(R.id.category);
        brand = findViewById(R.id.brand);
        averageRating = findViewById(R.id.averageRating);
        reviewCount = findViewById(R.id.reviewCount);
        productBrandDescription = findViewById(R.id.productBrandDescription);
        productTypeName = findViewById(R.id.productTypeName);
        productSections = findViewById(R.id.productSections);

        // Lấy đối tượng product từ Intent
        product = (Product) getIntent().getSerializableExtra("product");
        String brandDescription = getIntent().getStringExtra("brand_description");
        String productTypeNameText = getIntent().getStringExtra("product_type_name");
        List<ProductSection> sections = (List<ProductSection>) getIntent().getSerializableExtra("sections");

        // Kiểm tra nếu đối tượng product không phải null
        if (product != null) {
            productName.setText(product.getName());
            shortDescription.setText(product.getShort_description());
            specification.setText(product.getSpecification());
            originCountry.setText(product.getOrigin_country());
            manufacturer.setText(product.getManufacturer());
            category.setText(product.getCategory().getName());
            brand.setText(product.getBrand().getName());
            averageRating.setText(String.valueOf(product.getAverage_rating()));
            reviewCount.setText(String.valueOf(product.getReview_count()));

            // Hiển thị thêm thông tin
            productBrandDescription.setText("Brand Description: " + brandDescription);
            productTypeName.setText("Product Type: " + productTypeNameText);

            // Hiển thị các sections
            if (sections != null && !sections.isEmpty()) {
                StringBuilder sectionText = new StringBuilder();
                for (ProductSection section : sections) {
                    sectionText.append("Section: ").append(section.getSection().getName()).append("\n");
                    for (ProductSectionDetail detail : section.getDetails()) {
                        sectionText.append(detail.getTitle()).append(": ").append(detail.getContent()).append("\n");
                    }
                }
                productSections.setText(sectionText.toString());
            }
        } else {
            Log.d("ProductDetailAllActivity", "Product is null");
        }
    }
}



