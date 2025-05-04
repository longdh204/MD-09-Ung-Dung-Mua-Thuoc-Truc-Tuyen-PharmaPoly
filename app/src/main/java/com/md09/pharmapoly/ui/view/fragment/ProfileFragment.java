package com.md09.pharmapoly.ui.view.fragment;

import static androidx.core.app.ActivityCompat.finishAffinity;
import static androidx.core.app.ActivityCompat.recreate;
import static com.md09.pharmapoly.utils.Constants.USER_PROFILE_UPDATED_KEY;
import static com.md09.pharmapoly.utils.Constants.setLocale;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.User;
import com.md09.pharmapoly.ui.view.activity.AddressActivity;
import com.md09.pharmapoly.ui.view.activity.OrderManagementActivity;
import com.md09.pharmapoly.ui.view.activity.PhoneNumber;
import com.md09.pharmapoly.ui.view.activity.payment_card_manager;
//import com.md09.pharmapoly.ui.view.activity.payment_card_manager_empty;
import com.md09.pharmapoly.ui.view.activity.ChangePassword;
import com.md09.pharmapoly.ui.view.activity.ProfileUpdate;
import com.md09.pharmapoly.utils.DialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    private LinearLayout
            btn_personal_info,
            btn_change_password,
            btn_manage_address,
    //btn_manage_card,
    btn_processing,
            btn_shipping,
            btn_delivered,
            btn_cancelled,
            btn_language,
            layout_language,
            btn_vietnamese,
            btn_english;
    private TextView tv_phone_number, tv_full_name;
    private ImageView img_user_avatar, img_arrow_right_language;
    private AppCompatButton btn_logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        InitUI(view);

        // Thêm sự kiện click cho introduction
        LinearLayout introduction = view.findViewById(R.id.introduction);
        introduction.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.md09.pharmapoly.ui.view.aboutpharmapoly.pharmacy_introduction.class);
            startActivity(intent);
        });
        // Thêm sự kiện click cho introduction
        LinearLayout content = view.findViewById(R.id.content_policy);
        content.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.md09.pharmapoly.ui.view.aboutpharmapoly.Content_Policy.class);
            startActivity(intent);
        });
        LinearLayout shipping = view.findViewById(R.id.Shipping_Policy);
        shipping.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.md09.pharmapoly.ui.view.aboutpharmapoly.Shipping_Policy.class);
            startActivity(intent);
        });
        LinearLayout privacy = view.findViewById(R.id.privacy_policy);
        privacy.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.md09.pharmapoly.ui.view.aboutpharmapoly.Privacy_Policy.class);
            startActivity(intent);
        });

        LinearLayout[] buttons = {btn_processing, btn_shipping, btn_delivered, btn_cancelled};

        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            buttons[i].setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), OrderManagementActivity.class);
                intent.putExtra("order_status", index);
                startActivity(intent);
            });
        }


        btn_personal_info.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileUpdate.class);
            startActivity(intent);
        });

        btn_change_password.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePassword.class);
            startActivity(intent);
        });

        btn_manage_address.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddressActivity.class);
            startActivity(intent);
        });
        btn_language.setOnClickListener(v -> {
            if (layout_language.getVisibility() == View.VISIBLE) {
                layout_language.animate().scaleY(0).setDuration(300).withEndAction(() -> {
                    layout_language.setVisibility(View.GONE);
                }).start();
                img_arrow_right_language.animate().rotation(0).setDuration(300).start();
            } else {
                layout_language.setVisibility(View.VISIBLE);
                layout_language.setScaleY(0);
                layout_language.animate().scaleY(1).setDuration(300).start();
                img_arrow_right_language.animate().rotation(90).setDuration(300).start();
            }
        });

        btn_vietnamese.setOnClickListener(v -> {
            new SharedPrefHelper(getContext()).saveLanguage("vi");
            setLocale(getContext(), "vi");
        });
        btn_english.setOnClickListener(v -> {
            new SharedPrefHelper(getContext()).saveLanguage("en");
            setLocale(getContext(), "en");
        });

//        btn_manage_card.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), payment_card_manager.class);
//            startActivity(intent);
//        });
        btn_logout.setOnClickListener(v -> {
            DialogHelper.ShowConfirmationDialog(
                    getContext(),
                    getString(R.string.logout_title),
                    getString(R.string.logout_message),
                    getString(R.string.yes),
                    getString(R.string.no),
                    () -> {
                        performLogout();
                    }
            );
        });
        return view;
    }

    private void performLogout() {
        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(getContext());
        sharedPrefHelper.clearData();

        Intent intent = new Intent(getContext(), PhoneNumber.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finishAffinity(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (new SharedPrefHelper(getContext()).getBooleanState(USER_PROFILE_UPDATED_KEY, false)) {
            LoadUserInfo();
            new SharedPrefHelper(getContext()).resetBooleanState(USER_PROFILE_UPDATED_KEY);
        }
    }

    private void InitUI(View view) {
        btn_personal_info = view.findViewById(R.id.btn_personal_info);
        btn_change_password = view.findViewById(R.id.btn_change_password);
        btn_manage_address = view.findViewById(R.id.btn_manage_address);

        btn_processing = view.findViewById(R.id.btn_processing);
        btn_shipping = view.findViewById(R.id.btn_shipping);
        btn_delivered = view.findViewById(R.id.btn_delivered);
        btn_cancelled = view.findViewById(R.id.btn_cancelled);

        tv_phone_number = view.findViewById(R.id.tv_phone_number);
        tv_full_name = view.findViewById(R.id.tv_full_name);
        //btn_manage_card = view.findViewById(R.id.btn_manage_card);
        img_user_avatar = view.findViewById(R.id.img_user_avatar);
        img_arrow_right_language = view.findViewById(R.id.img_arrow_right_language);

        btn_logout = view.findViewById(R.id.btn_logout);
        btn_language = view.findViewById(R.id.btn_language);
        btn_vietnamese = view.findViewById(R.id.btn_vietnamese);
        btn_english = view.findViewById(R.id.btn_english);
        layout_language = view.findViewById(R.id.layout_language);
        LoadUserInfo();
    }

    private void LoadUserInfo() {
        User user = new SharedPrefHelper(getContext()).getUser();
        if (user.getFull_name() == null || user.getFull_name().trim().isEmpty()) {
            tv_full_name.setText(getString(R.string.customer));
        } else {
            tv_full_name.setText(user.getFull_name());
        }
//        tv_phone_number.setText(user.getPhone_number());
        if (user.getAvatar_url() != null && !user.getAvatar_url().isEmpty()) {
            Glide.with(this)
                    .load(user.getAvatar_url())
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(img_user_avatar);
        }
    }
}