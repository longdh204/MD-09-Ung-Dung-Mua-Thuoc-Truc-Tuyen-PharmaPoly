package com.md09.pharmapoly.ui.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.md09.pharmapoly.AddAddress;
import com.md09.pharmapoly.ContentPolicyActivity;
import com.md09.pharmapoly.DepositPolicyActivity;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.payment_card_manager_empty;
import com.md09.pharmapoly.ui.view.activity.IntroductionActivity;
import com.md09.pharmapoly.ui.view.activity.ProfileOptionsActivity;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        InitUI(view);
        return view;
    }

    private void InitUI(View view) {
        LinearLayout btnPersonalInfo = view.findViewById(R.id.btn_personal_info);
        btnPersonalInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileOptionsActivity.class);
            startActivity(intent);
        });
        LinearLayout btnIntroduction =view.findViewById(R.id.btn_introduction);
        btnIntroduction.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), IntroductionActivity.class);
            startActivity(intent);
        });
        LinearLayout btnAddress = view.findViewById(R.id.btn_manage_address);
        btnAddress.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddAddress.class);
            startActivity(intent);
        });
        LinearLayout btnPayment=view.findViewById(R.id.btn_payment);
        btnPayment.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), payment_card_manager_empty.class);
            startActivity(intent);
        });
        LinearLayout btnDeposit=view.findViewById(R.id.btn_deposit_profile);
        btnDeposit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DepositPolicyActivity.class);
            startActivity(intent);
        });
        LinearLayout btnContent=view.findViewById(R.id.btn_content_profile);
        btnContent.setOnClickListener(v -> {
            Intent intent= new Intent(getActivity(), ContentPolicyActivity.class);
            startActivity(intent);
        });

    }

}