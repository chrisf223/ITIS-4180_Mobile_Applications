package com.example.assignment_05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Profile_Fragment extends Fragment {
    private static final String ARG_PARAM_PROFILE = "param1";

    private static User mUser;
    private String data;

    public static void setData(User user) {
        mUser = user;
    }

    public Profile_Fragment() {
        // Required empty public constructor
    }

    public static Profile_Fragment newInstance(User user) {
        Profile_Fragment fragment = new Profile_Fragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_PROFILE, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(ARG_PARAM_PROFILE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_, container, false);
    }

    TextView name;
    TextView email;
    TextView role;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.profileName);
        email = view.findViewById(R.id.profileEmail);
        role = view.findViewById(R.id.profileRole);

        name.setText(mUser.getName());
        email.setText(mUser.getEmail());
        role.setText(mUser.getRole());

        view.findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToEdit();
            }
        });
    }

    Profile_Fragment.ProfileListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (Profile_Fragment.ProfileListener) context;
    }

    public interface ProfileListener {
        void goToEdit();
    }
}