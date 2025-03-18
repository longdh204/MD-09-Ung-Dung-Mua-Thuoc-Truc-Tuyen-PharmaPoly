package com.md09.pharmapoly.ui.view.fragment;

import static com.md09.pharmapoly.utils.Constants.USER_PROFILE_UPDATED_KEY;

import android.content.Intent;
import android.os.Bundle;

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
import com.md09.pharmapoly.ui.view.activity.ChangePassword;
import com.md09.pharmapoly.ui.view.activity.ProfileUpdate;
import com.md09.pharmapoly.utils.SharedPrefHelper;

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

    private LinearLayout btn_personal_info;
    private LinearLayout btn_change_password;
    private TextView tv_phone_number,tv_full_name;
    private ImageView img_user_avatar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        InitUI(view);

        btn_personal_info.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileUpdate.class);
            startActivity(intent);
        });

        btn_change_password.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePassword.class);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (new SharedPrefHelper(getContext()).getBooleanState(USER_PROFILE_UPDATED_KEY,false)) {
            LoadUserInfo();
            new SharedPrefHelper(getContext()).resetBooleanState(USER_PROFILE_UPDATED_KEY);
        }
    }

    private void InitUI(View view) {
        btn_personal_info = view.findViewById(R.id.btn_personal_info);
        btn_change_password = view.findViewById(R.id.btn_change_password);

        tv_phone_number = view.findViewById(R.id.tv_phone_number);
        tv_full_name = view.findViewById(R.id.tv_full_name);

        img_user_avatar = view.findViewById(R.id.img_user_avatar);

        LoadUserInfo();
    }
    private void LoadUserInfo() {
        User user = new SharedPrefHelper(getContext()).getUser();
        if (user.getFull_name().trim().isEmpty()) {
            tv_full_name.setText(getString(R.string.customer));
        } else {
            tv_full_name.setText(user.getFull_name());
        }
        tv_phone_number.setText(user.getPhone_number());
        if (user.getAvatar_url() != null && !user.getAvatar_url().isEmpty()) {
            Glide.with(this)
                    .load(user.getAvatar_url())
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(img_user_avatar);
        }
    }
}