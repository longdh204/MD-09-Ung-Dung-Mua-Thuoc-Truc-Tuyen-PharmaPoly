package com.md09.pharmapoly.ui.view.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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
    private TextView
            //productName,
            shortDescription,
            specification,
            originCountry,
            manufacturer,
            category;
            //brand,
            //averageRating,
            //reviewCount,
            //productBrandDescription,
            //productTypeName,
            //productSections;
    private Product product;
    private ImageButton btn_back;
    private LinearLayout layout_section;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.product_detailall);

        // Ánh xạ các TextView từ layout
        InitUI();

        product = (Product) getIntent().getSerializableExtra("product");

        List<ProductSection> sections = (List<ProductSection>) getIntent().getSerializableExtra("sections");

        if (sections != null) {
            layout_section.removeAllViews();

            for (ProductSection section : sections) {
                View view = LayoutInflater.from(this).inflate(R.layout.item_section,null,false);
                TextView tv_section_name = view.findViewById(R.id.tv_section_name);
                LinearLayout layout_section_detail = view.findViewById(R.id.layout_section_detail);
                ImageView btn_show_more = view.findViewById(R.id.btn_show_more);


                btn_show_more.setOnClickListener(v -> {
                    if (layout_section_detail.getVisibility() == View.VISIBLE) {
                        layout_section_detail.animate().scaleY(0).setDuration(300).withEndAction(() -> {
                            layout_section_detail.setVisibility(View.GONE);
                        }).start();
                        btn_show_more.animate().rotation(0).setDuration(300).start();
                    } else {
                        layout_section_detail.setVisibility(View.VISIBLE);
                        layout_section_detail.setScaleY(0);
                        layout_section_detail.animate().scaleY(1).setDuration(300).start();
                        btn_show_more.animate().rotation(90).setDuration(300).start();
                    }
                });

                tv_section_name.setText(section.getSection().getName());
                for (ProductSectionDetail detail : section.getDetails()) {
                    layout_section_detail.addView(createDetailTitle(detail.getTitle()));

                    layout_section_detail.addView(createDetailContent(detail.getContent()));
                }
                layout_section.addView(view);
            }
        }
        // Kiểm tra nếu đối tượng product không phải null
        if (product != null) {
            //productName.setText(product.getName());
            shortDescription.setText(product.getShort_description());
            specification.setText(product.getSpecification());
            originCountry.setText(product.getOrigin_country());
            manufacturer.setText(product.getManufacturer());
            category.setText(product.getCategory().getName());
//            brand.setText(product.getBrand().getName());
//            averageRating.setText(String.valueOf(product.getAverage_rating()));
//            reviewCount.setText(String.valueOf(product.getReview_count()));

            // Hiển thị thêm thông tin
//            productBrandDescription.setText("Brand Description: " + brandDescription);
//            productTypeName.setText("Product Type: " + productTypeNameText);


        } else {
            Log.d("ProductDetailAllActivity", "Product is null");
        }
        btn_back.setOnClickListener(v -> {
            finish();
        });
    }

    private TextView createDetailTitle(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(18);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.be_vietnam_pro_medium);
        textView.setTypeface(typeface, Typeface.BOLD_ITALIC);
        //textView.setPadding(16, 8, 16, 4);
        return textView;
    }

    private TextView createDetailContent(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(16);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.be_vietnam_pro_medium);
        textView.setTypeface(typeface, Typeface.NORMAL);
        //textView.setPadding(0, 4, 16, 8);
        return textView;
    }

    private void InitUI() {
        btn_back = findViewById(R.id.btn_back);
        //productName = findViewById(R.id.productName);
        shortDescription = findViewById(R.id.shortDescription);
        specification = findViewById(R.id.specification);
        originCountry = findViewById(R.id.originCountry);
        manufacturer = findViewById(R.id.manufacturer);
        category = findViewById(R.id.category);
        layout_section = findViewById(R.id.layout_section);
//        brand = findViewById(R.id.brand);
//        averageRating = findViewById(R.id.averageRating);
//        reviewCount = findViewById(R.id.reviewCount);
        //productBrandDescription = findViewById(R.id.productBrandDescription);
//        productTypeName = findViewById(R.id.productTypeName);
//        productSections = findViewById(R.id.productSections);
    }
}



