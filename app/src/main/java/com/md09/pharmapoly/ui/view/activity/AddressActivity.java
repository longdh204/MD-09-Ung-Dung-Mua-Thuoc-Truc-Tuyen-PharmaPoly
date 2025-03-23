package com.md09.pharmapoly.ui.view.activity;

import static com.md09.pharmapoly.utils.Constants.findObjectById;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.md09.pharmapoly.Adapters.SpinnerDistrictAdapter;
import com.md09.pharmapoly.Adapters.SpinnerProvinceAdapter;
import com.md09.pharmapoly.Adapters.SpinnerWardAdapter;
import com.md09.pharmapoly.Models.District;
import com.md09.pharmapoly.Models.DistrictRequest;
import com.md09.pharmapoly.Models.UserAddress;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.data.model.User;
import com.md09.pharmapoly.network.GHNRequest;
import com.md09.pharmapoly.Models.GHNResponse;
import com.md09.pharmapoly.Models.Province;
import com.md09.pharmapoly.Models.Ward;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.PopupHelper;
import com.md09.pharmapoly.utils.ProgressDialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.md09.pharmapoly.utils.SuccessMessageBottomSheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity {
    private EditText edt_myAddress;
    private Spinner spinner_province,spinner_district,spinner_ward;
    private SpinnerProvinceAdapter spinnerProvinceAdapter;
    private SpinnerDistrictAdapter spinnerDistrictAdapter;
    private SpinnerWardAdapter spinnerWardAdapter;
    private ImageButton btn_back;
    private GHNRequest ghnRequest;
    private UserAddress userAddress;
    private Province getProvince = null;
    private District getDistrict = null;
    private Ward getWard = null;
    private ArrayList<Province> provinces;
    private ArrayList<District> districts;
    private ArrayList<Ward> wards;
    private CardView btn_update_address;
    private TextView profile_update_complete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        ProgressDialogHelper.showLoading(this);

        InitUI();
        userAddress = new SharedPrefHelper(this).getUser().getAddress();

        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.spinner_province) {
                    Province province = (Province)parent.getAdapter().getItem(position);
                    getProvince = province;
                    DistrictRequest districtRequest = new DistrictRequest(province.getProvinceID());
                    ghnRequest.callAPI().getListDistrict(districtRequest).enqueue(getDistricts);
                } else if (parent.getId() == R.id.spinner_district) {
                    District district = (District) parent.getAdapter().getItem(position);
                    getDistrict = district;
                    ghnRequest.callAPI().getListWard(district.getDistrictID()).enqueue(getWards);
                } else if (parent.getId() == R.id.spinner_ward) {
                    Ward ward = (Ward) parent.getAdapter().getItem(position);
                    getWard = ward;
                }
                updateButtonState();
                //FillAddress();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner_province.setOnItemSelectedListener(onItemSelectedListener);
        spinner_district.setOnItemSelectedListener(onItemSelectedListener);
        spinner_ward.setOnItemSelectedListener(onItemSelectedListener);
        if (userAddress != null) {
            edt_myAddress.setText(userAddress.getStreet_address());
        }
        ghnRequest.callAPI().getListProvince().enqueue(getProvinces);

        btn_back.setOnClickListener(v -> {
            finish();
        });
        edt_myAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateButtonState();
            }
        });
        btn_update_address.setOnClickListener(v -> {
            ProgressDialogHelper.showLoading(this);
            userAddress = new UserAddress();
            userAddress.setUser_id(new SharedPrefHelper(this).getUser().get_id());
            userAddress.setProvince(getProvince);
            userAddress.setDistrict(getDistrict);
            userAddress.setWard(getWard);
            userAddress.setStreet_address(edt_myAddress.getText().toString());

            new RetrofitClient().callAPI().updateAddress(
                    userAddress,
                    "Bearer " + new SharedPrefHelper(this).getToken()
            ).enqueue(new Callback<ApiResponse<UserAddress>>() {
                @Override
                public void onResponse(Call<ApiResponse<UserAddress>> call, Response<ApiResponse<UserAddress>> response) {
                    if (response.isSuccessful() && response.body().getStatus() == 200) {
                        UserAddress updatedAddress = response.body().getData();

                        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
                        User currentUser = sharedPrefHelper.getUser();

                        if (currentUser != null) {
                            currentUser.setAddress(updatedAddress);
                            sharedPrefHelper.updateUser(currentUser);
                        }
                        if (ProgressDialogHelper.isShowing()) {
                            ProgressDialogHelper.hideLoading();
                        }
                        SuccessMessageBottomSheet bottomSheet = SuccessMessageBottomSheet.newInstance(getString(R.string.address_update_success));
                        bottomSheet.show(getSupportFragmentManager(), "SuccessMessageBottomSheet");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<UserAddress>> call, Throwable t) {
                    if (ProgressDialogHelper.isShowing()) {
                        ProgressDialogHelper.hideLoading();
                    }
                }
            });
        });
    }
    Callback<GHNResponse<ArrayList<Province>>> getProvinces = new Callback<GHNResponse<ArrayList<Province>>>() {
        @Override
        public void onResponse(Call<GHNResponse<ArrayList<Province>>> call, retrofit2.Response<GHNResponse<ArrayList<Province>>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    provinces = new ArrayList<>(response.body().getData());
                    provinces.remove(0);
                    spinnerProvinceAdapter = new SpinnerProvinceAdapter(AddressActivity.this,R.layout.item_view_spinner,provinces);
                    spinner_province.setAdapter(spinnerProvinceAdapter);
                    if (userAddress != null) {
                        int index = provinces.indexOf(findObjectById(provinces,String.valueOf(userAddress.getProvince_id())));
                        spinner_province.setSelection(index);
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<GHNResponse<ArrayList<Province>>> call, Throwable t) {
            if (ProgressDialogHelper.isShowing()) {
                ProgressDialogHelper.hideLoading();
            }
        }
    };
    Callback<GHNResponse<ArrayList<District>>> getDistricts = new Callback<GHNResponse<ArrayList<District>>>() {
        @Override
        public void onResponse(Call<GHNResponse<ArrayList<District>>> call, retrofit2.Response<GHNResponse<ArrayList<District>>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    districts = new ArrayList<>(response.body().getData());
                    //districts.remove(0);
                    spinnerDistrictAdapter = new SpinnerDistrictAdapter(AddressActivity.this,R.layout.item_view_spinner,districts);
                    spinner_district.setAdapter(spinnerDistrictAdapter);
                    if (userAddress != null &&
                            getProvince != null &&
                            getProvince.getProvinceID() == userAddress.getProvince().getProvinceID()) {
                        int index = districts.indexOf(findObjectById(districts,String.valueOf(userAddress.getDistrict().getDistrictID())));
                        spinner_district.setSelection(index);
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<GHNResponse<ArrayList<District>>> call, Throwable t) {
            if (ProgressDialogHelper.isShowing()) {
                ProgressDialogHelper.hideLoading();
            }
        }
    };
    Callback<GHNResponse<ArrayList<Ward>>> getWards = new Callback<GHNResponse<ArrayList<Ward>>>() {
        @Override
        public void onResponse(Call<GHNResponse<ArrayList<Ward>>> call, retrofit2.Response<GHNResponse<ArrayList<Ward>>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    wards = new ArrayList<>(response.body().getData());
                    //wards.remove(0);
                    spinnerWardAdapter = new SpinnerWardAdapter(AddressActivity.this,R.layout.item_view_spinner,wards);
                    spinner_ward.setAdapter(spinnerWardAdapter);
                    if (userAddress != null &&
                            getDistrict != null &&
                            getDistrict.getDistrictID() == userAddress.getDistrict().getDistrictID()) {
                        int index = wards.indexOf(findObjectById(wards,userAddress.getWard().getWardCode()));
                        spinner_ward.setSelection(index);
                    }
                }
                if (ProgressDialogHelper.isShowing()) {
                    ProgressDialogHelper.hideLoading();
                }
            }
        }

        @Override
        public void onFailure(Call<GHNResponse<ArrayList<Ward>>> call, Throwable t) {
            if (ProgressDialogHelper.isShowing()) {
                ProgressDialogHelper.hideLoading();
            }
        }
    };
    private void updateButtonState() {
        boolean isValid = isAddressChanged();

        int colorFromCard = btn_update_address.getCardBackgroundColor().getDefaultColor();
        int colorToCard = ContextCompat.getColor(this, isValid ? R.color.blue_CE4 : R.color.gray_DCD);
        int colorFromText = profile_update_complete.getCurrentTextColor();
        int colorToText = ContextCompat.getColor(this, isValid ? R.color.white_FFF : R.color.gray_E9E);

        btn_update_address.setEnabled(isValid);

        ValueAnimator cardColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromCard, colorToCard);
        cardColorAnimator.setDuration(300);
        cardColorAnimator.addUpdateListener(animator -> btn_update_address.setCardBackgroundColor((int) animator.getAnimatedValue()));
        cardColorAnimator.start();

        ValueAnimator textColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromText, colorToText);
        textColorAnimator.setDuration(300);
        textColorAnimator.addUpdateListener(animator -> profile_update_complete.setTextColor((int) animator.getAnimatedValue()));
        textColorAnimator.start();
    }
    private boolean isAddressChanged() {
        String currentStreetAddress = edt_myAddress.getText().toString().trim();
        String userStreetAddress = userAddress != null ? userAddress.getStreet_address().trim() : "";

        return userAddress == null
                || (getProvince != null && userAddress.getProvince().getProvinceID() != getProvince.getProvinceID())
                || (getDistrict != null && userAddress.getDistrict().getDistrictID() != getDistrict.getDistrictID())
                || (getWard != null && !userAddress.getWard().getWardCode().equals(getWard.getWardCode()))
                || !currentStreetAddress.equals(userStreetAddress);
    }
    private void InitUI() {
        spinner_province = findViewById(R.id.spinner_province);
        spinner_district = findViewById(R.id.spinner_district);
        spinner_ward = findViewById(R.id.spinner_ward);

        edt_myAddress = findViewById(R.id.edt_myAddress);

        btn_back = findViewById(R.id.btn_back);
        btn_update_address = findViewById(R.id.btn_update_address);

        profile_update_complete = findViewById(R.id.profile_update_complete);

        ghnRequest = new GHNRequest();
    }
}