package com.md09.pharmapoly.ui.view.activity;

import static com.md09.pharmapoly.utils.Constants.PICK_IMAGE_REQUEST;
import static com.md09.pharmapoly.utils.Constants.USER_PROFILE_UPDATED_KEY;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.data.model.User;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.SuccessMessageBottomSheet;
import com.md09.pharmapoly.utils.ProgressDialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileUpdate extends AppCompatActivity {

    private EditText edtFullName, edtPhoneNumber, edtShippingPhoneNumber;
    private RadioGroup rg_gender;
    private CardView btnSaveProfile;
    private SharedPrefHelper sharedPrefHelper;
    private TextView profileUpdateComplete;
    private RetrofitClient retrofitClient;
    private TextView tvDateOfBirth;
//    private ImageButton btn_back;
    private User user;
    private User newUser;
    private File selectedAvatarFile;
    private ImageView img_user_avatar;
    private boolean isDateSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_update);

        InitUI();

        loadUserData();

//        btnSaveProfile.setOnClickListener(v -> updateUserProfile());
        btnSaveProfile.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 101);
                    return;
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            } else {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }
            }
            updateUserProfile();
        });


        edtFullName.addTextChangedListener(textWatcher);
        edtShippingPhoneNumber.addTextChangedListener(textWatcher);

        tvDateOfBirth.setOnClickListener(v -> showDatePickerDialog());
//        btn_back.setOnClickListener(v -> {
//            finish();
//        });
        img_user_avatar.setOnClickListener(v -> {
            pickImageFromGallery();
        });
        rg_gender.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedGender;

            if (checkedId == R.id.update_profile_rb_male) {
                selectedGender = 0;
            } else if (checkedId == R.id.update_profile_rb_female) {
                selectedGender = 1;
            } else {
                selectedGender = 2;
            }

            newUser.setGender(selectedGender);
            updateButtonState();
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            selectedAvatarFile = new File(getRealPathFromURI(imageUri));

            Picasso.get().load(imageUri).into(img_user_avatar);

            updateButtonState();
        }
    }
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    tvDateOfBirth.setText(formattedDate);
                    isDateSelected = true;
                    updateButtonState();
                }, year, month, day);

        datePickerDialog.show();
    }
    private void loadUserData() {
        edtFullName.setText(user.getFull_name());
        edtPhoneNumber.setText(user.getPhone_number());
        edtShippingPhoneNumber.setText(user.getShipping_phone_number());

        int selectedGenderId = (user.getGender() == 0) ? R.id.update_profile_rb_male :
                (user.getGender() == 1) ? R.id.update_profile_rb_female :
                        R.id.update_profile_rb_other;
        rg_gender.check(selectedGenderId);

        if (user.getDate_of_birth() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tvDateOfBirth.setText(sdf.format(user.getDate_of_birth()));
        }
        if (user.getAvatar_url() != null && !user.getAvatar_url().isEmpty()) {
            Glide.with(this)
                    .load(user.getAvatar_url())
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(img_user_avatar);
        }
    }

    private void updateUserProfile() {
        ProgressDialogHelper.showLoading(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = (newUser.getDate_of_birth() != null) ? dateFormat.format(newUser.getDate_of_birth()) : null;


        RequestBody fullNameBody = RequestBody.create(MediaType.parse("text/plain"), newUser.getFull_name());
        RequestBody shippingPhoneNumberBody = RequestBody.create(MediaType.parse("text/plain"), newUser.getShipping_phone_number());
        RequestBody genderBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(newUser.getGender()));
        RequestBody dateOfBirthBody = (formattedDate != null) ?
                RequestBody.create(MediaType.parse("text/plain"), formattedDate) :
                null;

        MultipartBody.Part avatarPart;
        if (selectedAvatarFile != null) {
            avatarPart = MultipartBody.Part.createFormData(
                    "avatar",
                    selectedAvatarFile.getName(),
                    RequestBody.create(MediaType.parse("image/*"), selectedAvatarFile)
            );
        } else {
            avatarPart = null;
        }

        retrofitClient.callAPI().updateProfile(
                "Bearer " + new SharedPrefHelper(this).getToken(),
                avatarPart,
                fullNameBody,
                shippingPhoneNumberBody,
                genderBody,
                dateOfBirthBody
        ).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                ProgressDialogHelper.hideLoading();
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    new SharedPrefHelper(ProfileUpdate.this).updateUser(response.body().getData());
                    //user = response.body().getData();
                    new SharedPrefHelper(ProfileUpdate.this).setBooleanState(USER_PROFILE_UPDATED_KEY,true);
                    SuccessMessageBottomSheet bottomSheet = SuccessMessageBottomSheet.newInstance(getString(R.string.user_update_success));
                    bottomSheet.show(getSupportFragmentManager(), "SuccessMessageBottomSheet");
                    user = response.body().getData();
                    loadUserData();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                ProgressDialogHelper.hideLoading();
            }
        });
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateButtonState();
        }
    };


    private void updateButtonState() {
        String fullName = edtFullName.getText().toString().trim();
        String shippingPhoneNumber = edtShippingPhoneNumber.getText().toString().trim();
        int gender = rg_gender.getCheckedRadioButtonId() == R.id.update_profile_rb_male ? 0 :
                rg_gender.getCheckedRadioButtonId() == R.id.update_profile_rb_female ? 1 : 2;

        Date newDateOfBirth = null;
        if (isDateSelected) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                newDateOfBirth = sdf.parse(tvDateOfBirth.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        newUser.setFull_name(fullName);
        newUser.setShipping_phone_number(shippingPhoneNumber);
        newUser.setGender(gender);
        newUser.setDate_of_birth(isDateSelected ? newDateOfBirth : user.getDate_of_birth());

        boolean isChanged = !newUser.equals(user) || selectedAvatarFile != null;

        boolean isValid = !fullName.isEmpty() && !shippingPhoneNumber.isEmpty() && isChanged;


        int colorFromCard = btnSaveProfile.getCardBackgroundColor().getDefaultColor();
        int colorToCard = ContextCompat.getColor(this, isValid ? R.color.blue_CE4 : R.color.gray_DCD);
        int colorFromText = profileUpdateComplete.getCurrentTextColor();
        int colorToText = ContextCompat.getColor(this, isValid ? R.color.white_FFF : R.color.gray_E9E);

        btnSaveProfile.setEnabled(isValid);

        ValueAnimator cardColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromCard, colorToCard);
        cardColorAnimator.setDuration(300);
        cardColorAnimator.addUpdateListener(animator -> btnSaveProfile.setCardBackgroundColor((int) animator.getAnimatedValue()));
        cardColorAnimator.start();

        ValueAnimator textColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromText, colorToText);
        textColorAnimator.setDuration(300);
        textColorAnimator.addUpdateListener(animator -> profileUpdateComplete.setTextColor((int) animator.getAnimatedValue()));
        textColorAnimator.start();
    }

    private void InitUI() {
//        btn_back = findViewById(R.id.btn_back);

        edtFullName = findViewById(R.id.update_profile_full_name);
        edtPhoneNumber = findViewById(R.id.update_profile_phone_number);
        edtShippingPhoneNumber = findViewById(R.id.update_profile_shipping_phone);
        rg_gender = findViewById(R.id.rg_gender);
        btnSaveProfile = findViewById(R.id.btn_update_profile);
        sharedPrefHelper = new SharedPrefHelper(this);
        retrofitClient = new RetrofitClient();
        profileUpdateComplete = findViewById(R.id.profile_update_complete);

        tvDateOfBirth = findViewById(R.id.tvDateOfBirth);

        img_user_avatar = findViewById(R.id.img_user_avatar);

        user = new SharedPrefHelper(this).getUser();
        newUser = new User(user);
    }
}


//    private void updateButtonState() {
//        boolean isValid = !edtFullName.getText().toString().trim().isEmpty() &&
//                !edtPhoneNumber.getText().toString().trim().isEmpty();
//
//        int colorFromCard = btnSaveProfile.getSolidColor();
//        int colorToCard = ContextCompat.getColor(this, isValid ? R.color.blue_CE4 : R.color.gray_DCD);
//        int colorFromText = profileUpdateComplete.getCurrentTextColor();
//        int colorToText = ContextCompat.getColor(this, isValid ? R.color.white_FFF : R.color.gray_E9E);
//
//        btnSaveProfile.setEnabled(isValid);
//
//        ValueAnimator cardColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromCard, colorToCard);
//        cardColorAnimator.setDuration(300);
//        cardColorAnimator.addUpdateListener(animator ->
//                btnSaveProfile.setBackgroundColor((int) animator.getAnimatedValue())
//        );
//        cardColorAnimator.start();
//
//        ValueAnimator textColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromText, colorToText);
//        textColorAnimator.setDuration(300);
//        textColorAnimator.addUpdateListener(animator ->
//                profileUpdateComplete.setTextColor((int) animator.getAnimatedValue())
//        );
//    }
